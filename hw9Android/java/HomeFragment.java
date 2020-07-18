package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements LocationListener {

    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    NewsRecyclerViewAdapter adapter;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();

        // new an empty adapter, because the onResume will be called immediately
        ArrayList<String> empty = new ArrayList<>();
        adapter = new NewsRecyclerViewAdapter(context,
                empty, empty, empty, empty, empty, empty, empty);

        checkLocationPermission();

        setWeatherCard(view);
        setRecyclerView(view);

        swipeRefreshLayout = view.findViewById(R.id.home_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerView(view);
                setWeatherCard(view);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView(final View view) {

        final String url = "https://yindahw9server.appspot.com/guardian";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Log.d("Error.Response", response.toString());
                try {
                    ArrayList<String> mTitles = new ArrayList<>();
                    ArrayList<String> mImages = new ArrayList<>();
                    ArrayList<String> mTimes = new ArrayList<>();
                    ArrayList<String> mSections = new ArrayList<>();
                    ArrayList<String> mIds = new ArrayList<>();
                    ArrayList<String> mUrls = new ArrayList<>();
                    ArrayList<String> mDates = new ArrayList<>();

                    for (int i = 0; i < response.length(); ++i) {
                        JSONObject mNews = response.getJSONObject(i);
                        mImages.add(mNews.getString("image"));
                        mTitles.add(mNews.getString("title"));
                        mTimes.add(mNews.getString("date"));
                        mSections.add(mNews.getString("section"));
                        mIds.add(mNews.getString("article-id"));
                        mUrls.add(mNews.getString("section-url"));
                        mDates.add(mNews.getString("format-date"));
                    }

                    RecyclerView recyclerView = view.findViewById(R.id.home_recycler_view);
                    adapter = new NewsRecyclerViewAdapter(context,
                            mTitles, mImages, mTimes, mSections, mIds, mUrls, mDates);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    view.findViewById(R.id.progress_layout).setVisibility(View.GONE);

                } catch (JSONException e) {
                    Log.d("Error.Response", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        });

        queue.add(request);
    }

    private void setWeatherCard(View view) {
        // get really time gps location
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        double longitude = -118.24, latitude = 34.05;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location location = locationManager.getLastKnownLocation("gps");

            if(location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            Log.d("locale", Double.toString(longitude));
            Log.d("locale", Double.toString(latitude));
        }

        // get city from location
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();

        TextView mCity = view.findViewById(R.id.weather_city);
        TextView mState  = view.findViewById(R.id.weather_state);
        mCity.setText(cityName);
        mState.setText(stateName);

        loadWeatherFromApi(view, cityName);
    }

    private void loadWeatherFromApi(final View view, String cityName) {
        String key = "14a06e6eec888f093ee97f375467442f";
        cityName = cityName.replace(' ', '+');
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + key;

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("Error.Response", response.toString());
                try {
                    String myMain = response.getJSONArray("weather").getJSONObject(0).getString("main");
                    String myTemp = Integer.toString((int)response.getJSONObject("main").getDouble("temp"));
                    // Valley do an async job, so need a callback function to do the remaining job
                    setWeatherCardContinue(view, myMain, myTemp);
                } catch (JSONException e) {
                    Log.d("Error.Response", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", String.valueOf(error));
            }
        });

        queue.add(getRequest);
    }

    private void setWeatherCardContinue(View view, String myMain, String myTemp) {
        // callback function for Valley onResponse
        TextView mTemp = view.findViewById(R.id.weather_temp);
        TextView mMain  = view.findViewById(R.id.weather_main);
        mTemp.setText(String.format("%s ℃", myTemp));
        mMain.setText(myMain);

        String backgroundUrl = getBackgroundUrl(myMain);

        ImageView weather_image = view.findViewById(R.id.weather_image);
        Glide.with(context)
                .load(backgroundUrl)
                .into(weather_image);
    }

    private String getBackgroundUrl(String weather_main) {
        String backgroundUrl;
        switch (weather_main) {
            case "Clouds":
                backgroundUrl = "https://csci571.com/hw/hw9/images/android/cloudy_weather.jpg";
                break;
            case "Clear":
                backgroundUrl = "https://csci571.com/hw/hw9/images/android/clear_weather.jpg";
                break;
            case "Snow":
                backgroundUrl = "https://csci571.com/hw/hw9/images/android/snowy_weather.jpeg";
                break;
            case "Rain":
            case "Drizzle":
                backgroundUrl = "https://csci571.com/hw/hw9/images/android/rainy_weather.jpg";
                break;
            case "Thunderstorm":
                backgroundUrl = "https://csci571.com/hw/hw9/images/android/thunder_weather.jpg";
                break;
            default:
                backgroundUrl = "https://csci571.com/hw/hw9/images/android/sunny_weather.jpg";
                break;
        }
        return backgroundUrl;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates("gps", 400, 1, this);
                    }

                }
                return;
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
