# async-expandable-list
============
async-expandable-list contains 2 View classes: CollectionView and AsynExpandableListView.


![Demo](https://cloud.githubusercontent.com/assets/3691022/19348717/0d6c98ec-919b-11e6-97c3-a8ff782a059b.gif)  ![Demo](https://cloud.githubusercontent.com/assets/3691022/19406879/cb982648-92da-11e6-86bf-7c82e8505e6c.gif)

Introduction
-------------------
CollectionView displays a list of headers and sub-items:
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
       
       
AsyncExpandableListView displays a list of headers and loads a sub-list under a header when a header item is clicked. The loading of sub-items can be done asynchronously and there are callbacks to populate the data into the list when it's done loading. 

 
 1. CollectionView in 3 steps
-------------------
1. Add the CollectionView to the layout.xml file where you want to display the list (Optional, population the CollectionView class in java has the same result)
```xml
<com.ericliu.asyncexpandablelist.CollectionView
        android:id="@+id/collectionView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

```

2. Pouplating data
  * find the CollectionView instance and call setCollectionCallbacks() to setup callbacks for the CollectionView, which will be responsible for creating ViewHolders and bind data into the ViewHoders - works the same as the RecyclerView.Adapter except that you don't have to worry about view types.
  * Create a CollectionView.Inventory instance, the Inventory instance represents the whole data structure that's gonna be populated into the list.
    
  
  ```java
 public class MainActivity extends Activity implements CollectionViewCallbacks<String, News> {
    private CollectionView<String, News> mCollectionView;
    private CollectionView.Inventory<String, News> inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCollectionView = (CollectionView) findViewById(R.id.collectionView);
        mCollectionView.setCollectionCallbacks(this);
        
        // the inventory represent all the whole data structure that's gonna be populated into the list.
        inventory = CollectionView.Inventory.newInstance();

  ```
  * Create InventoryGroup intances and add header item and sub-items into the InventoryGroup instance.
  Note the the newGroup(int groupOrdinal) method provided in the Inventory class requires an integer parameter: groupOrdinal.
  All the groups will be displayed in the list in an ascending order on the groupOrdinal.
    An InventoryGroup represents a header item and all sub-items under that header in the list.
    
    
  ```java
        int groupOrdinal = 0; // groupOrdinal dictates the sequence of groups to be displayed in the list
        CollectionView.InventoryGroup<String, News> group1 = inventory.newGroup(groupOrdinal); 
        
        // creating objects to be populated into the list.
        News news1 = new News();
        ...  
        News news2 = new News(); ......
        .......
        
        // set the header item, in this case, it is simply a String.
        group1.setHeaderItem("Top Stories");
        // add items under this header.
        group1.addItem(news1);
        group1.addItem(news2);
        group1.addItem(news3);
          ....
  
  ```
  
  * Call updateInventory() to display the data structure we just created
  ```java
   mCollectionView.updateInventory(inventory);
  ```
  All done, the list will display the exact header-items structure. 
  
2. AsyncExpandableListView in 3 steps
-------------------
 
1. add AsyncExpandableListView to layout.xml file where you want to display the expandable list. (Optional, population the AsyncExpandableListView class in java has the same result).
```xml
<com.ericliu.asyncexpandablelist.async.AsyncExpandableListView
        android:id="@+id/asyncExpandableCollectionView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```
2. Populating data
  * find the AsyncExpandableListView and call setCallbacks() and supply an AsyncExpandableListViewCallbacks instance to the view. The callbacks will handle the creation of ViewHolders and binding data to the ViewHolders. 
  ```java
  public class AsyncActivity extends Activity implements AsyncExpandableListViewCallbacks<String, News> {

    private AsyncExpandableListView<String, News> mAsyncExpandableListView;
    private CollectionView.Inventory<String, News> inventory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);
        mAsyncExpandableListView = (AsyncExpandableListView) findViewById(R.id.asyncExpandableCollectionView);
        mAsyncExpandableListView.setCallbacks(this);
  
  ```
  
  
  * In particular the ```java void onStartLoadingGroup(int groupOrdinal); ``` method in the AsyncExpandableListViewCallbacks will be triggered on the header item click events, which gives the client a changes to trigger loading sub-item data calls here. When the call comes back, the client should call the method ```java onFinishLoadingGroup(mGroupOrdinal, items);``` on the AsyncExpandableListView instance to display the data as well as updating UI.
  * The steps to add groups is the same as CollectionView mentioned above, but we don't need to add sub-items to groups at this step, as the code snippet showed below:
```java
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

```

3. Handle the async calls
  * Making the call to load all sub-items under a header in the method onStartLoadingGroup() in the AsyncExpandableListViewCallbacks.
  
  ```java
   @Override
    public void onStartLoadingGroup(int groupOrdinal) {
        new LoadDataTask(groupOrdinal, mAsyncExpandableListView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    ```
  * When the data come back, call ```java onFinishLoadingGroup(mGroupOrdinal, items); ``` to display data. 
  ```java
    mAsyncExpandableListView.onFinishLoadingGroup(mGroupOrdinal, items);
  ```
