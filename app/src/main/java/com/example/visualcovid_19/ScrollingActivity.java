package com.example.visualcovid_19;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class ScrollingActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static JSONArray covidData;
    public static FragmentManager fragmentManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout cardsContainerLayout;
    private ProgressBar progressSpinner;

    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        if (isOnline()) {
            //do whatever you want to do
            onSuccessNetworkConnection();
        } else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Error");
                alertDialog.setMessage("No Internet Connection.");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });

                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    public void onSuccessNetworkConnection(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressSpinner = (ProgressBar)findViewById(R.id.progressSpinner);
        progressSpinner.setVisibility(View.VISIBLE);
        cardsContainerLayout = (LinearLayout) findViewById(R.id.cardsContainer);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                // CallYourRefreshingMethod();
                refreshData();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

        setSupportActionBar(toolbar);
        fetchData();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        myMap = googleMap;
        googleMap.clear();
        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        googleMap.setInfoWindowAdapter(customInfoWindow);
        for (int i = 0; i < covidData.length(); i++){
            try {
                JSONObject countryData = covidData.getJSONObject(i);
                int totalCases = countryData.getInt("cases");
                int activeCases = countryData.getInt("active");
                int recoveredCases = countryData.getInt("recovered");
                int fatalCases = countryData.getInt("deaths");

                MarkerInfoWindowData infoWindowData = new MarkerInfoWindowData();
                infoWindowData.setTotalCases(NumberFormat.getNumberInstance(Locale.US).format(totalCases));
                infoWindowData.setActiveCases(NumberFormat.getNumberInstance(Locale.US).format(activeCases));
                infoWindowData.setRecoveredCases(NumberFormat.getNumberInstance(Locale.US).format(recoveredCases));
                infoWindowData.setFatalCases(NumberFormat.getNumberInstance(Locale.US).format(fatalCases));
                infoWindowData.setCountryName(countryData.getString("country"));

                LatLng coordinates = new LatLng(countryData.getJSONObject("countryInfo").getInt("lat"),countryData.getJSONObject("countryInfo").getInt("long"));
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(coordinates).title(countryData.getString("country"))
                        .snippet("Total cases: " + totalCases + "\n Active cases: " + activeCases + "\n Recovered cases: " + recoveredCases + "\n Fatal cases: " + fatalCases);;
                Marker marker = googleMap.addMarker(markerOptions);
                marker.setTag(infoWindowData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        googleMap.setOnMarkerClickListener(this);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //myMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        Projection projection = myMap.getProjection();
        LatLng markerPosition = marker.getPosition();
        Point markerPoint = projection.toScreenLocation(markerPosition);
        Point targetPoint = new Point(markerPoint.x, markerPoint.y - 1000);
        LatLng targetPosition = projection.fromScreenLocation(targetPoint);
        myMap.animateCamera(CameraUpdateFactory.newLatLng(targetPosition), 1000, null);

        myMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );
        return false;
    }


    public  void refreshData(){
        cardsContainerLayout.removeAllViews();
        fetchData();
    }

    public void fetchData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://corona.lmao.ninja/v2/countries?sort=cases";

        // Request a string response from the provided URL.
        StringRequest jsonRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonResponse = new JSONArray(response);
                            covidData = jsonResponse;

                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(ScrollingActivity.this);
                            addCountryCards();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

    public void addCountryCards(){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(findViewById(R.id.cardsContainer) != null) {

            for (int i = 0; i < covidData.length(); i++){
                try {
                    JSONObject countryData = covidData.getJSONObject(i);
                    String countryName = countryData.getString("country");
                    String formattedCasesCount = NumberFormat.getNumberInstance(Locale.US).format(countryData.getInt("cases"));
                    CountryCardFragment cardFragment = CountryCardFragment.newInstance(countryName, formattedCasesCount);
                    fragmentTransaction.add(R.id.cardsContainer, cardFragment, null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            fragmentTransaction.commit();
        }
        progressSpinner.setVisibility(View.INVISIBLE);

    }





    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


}
