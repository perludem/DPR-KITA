package id.gits.dprkita.dao.anggota;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yatnosudar on 2/5/15.
 */
public class ResultsAnggota implements Serializable {
    private int count;
    private int total;
    private List<Anggota> anggota;

    public int getCount() {
        return count;
    }

    public int getTotal() {
        return total;
    }

    public List<Anggota> getAnggota() {
        return anggota;
    }
}
