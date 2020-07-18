package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class BookmarksFragment extends Fragment {

    Context context;
    private SharedPreferences pref;
    private BookmarksNewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        context = getContext();

        ArrayList<String> empty = new ArrayList<>();
        adapter = new BookmarksNewsAdapter(context, null,
                empty, empty, empty, empty, empty, empty);

        pref = getContext().getSharedPreferences("NewsApp", 0);
//        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
//        editor.clear();
//        editor.commit();

        setRecyclerView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // for solve the bug (clear the adapter, then re-make it)
        RecyclerView recyclerView = requireView().findViewById(R.id.bookmark_recycler_view);
        ArrayList<String> empty = new ArrayList<>();
        adapter = new BookmarksNewsAdapter(requireContext(), requireView(),
                empty, empty, empty, empty, empty, empty);
        System.out.println(adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        setRecyclerView(requireView());
    }

    private void setRecyclerView(View view) {
        Map<String, ?> saved_news = pref.getAll();

        if(saved_news.size() == 0)
            view.findViewById(R.id.bookmarks_no_content).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.bookmarks_no_content).setVisibility(View.GONE);


        ArrayList<String> mTitles = new ArrayList<>();
        ArrayList<String> mImages = new ArrayList<>();
        ArrayList<String> mSections = new ArrayList<>();
        ArrayList<String> mIds = new ArrayList<>();
        ArrayList<String> mUrls = new ArrayList<>();
        ArrayList<String> mTimes = new ArrayList<>();

        for(String article_id: saved_news.keySet()) {
            String news_string = (String) saved_news.get(article_id);
            try {
                JSONObject news_data = new JSONObject(news_string);
                Log.d("setRecyclerView", article_id);
                Log.d("setRecyclerView", news_string);

                mIds.add(article_id);
                mTitles.add(news_data.getString("title"));
                mImages.add(news_data.getString("image"));
                mSections.add(news_data.getString("section"));
                mUrls.add(news_data.getString("url"));
                mTimes.add(news_data.getString("date"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            RecyclerView recyclerView = view.findViewById(R.id.bookmark_recycler_view);

            // send view into adapter, for show the no-content when adapter is empty
            adapter = new BookmarksNewsAdapter(context, view,
                    mTitles, mImages, mSections, mIds, mUrls, mTimes);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        }
    }
}
