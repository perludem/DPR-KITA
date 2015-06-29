package id.gits.dprkita.adapter;

import android.app.Activity;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.activity.CommentActivity;
import id.gits.dprkita.activity.ImageViewActivity;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.utils.Constant;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.SharePreferences;
import id.gits.wplib.apis.LoginApi;
import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterReport extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER = 0;
    private final int ITEM = 1;
    List<PostDao> data;
    Activity activity;
    LoginApi.ApiDao sessionLogin;
    String idAnggotaDPR = null;
    Anggota profileAnggotaDPR = null;


    public AdapterReport(Activity activity, List<PostDao> data) {
        this.data = data;
        this.activity = activity;
        sessionLogin = SharePreferences.sessionLogin(activity);
        idAnggotaDPR = SharePreferences.getString(activity, Constant.TAG.ID_PEMILUAPI);
        profileAnggotaDPR = SharePreferences.profileAnggotaDPR(activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline_home, parent, false);
        return new ViewHolderItem(v, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PostDao postDao = data.get(position);
        ViewHolderItem item = ((ViewHolderItem) holder);


        if (item.viewType == HEADER) {
            item.space.setVisibility(View.VISIBLE);
        }

        List<HashMap<String, String>> object = postDao.getCustom_fields();
        String komisi = "";
        if (postDao.getTitle() != null) {
            if (postDao.getTitle().contains(Constant.TAG.ASPIRASI)) {
                String[] kom = postDao.getTitle().split(":");
                komisi = " untuk " + kom[1];
            }
        }
        String from = "";
        String komISI = "";
        for (HashMap<String, String> objectHashMap : object) {
            String key = objectHashMap.get(WPKey.KEY.KEY);
            String val = objectHashMap.get(WPKey.KEY.VALUE);

            if (key.equals("username")) {
                item.nama_lengkap.setText(val + komisi);
                from = val;
            }
            if (key.equals("name_dpr")) {
                item.nama_lengkap.setText(val);
                from = val;
            }
            if (key.equals("komisi")) {
                komISI = val;
            }
        }


        if (postDao.getAttachment_image() != null) {
            item.img_posting.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(postDao.getAttachment_image())
                    .resize(400, 400).centerInside()
                    .placeholder(R.drawable.gradient_header_background)
                    .error(R.drawable.gradien_black).into(item.img_posting);
        } else {
            item.img_posting.setVisibility(View.GONE);
        }


        item.tv_posting.setText(Html.fromHtml(postDao.getPost_content().replaceAll("\\r\\n|\\r|\\n", "")).toString().trim());
//        item.img_profile.setImageDrawable(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user));
        Picasso.with(activity)
                .load(postDao.getUrl_image_profile())
                .resize(400, 400).centerInside()
                .placeholder(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user))
                .error(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user)).into(item.img_profile);


        Date d = postDao.getPost_date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        item.tv_date_posting.setText(simpleDateFormat.format(d));
        itemClick(item.itemView, from);

        final String finalFrom = from;
        boolean isComment = false;
        if (profileAnggotaDPR != null) {
            String splitKomisi = Helper.splitKomisi(" ", profileAnggotaDPR.getKomisi().getName());
            String koMisi[] = komISI.split(" ");
            if (profileAnggotaDPR.getNama().equals(from)) {
                isComment = true;
//                item.btn_comment.setVisibility(View.VISIBLE);
            } else if (splitKomisi.equals(koMisi[koMisi.length - 1])) {
                isComment = true;
//                item.btn_comment.setVisibility(View.VISIBLE);
            }

            if (profileAnggotaDPR.getKomisi().getName().equals(komISI)) {
                isComment = true;
            }
        } else if (sessionLogin != null && sessionLogin.getUsername().equals(postDao.getPost_author())) {
//            item.btn_comment.setVisibility(View.VISIBLE);
            isComment = true;
        }

        item.btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.dialogReport(activity, finalFrom);
            }
        });
        item.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showMenu(activity, v, postDao.getPost_content(), finalFrom,0);
            }
        });


        final boolean finalIsComment = isComment;
        if (isComment == false) {
            if (postDao.getTotal_comment() > 0) {
                item.btn_comment.setText("{fa_reply} " + postDao.getTotal_comment() + " TANGGAPAN");
                item.btn_comment.setVisibility(View.VISIBLE);
            } else {
                item.btn_comment.setVisibility(View.GONE);
            }
            if (sessionLogin != null && from.equals(sessionLogin.getDisplay_name())) {
                item.btn_comment.setText("{fa_reply} " + postDao.getTotal_comment() + " TANGGAPAN");
                item.btn_comment.setVisibility(View.VISIBLE);
            }

        } else {
            if (postDao.getTotal_comment() > 0) {
                item.btn_comment.setText("{fa_reply} " + postDao.getTotal_comment() + " TANGGAPAN");
                item.btn_comment.setVisibility(View.VISIBLE);
            } else {
                item.btn_comment.setVisibility(View.VISIBLE);
            }
            if (sessionLogin != null && from.equals(sessionLogin.getDisplay_name())) {
                item.btn_comment.setText("{fa_reply} " + postDao.getTotal_comment() + " TANGGAPAN");
                item.btn_comment.setVisibility(View.VISIBLE);
            }
        }


        item.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.launchActivity(activity, Integer.parseInt(postDao.getPost_id()), finalIsComment);
            }
        });


        item.img_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewActivity.startActivity(activity,postDao.getAttachment_image());
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        if (position == HEADER)
            return HEADER;
        else
            return ITEM;
    }

    private void itemClick(View itemView, final String username) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Helper.dialogReport(activity, username);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View itemView;
        public int viewType;
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
        @InjectView(R.id.space)
        View space;
        @InjectView(R.id.popupmenu)
        IconButton popupmenu;

        public ViewHolderItem(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;
            this.viewType = viewType;
            ButterKnife.inject(this, itemView);
        }
    }
}