package com.ericliu.asyncexpandablelist.async;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by ericliu on 11/10/16.
 */

public interface AsyncExpandableListViewCallbacks<T1, T2> {

    void onStartLoadingGroup(int groupOrdinal);

    /**
     * Returns a new custom View that will be used for each of the collection group headers.
     */
    AsyncHeaderViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent);


    /**
     * Returns a new custom View that will be used for each of the collection item.
     *
     * @param context
     * @param groupOrdinal - the groupOrdinal decides the sequence of groups being displayed, the smallest int is displayed first and in an asending order
     * @param parent
     * @return
     */
    RecyclerView.ViewHolder newCollectionItemView(Context context, int groupOrdinal, ViewGroup parent);

    /**
     * Binds the given data (like the header label) with the given collection group header View.
     */
    void bindCollectionHeaderView(Context context, AsyncHeaderViewHolder holder, int groupOrdinal, T1 headerItem);

    /**
     * Binds the given data with the given collection item View.
     */
    void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, T2 item);

}

