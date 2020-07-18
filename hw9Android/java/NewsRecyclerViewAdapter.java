package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mTitles;
    private ArrayList<String> mImages;
    private ArrayList<String> mTimes;
    private ArrayList<String> mSections;
    private ArrayList<String> mIds;
    private ArrayList<String> mUrls;
    private ArrayList<String> mDates;

    private SharedPreferences pref;

    public NewsRecyclerViewAdapter(Context mContext, ArrayList<String> mTitles, ArrayList<String> mImages, ArrayList<String> mTimes,
                                   ArrayList<String> mSections, ArrayList<String> mIds, ArrayList<String> mUrls, ArrayList<String> mDates) {
        this.mContext = mContext;
        this.mTitles = mTitles;
        this.mImages = mImages;
        this.mTimes = mTimes;
        this.mSections = mSections;
        this.mIds = mIds;
        this.mUrls = mUrls;
        this.mDates = mDates;

        this.pref = mContext.getSharedPreferences("NewsApp", 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_news, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
        holder.title.setText(mTitles.get(position));
        holder.time.setText(transformDate(mTimes.get(position)));
        holder.section.setText(mSections.get(position));

        String id = pref.getString(mIds.get(position), "");
        if(!id.equals("")) // saved before
            holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));
        else
            holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_24px));

        // click then jump to detail page
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // jump to another activity
                Intent intent = new Intent(mContext, DetailPageActivity.class);
                intent.putExtra("article-id", mIds.get(position));
                mContext.startActivity(intent);

            }
        });

        // long click show dialog
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_news);

                TextView dialog_title =  dialog.findViewById(R.id.dialog_title);
                ImageView dialog_image = dialog.findViewById(R.id.dialog_image);
                ImageView dialog_twitter = dialog.findViewById(R.id.dialog_twitter);
                final ImageView dialog_bookmark = dialog.findViewById(R.id.dialog_bookmark);

                Glide.with(mContext)
                        .asBitmap()
                        .load(mImages.get(position))
                        .into(dialog_image);
                dialog_title.setText(mTitles.get(position));

                // set dialog bookmark
                String id = pref.getString(mIds.get(position), "");
                if(!id.equals("")) // saved before
                    dialog_bookmark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));

                dialog_twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "http://www.twitter.com/intent/tweet?";
                        url += "text=Check out this Link:&";
                        url += "url=" + mUrls.get(position) + "&";
                        url += "hashtags=CSCI571NewsSearch";

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mContext.startActivity(intent);
                    }
                });

                dialog_bookmark.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onClick(View v) {
                        String saved_info = pref.getString(mIds.get(position), "");
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
                        // Log.d("bookmark", saved_info);

                        if(saved_info.equals("")) {
                            // add into memory and change icon
                            holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));
                            dialog_bookmark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));

                            String json_string = transformJson(position);
//                            Log.d("bookmark", json_string);

                            editor.putString(mIds.get(position), json_string);
                            editor.commit();

                            // make toast
                            String toastText = "\"" + mTitles.get(position) + "\" was added to bookmarks";
                            Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // delete from memory
                            holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_24px));
                            dialog_bookmark.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_24px));

                            editor.remove(mIds.get(position));
                            editor.commit();

                            // make toast
                            String toastText = "\"" + mTitles.get(position) + "\" was removed from favorites";
                            Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
                return true;
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onClick(View v) {
                String saved_info = pref.getString(mIds.get(position), "");
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
//                Log.d("bookmark", saved_info);

                if(saved_info.equals("")) {
                    // add into memory and change icon
                    holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_24px));

                    String json_string = transformJson(position);
//                    Log.d("bookmark", json_string);

                    editor.putString(mIds.get(position), json_string);
                    editor.commit();

                    // make toast
                    String toastText = "\"" + mTitles.get(position) + "\" was added to bookmarks";
                    Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                }
                else {
                    // delete from memory
                    holder.icon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_bookmark_border_24px));

                    editor.remove(mIds.get(position));
                    editor.commit();

                    // make toast
                    String toastText = "\"" + mTitles.get(position) + "\" was removed from favorites";
                    Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String transformJson(int position) {
        Map<String, String> card_data = new HashMap<String, String>();

        card_data.put("image", mImages.get(position));
        card_data.put("title", mTitles.get(position));
        card_data.put("section", mSections.get(position));
        card_data.put("date", mTimes.get(position));
        card_data.put("url", mUrls.get(position));
        JSONObject json = new JSONObject(card_data);

        return json.toString();
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;
        TextView time;
        TextView section;
        RelativeLayout card;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.news_image);
            title = itemView.findViewById(R.id.news_title);
            card = itemView.findViewById(R.id.news_card);
            time = itemView.findViewById(R.id.news_time);
            section = itemView.findViewById(R.id.news_section);
            icon = itemView.findViewById(R.id.bookmark_icon);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String transformDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        LocalDateTime mDate = LocalDateTime.parse(date, inputFormatter);

        ZoneId zoneId = ZoneId.of("America/Los_Angeles");
        ZonedDateTime ZonedDate = mDate.atZone(zoneId);

        LocalDateTime localTime = LocalDateTime.now();
        ZonedDateTime ZonedLocalTime = localTime.atZone(zoneId);

        int duration = (int)Duration.between(ZonedDate, ZonedLocalTime).getSeconds() + 5 * 3600;

        if(duration < 60)
            return duration + "s ago";
        else if(duration < 3600)
            return duration / 60 + "m ago";
        else
            return duration / 3600 + "h ago";
    }
}





