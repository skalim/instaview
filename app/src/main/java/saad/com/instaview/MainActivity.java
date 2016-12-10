package saad.com.instaview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    private LinkedList<String> queueBackgroundImages;

    @BindView(R.id.activity_main_image_view_background)
    ImageView imageViewBackgroud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        queueBackgroundImages = new LinkedList<>();
        String[] urlArray = getResources().getStringArray(R.array.background_images);
        for( int i = 0; i < urlArray.length; i++ ){
            queueBackgroundImages.add(urlArray[i]);
        }

        Picasso.with(this).load(queueBackgroundImages.get(0)).into(imageViewBackgroud);
    }

    @OnClick(R.id.activity_main_login_button)
    public void onLogin(){
        AuthorisationManager.startAuthorisation(this);
    }
}
