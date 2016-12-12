package saad.com.instaview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

    @BindView(R.id.swiping_view)
    SwipingImagesView swipingImagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(  intent != null ){
            Uri uri = intent.getData();
            if( uri != null ){
                String accessToken = uri.toString();
                if( accessToken != null ){
                    accessToken = accessToken.replace(InstaViewConsts.REDIRECT_URI + "#access_token=", "");
                    MyInstagram.init(new Token(accessToken, ""));
                }
            }
        }

        String[] urlArray = getResources().getStringArray(R.array.background_images);
        ArrayList<String> paths = new ArrayList<>();
        for (String url: urlArray) {
            paths.add(url);
        }
        swipingImagesView.setImagePaths(paths);
    }

    @OnClick(R.id.search_activity_button_search)
    public void onSearch(){
        String tag = editTextSearch.getText().toString();
        FetchPicturesTask fetchPicturesTask = new FetchPicturesTask(this);
        fetchPicturesTask.execute(tag);
    }
}
