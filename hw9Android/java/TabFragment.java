package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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


public class TabFragment extends Fragment {

    private String mTabName;
    SwipeRefreshLayout swipeRefreshLayout;
    NewsRecyclerViewAdapter adapter;

    ArrayList<String> mTitles = new ArrayList<>();
    ArrayList<String> mImages = new ArrayList<>();
    ArrayList<String> mTimes = new ArrayList<>();
    ArrayList<String> mSections = new ArrayList<>();
    ArrayList<String> mIds = new ArrayList<>();
    ArrayList<String> mUrls = new ArrayList<>();
    ArrayList<String> mDates = new ArrayList<>();

    TabFragment(String TabName) {
        mTabName = TabName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_headline_content, container, false);
        final Context context = getContext();

        ArrayList<String> empty = new ArrayList<>();
        adapter = new NewsRecyclerViewAdapter(context,
                empty, empty, empty, empty, empty, empty, empty);

        setRecyclerView(view, context);

        swipeRefreshLayout = view.findViewById(R.id.tab_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRecyclerView(view, context);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView recyclerView = requireView().findViewById(R.id.tab_recycler_view);
        adapter = new NewsRecyclerViewAdapter(requireContext(),
                mTitles, mImages, mTimes, mSections, mIds, mUrls, mDates);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView(final View view, final Context context) {

        final String url = "https://yindahw9server.appspot.com/guardian/" + mTabName;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Error.Response", response.toString());
                try {


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

                    RecyclerView recyclerView = view.findViewById(R.id.tab_recycler_view);
                    adapter = new NewsRecyclerViewAdapter(context,
                            mTitles, mImages, mTimes, mSections, mIds, mUrls, mDates);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    if(swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    view.findViewById(R.id.tab_progress_layout).setVisibility(View.GONE);

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

}
