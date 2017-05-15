Bubble-ImageView
======================
## [English](README_EN.md)

### ScaleType

支持所有scaleType以及adjustViewBounds，与原生ImageView行为完全一致。

![Example](images/center_crop.jpg "working example")![Example](images/center_inside.jpg "working example")![Example](images/fit_xy.jpg "working example")![Example](images/fit_end.jpg "working example")

### 自定义

![](images/custom.gif)

支持自定义以下几种属性

| attrs          | values                | description        |
| :------------- | --------------------- | ------------------ |
| borderColor    | color                 | 边线颜色               |
| offset         | dimension             | 箭头位置偏移量（从上到下，从左到右） |
| borderWidth    | dimension             | 边线宽度               |
| radius         | dimension             | 圆角半径               |
| orientation    | left,right,top,bottom | 箭头朝向               |
| triangleWidth  | dimension             | 箭头底边长              |
| triangleHeight | dimension             | 箭头高度               |



### 安装

在build.gradle 文件下加入以下代码:

```Java
compile 'rouchuan.bubbleview:bubbleImageView:1.0.1'
```
### 使用

直接在 xml 文件中引用即可:

```xml
<com.ruochuan.bubbleview.BubbleImageView
        android:id="@+id/bubble"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:adjustViewBounds="true"
        android:src="@drawable/masami"
        app:orientation="top"/>
```

### 接下来要做的事

1. Bubble-ViewGroup 
2. 支持阴影

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