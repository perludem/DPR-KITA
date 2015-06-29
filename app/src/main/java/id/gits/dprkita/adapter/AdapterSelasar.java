package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.selasar.Infografis;
import id.gits.dprkita.fragment.ViewImageFragment;
import id.gits.dprkita.utils.Helper;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterSelasar extends RecyclerView.Adapter<AdapterSelasar.ViewHolder> {

    int width;
    int height;
    List<Infografis> data;
    Activity activity;


    public AdapterSelasar(Activity activity, List<Infografis> data) {
        this.data = data;
        this.activity = activity;
        width = activity.getResources().getDisplayMetrics().widthPixels;
        height = width / 2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_infografis, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.tv_title.setText(data.get(i).getJudul());
        viewHolder.img_selasar.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        //.transform(new CropTopTransformation(width))
        Picasso.with(activity)
                .load(data.get(i).getUrl_infografis().replace(" ", "%20"))
                .placeholder(R.drawable.gradient_header_background)
                .error(R.drawable.gradien_black)
                .resize(600, 600)
                .centerInside()
                .into(viewHolder.img_selasar);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(activity,
                        ViewImageFragment.newInstance(data.get(i).getJudul(),
                                data.get(i).getUrl_infografis()),
                        R.id.container,
                        Helper.getFragmentManager(activity));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        public TextView tv_title;
        public ImageView img_selasar;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            this.img_selasar = (ImageView) itemView.findViewById(R.id.img_selasar);
        }
    }
}