package id.gits.dprkita.dao.dapil;

import java.util.List;

/**
 * Created by yatnosudar on 2/3/15.
 */
public class Dapil {
    private String id;
    private String kode;
    private String nama;
    private String nama_lengkap;
    private String nama_lembaga;
    private int jumlah_kursi;
    private long jumlah_penduduk;
    private Provinsi province;
    private String subdapil;
    private List<Wilayah> wilayah;

    public String getKode() {
        return kode;
    }

    public List<Wilayah> getWilayah() {
        return wilayah;
    }

    public String getSubdapil() {
        return subdapil;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getNama_lembaga() {
        return nama_lembaga;
    }

    public int getJumlah_kursi() {
        return jumlah_kursi;
    }

    public long getJumlah_penduduk() {
        return jumlah_penduduk;
    }

    public Provinsi getProvinsi() {
        return province;
    }
}
