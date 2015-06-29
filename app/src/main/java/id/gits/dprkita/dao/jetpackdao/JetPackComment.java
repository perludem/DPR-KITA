package id.gits.dprkita.dao.jetpackdao;

/**
 * Created by yatnosudar on 3/1/15.
 */
public class JetPackComment {

    private int ID;
    private JetPackPostComment post;
    private JetPackAuthor author;
    private String date;
    private String content;
    private int like;
    private String i_like;

    public int getLike() {
        return like;
    }

    public String getI_like() {
        return i_like;
    }

    public int getID() {
        return ID;
    }

    public JetPackPostComment getPost() {
        return post;
    }

    public JetPackAuthor getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
