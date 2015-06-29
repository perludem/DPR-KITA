package id.gits.dprkita.dao.jetpackdao;

import java.util.List;

/**
 * Created by dodulz on 19/02/15.
 */
public class JetPackPostsPage {
    int found;
    List<JetPackPostPage> posts;

    public int getFound() {
        return found;
    }

    public List<JetPackPostPage> getPosts() {
        return posts;
    }
}
