package id.gits.dprkita.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import id.gits.dprkita.R;
import id.gits.dprkita.utils.Helper;
import id.gits.dprkita.utils.view.TouchImageView;

public class ImageViewActivity extends BaseActivity {

    private static final String  URLIMAGE = "urlimage";
    private String mParam1 = "IMAGE_ATTACH",mParam2;
    @InjectView(R.id.touch_image)
    TouchImageView touchImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        mParam2 = getIntent().getStringExtra(URLIMAGE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this)
                .load(mParam2.replace(" ", "%20"))
                .placeholder(R.drawable.gradient_header_background)
                .error(R.drawable.gradien_black)
                .into(touchImageView);

        HashMap<String, String> props = new HashMap<>();
        props.put("title", mParam1);
        Helper.addActionMixpanel(this, getResources().getString(R.string.lihat_image), props);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_view;
    }


    public static void startActivity(Activity ctx,String url) {
        Intent iz = new Intent(ctx, ImageViewActivity.class);
        iz.putExtra(URLIMAGE,url);
        ctx.startActivity(iz);
    }

    public void onPasswordClick(View view){
        
    }
}
