package id.gits.wplib.utils;

/**
 * Created by yatnosudar on 2/14/15.
 */
public class WPKey {

    public static class URL {
        //TODO
        public static final String XMLRPC_URL = "<REPLACE WITH YOU XML RPC URL>";
    }

    public static class METHOD {
        public static final String NEW_POST = "wp.newPost";
        public static final String GET_POST = "wp.getPosts";
        public static final String EXTAPI = "extapi.callWpMethod";
        public static final String UPLOAD_IMAGE = "wp.uploadFile";
        public static final String UPDATE_PROFILE = "wp.editProfile";
        public static final String NEWCOMMENT = "wp.newComment";
    }

    public static class KEY {
        public static final String USERNAME = "username";
        public static final String POST_TITLE = "post_title";
        public static final String POST_TYPE = "post_type";
        public static final String POST_STATUS = "post_status";
        public static final String POST_CONTENT = "post_content";
        public static final String CUSTOM_FIELD = "custom_fields";
        public static final String KEY = "key";
        public static final String VALUE = "value";
        public static final String ID_ANGGOTA_DPR = "id_dpr_pemiluapi";
        public static final String NAMA_ANGGOTA = "name_dpr";
        public static final String META_QUERY = "meta_query";
        public static final String ID_DPR_PEMILU = "id_dpr_pemiluapi";
        public static final String DAPIL = "dapil";

        public static final String TGL_AGENDA = "tanggal_agenda";
        public static final String KOMISI = "komisi";


        //user
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
        public static final String URL = "url";
        public static final String NICKNAME = "nickname";
        public static final String CONTENT = "content";
        public static final String AUTHOR = "author";
        public static final String AUTHOR_URL = "author_url";
        public static final String AUTHOR_EMAIL = "author_email";
    }

    public static class CUSTOM_FIELD {
        public static final String BIOCOMMENT = "biocomment";
        public static final String STATUSANGGOTA = "status";
        public static final String ASPIRASI = "aspirasi";
        public static final String AGENDA = "agenda";
        public static final String DUKUNG = "dukung";
    }
}
