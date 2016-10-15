# async-expandable-list
============
async-expandable-list contains 2 View classes: CollectionView and AsynExpandableListView.


![Demo](https://cloud.githubusercontent.com/assets/3691022/19348717/0d6c98ec-919b-11e6-97c3-a8ff782a059b.gif)  ![Demo](https://cloud.githubusercontent.com/assets/3691022/19406879/cb982648-92da-11e6-86bf-7c82e8505e6c.gif)

Introduction
-------------------
CollectionView handles display of a list in header-items struture:
   * Header A
       * item a1
       * item a2
   * Header B
       * item b1
       * item b2
       * item b3
   * Header C
       * item c1
       * item c2
       * item c3
       
       
AsyncExpandableListView displays a list of headers and loads a sub-list under a header when a header item is clicked.

 
 CollectionView in 3 steps
-------------------
1. Add the CollectionView to the layout.xml file where you want to display the list (Optional, population the CollectionView class in java has the same result)
```xml
<com.ericliu.asyncexpandablelist.CollectionView
        android:id="@+id/collectionView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

```

2. Pouplating data
  * 
