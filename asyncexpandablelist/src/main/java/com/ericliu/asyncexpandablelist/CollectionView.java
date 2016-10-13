package com.ericliu.asyncexpandablelist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Liu on 18/01/2016.
 */
public class CollectionView<T1, T2> extends RecyclerView {
    private static final String TAG = CollectionView.class.getSimpleName();
    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEW_TYPE_NON_HEADER = 10;
    protected final LinearLayoutManager mLinearLayoutManager;

    final protected Inventory<T1, T2> mInventory = new Inventory();
    private CollectionViewCallbacks<T1, T2> mCallbacks = null;
    private MyListAdapter mAdapter = null;


    public CollectionView(Context context) {
        this(context, null);
    }

    public CollectionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CollectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.setLayoutManager(mLinearLayoutManager);
        mAdapter = new MyListAdapter();
        setAdapter(mAdapter);
    }


    public void setCollectionCallbacks(CollectionViewCallbacks<T1, T2> adapter) {
        mCallbacks = adapter;
    }


    public Inventory<T1, T2> getInventory() {
        return mInventory;
    }

    public void clearInventory(){
        mInventory.mGroups.clear();
    }

    public void updateInventory() {
        mAdapter.notifyDataSetChanged();
    }


    public T1 getHeader(int groupOrdinal) {
        InventoryGroup<T1, T2> group = mInventory.mGroups.get(groupOrdinal);
        if (group != null) {
            return group.getHeaderItem();
        } else {
            return null;
        }
    }

    public void addGroup(InventoryGroup<T1, T2> group) {
        mInventory.addGroup(group);
        int itemCountBeforeGroup = mInventory.getRowCountBeforeGroup(group);

        mAdapter.notifyItemRangeInserted(itemCountBeforeGroup, group.getRowCount());
    }

    public void addItemsInGroup(int groupOrdinal, List<? extends T2> items) {
        InventoryGroup<T1, T2> group = mInventory.findGroup(groupOrdinal);
        int rowCountBeforeAddingItems = group.getRowCount();
        int itemCountBeforeGroup = mInventory.getRowCountBeforeGroup(group);

        group.addItems(items);

        mAdapter.notifyItemRangeInserted(itemCountBeforeGroup + rowCountBeforeAddingItems, items.size());
    }


    public void removeAllItemsInGroup(int groupOrdinal) {
        InventoryGroup<T1, T2> group = mInventory.findGroup(groupOrdinal);
        int itemCount = group.mItems.size();
        int itemCountBeforeGroup = mInventory.getRowCountBeforeGroup(group);
        group.mItems.clear();

        mAdapter.notifyItemRangeRemoved(itemCountBeforeGroup + 1, itemCount);

    }


    private final class MyListAdapter extends Adapter {
        private MyListAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return getRowViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            populatRoweData(holder, position);
        }

        @Override
        public int getItemViewType(int position) {
            RowInformation<T1, T2> rowInfo = computeRowContent(position);
            if (rowInfo.isComputedSuccessful) {
                if (rowInfo.isHeader) {
                    return VIEWTYPE_HEADER - mInventory.mGroups.indexOfKey(rowInfo.groupOrdinal);
                } else {
                    return VIEW_TYPE_NON_HEADER + mInventory.mGroups.indexOfKey(rowInfo.groupOrdinal);
                }

            } else {
                Log.e(TAG, "Invalid row passed to getItemViewType: " + position);
                return 0;
            }
        }


        @Override
        public int getItemCount() {
            int rowCount = 0;

            for (int i = 0; i < mInventory.mGroups.size(); i++) {
                int key = mInventory.mGroups.keyAt(i);
                InventoryGroup group = mInventory.mGroups.get(key);
                int thisGroupRowCount = group.getRowCount();
                rowCount += thisGroupRowCount;
            }

            return rowCount;
        }
    }

    protected RowInformation<T1, T2> populatRoweData(ViewHolder holder, int position) {
        if (mCallbacks == null) {
            return null;
        }

        RowInformation<T1, T2> rowInfo = computeRowContent(position);
        if (!rowInfo.isComputedSuccessful) {
            return null;
        }

        if (rowInfo.isHeader) {
            mCallbacks.bindCollectionHeaderView(getContext(), holder, rowInfo.groupOrdinal, rowInfo.group.getHeaderItem());
        } else {
            T2 item = rowInfo.group.getItem(rowInfo.positionInGroup);
            mCallbacks.bindCollectionItemView(getContext(), holder, rowInfo.groupOrdinal, item);
        }

        return rowInfo;
    }

    private ViewHolder getRowViewHolder(ViewGroup parent, final int viewType) {
        ViewHolder placeHolder = new ViewHolder(new View(getContext())) {
            @Override
            public String toString() {
                return "Invalid Item, view type: " + viewType;
            }
        };
        if (mCallbacks == null) {
            Log.e(TAG, "Call to makeRow without an adapter installed");
            return placeHolder;
        }


        ViewHolder holder;
        if (viewType <= VIEWTYPE_HEADER) {
            int groupIndex = VIEWTYPE_HEADER - viewType;
            int key = mInventory.mGroups.keyAt(groupIndex);
            int groupOrdinal = mInventory.mGroups.get(key).mOrdinal;

            // return header ViewHolder
            holder = mCallbacks.newCollectionHeaderView(getContext(), groupOrdinal, parent);
        } else {
            int groupIndex = viewType - VIEW_TYPE_NON_HEADER;
            int key = mInventory.mGroups.keyAt(groupIndex);
            int groupOrdinal = mInventory.mGroups.get(key).mOrdinal;
            // return item ViewHolder
            holder = mCallbacks.newCollectionItemView(getContext(), groupOrdinal, parent);
        }

        if (holder != null) {
            return holder;
        } else {
            return placeHolder;
        }

    }


    protected static class RowInformation<T1, T2> {
        boolean isComputedSuccessful = false;
        int row;
        boolean isHeader;
        int groupOrdinal;
        InventoryGroup<T1, T2> group;
        int positionInGroup;

        public boolean isHeader() {
            return isHeader;
        }

        public int getGroupOrdinal() {
            return groupOrdinal;
        }

        public int getPositionInGroup() {
            return positionInGroup;
        }
    }


    protected RowInformation<T1, T2> computeRowContent(int row) {
        RowInformation<T1, T2> result = new RowInformation<T1, T2>();
        int rowCounter = 0;
        int positionInGroup;


        for (int i = 0; i < mInventory.mGroups.size(); i++) {
            int key = mInventory.mGroups.keyAt(i);
            InventoryGroup<T1, T2> group = mInventory.mGroups.get(key);
            if (rowCounter == row) {
                // row is a group header
                result.isComputedSuccessful = true;
                result.row = row;
                result.isHeader = true;
                result.groupOrdinal = group.mOrdinal;
                result.group = group;
                result.positionInGroup = -1;
                return result;
            }
            rowCounter++; // incremented by 1 because it just past the Header row

            positionInGroup = 0;
            while (positionInGroup < group.mItems.size()) {
                if (rowCounter == row) {
                    // this is the row we are looking for
                    result.isComputedSuccessful = true;
                    result.row = row;
                    result.isHeader = false;
                    result.groupOrdinal = group.mOrdinal;
                    result.group = group;
                    result.positionInGroup = positionInGroup;
                    return result;
                }

                // move to the next row
                positionInGroup++;
                rowCounter++;
            }
        }

        return result;
    }


    /**
     * Represents a group of items with a header to be displayed in the {@link CollectionView}.
     */
    public final static class InventoryGroup<T1, T2> {

        final private int mOrdinal;

        private T1 mHeaderItem;
        private ArrayList<T2> mItems = new ArrayList<>();


        private InventoryGroup(int oridinal) {
            mOrdinal = oridinal;
        }


        public int getOrdinal() {
            return mOrdinal;
        }

        public T1 getHeaderItem() {
            return mHeaderItem;
        }

        public InventoryGroup setHeaderItem(T1 headerItem) {
            mHeaderItem = headerItem;
            return this;
        }


        public void addItem(T2 item) {
            mItems.add(item);
        }


        public int getRowCount() {
            return 1 + mItems.size();
        }


        public T2 getItem(int index) {
            return mItems.get(index);
        }

        public void addItems(List<? extends T2> items) {
            mItems.addAll(items);
        }


    }


    /**
     * Represents the data of the items to display in the {@link CollectionView}.
     * This is defined as a list of {@link InventoryGroup} which represents a group of items with a
     * header.
     */
    public final static class Inventory<T1, T2> {
        private SparseArray<InventoryGroup<T1, T2>> mGroups = new SparseArray<>();


        private Inventory() {
        }

        private void addGroup(InventoryGroup<T1, T2> group) {
            mGroups.put(group.mOrdinal, group);
        }

        public InventoryGroup<T1, T2> newGroup(int groupOrdinal) {
            InventoryGroup<T1, T2> group = new InventoryGroup(groupOrdinal);
            addGroup(group);
            return group;
        }

        public InventoryGroup<T1, T2> putGroup(int groupOrdinal) {
            return newGroup(groupOrdinal);
        }


        private InventoryGroup findGroup(int groupOrdinal) {
            return mGroups.get(groupOrdinal);
        }


        public int getTotalItemCount() {
            int total = 0;

            for (int i = 0; i < mGroups.size(); i++) {
                int key = mGroups.keyAt(i);
                total += mGroups.get(key).mItems.size();
            }
            return total;
        }

        public int getGroupCount() {
            return mGroups.size();
        }

        public int getGroupIndex(int groupOrdinal) {
            for (int i = 0; i < mGroups.size(); i++) {
                int key = mGroups.keyAt(i);
                if (mGroups.get(key).mOrdinal == groupOrdinal) {
                    return i;
                }
            }
            return -1;
        }

        public int getRowCountBeforeGroup(InventoryGroup group) {
            return getRowCountBeforeGroup(group.mOrdinal);
        }

        public int getRowCountBeforeGroup(int groupOrdinal) {
            int count = 0;
            for (int i = 0; i < mGroups.size(); i++) {
                if (groupOrdinal == mGroups.keyAt(i)) {
                    break;
                }
                int key = mGroups.keyAt(i);
                count += mGroups.get(key).getRowCount();
            }
            return count;
        }


        public SparseArray<InventoryGroup<T1, T2>> getGroups() {
            return mGroups;
        }
    }
}
