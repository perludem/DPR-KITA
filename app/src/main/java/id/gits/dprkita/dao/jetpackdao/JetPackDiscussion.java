package id.gits.dprkita.dao.jetpackdao;

import java.io.Serializable;

/**
 * Created by yatnosudar on 3/7/15.
 */
public class JetPackDiscussion implements Serializable {

    private boolean comments_open;
    private String comment_status;
    private boolean pings_open;
    private int comment_count;

    public boolean isComments_open() {
        return comments_open;
    }

    public String getComment_status() {
        return comment_status;
    }

    public boolean isPings_open() {
        return pings_open;
    }

    public int getComment_count() {
        return comment_count;
    }
}
