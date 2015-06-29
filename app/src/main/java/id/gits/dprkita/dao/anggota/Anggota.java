package id.gits.dprkita.dao.anggota;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yatnosudar on 2/5/15.
 */
public class Anggota implements Serializable {

    String id;
    String tahun;
    String lembaga;
    String nama;
    String jenis_kelamin;
    String agama;
    String tempat_lahir;
    String tanggal_lahir;
    String status_perkawinan;
    String nama_pasangan;
    String jumlah_anak;
    String kelurahan_tinggal;
    String kecamatan_tinggal;
    String kab_kota_tinggal;
    String provinsi_tinggal;
    List<Riwayat> riwayat_pendidikan;
    List<Riwayat> riwayat_pekerjaan;
    List<Riwayat> riwayat_organisasi;
    List<Riwayat> sikap_politik;
    List<Akd> akd;
    Detail provinsi;
    Detail dapil;
    Detail partai;
    Detail komisi;

    String urutan;
    String foto_url;
    String suara_sah;
    String peringkat_suara_sah_calon;
    String terpilih;

    public String getId() {
        return id;
    }

    public String getTahun() {
        return tahun;
    }

    public String getLembaga() {
        return lembaga;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getAgama() {
        return agama;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public String getStatus_perkawinan() {
        return status_perkawinan;
    }

    public String getNama_pasangan() {
        return nama_pasangan;
    }

    public String getJumlah_anak() {
        return jumlah_anak;
    }

    public String getKelurahan_tinggal() {
        return kelurahan_tinggal;
    }

    public String getKecamatan_tinggal() {
        return kecamatan_tinggal;
    }

    public String getKab_kota_tinggal() {
        return kab_kota_tinggal;
    }

    public String getProvinsi_tinggal() {
        return provinsi_tinggal;
    }

    public List<Riwayat> getRiwayat_pendidikan() {
        return riwayat_pendidikan;
    }

    public List<Riwayat> getRiwayat_pekerjaan() {
        return riwayat_pekerjaan;
    }

    public List<Riwayat> getRiwayat_organisasi() {
        return riwayat_organisasi;
    }

    public List<Riwayat> getSikap_politik() {
        return sikap_politik;
    }

    public Detail getProvinsi() {
        return provinsi;
    }

    public Detail getDapil() {
        return dapil;
    }

    public Detail getPartai() {
        return partai;
    }

    public Detail getKomisi() {
        return komisi;
    }

    public String getUrutan() {
        return urutan;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public String getSuara_sah() {
        return suara_sah;
    }

    public String getPeringkat_suara_sah_calon() {
        return peringkat_suara_sah_calon;
    }

    public String getTerpilih() {
        return terpilih;
    }

    public List<Akd> getAkd() {
        return akd;
    }
}
