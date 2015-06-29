package id.gits.dprkita.dao.news;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yatnosudar on 2/3/15.
 */
public class NewsDao implements Serializable {
    String status;
    int count;
    int count_total;
    int pages;
    List<PagesDao> posts;

    public int getPages() {
        return pages;
    }

    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public int getCount_total() {
        return count_total;
    }

    public List<PagesDao> getPosts() {
        return posts;
    }
}
