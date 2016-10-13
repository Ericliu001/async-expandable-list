/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package com.ericliu.asyncexpandablelist.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.collectionview.R;


/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p/>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends Activity implements CollectionViewCallbacks<String, News> {

    public static final String TAG = "MainActivity";


    private CollectionView<String, News> mCollectionView;
    private CollectionView.Inventory<String, News> inventory;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCollectionView = (CollectionView) findViewById(R.id.collectionView);


        inventory = new CollectionView.Inventory();

        CollectionView.InventoryGroup<String, News> group1 = inventory.newGroup(0); // groupOrdinal is the smallest, displayed first
        News news;

        group1.setHeaderItem("Top Stories");
        news = new News();
        news.setNewsTitle("Australian Police Arrest 2 Sydney Teens, Seize Knives");
        news.setNewsBody("SYDNEY - Australian police arrested two teenagers and seized knives in Sydney on Wednesday as the country marked the 14th anniversary of extremist bombings in Indonesia that killed 202, including 88 Australians, police said.");
        group1.addItem(news);

        news = new News();
        news.setNewsTitle("Queensland report outlines 50 per cent renewable energy map");
        news.setNewsBody("The security of Queensland's power supply won't be undermined by a government target of 50 per cent renewable energy by 2030, Energy Minister Mark Bailey says.");
        group1.addItem(news);

        news = new News();
        news.setNewsTitle("VB and sheep farms, Lee brings the laughs");
        news.setNewsBody("VB beer, barbecues and sheep farms - they're not the kind of things you expect to hear from a visiting leader in a formal speech to federal parliament.");
        group1.addItem(news);

        CollectionView.InventoryGroup<String, News> group2 = inventory.newGroup(2);
        group2.setHeaderItem("World");

        news = new News();
        news.setNewsTitle("'Sanctions brought nothing': German politicians call for rapprochement with Russia");
        news.setNewsBody("The current policy of “saber rattling” should not continue, Erwin Sellering, the prime minister of the German state of Mecklenburg-Western Pomerania, told Germany's weekly Welt am Sonntag newspaper, as he called for lifting anti-Russian sanctions.");
        group2.addItem(news);

        news = new News();
        news.setNewsTitle("Thai prince to visit ailing king, hospital says as health fears grow");
        news.setNewsBody("Thai King Bhumibol Adulyadej attends a parade to mark his 81st birthday in Bangkok, Thailand, 02 December 2008. (AAP ). Previous Next Show Grid.");
        group2.addItem(news);



        CollectionView.InventoryGroup<String, News> group3 = inventory.newGroup(3); // 2 is smaller than 10, displayed second
        group3.setHeaderItem("Australia");

        news = new News();
        news.setNewsTitle("'I beg you, do not just put it in the filing cabinet', witness pleads at NT Royal Commission");
        news.setNewsBody("The survival of Indigenous people in the Northern Territory depended on the outcome of the Royal Commission into Child Protection and Detention, a witness told the commission hearing in Darwin.");
        group3.addItem(news);

        news = new News();
        news.setNewsTitle("HSC 2016: 77000 students to sit first exams across NSW");
        news.setNewsBody("More than 77,000 NSW high school students will sit their first HSC exams this week as one of the final cohorts to sit the test before the NSW government enacts sweeping reforms across the state.");
        group3.addItem(news);


        news = new News();
        news.setNewsTitle("Lawyers meet voluntary pro bono target for first time since 2013");
        news.setNewsBody("A voluntary target for the amount of pro bono work done by Australian lawyers has been met for the first time since 2013. Key points: The Australian Pro Bono Centre's asks lawyers to do 35 hours of free community work a year; Pro bono services can help ...\n");
        group3.addItem(news);

        mCollectionView.setCollectionCallbacks(this);
        mCollectionView.updateInventory(inventory);

    }


    @Override
    public RecyclerView.ViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.header_row_item, parent, false);

        return new TitleHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder newCollectionItemView(Context context, int groupOrdinal, ViewGroup parent) {
        // Create a new view.
        View v = LayoutInflater.from(context)
                .inflate(R.layout.text_row_item, parent, false);

        return new NewsItemHolder(v);
    }

    @Override
    public void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, String headerItem) {
        ((TitleHolder) holder).getTextView().setText((String) headerItem);
    }

    @Override
    public void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, News item) {
        NewsItemHolder newsItemHolder = (NewsItemHolder) holder;
        newsItemHolder.getTextViewTitle().setText(item.getNewsTitle());
        newsItemHolder.getTextViewDescrption().setText(item.getNewsBody());
    }


    public static class TitleHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public TitleHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public static class NewsItemHolder extends RecyclerView.ViewHolder {


        private final TextView tvTitle;
        private final TextView tvDescription;

        public NewsItemHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            tvTitle = (TextView) v.findViewById(R.id.title);
            tvDescription = (TextView) v.findViewById(R.id.description);
        }

        public TextView getTextViewTitle() {
            return tvTitle;
        }

        public TextView getTextViewDescrption() {
            return tvDescription;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.launch) {
            Intent intent = new Intent(this, AsyncActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

