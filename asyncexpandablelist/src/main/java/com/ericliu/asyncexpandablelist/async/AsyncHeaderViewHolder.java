package com.ericliu.asyncexpandablelist.async;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ericliu on 12/10/16.
 */

public abstract class AsyncHeaderViewHolder extends RecyclerView.ViewHolder implements AsyncExpandableListView.OnGroupStateChangeListener {
    private final int mGroupOrdinal;
    private final AsyncExpandableListView mAsyncExpandableListView;

    public AsyncHeaderViewHolder(View itemView, int groupOrdinal, AsyncExpandableListView asyncExpandableListView) {
        super(itemView);
        mGroupOrdinal = groupOrdinal;
        mAsyncExpandableListView = asyncExpandableListView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAsyncExpandableListView.onGroupClicked(mGroupOrdinal);
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
