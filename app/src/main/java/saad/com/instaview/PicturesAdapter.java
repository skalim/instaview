package saad.com.instaview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder> {
    private ArrayList<String> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recycler_image_layout_image_view)
        ImageView iv;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public PicturesAdapter(ArrayList<String> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @Override
    public PicturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_image_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(mDataset.get(position)).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setmDataset(ArrayList<String> mDataset) {
        this.mDataset = new ArrayList<>(mDataset);
        notifyDataSetChanged();
    }
}
