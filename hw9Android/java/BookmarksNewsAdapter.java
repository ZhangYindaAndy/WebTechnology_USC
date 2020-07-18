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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BookmarksNewsAdapter extends RecyclerView.Adapter<BookmarksNewsAdapter.ViewHolder> {

    private Context mContext;
    private View mView;

    private ArrayList<String> mTitles;
    private ArrayList<String> mImages;
    private ArrayList<String> mSections;
    private ArrayList<String> mIds;
    private ArrayList<String> mUrls;
    private ArrayList<String> mDates;

    private SharedPreferences pref;


    public BookmarksNewsAdapter(Context mContext, View mView, ArrayList<String> mTitles, ArrayList<String> mImages,
                                   ArrayList<String> mSections, ArrayList<String> mIds, ArrayList<String> mUrls, ArrayList<String> mDates) {
        this.mContext = mContext;
        this.mView = mView;

        this.mTitles = mTitles;
        this.mImages = mImages;
        this.mSections = mSections;
        this.mIds = mIds;
        this.mUrls = mUrls;
        this.mDates = mDates;

        this.pref = mContext.getSharedPreferences("NewsApp", 0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bookmarks, parent, false);
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
        holder.time.setText(transformDate(mDates.get(position)));
        holder.section.setText(mSections.get(position));

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

                final Dialog dialog = new Dialog(mContext);
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
                    @Override
                    public void onClick(View v) {

                        // delete the card and close dialog
                        dialog.dismiss();
                        removeItem(position);
                    }
                });

                dialog.show();
                return true;
            }
        });

        holder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("removeItem", Integer.toString(position));
//                Log.d("removeItem", "size " + Integer.toString(getItemCount()));

                // delete the card
                removeItem(position);
            }
        });
    }

    @SuppressLint("ApplySharedPref")
    private void removeItem(int position) {
        // delete from memory
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
        editor.remove(mIds.get(position));
        editor.commit();

        // make toast
        String toastText = "\"" + mTitles.get(position) + "\" was removed from favorites";
        Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();

        mTitles.remove(position);
        mImages.remove(position);
        mSections.remove(position);
        mIds.remove(position);
        mUrls.remove(position);
        mDates.remove(position);

        notifyItemRemoved(position);
        // notify (will reindex the card), or will get index error
        notifyDataSetChanged();
//        notifyItemRangeRemoved(position, getItemCount());

        // show no bookmark text
        if(getItemCount() == 0)
            mView.findViewById(R.id.bookmarks_no_content).setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String transformDate(String mTime) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        LocalDateTime mDate = LocalDateTime.parse(mTime, inputFormatter).minusHours(7);

        int month = mDate.getMonthValue();
        int day = mDate.getDayOfMonth();

        List<String> monthName = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        String mDay = (day >= 10) ? Integer.toString(day) : "0" + day;
        String mMonth = monthName.get(month - 1);

        return mDay + " " + mMonth;
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
            image = itemView.findViewById(R.id.bookmarks_news_image);
            title = itemView.findViewById(R.id.bookmarks_news_title);
            card = itemView.findViewById(R.id.bookmarks_card);
            time = itemView.findViewById(R.id.bookmarks_news_time);
            section = itemView.findViewById(R.id.bookmarks_news_section);
            icon = itemView.findViewById(R.id.bookmarks_bookmark_icon);
        }
    }


}





