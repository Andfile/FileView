# FileView
Simple library to show large files using Jetpack Paging Library

<img src=https://user-images.githubusercontent.com/3678050/61577487-8dd61000-aaf0-11e9-91a2-f341d0324894.png width="175" height="300">


## **how to use**

in code 
```
        fileView = findViewById(R.id.fileview)
        fileView.setFilename("/path/to/file")
                .setLayoutRes(R.layout.fileview_item)
                .setPageSize(50)
                .scrollTop()
      
```
in xml 

Example of **fileview_item.xml**
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <TextView
        android:id="@+id/line"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="#303030"
        android:layout_marginLeft="5dp"
        android:layout_marginRight="5dp"
        android:textSize="16sp" />
</LinearLayout>
```
**main_activity.xml**

```
<com.ydn.fileview.FileView
        android:id="@+id/fileview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="100dp" />
```          
  ### **Sample application**

Sample application is also available in the **releases** section      	

  ### **Developed By**
  - Dmitry Yablokov - [dnyablokov@gmail.com](mailto:dnyablokov@gmail.com)


  ### **License**
```      

Copyright 2019 Dmitry Yablokov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```      

