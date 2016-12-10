package saad.com.instaview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SwipingImagesView extends FrameLayout {

    private float thresholdRightX;
    private float thresholdLeftX;

    private ImageView imageViewForeground;
    private ImageView imageViewBackground;
    private ArrayList<String> imagePaths;

    private int screenWidth;
    private float startX, startY, currentX, currentY, dx, degreesToRotate;
    private boolean isSwiping;

    public SwipingImagesView(Context context) {
        super(context);
        init();
    }

    public SwipingImagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipingImagesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        isSwiping = false;
        imagePaths = new ArrayList<>();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageViewForeground = new ImageView(getContext());
        imageViewForeground.setLayoutParams(layoutParams);
        imageViewBackground = new ImageView(getContext());
        imageViewBackground.setLayoutParams(layoutParams);
        addView(imageViewBackground);
        addView(imageViewForeground);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        thresholdRightX = screenWidth - 100f;
        thresholdLeftX = 100f;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean toReturn;
        switch (event.getAction()){
            case (MotionEvent.ACTION_DOWN) :
                isSwiping = true;
                startX = event.getRawX();
                startY = event.getRawY();
                currentX = event.getRawX();
                currentY = event.getRawY();
                toReturn = true;
                break;
            case (MotionEvent.ACTION_MOVE) :
                isSwiping = true;
                currentX = event.getRawX();
                currentY = event.getRawY();
                toReturn = true;
                break;
            case (MotionEvent.ACTION_UP) :
                isSwiping = false;
                toReturn = true;
                break;
            default :
                isSwiping = false;
                toReturn = super.onTouchEvent(event);
        }

        if( isSwiping ){
            animateImage();
        } else {
            resetCoords();
            resetImagePosition();
        }

        return toReturn;
    }

    private void resetCoords(){
        startX = 0;
        startY = 0;
        currentY = 0;
        currentY = 0;
    }

    private void resetImagePosition(){
        Log.d("", "");
        imageViewForeground.setX(0);
        imageViewForeground.setRotation(0);
    }

    private void animateImage(){
        Log.d("", "");
        if( currentX > thresholdRightX ){
            swipedRight();
        } else if( currentX < thresholdLeftX ){
            swipedLeft();
        }

        dx = currentX - startX;
        imageViewForeground.setX(dx);
        degreesToRotate = scale(dx, 0, screenWidth/2, 0, 15);
        imageViewForeground.setRotation(degreesToRotate);
    }

    private float scale(final float valueIn, final float baseMin, final float baseMax, final float limitMin, final float limitMax) {
        return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    private void swipedRight(){

    }

    private void swipedLeft(){

    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
        if( !imagePaths.isEmpty() ){
            Picasso.with(getContext()).load(imagePaths.get(0)).into(imageViewForeground);
        }

        if( imagePaths.size() > 1 ){
            Picasso.with(getContext()).load(imagePaths.get(1)).into(imageViewBackground);
        }
    }
}
