package saad.com.instaview;

import android.content.SearchRecentSuggestionsProvider;


public class TagSearchRecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = TagSearchRecentSuggestionsProvider.class.getCanonicalName();
    public final static int MODE = DATABASE_MODE_QUERIES;

    public TagSearchRecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
