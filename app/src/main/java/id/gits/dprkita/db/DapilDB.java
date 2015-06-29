package id.gits.dprkita.db;

import com.orm.SugarRecord;

import java.io.Serializable;

import id.gits.dprkita.dao.dapil.Provinsi;

/**
 * Created by yatnosudar on 2/3/15.
 */
public class DapilDB extends SugarRecord<DapilDB> implements Serializable {

    String idDapil;
    String nama;
    int jumlah_kursi;
    long jumlah_penduduk;
    String nama_provinsi;
    Provinsi provinsi;
    String subdapil;


    public DapilDB() {
    }

    public String getSubdapil() {
        return subdapil;
    }

    public void setSubdapil(String subdapil) {
        this.subdapil = subdapil;
    }

    public String getNama_provinsi() {
        return nama_provinsi;
    }

    public void setNama_provinsi(String nama_provinsi) {
        this.nama_provinsi = nama_provinsi;
    }

    public String getIdDapil() {
        return idDapil;
    }

    public void setIdDapil(String idDapil) {
        this.idDapil = idDapil;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public Provinsi getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(Provinsi provinsi) {
        this.provinsi = provinsi;
    }
}
