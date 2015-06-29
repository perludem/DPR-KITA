package id.gits.dprkita.utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class CropTopTransformation implements Transformation {
    private int mScreenWidth;

    public CropTopTransformation(int screenWidth) {
        mScreenWidth = screenWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int x = 0;
        int y = 0;
        Bitmap result = Bitmap.createScaledBitmap(source, mScreenWidth, source.getHeight() * mScreenWidth / source.getWidth(), false);
        result = Bitmap.createBitmap(result, x, y, result.getWidth(), result.getWidth() / 2);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "topcrop()";
    }
}