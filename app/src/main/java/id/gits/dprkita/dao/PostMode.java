package id.gits.dprkita.dao;

import java.io.Serializable;

/**
 * Created by yatnosudar on 2/17/15.
 */
public class PostMode implements Serializable {
    String title;
    String post_type;
    String id_anggota_dewan;
    String name_anggota_dewan;

    public PostMode() {
    }

    /*
    * String title, String post_type, String id_anggota_dewan, String name_anggota_dewan
    * */
    public PostMode(String title, String post_type, String id_anggota_dewan, String name_anggota_dewan) {
        this.title = title;
        this.post_type = post_type;
        this.id_anggota_dewan = id_anggota_dewan;
        this.name_anggota_dewan = name_anggota_dewan;
    }

    public String getTitle() {
        return title;
    }

    public String getPost_type() {
        return post_type;
    }

    public String getId_anggota_dewan() {
        return id_anggota_dewan;
    }

    public String getName_anggota_dewan() {
        return name_anggota_dewan;
    }
}
