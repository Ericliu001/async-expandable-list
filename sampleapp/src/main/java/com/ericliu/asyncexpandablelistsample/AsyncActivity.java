package com.ericliu.asyncexpandablelistsample;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ericliu.asyncexpandablelist.CollectionView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListView;
import com.ericliu.asyncexpandablelist.async.AsyncExpandableListViewCallbacks;
import com.ericliu.asyncexpandablelist.async.AsyncHeaderViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class AsyncActivity extends Activity implements AsyncExpandableListViewCallbacks<String, News> {

    private AsyncExpandableListView<String, News> mAsyncExpandableListView;
    private CollectionView.Inventory<String, News> inventory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        mAsyncExpandableListView = (AsyncExpandableListView) findViewById(R.id.asyncExpandableCollectionView);
        mAsyncExpandableListView.setCallbacks(this);

        inventory = CollectionView.Inventory.newInstance();

        CollectionView.InventoryGroup<String, News> group1 = inventory.newGroup(0); // groupOrdinal is the smallest, displayed first
        group1.setHeaderItem("Top Stories");


        CollectionView.InventoryGroup<String, News> group2 = inventory.newGroup(2);
        group2.setHeaderItem("World");


        CollectionView.InventoryGroup<String, News> group3 = inventory.newGroup(3); // 2 is smaller than 10, displayed second
        group3.setHeaderItem("Australia");

        CollectionView.InventoryGroup<String, News> group4 = inventory.newGroup(4); // 2 is smaller than 10, displayed second
        group4.setHeaderItem("International");

        CollectionView.InventoryGroup<String, News> group5 = inventory.newGroup(5); // 2 is smaller than 10, displayed second
        group5.setHeaderItem("Businesses");

        CollectionView.InventoryGroup<String, News> group6 = inventory.newGroup(6); // 2 is smaller than 10, displayed second
        group6.setHeaderItem("Technology");

        mAsyncExpandableListView.updateInventory(inventory);
    }

    @Override
    public void onStartLoadingGroup(int groupOrdinal) {
        new LoadDataTask(groupOrdinal, mAsyncExpandableListView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }




    private static class LoadDataTask extends AsyncTask<Void, Void, Void> {

        private final int mGroupOrdinal;
        private WeakReference<AsyncExpandableListView<String, News>> listviewRef = null;

        public LoadDataTask(int groupOrdinal, AsyncExpandableListView<String, News> listview) {
            mGroupOrdinal = groupOrdinal;
            listviewRef = new WeakReference<>(listview);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            List<News> items = new ArrayList<>();
            News news = new News();
            news.setNewsTitle("Lawyers meet voluntary pro bono target for first time since 2013");
            news.setNewsBody("A voluntary target for the amount of pro bono work done by Australian lawyers has been met for the first time since 2013. Key points: The Australian Pro Bono Centre's asks lawyers to do 35 hours of free community work a year; Pro bono services can help ...\n");
            items.add(news);

            news = new News();
            news.setNewsTitle("HSC 2016: 77000 students to sit first exams across NSW");
            news.setNewsBody("More than 77,000 NSW high school students will sit their first HSC exams this week as one of the final cohorts to sit the test before the NSW government enacts sweeping reforms across the state.");
            items.add(news);

            if (listviewRef.get() != null) {
                listviewRef.get().onFinishLoadingGroup(mGroupOrdinal, items);
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item_async, parent, false);

        return new MyHeaderViewHolder(v, groupOrdinal, mAsyncExpandableListView);
    }

    @Override
    public RecyclerView.ViewHolder newCollectionItemView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.text_row_item_async, parent, false);

        return new MainActivity.NewsItemHolder(v);
    }

    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, String headerItem) {
        MyHeaderViewHolder myHeaderViewHolder = (MyHeaderViewHolder) holder;
        myHeaderViewHolder.getTextView().setText(headerItem);
    }

    @Override
    public void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, News item) {
        MainActivity.NewsItemHolder newsItemHolder = (MainActivity.NewsItemHolder) holder;
        newsItemHolder.getTextViewTitle().setText(item.getNewsTitle());
        newsItemHolder.getTextViewDescrption().setText(item.getNewsBody());
    }

    public static class MyHeaderViewHolder extends AsyncHeaderViewHolder implements AsyncExpandableListView.OnGroupStateChangeListener {

        private final TextView textView;
        private final ProgressBar mProgressBar;
        private ImageView ivExpansionIndicator;

        public MyHeaderViewHolder(View v, int groupOrdinal, AsyncExpandableListView asyncExpandableListView) {
            super(v, groupOrdinal, asyncExpandableListView);
            textView = (TextView) v.findViewById(R.id.title);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF,
                    android.graphics.PorterDuff.Mode.MULTIPLY);
            ivExpansionIndicator = (ImageView) v.findViewById(R.id.ivExpansionIndicator);
        }


        public TextView getTextView() {
            return textView;
        }


        @Override
        public void onGroupStartExpending() {
            mProgressBar.setVisibility(View.VISIBLE);
            ivExpansionIndicator.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onGroupExpanded() {
            mProgressBar.setVisibility(View.GONE);
            ivExpansionIndicator.setVisibility(View.VISIBLE);
            ivExpansionIndicator.setImageResource(R.drawable.ic_arrow_up);
        }

        @Override
        public void onGroupCollapsed() {
            mProgressBar.setVisibility(View.GONE);
            ivExpansionIndicator.setVisibility(View.VISIBLE);
            ivExpansionIndicator.setImageResource(R.drawable.ic_arrow_down);

        }
    }
}
