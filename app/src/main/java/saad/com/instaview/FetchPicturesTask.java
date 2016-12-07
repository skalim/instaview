package saad.com.instaview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.jinstagram.Instagram;
import org.jinstagram.entity.common.ImageData;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

import java.util.ArrayList;
import java.util.List;


public class FetchPicturesTask extends AsyncTask<String, Void, ArrayList<String>> {

    private static final String TAG = FetchPicturesTask.class.getSimpleName();

    Context context;

    public FetchPicturesTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> list = new ArrayList<>();

        if(params == null || params.length == 0){
            return list;
        }

        try {
            Instagram instagram = MyInstagram.getInstance();
            TagMediaFeed tagMediaFeed = instagram.getRecentMediaTags(params[0]);
            List<MediaFeedData> mediaFeed = tagMediaFeed.getData();
            Log.d(TAG, "Fetched " + mediaFeed.size() + " pictures");
            for( MediaFeedData mediaFeedData : mediaFeed ){
                ImageData imageData = mediaFeedData.getImages().getStandardResolution();
                list.add(imageData.getImageUrl());
            }
        } catch (InstagramException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        ((SearchActivity) context).onPicturesFetched(strings);
    }
}
