package id.gits.dprkita.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.dapil.Dapil;
import id.gits.dprkita.db.DapilDB;
import id.gits.dprkita.fragment.KomisiFragment;
import id.gits.dprkita.fragment.PartaiFragment;
import id.gits.dprkita.fragment.ProfileFragment;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.json.DapilJSON;
import id.gits.wplib.dao.PostDao;
import id.gits.wplib.utils.WPKey;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class AdapterTimeline extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int HEADER = 0;
    private static int CHILD = 1;
    List<PostDao> data;
    List<Anggota> anggotas;
    Activity activity;

    //    private HomeListFragment.HomeListener fragmentListener;
    public AdapterTimeline(Activity activity, List<PostDao> data, List<Anggota> anggotas) {
        this.data = data;
        this.activity = activity;
        this.anggotas = anggotas;
//        this.fragmentListener = fragmentListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_header_home, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_timeline_home, parent, false);
            return new ViewHolderItem(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItem) {
            //((ViewHolderItem) holder).nama_lengkap.setText("Yatno Sudar");
            item((ViewHolderItem) holder, position);
        }
        if (holder instanceof ViewHolderHeader) {
            header((ViewHolderHeader) holder);
        }

    }

    private void item(ViewHolderItem item, int position) {
        final PostDao postDao = data.get(position - 1);
        List<HashMap<String, String>> object = postDao.getCustom_fields();

        String nama_lengkap = "";
        String komisi = "";
        String id_user = "";

        for (HashMap<String, String> objectHashMap : object) {
            String key = objectHashMap.get(WPKey.KEY.KEY);
            String val = objectHashMap.get(WPKey.KEY.VALUE);

            if (key.equals("username"))
                nama_lengkap = val;
            if (key.equals("name_dpr"))
                nama_lengkap = val;
            if (key.equals("komisi"))
                komisi = val;
            if (key.equals("id_dpr_pemiluapi"))
                id_user = val;
        }

        item.nama_lengkap.setText(nama_lengkap + " Komisi " + komisi);

        item.tv_posting.setText(Html.fromHtml(postDao.getPost_content().replaceAll("\\r\\n|\\r|\\n", "")).toString().trim());
        item.img_posting.setVisibility(View.GONE);
        item.img_profile.setImageDrawable(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user));

        Date d = postDao.getPost_date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        item.tv_date_posting.setText(simpleDateFormat.format(d));


        Picasso.with(activity)
                .load(postDao.getUrl_image_profile())
                .resize(400, 400).centerInside()
                .placeholder(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user))
                .error(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_user)).into(item.img_profile);

        final ProfileFragment profileFragment = ProfileFragment.newInstance(id_user, nama_lengkap);
        item.nama_lengkap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(activity, profileFragment, R.id.container, Helper.getFragmentManager(activity));
            }
        });

        item.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.replaceFragment(activity, profileFragment, R.id.container, Helper.getFragmentManager(activity));
            }
        });

        if (postDao.getTotal_comment() > 0) {
            item.btn_comment.setText("{fa_reply} " + postDao.getTotal_comment() + " TANGGAPAN");
        }
        item.btn_comment.setVisibility(View.VISIBLE);
        item.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.launchActivity(activity, Integer.valueOf(postDao.getPost_id()), true);
            }
        });
        item.btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.dialogReport(activity, postDao.getPost_author());
            }
        });
        item.popupmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showMenu(activity, v, postDao.getPost_content(), postDao.getPost_author(),1);
            }
        });
    }

    private void header(ViewHolderHeader holder) {
        holder.lin_anggota_dapil.removeAllViews();

        for (int i = 0; i < anggotas.size(); i++) {
            final Anggota anggota = anggotas.get(i);
            if (i == 0) {
                try {
                    List<DapilDB> dapilDBs = DapilDB.find(DapilDB.class, "ID_DAPIL = ?", anggotas.get(0).getDapil().getId());
                    if (dapilDBs != null) {
                        holder.tv_dapil.setText("DAPIL " + dapilDBs.get(0).getNama());
                        holder.tv_dapil_list.setText("Podium Anggota DPR");
                    }
                } catch (Exception e) {
                    id.gits.dprkita.dao.dapil.BaseDaoDapil d = new Gson().fromJson(DapilJSON.DAPIL, id.gits.dprkita.dao.dapil.BaseDaoDapil.class);
                    List<Dapil> dapilDBs = d.getData().getResults().getDapil();
                    for (Dapil dapil : dapilDBs) {
                        if (dapil.getId().equals(anggotas.get(0).getId())) {
                            if (dapilDBs != null) {
                                holder.tv_dapil.setText("DAPIL " + dapilDBs.get(0).getNama_lengkap());
                                holder.tv_dapil_list.setText("Podium Anggota DPR");
                            }
                            break;
                        }
                    }
                }


            }
            View view = LayoutInflater.from(activity).inflate(R.layout.item_header_anggota, null);
            ImageView image_profile = ButterKnife.findById(view, R.id.img_profile_dapil);
            TextView tv_komisi_dapil = ButterKnife.findById(view, R.id.tv_komisi_dapil);
            TextView nama_lengkap_dapil = ButterKnife.findById(view, R.id.nama_lengkap_dapil);
            TextView tv_partai_dapil = ButterKnife.findById(view, R.id.tv_partai_dapil);

            if (anggota.getKomisi() != null)
                tv_komisi_dapil.setText("Komisi: " + anggota.getKomisi().getName());

            if (anggota.getPartai() != null)
                tv_partai_dapil.setText("Fraksi: " + anggota.getPartai().getName());

            nama_lengkap_dapil.setText(anggota.getNama());
            Picasso.with(activity).load(anggota.getFoto_url()).resize(200, 200).centerInside().into(image_profile);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.replaceFragment(activity, ProfileFragment.newInstance(anggota.getId(), anggota.getNama()), R.id.container, Helper.getFragmentManager(activity));
                }
            });

            holder.lin_anggota_dapil.addView(view);
        }


        holder.img_next1.setImageDrawable(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_angle_right));
        holder.img_next2.setImageDrawable(Helper.makeOwnIcon(activity, R.color.bluegray700, Iconify.IconValue.fa_angle_right));

        holder.komisi_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentListener.currentPosition(2);
                Helper.replaceFragment(activity, KomisiFragment.newInstance(), R.id.container, Helper.getFragmentManager(activity));
            }
        });

        holder.fraksi_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fragmentListener.currentPosition(1);
                Helper.replaceFragment(activity, PartaiFragment.newInstance(), R.id.container, Helper.getFragmentManager(activity));
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        if (position == HEADER) {
            return HEADER;
        } else
            return CHILD;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
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
        @InjectView(R.id.btn_comment)
        IconButton btn_comment;
        @InjectView(R.id.btn_report)
        IconButton btn_report;
        @InjectView(R.id.popupmenu)
        IconButton popupmenu;

        public ViewHolderItem(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


    public static class ViewHolderHeader extends RecyclerView.ViewHolder {

        public View itemView;

        @InjectView(R.id.tv_dapil)
        TextView tv_dapil;
        @InjectView(R.id.lin_anggota_dapil)
        LinearLayout lin_anggota_dapil;
        @InjectView(R.id.img_next1)
        ImageView img_next1;
        @InjectView(R.id.img_next2)
        ImageView img_next2;
        @InjectView(R.id.tv_dapil_list)
        TextView tv_dapil_list;
        @InjectView(R.id.fraksi_card)
        LinearLayout fraksi_card;
        @InjectView(R.id.komisi_card)
        LinearLayout komisi_card;


        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }


}