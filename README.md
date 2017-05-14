Bubble-ImageView
======================
### ScaleType

Working exactly same as imageView.Feel free to use all the scale types and adjustViewBounds.

![Example](images/center_crop.jpg "working example")![Example](images/center_inside.jpg "working example")![Example](images/fit_xy.jpg "working example")![Example](images/fit_end.jpg "working example")

### Customize

You can cutstomize all the attributes below.

| attrs          | values                | description                              |
| :------------- | --------------------- | ---------------------------------------- |
| borderColor    | color                 | color of border line                     |
| offset         | dimension             | offset of the arrow (top to bottom, left to right) |
| borderWidth    | dimension             | width of border line                     |
| radius         | dimension             | radius of corner                         |
| orientation    | left,right,top,bottom | orientation of the arrow                 |
| triangleWidth  | dimension             | width of the arrow                       |
| triangleHeight | dimension             | height of the arrow                      |



## Download

Add the dependency to your project build.gradle file:

```Java
compile 'rouchuan.bubbleview:bubbleImageView:1.0.0'
```
### Usage

You can simply use it in xml file:

```xml
<com.ruochuan.bubbleview.BubbleImageView
        android:id="@+id/bubble"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:adjustViewBounds="true"
        android:src="@drawable/masami"
        app:orientation="top"/>
```

### Things need to be done

1. Bubble-Viewgroup
2. Support shadow effect

## License ##

    Copyright 2017 shenruochuan
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.