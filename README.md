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
  * find the view instance and call setCollectionCallbacks() to setup callbacks for the CollectionView, which will be responsible for creating ViewHolders and bind data into the ViewHoders.
    Get a CollectionView.Inventory instance from the view, the Inventory instance represents the whole data structure that's gonna be populated into the list.
    
  
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
        inventory = mCollectionView.getInventory();

  ```
  * Create InventoryGroup intances and add header item and sub-items into the InventoryGroup instance.
    An InventoryGroup represents a header item and all sub-items under that header in the list.
    
    
  ```java
  CollectionView.InventoryGroup<String, News> group1 = inventory.newGroup(0); // groupOrdinal is the smallest, displayed first
        
        // creating objects to be populated into the list.
        News news1 = new News();
        news.setNewsTitle("Australian Police Arrest 2 Sydney Teens, Seize Knives");
        news.setNewsBody("SYDNEY - Australian police arreste....... killed 202, including 88 Australians, police said.");  
        
        
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
  
  
