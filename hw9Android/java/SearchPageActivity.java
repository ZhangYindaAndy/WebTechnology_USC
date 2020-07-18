package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchPageActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    NewsRecyclerViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        ArrayList<String> empty = new ArrayList<>();
        adapter = new NewsRecyclerViewAdapter(this,
                empty, empty, empty, empty, empty, empty, empty);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String query = intent.getStringExtra("query");

        // set toolbar text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView search_title = findViewById(R.id.search_title);
        search_title.setText("Search Results for " + query);

        // set refresh
        swipeRefreshLayout = findViewById(R.id.search_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createSearchPage(query);
            }
        });

        createSearchPage(query);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void createSearchPage(String query) {
        final Context mContext = this;
        final String url = "https://yindahw9server.appspot.com/guardian-search/" + query;
        RequestQueue queue = Volley.newRequestQueue(mContext);
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

                    for(int i = 0; i < response.length(); ++i) {
                        JSONObject mNews = response.getJSONObject(i);
                        mImages.add(mNews.getString("image"));
                        mTitles.add(mNews.getString("title"));
                        mTimes.add(mNews.getString("date"));
                        mSections.add(mNews.getString("section"));
                        mIds.add(mNews.getString("article-id"));
                        mUrls.add(mNews.getString("section-url"));
                        mDates.add(mNews.getString("format-date"));
                    }

                    RecyclerView recyclerView = findViewById(R.id.search_recycler_view);
                    adapter = new NewsRecyclerViewAdapter(mContext,
                            mTitles, mImages, mTimes, mSections, mIds, mUrls, mDates);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                    if(swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    findViewById(R.id.search_progress_layout).setVisibility(View.GONE);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
