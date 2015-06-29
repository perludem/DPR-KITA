package id.gits.dprkita.dao.komisi;

import java.io.Serializable;

/**
 * Created by yatnosudar on 2/12/15.
 */
public class Komisi implements Serializable {
    int id;
    String nama;

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
