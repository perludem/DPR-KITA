package id.gits.dprkita.utils.view;

import android.content.Context;
import android.util.AttributeSet;

public class MyImageView extends android.widget.ImageView {

    private int mDrawableWidth;
    private int mDrawableHeight;
    private boolean mAdjustViewBounds;
    private int mMaxWidth;
    private int mMaxHeight;

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context) {
        super(context);
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        super.setAdjustViewBounds(adjustViewBounds);
        mAdjustViewBounds = adjustViewBounds;
    }

    public void setMaxWidth(int maxWidth) {
        super.setMaxWidth(maxWidth);
        mMaxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        super.setMaxHeight(maxHeight);
        mMaxHeight = maxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getDrawable() == null)
            return;
        mDrawableWidth = getDrawable().getIntrinsicWidth();
        mDrawableHeight = getDrawable().getIntrinsicHeight();

        int w = 0;
        int h = 0;

        // Desired aspect ratio of the view's contents (not including padding)
        float desiredAspect = 0.0f;

        // We are allowed to change the view's width
        boolean resizeWidth = false;

        // We are allowed to change the view's height
        boolean resizeHeight = false;

        if (mDrawableWidth > 0) {
            w = mDrawableWidth;
            h = mDrawableHeight;
            if (w <= 0)
                w = 1;
            if (h <= 0)
                h = 1;

            // We are supposed to adjust view bounds to match the aspect
            // ratio of our drawable. See if that is possible.
            if (mAdjustViewBounds) {

                int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
                int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

                resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
                resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;

                desiredAspect = (float) w / (float) h;
            }
        }

        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;

        if (resizeWidth || resizeHeight) {
            /*
			 * If we get here, it means we want to resize to match the drawables
			 * aspect ratio, and we have the freedom to change at least one
			 * dimension.
			 */

            // Get the max possible width given our constraints
            widthSize = resolveAdjustedSize(w + pleft + pright, mMaxWidth,
                    widthMeasureSpec);

            // Get the max possible height given our constraints
            heightSize = resolveAdjustedSize(h + ptop + pbottom, mMaxHeight,
                    heightMeasureSpec);

            if (desiredAspect != 0.0f) {
                // See what our actual aspect ratio is
                float actualAspect = (float) (widthSize - pleft - pright)
                        / (heightSize - ptop - pbottom);

                if (Math.abs(actualAspect - desiredAspect) > 0.0000001) {

                    boolean done = false;

                    // Try adjusting width to be proportional to height
                    if (resizeWidth) {
                        int newWidth = (int) (desiredAspect * (heightSize
                                - ptop - pbottom))
                                + pleft + pright;
                        if (newWidth <= widthSize) {
                            widthSize = newWidth;
                            done = true;
                        }
                    }

                    // Try adjusting height to be proportional to width
                    if (!done && resizeHeight) {
                        int newHeight = (int) ((widthSize - pleft - pright) / desiredAspect)
                                + ptop + pbottom;
                        // if (newHeight <= heightSize) {
                        heightSize = newHeight;
                        // }
                    }
                }
            }
        } else {
			/*
			 * We are either don't want to preserve the drawables aspect ratio,
			 * or we are not allowed to change view dimensions. Just measure in
			 * the normal way.
			 */
            w += pleft + pright;
            h += ptop + pbottom;

            w = Math.max(w, getSuggestedMinimumWidth());
            h = Math.max(h, getSuggestedMinimumHeight());

            widthSize = resolveSize(w, widthMeasureSpec);
            heightSize = resolveSize(h, heightMeasureSpec);
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // int width = MeasureSpec.getSize(widthMeasureSpec);
    // setMeasuredDimension(width, width);
    // }

    private int resolveAdjustedSize(int desiredSize, int maxSize,
                                    int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
			/*
			 * Parent says we can be as big as we want. Just don't be larger
			 * than max size imposed on ourselves.
			 */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }
}