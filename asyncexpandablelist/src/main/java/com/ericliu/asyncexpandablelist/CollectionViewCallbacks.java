package com.ericliu.asyncexpandablelist;/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Defines an interface to the callbacks that a {@link CollectionView} will be called to create each
 * elements of the collection.
 */
public interface CollectionViewCallbacks<T1, T2> {

    /**
     * Returns a new custom View that will be used for each of the collection group headers.
     */
    RecyclerView.ViewHolder newCollectionHeaderView(Context context, int groupOrdinal, ViewGroup parent);


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
    void bindCollectionHeaderView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, T1 headerItem);

    /**
     * Binds the given data with the given collection item View.
     */
    void bindCollectionItemView(Context context, RecyclerView.ViewHolder holder, int groupOrdinal, T2 item);


}
