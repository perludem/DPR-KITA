package id.gits.dprkita.db;

import com.orm.SugarRecord;

/**
 * Created by yatnosudar on 2/3/15.
 */

public class WilayahDB extends SugarRecord<WilayahDB> {
    private String nama;
    private DapilDB dapil;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public DapilDB getDapil() {
        return dapil;
    }

    public void setDapil(DapilDB dapil) {
        this.dapil = dapil;
    }
}
