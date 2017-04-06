package core.base.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import core.base.R;
import core.base.views.imageview.CircleImageView;
import core.base.views.imageview.roundedimageview.RoundedImageView;

/**
 * Created by Nowy on 2016/4/8.
 */
public class GlideDisplay {
    public static int BG_DEF = R.color.gray;

    public static boolean isCircle(View v){
        return v instanceof CircleImageView || v instanceof RoundedImageView;
    }

    /**
     * 适配第三方自定义ImageView
     * @param builder
     * @param iv
     */
    private static void fitThirdImageView(DrawableRequestBuilder builder,ImageView iv){
        RequestListener listener = null;
        if(isCircle(iv)) {
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            listener = new AgRequestListener(builder);
            builder.listener(listener);
        }
        builder.into(new MyImageViewTarget(iv));
    }
    public static void display(ImageView iv, String url){
        DrawableRequestBuilder builder = Glide.with(iv.getContext()).load(url).centerCrop()
                .error(BG_DEF)
                .crossFade(300);
        fitThirdImageView(builder, iv);
    }


    public static void display(ImageView iv, File file){
        DrawableRequestBuilder<File> builder = Glide.with(iv.getContext()).load(file).centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade();
        fitThirdImageView(builder, iv);
    }

    public static void display(ImageView iv, Uri uri){
        DrawableRequestBuilder<Uri> builder = Glide.with(iv.getContext()).load(uri).centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .dontAnimate()
                .crossFade();
        fitThirdImageView(builder, iv);
    }

    public static void display(ImageView iv, String uri,int resDef){
        DrawableRequestBuilder<String> builder = Glide.with(iv.getContext()).load(uri).centerCrop()
                .placeholder(resDef)
                .error(resDef)
                .crossFade();
        fitThirdImageView(builder, iv);
    }

    public static void display(ImageView iv, int resId){
        DrawableRequestBuilder<Integer> builder = Glide.with(iv.getContext()).load(resId).centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade();
        fitThirdImageView(builder, iv);
    }

    public static void dispalyWithNoFade(ImageView iv,String url){
        DrawableRequestBuilder<String> builder = Glide.with(iv.getContext()).load(url).centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF);
        fitThirdImageView(builder, iv);
    }
    public static void displayGif(ImageView iv, String url){
        GifRequestBuilder<String> stringGifRequestBuilder = Glide.with(iv.getContext()).load(url).asGif().centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade();
        stringGifRequestBuilder.into(buildTarget(iv, GifDrawable.class));
    }


    public static void dispalyWithCenterCrop(final ImageView imageView,String url){
        DrawableRequestBuilder<String> builder = Glide.with(imageView.getContext()).load(url).centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade();
        fitThirdImageView(builder, imageView);
    }

    public static void dispalyWithCenterCrop(ImageView iv,File file){
        DrawableRequestBuilder<File> builder = Glide.with(iv.getContext()).load(file).centerCrop()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade();
        fitThirdImageView(builder, iv);
    }


    public static void dispalyWithCenterCrop(ImageView iv,File file,int placeholderId,int width,int height){
        DrawableRequestBuilder<File> builder = Glide.with(iv.getContext()).load(file).centerCrop()
                .placeholder(placeholderId)
                .error(placeholderId)
                .override(width, height)
                .crossFade();
        fitThirdImageView(builder, iv);
    }


    public static void dispalyWithCenterCrop(ImageView iv,File file,int placeholderId){
        DrawableRequestBuilder<File> builder = Glide.with(iv.getContext()).load(file).centerCrop()
                .placeholder(placeholderId)
                .crossFade();
        fitThirdImageView(builder, iv);
    }

    public static void dispalyWithFitCenter(ImageView iv,String url){

        DrawableRequestBuilder<String> builder = Glide.with(iv.getContext()).load(url).fitCenter()
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade();
        fitThirdImageView(builder, iv);
    }


    public static void dispalyWithFitCenterDef(ImageView iv,String url,int resImg){

        DrawableRequestBuilder<String> builder = Glide.with(iv.getContext()).load(url).fitCenter()
                .placeholder(resImg)
                .error(resImg)
                .crossFade();
        fitThirdImageView(builder, iv);
    }


    public static void display(ImageView iv, String url, int width, int height){
        DrawableRequestBuilder<String> builder = Glide.with(iv.getContext()).load(url)
                .placeholder(BG_DEF)
                .error(BG_DEF)
                .crossFade()
                .override(width, height)
                .centerCrop();
        fitThirdImageView(builder,iv);

    }

    public static class AgRequestListener implements RequestListener{
        private DrawableRequestBuilder builder;
        AgRequestListener( DrawableRequestBuilder builder){
            this.builder = builder;
        }
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            builder.listener(null);//去掉监听，防止死循环
            builder.load(model).crossFade().centerCrop().into(target);
            return false;
        }
    }
    public static  class MyImageViewTarget extends ImageViewTarget<GlideDrawable> {
        public MyImageViewTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(GlideDrawable resource) {
            view.setImageDrawable(resource);
        }

        @Override
        public void setRequest(Request request) {
            //动态获取id
            int glide_tag_id = view.getContext().getResources().getIdentifier("glide_tag_id", "id", view.getContext().getPackageName());
            view.setTag(glide_tag_id, request);
        }

        @Override
        public Request getRequest() {
            //动态获取id
            int glide_tag_id = view.getContext().getResources().getIdentifier("glide_tag_id", "id", view.getContext().getPackageName());
            return (Request) view.getTag(glide_tag_id);
        }
    }
    public static  class MyGifTarget extends ImageViewTarget<GifDrawable> {
        public MyGifTarget(ImageView view) {
            super(view);
        }

        @Override
        protected void setResource(GifDrawable resource) {
            view.setImageDrawable(resource);
        }

        @Override
        public void setRequest(Request request) {
            //动态获取id
            int glide_tag_id = view.getContext().getResources().getIdentifier("glide_tag_id", "id", view.getContext().getPackageName());
            view.setTag(glide_tag_id, request);
        }

        @Override
        public Request getRequest() {
            //动态获取id
            int glide_tag_id = view.getContext().getResources().getIdentifier("glide_tag_id", "id", view.getContext().getPackageName());
            return (Request) view.getTag(glide_tag_id);
        }
    }
    public static class MyGifImageViewTarget extends GlideDrawableImageViewTarget{

        public MyGifImageViewTarget(ImageView view) {
            super(view);
        }
        @Override
        public void setRequest(Request request) {
            //动态获取id
            int glide_tag_id = view.getContext().getResources().getIdentifier("glide_tag_id", "id", view.getContext().getPackageName());
            view.setTag(glide_tag_id,request);
        }

        @Override
        public Request getRequest() {
            //动态获取id
            int glide_tag_id = view.getContext().getResources().getIdentifier("glide_tag_id", "id", view.getContext().getPackageName());
            return (Request) view.getTag(glide_tag_id);
        }
    }
    @SuppressWarnings("unchecked")
    public static <Z> Target<Z> buildTarget(ImageView view, Class<Z> clazz) {
        if (GlideDrawable.class.isAssignableFrom(clazz)) {
            return (Target<Z>) new MyGifImageViewTarget(view);
        } else if (Bitmap.class.equals(clazz)) {
            return (Target<Z>) new BitmapImageViewTarget(view);
        } else if (Drawable.class.isAssignableFrom(clazz)) {
            return (Target<Z>) new DrawableImageViewTarget(view);
        } else {
            throw new IllegalArgumentException("Unhandled class: " + clazz
                    + ", try .as*(Class).transcode(ResourceTranscoder)");
        }
    }
}
