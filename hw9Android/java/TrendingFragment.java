package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class TrendingFragment extends Fragment {

    private LineChart mLineChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_trending, container, false);

        createTrending(view, "Coronavirus");
        EditText editText = view.findViewById(R.id.trending_query);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                createTrending(view, v.getText().toString());
                return true;
            }
        });

        return view;
    }

    private void createTrending(final View view, final String query) {
        final ArrayList<Entry> entries = new ArrayList<Entry>();
        String url = "https://yindahw9server.appspot.com/google-trends/" + query;

        RequestQueue queue = Volley.newRequestQueue(this.requireContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Error.Response", response.toString());
                try {
                    for(int i = 0; i < response.length(); ++i)
                        entries.add(new Entry(i, response.getInt(i)));

                    // Valley do an async job, so need a callback function to do the remaining job
                    mLineChart = view.findViewById(R.id.line_chart);
                    ArrayList<ILineDataSet> mDataSet = new ArrayList<>();
                    LineDataSet lineDataSet = new LineDataSet(entries, "Trending Chart for " + query);

                    lineDataSet.setColor(Color.argb(150, 75, 0, 255));
                    lineDataSet.setValueTextColor(Color.argb(150, 75, 0, 255));
                    lineDataSet.setCircleRadius(2);
                    lineDataSet.setCircleColor(Color.argb(150, 75, 0, 255));
                    lineDataSet.setCircleHoleColor(Color.argb(150, 75, 0, 255));

                    mDataSet.add(lineDataSet);
                    mLineChart.setData(new LineData(mDataSet));
                    mLineChart.invalidate();

                    mLineChart.getLegend().setTextSize(14);

                    // remove the grid and axis
                    mLineChart.getAxisLeft().setDrawAxisLine(false);
                    mLineChart.getXAxis().setDrawGridLines(false);
                    mLineChart.getAxisRight().setDrawGridLines(false);
                    mLineChart.getAxisLeft().setDrawGridLines(false);

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
