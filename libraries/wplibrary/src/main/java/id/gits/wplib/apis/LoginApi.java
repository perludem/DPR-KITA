package id.gits.wplib.apis;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ibun on 13/02/2015.
 */
public class LoginApi extends BaseWpApi {
    private static LoginApi api;

    public static LoginApi newInstance() {
        if (api == null)
            api = new LoginApi();
        return api;
    }

    public void callApi(String username, String password, WpCallback<ApiDao> callback) {
        Type myType = new TypeToken<ApiDao>() {
        }.getType();
        callWPApi(callback, myType, "wp.getProfile", "", username, password);
    }

    public class ApiDao {
        private String user_id;
        private String username;
        private String first_name;
        private String last_name;
        private String bio;
        private String email1;
        private String email;
        private String nickname;
        private String nicename;
        private String url;
        private String display_name;
        private String registered;
        private List<String> roles;

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public void setEmail1(String email1) {
            this.email1 = email1;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setNicename(String nicename) {
            this.nicename = nicename;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDisplay_name(String display_name) {
            this.display_name = display_name;
        }

        public void setRegistered(String registered) {
            this.registered = registered;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public List<String> getRoles() {
            return roles;
        }

        public String getUsername() {
            return username;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getBio() {
            return bio;
        }

        public String getEmail1() {
            return email1;
        }

        public String getNickname() {
            return nickname;
        }

        public String getNicename() {
            return nicename;
        }

        public String getUrl() {
            return url;
        }

        public String getDisplay_name() {
            return display_name;
        }

        public String getRegistered() {
            return registered;
        }

        public String getUser_id() {
            return user_id;
        }
    }
}
