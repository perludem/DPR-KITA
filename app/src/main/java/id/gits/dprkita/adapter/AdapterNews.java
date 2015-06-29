package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.text.ParseException;
import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.news.PagesDao;
import id.gits.dprkita.fragment.DetailNewsFragment;
import id.gits.dprkita.utils.Helper;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    List<PagesDao> data;
    Activity activity;

    public AdapterNews(Activity activity, List<PagesDao> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_news, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.tv_title_news.setText(Html.fromHtml(data.get(i).getTitle()));
        String date = Html.fromHtml(data.get(i).getDate()).toString();
        try {
            viewHolder.tv_date_news.setText(Helper.dateParse(date, "yyyy-MM-dd hh:mm:ss", "EEEE, dd MMMM yyyy"));
        } catch (ParseException e) {
        }

        viewHolder.img_item_news.setImageDrawable(new IconDrawable(activity, Iconify.IconValue.fa_image)
                .colorRes(R.color.grey500).actionBarSize());
        viewHolder.img_item_news.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(activity, DetailNewsFragment.newInstance(data.get(i)), R.id.container, Helper.getFragmentManager(activity));
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
        public TextView tv_title_news, tv_date_news;
        public ImageView img_item_news;


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            this.tv_title_news = (TextView) itemView.findViewById(R.id.tv_title_news);
            this.tv_date_news = (TextView) itemView.findViewById(R.id.tv_date_news);
            this.img_item_news = (ImageView) itemView.findViewById(R.id.img_item_news);

        }
    }


}