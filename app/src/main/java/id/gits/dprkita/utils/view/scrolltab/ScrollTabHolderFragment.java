package id.gits.dprkita.utils.view.scrolltab;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {

	protected ScrollTabHolder mScrollTabHolder;

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		mScrollTabHolder = scrollTabHolder;
	}

	@Override
	public void onScroll(RecyclerView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
		// nothing
	}

}