package saad.com.instaview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;

import org.jinstagram.auth.model.Token;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_activity_search_edit_text)
    EditText editTextSearch;

    @BindView(R.id.activity_search_recycler_view)
    RecyclerView mRecyclerView;

    PicturesAdapter mAdapter;

    RecyclerView.LayoutManager mLayoutManager;

    @BindView(R.id.activity_search_scroll_gallery_view)
    ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(  intent != null){
            Uri uri = intent.getData();
            String accessToken = uri.toString().replace(AuthorisationManager.REDIRECT_URI + "#access_token=", "");
            MyInstagram.init(new Token(accessToken, ""));
        }

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PicturesAdapter(new ArrayList<String>(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.search_activity_button_search)
    public void onSearch(){
        String tag = editTextSearch.getText().toString();
        FetchPicturesTask fetchPicturesTask = new FetchPicturesTask(this);
        fetchPicturesTask.execute(tag);
    }


    public void onPicturesFetched(ArrayList<String> urls){
        //mAdapter.setmDataset(urls);

        List<MediaInfo> infos = new ArrayList<>(urls.size());
        for (String url : urls){
            infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));
        }

        scrollGalleryView
                .setThumbnailSize(100)
                .setZoom(true)
                .setFragmentManager(getSupportFragmentManager())
                .addMedia(infos);

    }
}
