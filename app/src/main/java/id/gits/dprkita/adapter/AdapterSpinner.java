package id.gits.dprkita.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.gits.dprkita.R;
import id.gits.dprkita.dao.komisi.Komisi;

/**
 * Created by yatnosudar on 1/30/15.
 */
public class AdapterSpinner extends BaseAdapter {


    private List<Komisi> mItems = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public AdapterSpinner(Context context) {
        super();
        layoutInflater = LayoutInflater.from(context);
    }

    public void clear() {
        mItems.clear();
    }

    public void addItem(Komisi yourObject) {
        mItems.add(yourObject);
    }

    public void addItems(List<Komisi> yourObjectList) {
        mItems.addAll(yourObjectList);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
            view = layoutInflater.inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            view.setTag("DROPDOWN");
        }

        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
            view = layoutInflater.inflate(R.layout.
                    toolbar_spinner_item_actionbar, parent, false);
            view.setTag("NON_DROPDOWN");
        }
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return view;
    }

    private String getTitle(int position) {
        return position >= 0 && position < mItems.size() ? "Komisi " + mItems.get(position).getId() : "";
    }
}