package saad.com.instaview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener{

    public static final String MY_PREFS_NAME = "InstaView";
    public static final String PREF_KEY_ACCESS_TOKEN = "access_token";

    private LinkedList<String> queueBackgroundImages;

    @BindView(R.id.activity_main_image_view_background)
    ImageView imageViewBackgroud;
    @BindView(R.id.activity_main_button_instagram)
    ImageView buttonInstagram;
    @BindView(R.id.activity_main_title)
    TextView textViewTitle;

    Animation animTitle, animButtonInsta, animBackground;
    boolean animationShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Check for existing session
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String accessToken = prefs.getString(PREF_KEY_ACCESS_TOKEN, null);
        if (accessToken != null) {
            Intent i = new Intent(this, SearchActivity.class);
            i.putExtra(PREF_KEY_ACCESS_TOKEN, accessToken);
            startActivity(i);
            finish();
        }

        ButterKnife.bind(this);
        queueBackgroundImages = new LinkedList<>();
        String[] urlArray = getResources().getStringArray(R.array.background_images);
        for( int i = 0; i < urlArray.length; i++ ){
            queueBackgroundImages.add(urlArray[i]);
        }

        Picasso.with(this).load(queueBackgroundImages.get(0)).into(imageViewBackgroud);
        animTitle = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_title);
        animTitle.setAnimationListener(this);
        animButtonInsta = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_insta_button);
        animBackground = AnimationUtils.loadAnimation(this, R.anim.anim_background);
        animationShown = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if( hasFocus && !animationShown ){
            animationShown = true;
            buttonInstagram.setVisibility(View.INVISIBLE);
            imageViewBackgroud.startAnimation(animBackground);
            textViewTitle.startAnimation(animTitle);
        }
    }

    @OnClick(R.id.activity_main_button_instagram)
    public void onLogin(){
        AuthorisationManager.startAuthorisation(this);
        finish();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if( animation == animTitle){
            buttonInstagram.setVisibility(View.VISIBLE);
            buttonInstagram.startAnimation(animButtonInsta);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
