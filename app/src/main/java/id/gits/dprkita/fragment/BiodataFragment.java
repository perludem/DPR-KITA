package id.gits.dprkita.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.dao.anggota.Akd;
import id.gits.dprkita.dao.anggota.Anggota;
import id.gits.dprkita.dao.anggota.Riwayat;

public class BiodataFragment extends BaseFragment implements ObservableScrollViewCallbacks {

    //view;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ANGGOTA = "anggota";
    @InjectView(R.id.scroll)
    ObservableScrollView scroll;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.tv_tempat_lahir)
    TextView tv_tempat_lahir;
    @InjectView(R.id.tv_tanggal_lahir)
    TextView tv_tanggal_lahir;
    @InjectView(R.id.tv_address)
    TextView tv_address;
    @InjectView(R.id.tv_phone)
    TextView tv_phone;
    @InjectView(R.id.tv_dapil)
    TextView tv_dapil;
    @InjectView(R.id.tv_komisi)
    TextView tv_komisi;
    @InjectView(R.id.tv_partai)
    TextView tv_partai;
    @InjectView(R.id.lin_organisasi)
    LinearLayout lin_organisasi;
    @InjectView(R.id.lin_pekerjaan)
    LinearLayout lin_pekerjaan;
    @InjectView(R.id.lin_education)
    LinearLayout lin_education;
    @InjectView(R.id.lin_akd)
    LinearLayout lin_akd;

    private Anggota anggota;

    public BiodataFragment() {
        // Required empty public constructor
    }

    public static BiodataFragment newInstance(Anggota anggota) {
        BiodataFragment fragment = new BiodataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ANGGOTA, anggota);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            anggota = (Anggota) getArguments().getSerializable(ANGGOTA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        scroll.setScrollViewCallbacks(this);
        if (anggota != null) {
            initData();
        }
        return view;
    }

    public void initData() {
        if (anggota.getKomisi() != null) {
            tv_komisi.setText("Komisi " + anggota.getKomisi().getId());
        }
        tv_name.setText(anggota.getNama());
        tv_tempat_lahir.setText(anggota.getTempat_lahir());
        tv_tanggal_lahir.setText(anggota.getTanggal_lahir());
        tv_address.setText(anggota.getKelurahan_tinggal() + "," + anggota.getProvinsi_tinggal());

        tv_partai.setText(anggota.getPartai().getName());
        tv_dapil.setText(anggota.getDapil().getName());
        tv_komisi.setText(anggota.getKomisi().getName());

        try {
            addItem(anggota.getRiwayat_pendidikan(), 0);
            addItem(anggota.getRiwayat_pekerjaan(), 1);
            addItem(anggota.getRiwayat_organisasi(), 2);
            addAKD(anggota.getAkd());
        } catch (Exception e) {
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void addItem(List<Riwayat> riwayats, int stat) {
        for (int i = 0; i < riwayats.size(); i++) {
            View viewItem = LayoutInflater.from(getActivity()).inflate(R.layout.item_education, null);
            TextView tv_education = (TextView) viewItem.findViewById(R.id.tv_education);
            TextView tv_number = (TextView) viewItem.findViewById(R.id.tv_number);
            tv_number.setText((i + 1) + ".");
            tv_education.setText(riwayats.get(i).getRingkasan());
            switch (stat) {
                case 0:
                    lin_education.addView(viewItem);
                    break;
                case 1:
                    lin_pekerjaan.addView(viewItem);
                    break;
                case 2:
                    lin_organisasi.addView(viewItem);
                    break;
            }
        }
    }

    private void addAKD(List<Akd> akds) {
        for (Akd akd : akds) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_akd, null);
            TextView title = ButterKnife.findById(view, R.id.nama_lengkap);
            TextView subTitle = ButterKnife.findById(view, R.id.nama);
            title.setText(akd.getLembaga());
            subTitle.setText("Jabatan: " + akd.getJabatan());
            lin_akd.addView(view);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_biodata;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
