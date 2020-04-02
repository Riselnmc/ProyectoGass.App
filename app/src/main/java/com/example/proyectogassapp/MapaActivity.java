package com.example.proyectogassapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
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

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;


public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        NavigationView.OnNavigationItemSelectedListener, MapboxMap.OnMapClickListener,
        MapboxMap.OnMarkerClickListener {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private String geojsonSourceLayerId = "geojsonSourceLayerId";
    private String symbolIconId = "symbolIconId";
    private PermissionsManager permissionsManager;
    ConexionDB db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Location originLocation;
    private Point destinationPoint;
    private Point originPoint;
    private Marker destinationMarker;
    private RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.TOKEN));
        setContentView(R.layout.activity_mapa);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        db = new ConexionDB(this);
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
        mapboxMap.addOnMapClickListener(this);
        mapboxMap.setOnMarkerClickListener(this);
        LatLng marcador1 = new LatLng(6.345024,-75.564173);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador1)));

        LatLng marcador2 = new LatLng(6.327083824544367,-75.5606060475111);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador2)));

        LatLng marcador3 = new LatLng(6.309283454931489, -75.55962024304738);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador3)));

        LatLng marcador4 = new LatLng(6.308696886205112, -75.55984087817768);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador4)));

        LatLng marcador5 = new LatLng(6.339463378470185, -75.54567143439789);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador5)));

        LatLng marcador6 = new LatLng(6.338639503531937, 75.54320852659262);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador6)));

        LatLng marcador7 = new LatLng(6.315068555754237, -75.55733757983401);
        mapboxMap.addMarker(new MarkerOptions().position(new LatLng(marcador7)));
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

                UiSettings uiSettings = mapboxMap.getUiSettings();

                uiSettings.setCompassEnabled(false);

                initSearchFab();

                enableLocationComponent(style);

                setUpSource(style);

                setupLayer(style);
            }
        });

    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {


            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);


            locationComponent.setCameraMode(CameraMode.TRACKING);


            locationComponent.setRenderMode(RenderMode.COMPASS);

            Location lastLocation = locationComponent.getLastKnownLocation();
            if (lastLocation!=null){
                originLocation = lastLocation;
            }
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        }
    }

    private void initSearchFab() {
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




    private void setUpSource(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(geojsonSourceLayerId));
    }

    private void setupLayer(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new SymbolLayer("SYMBOL_LAYER_ID", geojsonSourceLayerId).withProperties(
                iconImage(symbolIconId),
                iconOffset(new Float[] {0f, -8f})
        ));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);

            if (mapboxMap != null) {
                Style style = mapboxMap.getStyle();
                if (style != null) {
                    GeoJsonSource source = style.getSourceAs(geojsonSourceLayerId);
                    if (source != null) {
                        source.setGeoJson(FeatureCollection.fromFeatures(
                                new Feature[] {Feature.fromJson(selectedCarmenFeature.toJson())}));
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                                            ((Point) selectedCarmenFeature.geometry()).longitude()))
                                    .zoom(17)
                                    .build()), 4000);
                    mapboxMap.addMarker(new MarkerOptions().position(new LatLng(((Point) selectedCarmenFeature.geometry()).latitude(),
                            ((Point) selectedCarmenFeature.geometry()).longitude())));
                }
            }
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.perfil) {
            Intent movePerfil = new Intent(MapaActivity.this, PerfilActivity.class);
            startActivity(movePerfil);
        }else if (item.getItemId() == R.id.tarjetas){
            Intent moveTarjeta = new Intent(this, TarjetasActivity.class);
            startActivity(moveTarjeta);
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {

        if (destinationMarker != null){
            mapboxMap.removeMarker(destinationMarker);
        }

        destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(point));
        destinationPoint = Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPoint = Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());

        return true;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        CameraPosition position = new CameraPosition.Builder()
                .target(marker.getPosition())
                .zoom(16)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),2000);
        return true;
    }
}
