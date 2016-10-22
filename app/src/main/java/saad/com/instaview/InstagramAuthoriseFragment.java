package saad.com.instaview;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class InstagramAuthoriseFragment extends Fragment {

    @BindView(R.id.fragment_instagram_authorise_webview)
    WebView webView;

    AuthWebViewClient authWebViewClient;

    String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_instagram_authorise, container, false);
        ButterKnife.bind(this, v);
        //webView.getSettings().setJavaScriptEnabled(true);
        authWebViewClient = new AuthWebViewClient();
        authWebViewClient.setMainActivity((MainActivity)getActivity());
        webView.setWebViewClient(authWebViewClient);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadAuthPage();
    }

    public void loadAuthPage(){
        webView.loadUrl(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private class AuthWebViewClient extends WebViewClient{
        private final String TAG = AuthWebViewClient.class.getSimpleName();
        private static final String REDIRECT_URI = "https://github.com/skalim?code=";

        private MainActivity mainActivity;
        private String code;

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "redirect uri: " + REDIRECT_URI);
            Log.d(TAG, "URL: " + url);
            if (url.contains(REDIRECT_URI)) {
                code = url.replace(REDIRECT_URI, "");
                Log.d(TAG, "Verifier code: " + code);
                mainActivity.onAuthorised(code);
            }
            super.onPageStarted(view, url, favicon);
        }

        public String getCode() {
            return code;
        }
    }
}
