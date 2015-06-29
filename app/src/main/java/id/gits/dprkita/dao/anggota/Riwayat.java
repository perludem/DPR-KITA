package id.gits.dprkita.dao.anggota;

import java.io.Serializable;

/**
 * Created by yatnosudar on 2/5/15.
 */
public class Riwayat implements Serializable {

    int id;
    String ringkasan;

    public int getId() {
        return id;
    }

    public String getRingkasan() {
        return ringkasan;
    }
}
