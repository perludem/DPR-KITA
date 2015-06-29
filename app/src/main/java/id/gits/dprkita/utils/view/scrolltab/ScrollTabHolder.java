package id.gits.dprkita.utils.view.scrolltab;

import android.support.v7.widget.RecyclerView;

public interface ScrollTabHolder {

	void adjustScroll(int scrollHeight);

	void onScroll(RecyclerView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
