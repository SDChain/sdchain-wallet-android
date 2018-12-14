package com.io.sdchain.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.io.sdchain.base.BaseApplication;

/**
 * Created by xiey on 2017/8/19.
 */

public class ImageLoader {


    /**
     *
     * @param url
     * @param imageView
     */
    public static void loadImage(String url, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param imageView
     */
    public static void loadImage(byte[] url, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .into(imageView);
    }

    /**
     *
     * @param resource
     * @param imageView
     */
    public static void loadImage(int resource, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(resource);
        requestBuilder
                .into(imageView);
    }

    /**
     *
     * @param encodedImage
     * @param imageView
     */
    public static void loadBase64Image(String encodedImage, ImageView imageView) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }


    /**
     *
     * @param url
     * @param imageView
     */
    public static void loadRoundImage(String url, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleTransform())
                )
                .into(imageView);
    }

    /**
     *
     * @param resource
     * @param imageView
     */
    public static void loadRoundImage(int resource, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(resource);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleTransform())
                )
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform())
                )
                .into(imageView);
    }

    /**
     * @param resource
     * @param imageView
     */
    public static void loadRoundBorderImage(int resource, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(resource);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform())
                )
                .into(imageView);
    }

    /**
     * @param url
     * @param borderWidth
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, float borderWidth, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform(borderWidth))
                )
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param borderWidth
     * @param borderColor
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, float borderWidth, int borderColor, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform(borderWidth, borderColor))
                )
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param error
     * @param imageView
     */
    public static void loadImage(String url, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        RequestOptions options = new RequestOptions()
                .error(error);
        requestBuilder
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param error
     * @param imageView
     */
    public static void loadRoundImage(String url, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleTransform())
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param error
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform())
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param error
     * @param borderWidth
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, int error, float borderWidth, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform(borderWidth))
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param error
     * @param borderWidth
     * @param borderColor
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, int error, float borderWidth, int borderColor, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform(borderWidth, borderColor))
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadImage(String url, int placeholder, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(error);
        requestBuilder
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadImage(int url, int placeholder, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(error);
        requestBuilder
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadRoundImage(String url, int placeholder, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleTransform())
                        .placeholder(placeholder)
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, int placeholder, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform())
                        .placeholder(placeholder)
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     */
    public static void loadRoundBorderImage(int url, int placeholder, int error, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform())
                        .placeholder(placeholder)
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param borderWidth
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, int placeholder, int error, float borderWidth, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform(borderWidth))
                        .placeholder(placeholder)
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param borderWidth
     * @param borderColor
     * @param imageView
     */
    public static void loadRoundBorderImage(String url, int placeholder, int error, float borderWidth, int borderColor, ImageView imageView) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        requestBuilder
                .apply(RequestOptions
                        .bitmapTransform(new CircleBorderTransform(borderWidth, borderColor))
                        .placeholder(placeholder)
                        .error(error)
                )
                .transition(new DrawableTransitionOptions().crossFade())
                .into(imageView);
    }

    /**
     *
     * @param url
     * @param placeholder
     * @param error
     * @param imageView
     * @param duration
     */
    public static void loadImage(String url, int placeholder, int error, ImageView imageView, int duration) {
        RequestBuilder<Drawable> requestBuilder = Glide
                .with(BaseApplication.getContext())
                .load(url);
        RequestOptions options = new RequestOptions()
                .placeholder(placeholder)
                .error(error);
        requestBuilder
                .apply(options)
                .transition(new DrawableTransitionOptions().crossFade(duration))
                .into(imageView);
    }

}
