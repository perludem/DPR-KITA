package id.gits.dprkita.utils.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.gits.dprkita.R;

/**
 * @author Ibnu Sina Wardy
 */
public class MyProgressView extends RelativeLayout {
    Context ctx;
    ViewGroup ivProgress;
    ImageView ivProgressAnim;
    TextView tvMessage;
    Button btnRetry;

    Animation anim;

    /**
     * @param context
     * @param attrs
     */
    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;

        View v = LayoutInflater.from(context).inflate(R.layout.view_progress,
                null);

        // progress
        ivProgress = (ViewGroup) v.findViewById(R.id.progress);
        ivProgressAnim = (ImageView) v.findViewById(R.id.progress_anim);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);
        tvMessage = (TextView) v.findViewById(R.id.tv_message);

        anim = AnimationUtils.loadAnimation(ctx, R.anim.rotate_anim);
        ivProgressAnim.startAnimation(anim);

        addView(v);
    }

    public void setRetryClickListener(OnClickListener onClickListener) {
        btnRetry.setOnClickListener(onClickListener);
    }

    public void startProgress() {
        setVisibility(View.VISIBLE);
        ivProgress.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);

        ivProgressAnim.startAnimation(anim);
    }

    public void stopAndGone() {
        setVisibility(View.GONE);
    }

    public void stopAndError(String errorMessage, boolean isRetry) {
        ivProgress.clearAnimation();
        ivProgress.setVisibility(View.GONE);
        if (isRetry)
            btnRetry.setVisibility(View.VISIBLE);
        else
            btnRetry.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(errorMessage);
    }

}
