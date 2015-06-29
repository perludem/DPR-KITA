package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.partai.Partai;
import id.gits.dprkita.fragment.MainPartaiListFragment;
import id.gits.dprkita.utils.Helper;

/**
 * Created by yatnosudar on 2/13/15.
 */
public class AdapterPartai extends RecyclerView.Adapter<AdapterPartai.ViewHolder> {

    List<Partai> data;
    Activity activity;

    public AdapterPartai(Activity activity, List<Partai> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_partai, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Picasso.with(activity)
                .load(data.get(i).getUrl_logo_medium())
                .placeholder(R.drawable.gradient_header_background)
                .error(R.drawable.gradien_black)
                .into(viewHolder.img_partai);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(activity, MainPartaiListFragment.newInstance((java.util.ArrayList<Partai>) data, i), R.id.container, Helper.getFragmentManager(activity));
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
        public ImageView img_partai;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.img_partai = (ImageView) itemView.findViewById(R.id.img_partai);

        }
    }


}