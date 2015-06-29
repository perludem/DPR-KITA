package id.gits.dprkita.dao.jetpackdao;

import java.util.HashMap;

/**
 * Created by dodulz on 19/02/15.
 */
public class JetPackPostPage {
    int ID;
    String modified;
    String slug;
    JetPackAuthor author;
    String date;
    String title;
    String content;
    String type;
    String URL;
    String status;
    String excerpt;
    HashMap<String, JetPackAttachment> attachments;
    JetPackDiscussion discussion;

    public String getModified() {
        return modified;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public String getStatus() {
        return status;
    }

    public String getURL() {
        return URL;
    }

    public String getType() {
        return type;
    }

    public String getSlug() {
        return slug;
    }

    public JetPackDiscussion getDiscussion() {
        return discussion;
    }

    public int getID() {
        return ID;
    }

    public HashMap<String, JetPackAttachment> getAttachments() {
        return attachments;
    }

    public JetPackAuthor getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
