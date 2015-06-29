package id.gits.wplib.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PostDao {
    private String post_id;
    private String post_content;
    private Date post_date;
    private String post_status;
    private String link;
    private String post_author;
    private String title;
    private String attachment_image;
    private String url_image_profile;
    private int total_comment;
    private List<HashMap<String, String>> custom_fields;

    public int getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(int total_comment) {
        this.total_comment = total_comment;
    }

    public String getAttachment_image() {
        return attachment_image;
    }

    public void setAttachment_image(String attachment_image) {
        this.attachment_image = attachment_image;
    }

    public String getUrl_image_profile() {
        return url_image_profile;
    }

    public void setUrl_image_profile(String url_image_profile) {
        this.url_image_profile = url_image_profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public String getPost_status() {
        return post_status;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPost_author() {
        return post_author;
    }

    public void setPost_author(String post_author) {
        this.post_author = post_author;
    }

    public List<HashMap<String, String>> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<HashMap<String, String>> custom_fields) {
        this.custom_fields = custom_fields;
    }
}