package saad.com.instaview;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import skalim.github.io.saadsandroid.util.SaadKeyboardUtil;

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

        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                String accessToken = uri.toString();
                if (accessToken != null) {
                    accessToken = accessToken.replace(InstaViewConsts.REDIRECT_URI + "#access_token=", "");
                    MyInstagram.init(new Token(accessToken, null));
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String[] tags = parseTags(query);
            onSearch(tags);

            if( tags.length > 0 ) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        TagSearchRecentSuggestionsProvider.AUTHORITY, TagSearchRecentSuggestionsProvider.MODE);
                suggestions.saveRecentQuery(query, null);
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
