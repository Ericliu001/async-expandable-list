package com.ericliu.asyncexpandablelist.async;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ericliu on 12/10/16.
 */

public abstract class AsyncHeaderViewHolder extends RecyclerView.ViewHolder implements AsyncExpandableCollectionView.OnGroupStateChangeListener {
    private final int mGroupOrdinal;
    private final AsyncExpandableCollectionView mAsyncExpandableCollectionView;

    public AsyncHeaderViewHolder(View itemView, int groupOrdinal, AsyncExpandableCollectionView asyncExpandableCollectionView) {
        super(itemView);
        mGroupOrdinal = groupOrdinal;
        mAsyncExpandableCollectionView = asyncExpandableCollectionView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncExpandableCollectionView.onGroupClicked(mGroupOrdinal);
                onItemClick(v);
            }
        });
    }

    /**
     * triggered by onClick event, to be overriden.
     * @param view
     */
    public void onItemClick(View view){}


}
