package id.gits.dprkita.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.komisi.Komisi;
import id.gits.dprkita.fragment.KomisiFragment;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterListKomisi extends RecyclerView.Adapter<AdapterListKomisi.ViewHolder> {

    List<Komisi> data;
    Activity activity;

    public AdapterListKomisi(Activity activity, List<Komisi> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_dapil, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.nama_lengkap.setText("Komisi " + data.get(i).getId());
        viewHolder.nama.setText(data.get(i).getNama());
//        String[] changeName = data.get(i).getNama().split(" ");
//        data.get(i).setNama("KOMISI " + changeName[0] + " DPR RI");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KomisiFragment.komisi = data.get(i);
                Intent intent = new Intent();
                intent.putExtra("komisi", data.get(i));
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        public TextView nama_lengkap, nama;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.nama_lengkap = (TextView) itemView.findViewById(R.id.nama_lengkap);
            this.nama = (TextView) itemView.findViewById(R.id.nama);
        }
    }

}