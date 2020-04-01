package com.example.proyectogassapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.proyectogassapp.utilidades.Utilidades;
import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
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
import com.skyfishjy.library.RippleBackground;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, PermissionsListener,
        NavigationView.OnNavigationItemSelectedListener, MapboxMap.OnMarkerClickListener, MapboxMap.OnMapClickListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    Button btnFind;
    private MaterialSearchBar materialSearchBar;
    ConexionDB db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RippleBackground rippleBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.TOKEN));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        db = new ConexionDB(this);

        drawerLayout = findViewById(R.id.drawerLayout);
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                navigationView.setNavigationItemSelectedListener(MapActivity.this);
            }
        });
        btnFind = findViewById(R.id.find);
        rippleBackground = findViewById(R.id.round);
        navigationView = findViewById(R.id.navigationView);
        btnFind.setOnClickListener(v -> {
            rippleBackground.startRippleAnimation();
            new Handler().postDelayed(() -> {
                rippleBackground.stopRippleAnimation();
                Intent intent = new Intent(MapActivity.this, EstacionesActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        });

    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapActivity.this.mapboxMap = mapboxMap;

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


        mapboxMap.setOnMarkerClickListener(this);
        mapboxMap.addOnMapClickListener(this);

        mapboxMap.setStyle(Style.MAPBOX_STREETS, this::enableLocationComponent);
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

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.perfil) {
            Intent movePerfil = new Intent(MapActivity.this, PerfilActivity.class);
            startActivity(movePerfil);
        }else if (item.getItemId() == R.id.tarjetas){
            Intent moveTarjeta = new Intent(this, TarjetasActivity.class);
            startActivity(moveTarjeta);
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(marker.getPosition()))
                .zoom(17)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position),3000);
        return true;
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        materialSearchBar.setText(String.valueOf(point));
        return true;
    }
}
