package saad.com.instaview;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.oauth.InstagramService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    FragmentManager fragmentManager;
    InstagramAuthoriseFragment instagramAuthoriseFragment;

    InstagramService service;

    @BindView(R.id.activity_main_login_button)
    FancyButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        instagramAuthoriseFragment = new InstagramAuthoriseFragment();
        service = new InstagramAuthService()
                .apiKey("6d531664f106445e9a4af197073f99e9")
                .apiSecret("289550a757e9455a82b09ebd148f954f")
                .callback("https://github.com/skalim").scope("public_content")
                .build();
    }

    @OnClick(R.id.activity_main_login_button)
    public void onLogin(){
        String authorizationUrl = service.getAuthorizationUrl();
        Log.d(TAG, authorizationUrl);
        showFragment(authorizationUrl);
    }

    private void showFragment(String url){
        instagramAuthoriseFragment.setUrl(url);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_content_fragment, instagramAuthoriseFragment);
        fragmentTransaction.commit();
    }

    private void hideFragment(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(instagramAuthoriseFragment);
        fragmentTransaction.commit();
    }

    private void showLoading(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_content_fragment, new LoadingFragment());
        fragmentTransaction.commit();
    }

    public void onAuthorised(String code){
        showLoading();
        LoginTask task = new LoginTask( code, service, MainActivity.this);
        task.execute();
    }
}
