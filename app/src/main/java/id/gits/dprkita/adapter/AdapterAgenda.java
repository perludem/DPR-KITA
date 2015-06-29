package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterAgenda extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PostDao> data;
    Activity activity;


    public AdapterAgenda(Activity activity, List<PostDao> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agenda, parent, false);
        return new ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItem item = ((ViewHolderItem) holder);
        PostDao postDao = data.get(position);
        List<HashMap<String, String>> object = postDao.getCustom_fields();
        for (HashMap<String, String> objectHashMap : object) {
            String key = objectHashMap.get(WPKey.KEY.KEY);
            String val = objectHashMap.get(WPKey.KEY.VALUE);

            if (key.equals("jam_agenda"))
                item.tv_time.setText(val.trim());
        }

        item.tv_desc.setText(Html.fromHtml(postDao.getPost_content().replaceAll("\\r\\n|\\r|\\n", "")).toString().trim());
        item.tv_title.setText(Html.fromHtml(postDao.getTitle()));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        @InjectView(R.id.tv_title)
        TextView tv_title;
        @InjectView(R.id.tv_desc)
        TextView tv_desc;
        @InjectView(R.id.tv_time)
        TextView tv_time;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}