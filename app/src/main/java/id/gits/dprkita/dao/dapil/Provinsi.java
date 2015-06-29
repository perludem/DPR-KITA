package id.gits.dprkita.dao.dapil;

import java.io.Serializable;

/**
 * Created by yatnosudar on 2/3/15.
 */

/*
* provinsi: {
id: 11,
nama: "Aceh"
}*/
public class Provinsi implements Serializable {
    private int id;
    private String nama;

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }
}
