package id.gits.dprkita.dao.partai;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yatnosudar on 2/13/15.
 */
public class Partai implements Serializable {

    String id;
    int kursi;
    String nama;
    String nama_lengkap;
    String url_situs;
    String url_facebook;
    String url_twitter;
    String url_logo_mini;
    String url_logo_small;
    String url_logo_medium;
    List<String> colors;

    public int getKursi() {
        return kursi;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getUrl_situs() {
        return url_situs;
    }

    public String getUrl_facebook() {
        return url_facebook;
    }

    public String getUrl_twitter() {
        return url_twitter;
    }

    public String getUrl_logo_mini() {
        return url_logo_mini;
    }

    public String getUrl_logo_small() {
        return url_logo_small;
    }

    public String getUrl_logo_medium() {
        return url_logo_medium;
    }

    public List<String> getColors() {
        return colors;
    }
}
