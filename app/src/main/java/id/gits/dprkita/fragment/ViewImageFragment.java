package id.gits.dprkita.fragment;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.android.iconify.Iconify;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import id.gits.dprkita.R;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.DynamicShareActionProvider;
import id.gits.dprkita.utils.view.TouchImageView;

public class ViewImageFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TouchImageView touchImageView;

    public ViewImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewImageFragment.
     */
    public static ViewImageFragment newInstance(String param1, String param2) {
        ViewImageFragment fragment = new ViewImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ab.setTitle(mParam1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResource(), container, false);

        touchImageView = (TouchImageView) view.findViewById(R.id.touch_image);
        Picasso.with(getActivity())
                .load(mParam2.replace(" ", "%20"))
                .placeholder(R.drawable.gradient_header_background)
                .error(R.drawable.gradien_black)
                .into(touchImageView);

        HashMap<String, String> props = new HashMap<>();
        props.put("title", mParam1);
        Helper.addActionMixpanel(getActivity(), getResources().getString(R.string.mix_selasar_detail), props);
        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_view_image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_share, menu);
        menu.findItem(R.id.action_share).setIcon(Helper.makeOwnIcon(getActivity(), R.color.white, Iconify.IconValue.fa_share_alt));
        final String share_to = "Saya telah membaca infogragis" + mParam1 + " di aplikasi " + getResources().getString(R.string.app_name) + mParam2.replace(" ", "%20");

        DynamicShareActionProvider mShareActionProvider =
                (DynamicShareActionProvider)
                        MenuItemCompat.getActionProvider
                                (menu.findItem(R.id.action_share));

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareDataType("text/plain");
            mShareActionProvider.setOnShareIntentUpdateListener(new DynamicShareActionProvider.OnShareIntentUpdateListener() {
                @Override
                public Bundle onShareIntentExtrasUpdate() {
                    Bundle extras = new Bundle();
                    extras.putString(android.content.Intent.EXTRA_TEXT, share_to);
                    return extras;
                }
            });
        }
    }
}
