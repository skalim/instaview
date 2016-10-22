package saad.com.instaview;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.jinstagram.Instagram;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.common.ImageData;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.List;

/**
 * Created by kalsa12 on 2016-10-20.
 */

public class LoginTask extends AsyncTask<Void, Void, Void>{

    private static final String TAG = LoginTask.class.getSimpleName();
    private static final Token EMPTY_TOKEN = null;

    private Context context;
    private String code;
    private InstagramService service;

    public LoginTask(String code, InstagramService instagramService, Context context) {
        this.code = code;
        this.service = instagramService;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(verifier);
        MyInstagram.init(accessToken);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent i = new Intent(context, SearchActivity.class);
        context.startActivity(i);
        ((MainActivity) context).finish();
    }
}
