package com.example.proyectogassapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.proyectogassapp.entidades.Estaciones;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.skyfishjy.library.RippleBackground;
import java.util.List;
import java.util.Objects;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;


public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        NavigationView.OnNavigationItemSelectedListener, MapboxMap.OnMarkerClickListener {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private PermissionsManager permissionsManager;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RippleBackground rippleBackground;
    LocationComponent locationComponent;
    private DirectionsRoute currentRoute;
    private static final String TAG = "MapaActivity";
    private NavigationMapRoute navigationMapRoute;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Clave api de mapbox
        Mapbox.getInstance(this, getString(R.string.TOKEN));
        setContentView(R.layout.activity_mapa);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        findViewById(R.id.menu).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
            navigationView.setNavigationItemSelectedListener(MapaActivity.this);
        });
        rippleBackground = findViewById(R.id.round);
        findViewById(R.id.find).setOnClickListener(v -> {
            rippleBackground.startRippleAnimation();
            new Handler().postDelayed(() -> {
                rippleBackground.stopRippleAnimation();
                Intent intent = new Intent(MapaActivity.this, EstacionesActivity.class);
                startActivity(intent);
            },1500);
        });
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setOnMarkerClickListener(this);

        IconFactory iconFactory = IconFactory.getInstance(MapaActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.ic_marcador);
        Icon myIcon = IconFactory.recreate(icon.getId(), Bitmap.createScaledBitmap(icon.getBitmap(), 50, 50, false));

        //Mostrar los marcadores de las estaciones registradas
        db.collection("Estaciones").get().addOnSuccessListener(queryDocumentSnapshots -> {
            //Ciclo para recorrer toda la colección
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                Estaciones estaciones = snapshot.toObject(Estaciones.class);
                //Crear y darle valor a las variables para los marcadores de cada estación
                Double Latitud = estaciones.getLatitud();
                Double Longitud = estaciones.getLongitud();
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(Latitud,Longitud))
                        .icon(myIcon));
            }
        });

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {

            UiSettings uiSettings = mapboxMap.getUiSettings();

            //No mostrar la brujula en el mapa
            uiSettings.setCompassEnabled(false);

            //Instanciar metodos
            initSearchFab();

            enableLocationComponent(style);

            setUpSource(style);

            setupLayer(style);
        });

    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        //Verificar que los permisos de localización esten activos
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            //Ubicación en la que se encuentra el usuario
            locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            //Cargar el mapa en la ubicación del usuario
            locationComponent.setLocationComponentEnabled(true);

            //Estilo de la cámara
            locationComponent.setCameraMode(CameraMode.TRACKING);

            //Estilo del icono de ubicación
            locationComponent.setRenderMode(RenderMode.COMPASS);


        } else {
            //Si los permisos de localización no estan permitidos
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    //Metodo
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    //Metodo para el permiso de localización
    public void onPermissionResult(boolean granted) {
        //Si esta activado mostrará el estilo
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        }
    }

    //Metodo para el buscador
    private void initSearchFab() {
        /*
            * Referenciar con la vista
            * Validar que el token no sea nullo
            * Definir color de los items
            * Definir limite de items a mostrar
            * Definir estilo de los items y construirlo sobre este activity
            * Enviar los datos a esta activity
        */
        findViewById(R.id.fab_location_search).setOnClickListener(view -> {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken() != null ? Mapbox.getAccessToken() : getString(R.string.TOKEN))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(Color.parseColor("#EEEEEE"))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(MapaActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });
    }

    //
    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    //
    private void setupLayer(@NonNull Style loadedMapStyle) {
        String symbolIconId = "symbolIconId";
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Condición para validar que resultado sea verdadero
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            //Traer información del lugar seleccionado
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            //Verificar que el mapa tenga información
            if (mapboxMap != null) {
                //Dar estilo al mapa
                Style style = mapboxMap.getStyle();
                //Verificar que el mapa tiene un estilo
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        //Traer información del lugar
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    //Animación para que envie la camará del mapa cuando seleccione un lugar
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(17)
                                    .build()), 4000);
                    //Agregar marcador en el lugar seleccionado
                    mapboxMap.addMarker(new MarkerOptions().position(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude())));
                }
            }
        }
    }

    //Metodo de navegación para el menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Condición para enviar a la activity seleccionada
        if (item.getItemId() == R.id.perfil) {
            Intent movePerfil = new Intent(MapaActivity.this, PerfilActivity.class);
            startActivity(movePerfil);
        }else if (item.getItemId() == R.id.logout){
            cerrarSesion();
        }
        //Cerrar el menú lateral luego de seleccionar el activity
        drawerLayout.closeDrawers();
        return true;
    }

    //Metodo para cerrar sesión
    private void cerrarSesion(){
        //Creación de obeto para el @AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);
        //Definir titulo que tendrá el cuadro de dialogo
        builder.setTitle("¿Quieres cerrar la sesión?")
                //Botón positivo
                .setPositiveButton("Sí", (dialog, which) -> {
                    //Cerrar sesión
                    mAuth.signOut();
                    startActivity(new Intent(MapaActivity.this, LoginActivity.class));
                    finish();
                }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
    }

    //Metodo de animación para cuando el usuario le de click a un marcador
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);
        builder.setTitle("¿Qué deseas hacer?")
                .setPositiveButton("Generar ruta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Point originPosition = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),locationComponent.getLastKnownLocation().getLatitude());
                        Point desinationPosition = Point.fromLngLat(marker.getPosition().getLongitude(), marker.getPosition().getLatitude());

                        GeoJsonSource geoJsonSource = (GeoJsonSource) mapboxMap.getStyle().getSource("destination-source-id");
                        if (geoJsonSource != null){
                            geoJsonSource.setGeoJson(Feature.fromGeometry(desinationPosition));
                        }

                        getRoute(originPosition, desinationPosition);
                        dialog.dismiss();
                        CameraPosition position = new CameraPosition.Builder()
                                //Coordenadas del marcador
                                .target(marker.getPosition())
                                //Altura de la camara
                                .zoom(13)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),2000);
                    }
                }).setNegativeButton("Ver estación", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent moveEstacion = new Intent(MapaActivity.this, DetalleEstacionActivity.class);
                startActivity(moveEstacion);
            }
        }).setCancelable(false).show();
        return true;
    }

    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null){
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1){
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "Error: " + t.getMessage());
                    }
                });

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
