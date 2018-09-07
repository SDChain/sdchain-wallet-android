package com.io.sdchain.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @author xiey
 * @date created at 2018/1/29 15:57
 * @package com.io.io.utils
 * @email xiey94@qq.com
 * @motto Why should our days leave us never to return?
 */

public final class CircleTransform extends BitmapTransformation {

    /**
     *
     * @param pool
     * @param toTransform
     * @param outWidth
     * @param outHeight
     * @return
     */
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
//        float padding = 20.0F;
//        canvas.drawCircle(r, r, r - padding, paint);
//        Paint bounderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        bounderPaint.setStyle(Paint.Style.STROKE);
//        bounderPaint.setColor(Color.BLACK);
//        bounderPaint.setStrokeWidth(padding);
//        canvas.drawCircle(r, r, r - padding, bounderPaint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }
}
