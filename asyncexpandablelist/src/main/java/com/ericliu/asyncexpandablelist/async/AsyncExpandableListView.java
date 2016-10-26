package com.ericliu.asyncexpandablelist.async;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.ericliu.asyncexpandablelist.CollectionView;
import com.ericliu.asyncexpandablelist.CollectionViewCallbacks;


/**
 * Created by ericliu on 6/10/2016.
 */

public class AsyncExpandableListView<T1, T2> extends CollectionView<T1, T2> {

    private AsyncExpandableListViewCallbacks<T1, T2> mCallbacks;
    protected Map<Integer, OnGroupStateChangeListener> mOnGroupStateChangeListeners = new ArrayMap<>();
    protected int expandedGroupOrdinal = -1;


    public AsyncExpandableListView(Context context) {
        super(context);
    }

    public AsyncExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected CollectionView.RowInformation<T1, T2> populatRoweData(RecyclerView.ViewHolder holder, int position) {
        CollectionView.RowInformation<T1, T2> rowInfo = super.populatRoweData(holder, position);
        if (rowInfo.isHeader()) {
            mOnGroupStateChangeListeners.put(rowInfo.getGroupOrdinal(), (AsyncHeaderViewHolder) holder);
        }

        return rowInfo;
    }

    public interface OnGroupStateChangeListener {

        void onGroupStartExpending();

        void onGroupExpanded();

        void onGroupCollapsed();
    }


    public void onGroupClicked(int groupOrdinal) {
        if (groupOrdinal != expandedGroupOrdinal) {
            onStartExpandingGroup(groupOrdinal);
        } else {
            collapseGroup(groupOrdinal);
        }

    }


    public void setCallbacks(final AsyncExpandableListViewCallbacks<T1, T2> callbacks) {
        CollectionViewCallbacks<T1, T2> collectionViewCallbacks = new CollectionViewCallbacks<T1, T2>() {
            @Override
            public ViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent) {
                return callbacks.newCollectionHeaderView(context, groupOrdinal, parent);
            }

            @Override
            public ViewHolder newCollectionItemView(Context context, int groupOrdinal, ViewGroup parent) {
                return callbacks.newCollectionItemView(context, groupOrdinal, parent);
            }

            @Override
            public void bindCollectionHeaderView(Context context, ViewHolder holder, int groupOrdinal, T1 headerItem) {
                callbacks.bindCollectionHeaderView(context, (AsyncHeaderViewHolder) holder, groupOrdinal, headerItem);
            }

            @Override
            public void bindCollectionItemView(Context context, ViewHolder holder, int groupOrdinal, T2 item) {
                callbacks.bindCollectionItemView(context, holder, groupOrdinal, item);
            }
        };
        setCollectionCallbacks(collectionViewCallbacks);
        mCallbacks = callbacks;
    }

    protected void collapseGroup(int groupOrdinal) {
        expandedGroupOrdinal = -1;
        removeAllItemsInGroup(groupOrdinal);
        OnGroupStateChangeListener onGroupStateChangeListener = mOnGroupStateChangeListeners.get(groupOrdinal);
        if (onGroupStateChangeListener != null) {
            onGroupStateChangeListener.onGroupCollapsed();
        }
    }


    protected void onStartExpandingGroup(int groupOrdinal) {
        int ordinal = 0;
        for (int i = 0; i < mInventory.getGroups().size(); i++) {
            ordinal = mInventory.getGroups().keyAt(i);
            if (ordinal != groupOrdinal) {
                collapseGroup(ordinal);
            }
        }

        expandedGroupOrdinal = groupOrdinal;
        mCallbacks.onStartLoadingGroup(groupOrdinal);
        OnGroupStateChangeListener onGroupStateChangeListener = mOnGroupStateChangeListeners.get(groupOrdinal);
        if (onGroupStateChangeListener != null) {
            onGroupStateChangeListener.onGroupStartExpending();
        }
    }


    public boolean onFinishLoadingGroup(int groupOrdinal, List<T2> items) {
        if (expandedGroupOrdinal < 0 || groupOrdinal != expandedGroupOrdinal) {
            return false;
        }

        addItemsInGroup(expandedGroupOrdinal, items);
        OnGroupStateChangeListener onGroupStateChangeListener = mOnGroupStateChangeListeners.get(groupOrdinal);
        if (onGroupStateChangeListener != null) {
            onGroupStateChangeListener.onGroupExpanded();
        }

        return true;
    }

}
