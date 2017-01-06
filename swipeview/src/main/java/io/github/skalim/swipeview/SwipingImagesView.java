package io.github.skalim.swipeview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SwipingImagesView extends FrameLayout implements Animation.AnimationListener {

    interface OnSwipeListener {
        void onSwipeRight(int pos);

        void onSwipeLeft(int pos);
    }

    private float thresholdRightX;
    private float thresholdLeftX;

    private ImageView imageViewForeground, imageViewBackground;
    private View emptyView;
    private ArrayList<String> imagePaths;

    private int screenWidth, currentItemIndex;
    private float startX, startY, currentX, currentY, dx, degreesToRotate, alpha;
    private boolean isSwiping, swipedOut, showingLastImage;
    private OnSwipeListener onSwipeListener;

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

    private void init() {
        isSwiping = false;
        swipedOut = false;
        showingLastImage = false;
        currentItemIndex = 0;
        imagePaths = new ArrayList<>();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageViewForeground = new ImageView(getContext());
        imageViewForeground.setLayoutParams(layoutParams);
        imageViewBackground = new ImageView(getContext());
        imageViewBackground.setLayoutParams(layoutParams);
        imageViewForeground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);
        emptyView = new TextView(getContext());
        TextView emptyTextView = (TextView) emptyView;
        emptyTextView.setText("No images to show");
        emptyTextView.setTextSize(20);
        emptyTextView.setGravity(Gravity.CENTER);
        addView(emptyTextView);
        addView(imageViewBackground);
        addView(imageViewForeground);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        thresholdRightX = screenWidth - 200f;
        thresholdLeftX = 200f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if( imageViewForeground.getAnimation() != null && !imageViewForeground.getAnimation().hasEnded() ){
            // Ignore touch events if animation is playing
            return false;
        }

        boolean toReturn;
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                isSwiping = true;
                swipedOut = false;
                startX = event.getRawX();
                startY = event.getRawY();
                currentX = event.getRawX();
                currentY = event.getRawY();
                toReturn = true;
                break;
            case (MotionEvent.ACTION_MOVE):
                isSwiping = true;
                swipedOut = false;
                currentX = event.getRawX();
                currentY = event.getRawY();
                toReturn = true;
                break;
            case (MotionEvent.ACTION_UP):
                isSwiping = false;
                if (currentX > thresholdRightX) {
                    swipedOut = true;
                    swipedRight();
                } else if (currentX < thresholdLeftX) {
                    swipedOut = true;
                    swipedLeft();
                }
                toReturn = true;
                break;
            default:
                isSwiping = false;
                swipedOut = false;
                toReturn = super.onTouchEvent(event);
        }

        if (isSwiping) {
            moveImageWithSwipe();
        } else if (!swipedOut) {
            resetCoords();
            resetImage();
        }

        return toReturn;
    }

    private void resetCoords() {
        startX = 0;
        startY = 0;
        currentY = 0;
        currentY = 0;
    }

    private void resetImage() {
        // Animate image to default position
        imageViewForeground.animate()
                .translationX(0f)
                .rotation(0)
                .alpha(1)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator()).start();
    }

    private void resetAll() {
        imageViewForeground.setX(0);
        imageViewForeground.setRotation(0);
        imageViewForeground.setAlpha(1f);
        imageViewBackground.setX(0);
        imageViewBackground.setRotation(0);
        imageViewBackground.setAlpha(1f);
        isSwiping = false;
        swipedOut = false;
    }

    private void moveImageWithSwipe() {
        // Update image location
        dx = currentX - startX;
        imageViewForeground.setX(dx);

        // Update image rotation
        degreesToRotate = scale(dx, 0, screenWidth / 2, 0, 15);
        imageViewForeground.setRotation(degreesToRotate);

        // Update image opacity
        alpha = scale(Math.abs(dx), 0, screenWidth / 2, 1, 0.7f);
        imageViewForeground.setAlpha(alpha);
    }

    private float scale(final float valueIn, final float baseMin, final float baseMax, final float limitMin, final float limitMax) {
        return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    private void swipedRight() {
        // Move image out of screen to the right
        Animation animation = new TranslateAnimation(imageViewForeground.getX(),
                screenWidth, imageViewForeground.getY(), imageViewForeground.getY());
        animation.setDuration(400);
        animation.setAnimationListener(this);
        imageViewForeground.startAnimation(animation);
        if (onSwipeListener != null) {
            onSwipeListener.onSwipeRight(currentItemIndex);
        }
    }

    private void swipedLeft() {
        // Move image out of screen to the left
        Animation animation = new TranslateAnimation(imageViewForeground.getX(),
                -screenWidth, imageViewForeground.getY(), imageViewForeground.getY());
        animation.setDuration(400);
        animation.setAnimationListener(this);
        imageViewForeground.startAnimation(animation);
        if (onSwipeListener != null) {
            onSwipeListener.onSwipeLeft(currentItemIndex);
        }
    }

    private void swapImageViews() {
        ImageView tmp = imageViewForeground;
        imageViewForeground = imageViewBackground;
        imageViewBackground = tmp;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (showingLastImage) {
            showingLastImage = false;
            emptyView.setVisibility(VISIBLE);
        } else {
            emptyView.setVisibility(INVISIBLE);
        }
        imageViewBackground.bringToFront();
        invalidate();
        swapImageViews();
        loadNextImage();
        resetAll();
        currentItemIndex++;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void loadNextImage() {
        String url = getNextUrl();
        Picasso.with(getContext()).load(url).into(imageViewBackground);
        if (url == null) {
            showingLastImage = true;
            emptyView.setVisibility(VISIBLE);
        } else {
            showingLastImage = false;
            emptyView.setVisibility(INVISIBLE);
        }
    }

    private String getNextUrl() {
        if (imagePaths.isEmpty()) {
            return null;
        }

        return imagePaths.remove(0);
    }

    public void addImagePaths(@NonNull ArrayList<String> imagePaths) {
        for (String url : imagePaths) {
            this.imagePaths.add(url);
        }

        if (showingLastImage) {
            Picasso.with(getContext()).load(getNextUrl()).into(imageViewBackground);
        } else if (emptyView.getVisibility() == VISIBLE) {
            // No images are shown
            Picasso.with(getContext()).load(getNextUrl()).into(imageViewBackground);
            this.onAnimationEnd(null);
        }
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public void setEmptyView(@NonNull View emptyView) {
        removeView(this.emptyView);
        this.emptyView = emptyView;
        addView(this.emptyView);
        this.emptyView.setVisibility(INVISIBLE);
    }
}
