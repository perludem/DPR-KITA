package id.gits.dprkita.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.db.WilayahDB;
import id.gits.dprkita.utils.Constant;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterDapil extends RecyclerView.Adapter<AdapterDapil.ViewHolder> {

    List<DapilDB> data;
    Activity activity;

    public AdapterDapil(Activity activity, List<DapilDB> data) {
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


        List<WilayahDB> wilayahDBs = Select.from(WilayahDB.class)
                .where(Condition.prop("dapil").eq(data.get(i).getId()))
                .list();
        StringBuffer buffer = new StringBuffer();
        int z = 0;
        for (WilayahDB wilayahDB : wilayahDBs) {
            buffer.append(wilayahDB.getNama());
            if (z < wilayahDBs.size() - 1) {
                buffer.append(",");
            }
            z++;
        }
        String wil = buffer.toString();
        viewHolder.nama.setText(wil);
        viewHolder.nama_lengkap.setText(data.get(i).getNama() + " (" + data.get(i).getJumlah_kursi() + " Kursi)");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HomeListFragment.dataDapil = data.get(i);
                Intent intent = new Intent();
//                data.get(i).setIdDapil(dapils.get(i).getId());
                intent.putExtra(Constant.TAG.DATADAPIL, data.get(i));
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
//                this.total_kursi = (TextView) itemView.findViewById(R.id.total_kursi);
        }
    }

}