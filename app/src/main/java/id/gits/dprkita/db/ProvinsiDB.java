package id.gits.dprkita.db;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by yatnosudar on 2/3/15.
 */
public class ProvinsiDB extends SugarRecord<ProvinsiDB> implements Serializable {
    int idProvinsi;
    String nama;
    String nama_lengkap;
    String nama_inggris;
    int jumlah_kursi;
    long jumlah_penduduk;
    int pro_id;

    public ProvinsiDB() {
    }

    public int getIdProvinsi() {
        return idProvinsi;
    }

    public void setIdProvinsi(int idProvinsi) {
        this.idProvinsi = idProvinsi;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public String getNama_inggris() {
        return nama_inggris;
    }

    public void setNama_inggris(String nama_inggris) {
        this.nama_inggris = nama_inggris;
    }

    public int getJumlah_kursi() {
        return jumlah_kursi;
    }

    public void setJumlah_kursi(int jumlah_kursi) {
        this.jumlah_kursi = jumlah_kursi;
    }

    public long getJumlah_penduduk() {
        return jumlah_penduduk;
    }

    public void setJumlah_penduduk(long jumlah_penduduk) {
        this.jumlah_penduduk = jumlah_penduduk;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }
}
