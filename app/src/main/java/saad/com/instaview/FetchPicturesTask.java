package saad.com.instaview;

import android.content.Context;
import android.os.AsyncTask;

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

        if (params == null || params.length == 0) {
            return list;
        }

        Instagram instagram = MyInstagram.getInstance();
        List<MediaFeedData> mediaFeed;
        for (String tag : params) {

            try {
                TagMediaFeed tagMediaFeed = instagram.getRecentMediaTags(tag);
                mediaFeed = tagMediaFeed.getData();
                for (MediaFeedData mediaFeedData : mediaFeed) {
                    ImageData imageData = mediaFeedData.getImages().getStandardResolution();
                    list.add(imageData.getImageUrl());
                }
            } catch (InstagramException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        ((SearchActivity) context).swipingImagesView.addImagePaths(strings);
    }
}
