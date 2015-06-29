package id.gits.dprkita.dao.provinsi;

/**
 * Created by yatnosudar on 2/3/15.
 */

public class Provinsi {

    private int id;
    private String nama;
    private String nama_lengkap;
    private String nama_inggris;
    private int jumlah_kursi;
    private long jumlah_penduduk;
    private int pro_id;

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getNama_inggris() {
        return nama_inggris;
    }

    public int getJumlah_kursi() {
        return jumlah_kursi;
    }

    public long getJumlah_penduduk() {
        return jumlah_penduduk;
    }

    public int getPro_id() {
        return pro_id;
    }
}
