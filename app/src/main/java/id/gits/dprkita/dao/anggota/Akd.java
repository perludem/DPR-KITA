package id.gits.dprkita.dao.anggota;

import java.io.Serializable;

/**
 * Created by yatnosudar on 3/7/15.
 */
public class Akd implements Serializable {

    private int id;
    private int id_lembaga;
    private String lembaga;
    private String jabatan;

    public int getId() {
        return id;
    }

    public int getId_lembaga() {
        return id_lembaga;
    }

    public String getLembaga() {
        return lembaga;
    }

    public String getJabatan() {
        return jabatan;
    }
}
