package id.gits.dprkita.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.dao.jetpackdao.JetPackComment;
import id.gits.dprkita.utils.Helper;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterComment extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<JetPackComment> data;
    Activity activity;

    public AdapterComment(Activity activity, List<JetPackComment> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline_home, parent, false);
        return new ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderItem item = ((ViewHolderItem) holder);
        final JetPackComment komentar = data.get(position);

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        Date d = null;
        try {
            d = date.parse(komentar.getDate());
        } catch (ParseException e) {
        }
        final String namaLengkap = komentar.getAuthor().getName();
        item.nama_lengkap.setText(namaLengkap);
        item.tv_posting.setText(Html.fromHtml(komentar.getContent().replaceAll("\\r\\n|\\r|\\n", "")).toString().trim());
        Picasso.with(activity)
                .load(komentar.getAuthor().getAvatar_URL())
                .resize(400, 400).centerInside()
                .placeholder(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user))
                .error(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user)).into(item.img_profile);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        item.tv_date_posting.setText(simpleDateFormat.format(d));
        item.btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReport(namaLengkap);
            }
        });
        item.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showMenu(activity, v, komentar.getContent(), komentar.getAuthor().getName(),2);
            }
        });
        item.btn_comment.setVisibility(View.GONE);
    }

    private void itemClick(View itemView, final String username) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialogReport(username);
                return false;
            }
        });
    }

    public void dialogReport(final String username) {
        AlertDialog.Builder laporkan = new AlertDialog.Builder(activity);
        laporkan.setMessage(activity.getResources().getString(R.string.lapor));
        laporkan.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        laporkan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {

                    Intent gmail = new Intent(Intent.ACTION_VIEW);
                    gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{activity.getResources().getString(R.string.email_receive)});
                    gmail.setData(Uri.parse(activity.getResources().getString(R.string.email_receive)));
                    gmail.putExtra(Intent.EXTRA_SUBJECT, activity.getResources().getString(R.string.laporkan_subject) + username);
                    gmail.setType("plain/text");
                    gmail.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.body_email));
                    activity.startActivity(gmail);
                    dialog.cancel();
                } catch (Exception e) {
                    dialog.cancel();
                }
            }
        });
        laporkan.show();
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
        @InjectView(R.id.tv_posting)
        TextView tv_posting;
        @InjectView(R.id.tv_date_posting)
        TextView tv_date_posting;
        @InjectView(R.id.nama_lengkap)
        TextView nama_lengkap;
        @InjectView(R.id.img_profile)
        ImageView img_profile;
        @InjectView(R.id.img_posting)
        ImageView img_posting;
        @InjectView(R.id.btn_report)
        IconButton btn_report;
        @InjectView(R.id.btn_comment)
        IconButton btn_comment;
        @InjectView(R.id.popupmenu)
        IconButton popupmenu;

        public ViewHolderItem(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.inject(this, itemView);
        }
    }
}