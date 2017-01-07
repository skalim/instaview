# InstaView
### Full screen image viewer for Instagram.
<img src="https://lh3.googleusercontent.com/QveoFSrfzwiKhotcjFKWbAWhbdqqjpS55FxPIc8dlMvyMvNOnnDhsYG439Vpxco9kjjep0roQSB-xGLyayYZ1mNxIgkktjOZOwuj5sVnpoioZSTWZ1-7dNHPMUAMbhepdQ0PLA" height="526" width="300" >

### SwipingImagesView
To use the image viewer custom view in your project, add the following gradle dependency:
```gradle
compile 'io.github.skalim:swiping-images-view:1.0.0'
```

Add the view to your layout
```xml
<io.github.skalim.swipeview.SwipingImagesView
        android:id="@+id/swiping_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

Add image urls to show in the view
```java
swipingImagesView.addImagePaths(paths);
```



