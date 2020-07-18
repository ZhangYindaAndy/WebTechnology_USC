package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class DetailPageActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // hide for showing progress bar
        findViewById(R.id.detail_card_view).setVisibility(View.INVISIBLE);
        // hide toolbar icons
        findViewById(R.id.toolbar_icons).setVisibility(View.GONE);

        Intent intent = getIntent();
        String article_id = intent.getStringExtra("article-id");

        createDetailPage(article_id);
    }

    private void createDetailPage(final String article_id) {

        final Context mContext = this;
        pref = mContext.getSharedPreferences("NewsApp", 0);

        final String url = "https://yindahw9server.appspot.com/guardian-article?id=" + article_id;
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Error.Response", response.toString());
                try {
                    JSONObject news_info = response.getJSONObject(0);
                    final String mImage = news_info.getString("image");
                    final String mTitle = news_info.getString("title");
                    final String mSection = news_info.getString("section");
                    final String mTime = news_info.getString("date");
//                    final String mDate = news_info.getString("format-date");
                    String mDescription = news_info.getString("description");
                    final String mUrl = news_info.getString("section-url");

                    ImageView detail_image = findViewById(R.id.detail_image);
                    TextView detail_title = findViewById(R.id.detail_title);
                    TextView detail_section = findViewById(R.id.detail_section);
                    TextView detail_time = findViewById(R.id.detail_time);
                    TextView detail_description = findViewById(R.id.detail_description);
                    TextView detail_jump = findViewById(R.id.detail_jump);

                    Glide.with(mContext)
                            .asBitmap()
                            .load(mImage)
                            .into(detail_image);

                    detail_title.setText(mTitle);
                    detail_section.setText(mSection);
                    detail_time.setText(transformTime(mTime));
//                    detail_time.setText(mDate);
                    detail_description.setText(Html.fromHtml(mDescription));

                    // add underline and intent
                    detail_jump.setText(Html.fromHtml("<u>View Full Article</u>"));
                    detail_jump.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(mUrl));
                            mContext.startActivity(intent);
                        }
                    });

                    // hide progress bar and show the card
                    findViewById(R.id.detail_progress_layout).setVisibility(View.GONE);
                    findViewById(R.id.detail_card_view).setVisibility(View.VISIBLE);

                    // set toolbar title and icons
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    TextView toolbar_title = findViewById(R.id.toolbar_title);
                    toolbar_title.setText(mTitle);
                    findViewById(R.id.toolbar_icons).setVisibility(View.VISIBLE);

                    // add listener on icons
                    ImageView detail_twitter = findViewById(R.id.detail_twitter);
                    final ImageView detail_bookmark = findViewById(R.id.detail_bookmark);

                    String id = pref.getString(article_id, "");
                    if(!id.equals("")) // saved before
                        detail_bookmark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));

                    detail_twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "http://www.twitter.com/intent/tweet?";
                            url += "text=Check out this Link:&";
                            url += "url=" + mUrl + "&";
                            url += "hashtags=CSCI571NewsSearch";

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            mContext.startActivity(intent);
                        }
                    });

                    detail_bookmark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String saved_info = pref.getString(article_id, "");
                            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
                            // Log.d("bookmark", saved_info);

                            if(saved_info.equals("")) {
                                // add into memory and change icon
                                detail_bookmark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));

                                Map<String, String> card_data = new HashMap<String, String>();

                                card_data.put("image", mImage);
                                card_data.put("title", mTitle);
                                card_data.put("section", mSection);
                                card_data.put("url", mUrl);
                                card_data.put("date", mTime);
                                JSONObject json = new JSONObject(card_data);

                                editor.putString(article_id, json.toString());
                                editor.commit();

                                // make toast
                                String toastText = "\"" + mTitle + "\" was added to bookmarks";
                                Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // delete from memory
                                detail_bookmark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_24px));

                                editor.remove(article_id);
                                editor.commit();

                                // make toast
                                String toastText = "\"" + mTitle + "\" was removed from favorites";
                                Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String transformTime(String mTime) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        LocalDateTime mDate = LocalDateTime.parse(mTime, inputFormatter).minusHours(7);

        int year = mDate.getYear();
        int month = mDate.getMonthValue();
        int day = mDate.getDayOfMonth();

        List<String> monthName = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        String mDay = (day >= 10) ? Integer.toString(day) : "0" + day;
        String mMonth = monthName.get(month - 1);

        return mDay + " " + mMonth + " " + year;


    }

}
