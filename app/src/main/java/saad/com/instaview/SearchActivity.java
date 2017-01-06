package saad.com.instaview;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.FrameLayout;

import org.jinstagram.auth.model.Token;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.skalim.swipeview.SwipingImagesView;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_activity_search_edit_text)
    SearchView searchView;

    @BindView(R.id.swiping_view)
    SwipingImagesView swipingImagesView;

    @BindView(R.id.activity_search)
    View rootView;

    @BindView(R.id.search_activity_search_blur_view)
    View blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(cn));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        // Set margin for search field to allow for status bar
        ViewCompat.setOnApplyWindowInsetsListener(searchView, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int statusBar = insets.getSystemWindowInsetTop();
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) searchView.getLayoutParams();
                layoutParams.setMargins(0, statusBar, 0, 0);
                searchView.setLayoutParams(layoutParams);
                return insets;
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                // Start from search
                String query = intent.getStringExtra(SearchManager.QUERY);
                String[] tags = parseTags(query);
                onSearch(tags);

                if( tags.length > 0 ) {
                    SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                            TagSearchRecentSuggestionsProvider.AUTHORITY, TagSearchRecentSuggestionsProvider.MODE);
                    suggestions.saveRecentQuery(query, null);
                }

                return;
            }

            String accessToken = intent.getStringExtra(MainActivity.PREF_KEY_ACCESS_TOKEN);
            if( accessToken != null ){
                // Start from existing session
                MyInstagram.init(new Token(accessToken, null));
                return;
            }

            Uri uri = intent.getData();
            if (uri != null) {
                accessToken = uri.toString();
                if (accessToken != null) {
                    // New login
                    accessToken = accessToken.replace(InstaViewConsts.REDIRECT_URI + "#access_token=", "");
                    MyInstagram.init(new Token(accessToken, null));
                    SharedPreferences.Editor editor = getSharedPreferences(MainActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(MainActivity.PREF_KEY_ACCESS_TOKEN, accessToken);
                    editor.apply();
                    return;
                }

                String error = uri.getQueryParameter("error");
                if( error != null ) {
                    // New login error occurred
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    public void onSearch(String[] tags) {
        FetchPicturesTask fetchPicturesTask = new FetchPicturesTask(this);
        fetchPicturesTask.execute(tags);
    }

    private String[] parseTags(String query){
        if( !query.contains("#") ) {
            return new String[]{};
        }

        String s = query.replaceAll("\\s+","");
        s = s.replaceFirst("#", "");
        String[] tags = s.split("#");
        return tags;
    }
}
