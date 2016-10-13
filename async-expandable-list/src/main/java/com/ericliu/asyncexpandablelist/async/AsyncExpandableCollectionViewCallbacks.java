package com.ericliu.asyncexpandablelist.async;


import com.ericliu.asyncexpandablelist.CollectionViewCallbacks;

/**
 * Created by ericliu on 11/10/16.
 */

public interface AsyncExpandableCollectionViewCallbacks<T1, T2> extends CollectionViewCallbacks<T1, T2> {

    void onStartLoadingGroup(int groupOrdinal);



}

