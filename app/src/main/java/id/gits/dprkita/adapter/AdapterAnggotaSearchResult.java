package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.fragment.ProfileFragment;
import id.gits.dprkita.utils.Helper;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterAnggotaSearchResult extends RecyclerView.Adapter<AdapterAnggotaSearchResult.ViewHolder> {

    public static final int HEADER_PAGER = 0;
    public static final int CHILD = 1;
    List<Anggota> data;
    Activity activity;

    public AdapterAnggotaSearchResult(Activity activity, List<Anggota> data) {
        this.data = data;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        /*int layout;
        if (i==HEADER_PAGER){
            layout = R.layout.header_komisi;
        }else{
            layout = R.layout.item_anggota_dpr;
        }*/

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_anggota_dpr, viewGroup, false);
        ViewHolder vh = new ViewHolder(v, i);
        return vh;

    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Anggota anggota = data.get(i);
        viewHolder.tv_name.setText(anggota.getNama());
        if (anggota.getKomisi() != null) {
            viewHolder.tv_komisi.setText(anggota.getKomisi().getName());
            viewHolder.tv_komisi.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_komisi.setVisibility(View.GONE);
        }
        if (anggota.getPartai() != null) {
            viewHolder.tv_fraksi.setText(anggota.getPartai().getName());
            viewHolder.tv_fraksi.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_fraksi.setVisibility(View.GONE);

        }
        if (anggota.getDapil() != null) {
            viewHolder.tv_dapil.setText(anggota.getDapil().getName());
            viewHolder.tv_dapil.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_dapil.setVisibility(View.GONE);
        }

        Picasso.with(activity)
                .load(anggota.getFoto_url())
                .placeholder(R.drawable.gradient_header_background)
                .error(R.drawable.gradien_black)
                .into(viewHolder.img_view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(activity, ProfileFragment.newInstance(anggota.getId(), anggota.getNama()), R.id.container, Helper.getFragmentManager(activity));
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position==HEADER_PAGER){
            return HEADER_PAGER;
        }*/
        return CHILD;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        public TextView tv_name, tv_fraksi, tv_dapil, tv_komisi;
        public ImageView img_view;
        public int positionView;


        public ViewHolder(View itemView, int positionView) {
            super(itemView);
            this.itemView = itemView;
            this.positionView = positionView;
            if (positionView == CHILD) {
                this.tv_name = (TextView) itemView.findViewById(R.id.tv_title);
                this.tv_dapil = (TextView) itemView.findViewById(R.id.tv_dapil);
                this.tv_fraksi = (TextView) itemView.findViewById(R.id.tv_fraksi);
                this.tv_komisi = (TextView) itemView.findViewById(R.id.tv_komisi);
                this.img_view = (ImageView) itemView.findViewById(R.id.img_profile);
            }
        }
    }

}