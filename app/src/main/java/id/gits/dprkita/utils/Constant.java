package id.gits.dprkita.utils;

/**
 * Created by yatnosudar on 1/28/15.
 */
public class Constant {
    public static class URL_API {
        public static final String BASE_URL = "http://api.pemiluapi.org";
        public static final String BASE_URL_JETPACK = "https://public-api.wordpress.com/rest/v1.1/sites/dprkita.org";
        public static final String BASE_IMAGE_GRAVATAR = "http://www.gravatar.com/avatar/";
    }

    public static class KEY {
        public static final String PEMILUAPI = "<REPLACE_WITH_YOUR_API>";
        public static final String MIXPANEL = "<REPLACE_WITH_YOUR_API>";
        public static final String GOOGLE_SENDER_ID = "<REPLACE_WITH_YOUR_API>";


    }

    public static class TAG {
        public static final String DATADAPIL = "datadapil";
        public static final String DETAILANGGOTA = "detailanggota";
        public static final String PROFILE_FRAGMENT = "profile_fragment";
        public static final String WAS_SAVE_DAPIL = "save_dapil";
        public static final String WAS_SAVE_PROVINSI = "save_provinsi";
        public static final String WAS_SAVE_KOMISI = "save_komisi";
        public static final String LATITUDE = "lat";
        public static final String LONGITUDE = "lng";
        public static final int SUCCESS = 1;
        public static final int SUCCESS_V2 = 3;
        public static final int FAIL = 2;
        public static final String TYPE_JSON = "get_recent_posts";
        public static final String DPR = "DPR";
        public static final String LOGIN_SESSION = "login_session";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String WAS_SKIP = "skip";
        public static final int SKIP = 1;
        public static final String COMMENT = "Komentar";
        public static final String TIMELINE = "Podium";
        public static final String ASPIRASI = "Aspirasi";
        public static final String PUBLISH = "publish";
        public static final String RAKYAT = "Rakyat";
        public static final String ROLE_RAKYAT = "rakyat";
        public static final String ANGGOTADPR = "Anggota DPR";
        public static final String ROLE_ANGGOTADPR = "anggotadpr";
        public static final String ID_PEMILUAPI = "id_pemiluapi";
        public static final String NO_HP = "no_hp";
        public static final String ID_DAPIL = "id_dapil";
        public static final String SUCCESS_STRING = "success";
        public static final String FAILURE_STRING = "failure";

        public static final String BERITADPR = "berita-dpr";

    }

    public static class SAMPLE {
        public static final String loadHTML = "\n" +
                "<!DOCTYPE HTML>\n" +
                "<head>\n" +
                "    <title>Hitz FM</title>\n" +
                "\n" +
                "    <style type=\"text/css\">\n" +
                "        body {\n" +

                "            font-family: Arial;\n" +
                "            font-size: 13px;\n" +
                "            line-height: 22px;\n" +
                "            padding: 0px;\n" +
                "            margin:0px;\n" +
                "        }\n" +
                "        .photo {\n" +
                "            position: relative;\n" +
                "            display: inline-block;\n" +
                "        }\n" +
                "        .photo img {\n" +
                "            width: 100%;\n" +
                "            vertical-align: middle;\n" +
                "        }\n" +
                "        .photo:before {\n" +
                "            content: \"\";\n" +
                "            position: absolute;\n" +
                "            top: 0; right: 0; bottom: 0; left: 0;\n" +
                "            background: linear-gradient(\n" +
                "                to bottom, \n" +
                "                rgba(150,35,110,1) 0%, \n" +
                "                rgba(150,35,110,0) 0%, \n" +
                "                rgba(150,35,110,0) 0%, \n" +
                "                rgba(150,35,110,1) 100%\n" +
                "            ); \n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 0px 5%;\n" +
                "        }\n" +
                "        .content h1 {\n" +
                "            color: #212121;\n" +
                "        }\n" +
                "        .content .headline {\n" +
                "            color: #9E9E9E;\n" +
                "            font-size: 16px;\n" +
                "            text-transform: uppercase;\n" +
                "        }\n" +
                "        .content .headline span {\n" +
                "            font-size:11px;\n" +
                "            text-transform: capitalize;\n" +
                "        }\n" +
                "        .content p {\n" +
                "            color: #9E9E9E;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"content\">\n" +
                "    <h1>Lorem Ipsum sit dolor amet</h1>\n" +
                "    <div class=\"headline\">Headline <span>31 desember 2014</span></div>\n" +
                "    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>\n" +
                "\n" +
                "    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
