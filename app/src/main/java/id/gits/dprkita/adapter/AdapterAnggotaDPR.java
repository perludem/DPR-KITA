package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.AnggotaDPRDao;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterAnggotaDPR extends RecyclerView.Adapter<AdapterAnggotaDPR.ViewHolder> {

    List<AnggotaDPRDao> data;
    Activity activity;

    public AdapterAnggotaDPR(Activity activity, List<AnggotaDPRDao> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_anggota_dpr, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        public TextView tv_name, tv_komisi, tv_fraksi;
        public ImageView img_view;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.tv_name = (TextView) itemView.findViewById(R.id.tv_title);
            this.tv_komisi = (TextView) itemView.findViewById(R.id.tv_komisi);
            this.tv_fraksi = (TextView) itemView.findViewById(R.id.tv_fraksi);
            this.img_view = (ImageView) itemView.findViewById(R.id.img_profile);

        }
    }


}