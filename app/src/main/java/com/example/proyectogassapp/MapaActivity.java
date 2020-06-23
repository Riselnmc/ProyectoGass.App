package com.example.proyectogassapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

/*
    * Creación de la clase con herencia del AppCompatActivity
    * Implementacioes
    * Iniciar mapa, permiso de localización, selector de item en el menú lateral y escuchador de click en un marcador
 */
public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        NavigationView.OnNavigationItemSelectedListener, MapboxMap.OnMarkerClickListener {

    //Variables para los componentes de la vista
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RippleBackground rippleBackground;
    private MapView mapView;

    //Variables para los objetos de Mapbox
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationComponent locationComponent;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;
    private Point originPosition;

    //Variables especiales
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    //Variable para el autenticador
    private FirebaseAuth mAuth;
    //Variable para la base de datos
    private FirebaseFirestore db;

    /*
     * Ciclo de vida principal del activity
     * Se activa cuando el sistema crea la actividad por primera vez, se ejecuta la lógica básica
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Llamar la super clase onCreate para completar la creación de la activity
        super.onCreate(savedInstanceState);
        //Clave api de mapbox
        Mapbox.getInstance(this, getString(R.string.TOKEN));
        //Establecer el diseño de la interfaz de usuario para esta activity
        setContentView(R.layout.activity_mapa);

        mapView = findViewById(R.id.mapView);

        //Guardar datos del mapa al iniciar la activity
        mapView.onCreate(savedInstanceState);

        //Sincronizar el mapa en esta activity
        mapView.getMapAsync(this);

        //Instanciar autenticador
        mAuth = FirebaseAuth.getInstance();

        //Instanciar base de datos
        db = FirebaseFirestore.getInstance();

        //Referenciar la parte lógica con la vista
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        rippleBackground = findViewById(R.id.round);

        //Evento click para iniciar el menú lateral
        findViewById(R.id.menu).setOnClickListener(v -> {
            //Abrir el menú lateral
            drawerLayout.openDrawer(GravityCompat.START);
            //Enviar item seleccionado
            navigationView.setNavigationItemSelectedListener(MapaActivity.this);
        });

        //Evento click para mostrar animación
        findViewById(R.id.find).setOnClickListener(v -> {
            //Iniciar animación
            rippleBackground.startRippleAnimation();
            new Handler().postDelayed(() -> {
                //Pausar animación en 1.5 segundos
                rippleBackground.stopRippleAnimation();
                //Enviar a la activity de Estaciones
                startActivity(new Intent(MapaActivity.this, EstacionesActivity.class));
            },1500);
        });
    }

    //Metodo que contiene la lógica con la que inicia el mapa
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //Enviar evento click
        mapboxMap.setOnMarkerClickListener(this);

        //Creación del icono
        IconFactory iconFactory = IconFactory.getInstance(MapaActivity.this);
        //Elegir icono personalizado
        Icon icon = iconFactory.fromResource(R.drawable.ic_marcador);
        //Definir tamaño del icono
        Icon myIcon = IconFactory.recreate(icon.getId(), Bitmap.createScaledBitmap(icon.getBitmap(), 50, 50, false));

        //Mostrar los marcadores de las estaciones registradas
        db.collection("Estaciones").get().addOnSuccessListener(queryDocumentSnapshots -> {
            //Ciclo para recorrer toda la colección
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                Estaciones estaciones = snapshot.toObject(Estaciones.class);
                //Crear y darle valor a las variables para los marcadores de cada estación
                String nombreE = estaciones.getNombreEstacion();
                String direccionE = estaciones.getDireccionEstacion();
                Double Latitud = estaciones.getLatitud();
                Double Longitud = estaciones.getLongitud();
                //Añadir marcador con icono personalizado, nombre y descripción
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(Latitud,Longitud)).title(nombreE).snippet(direccionE)
                        .icon(myIcon));
            }
        });

        //Iniciar estilos del mapa
        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {

            UiSettings uiSettings = mapboxMap.getUiSettings();

            //No mostrar la brujula en el mapa
            uiSettings.setCompassEnabled(false);

            //Usar metodo para iniciar buscador
            initSearchFab();

            //Usar metodo para iniciar el mapa
            enableLocationComponent(style);

            setUpSource(style);

            setupLayer(style);
        });

    }


    //Metodo para iniciar el mapa en la ubicación del usuario
    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        //Verificar que los permisos de localización esten activos
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            //Ubicación en la que se encuentra el usuario
            locationComponent = mapboxMap.getLocationComponent();

            //Cuadro de mensaje para pedir activar la ubicación del dispositivo
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            //Cargar el mapa en la ubicación del usuario
            locationComponent.setLocationComponentEnabled(true);

            //Estilo de la cámara del mapa
            locationComponent.setCameraMode(CameraMode.TRACKING);

            //Estilo del icono de ubicación
            locationComponent.setRenderMode(RenderMode.COMPASS);


        } else {
            //Si los permisos de localización no estan permitidos no cargará el mapa
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    //Metodo para pedir permisos de ubicación
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    //Metodo para el resultado del permiso de ubicación
    public void onPermissionResult(boolean granted) {
        //Verificar que se hayan aceptado
        if (granted) {
            //Mostrar estilo del mapa
            mapboxMap.getStyle(this::enableLocationComponent);
        }
    }

    //Metodo para el buscador
    private void initSearchFab() {
        /*
            * Referenciar con la vista
            * Validar que el token no sea nullo
            * Definir color de los items
            * Definir cantidad máxima de items a mostrar
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

    //Metodo para obtener resultados de la activity del buscador
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Verificar que los datos sean iguales
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            //Traer información del lugar seleccionado
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            //Verificar que el mapa tenga información
            if (mapboxMap != null) {
                //Traer estilo del mapa
                Style style = mapboxMap.getStyle();
                //Verificar que el mapa tiene un estilo
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    //Verificar que source no sea nulo
                    if (source != null) {
                        //Traer información del lugar elegido por el usuario
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    /*
                        * Animación para enviar el mapa a las coordenadas del lugar elegido por el usuario en 4 segundos
                        * Con un zoom de 17
                     */
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(17)
                                    .build()), 4000);
                }
            }
        }
    }

    //Metodo de navegación para el menu lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Verificar que el item obtenido sea igual al id del item seleccionado
        if (item.getItemId() == R.id.perfil) {
            startActivity(new Intent(MapaActivity.this, PerfilActivity.class));
        }else if (item.getItemId() == R.id.logout){
            cerrarSesion();
        }else if (item.getItemId() == R.id.terminos){
            startActivity(new Intent(MapaActivity.this, TerminosYCondicionesActivity.class));
        }else if (item.getItemId() == R.id.misTarjetas){
            startActivity(new Intent(MapaActivity.this, TarjetasActivity.class));
        }
        else if (item.getItemId() == R.id.about){
            startActivity(new Intent(MapaActivity.this, AcercaDeActivity.class));
        }
        //Cerrar el menú lateral luego de seleccionar el item
        drawerLayout.closeDrawers();
        //Retornar verdadero
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
                    //Metodo de firebase para cerrar la sesión
                    mAuth.signOut();
                    startActivity(new Intent(MapaActivity.this, LoginActivity.class));
                    finish();
                    //Al darle en "No" solo cerrara el AlertDialog
                }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).setCancelable(false).show();
    }

    //Metodo para cuando se le de click en el marcador
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        //Varible de AlertDialog.Builder para construir e instanciar el objeto
        AlertDialog.Builder builder = new AlertDialog.Builder(MapaActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        //Obtener AlertDialog personalizado con el layout especificado
        View view = inflater.inflate(R.layout.item_alert_informacion,null);

        //Establecer vista personalizada
        builder.setView(view);

        //Variable de AlertDialog para construir y crear la vista
        AlertDialog dialog = builder.create();
        //Mostrar AlertDialog
        dialog.show();

        //Variables para la información de la estación
        TextView nombreEstacion,direccionEstacion;

        //Referenciar la parte lógica con la vista
        nombreEstacion = view.findViewById(R.id.tv_nombreEstacion);
        direccionEstacion = view.findViewById(R.id.tv_direccionEstacion);

        //Darles valor a los TextView con la información de la estación
        nombreEstacion.setText(marker.getTitle());
        direccionEstacion.setText(marker.getSnippet());

        //Creación de variable para el botón y referenciar la parte lógica con la vista
        Button btnRutas = view.findViewById(R.id.btn_generarRuta);
        //Evento click para generar la ruta
        btnRutas.setOnClickListener(v -> {
            //Verificar que las coordenadas no sean nulas
            assert locationComponent.getLastKnownLocation() != null;
            //Obtener las coordenadas del dispositivo movil
            originPosition = Point.fromLngLat(locationComponent.getLastKnownLocation().getLongitude(),locationComponent.getLastKnownLocation().getLatitude());
            //Obtener coordenadas del marcador
            Point destinationPosition = Point.fromLngLat(marker.getPosition().getLongitude(), marker.getPosition().getLatitude());


            GeoJsonSource geoJsonSource = (GeoJsonSource) Objects.requireNonNull(mapboxMap.getStyle()).getSource("destination-source-id");
            //Verificar que no este nulo
            if (geoJsonSource != null){
                geoJsonSource.setGeoJson(Feature.fromGeometry(destinationPosition));
            }

            /*
                *Generar ruta
                * @param originPosition posición actual del dispositivo
                * @param detinationPosition posición del marcador seleccionado
             */
            getRoute(originPosition, destinationPosition);
            //Dejar de mostrar AlertDialog
            dialog.dismiss();
        });

        //Retornar verdadero
        return true;
    }

    /*
        * Metodo para generar ruta
        * @param origin posición de origen
        * @param destination posición de destino
     */
    private void getRoute(Point origin, Point destination){
        //Validar que el acceso al token no sea nulo
        assert Mapbox.getAccessToken() != null;
        //Construir la navegación de ruta en esta activity
        NavigationRoute.builder(this)
                //Acceso al token
                .accessToken(Mapbox.getAccessToken())
                //Posición de origen
                .origin(origin)
                //Posición de destino
                .destination(destination)
                //Construir
                .build()
                //Generar ruta
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DirectionsResponse> call, @NonNull Response<DirectionsResponse> response) {
                        //Si el cuerpo de la ruta es nula no retorna nada
                        if (response.body() == null){
                            return;
                            //Si el tamaño de la ruta es menor a 1 no retorna nada
                        } else if (response.body().routes().size() < 1){
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        //Si ya existe una ruta la removera
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                            //Si no existe una ruta creará una nueva
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        //Añadir la ruta
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(@NonNull Call<DirectionsResponse> call, @NonNull Throwable t) {
                    }
                });

    }

    /*
        * Aquí es donde los componentes del ciclo de vida pueden habilitar
        * cualquier funcionalidad que necesite ejecutarse mientras el componente está visible y en primer plano
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /*
     * Hace que el usuario pueda ver la activity mientras la app se prepara para que esta entre
     * en primer plano y se convierta en interactiva
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    /*
        * Garantiza que continúe el trabajo relacionado con la IU
        * Incluso cuando el usuario esté viendo tu activity en el modo multiventana.
     */
    @Override
    protected void onStop() {
        super.onStop();
        //Pausar el estado de la activity
        mapView.onStop();
    }

    /*
     * Indica que la activity ya no está en primer plano
     * (aunque puede seguir siendo visible si el usuario está en el modo multiventana)
     */
    @Override
    public void onPause() {
        super.onPause();
        //Pausar el estado de la activity
        mapView.onPause();
    }

    /*
        * Esto se llama cuando el sistema general se está quedando sin memoria y
        * los procesos que se ejecutan activamente deberían reducir su uso de memoria
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //Reducir espacio usado del mapa
        mapView.onLowMemory();
    }

    /*
        * Hace referencia a cuando cerramos la aplicación
        * Todos los datos son eliminados
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Destruir el mapa
        mapView.onDestroy();
    }

    //invocado cuando la actividad se puede destruir temporalmente, guarde el estado de la instancia aquí
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Guardar el estado del mapa
        mapView.onSaveInstanceState(outState);
    }

}
