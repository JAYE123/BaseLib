package core.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import core.base.log.L;

public class ABBitmapUtil {
    private static String TAG = ABBitmapUtil.class.getName();

	/**
	 * 仿微信群组头像配置文件位置
	 */
	private static final String WX_PROPERTIES_1 = "1.0,1.0,198.0,198.0;";
	private static final String WX_PROPERTIES_2 = "1.0,51.0,98.0,98.0;101.0,51.0,98.0,98.0;";
	private static final String WX_PROPERTIES_3 = "51.0,1.0,98.0,98.0;1.0,101.0,98.0,98.0;101.0,101.0,98.0,98.0;";
	private static final String WX_PROPERTIES_4 = "1.0,1.0,98.0,98.0;101.0,1.0,98.0,98.0;1.0,101.0,98.0,98.0;101.0,101.0,98.0,98.0;";
	private static final String WX_PROPERTIES_5 = "34.333332,34.333336,64.666664,64.666664;101.0,34.333336,64.666664,64.666664;1.0,101.0,64.666664,64.666664;67.666664,101.0,64.666664,64.666664;134.33333,101.0,64.666664,64.666664;";
	private static final String WX_PROPERTIES_6 = "1.0,34.333336,64.666664,64.666664;67.666664,34.333336,64.666664,64.666664;134.33333,34.333336,64.666664,64.666664;1.0,101.0,64.666664,64.666664;67.666664,101.0,64.666664,64.666664;134.33333,101.0,64.666664,64.666664;";
	private static final String WX_PROPERTIES_7 = "67.666664,1.0,64.666664,64.666664;1.0,67.666664,64.666664,64.666664;67.666664,67.666664,64.666664,64.666664;134.33333,67.666664,64.666664,64.666664;1.0,134.33333,64.666664,64.666664;67.666664,134.33333,64.666664,64.666664;134.33333,134.33333,64.666664,64.666664;";
	private static final String WX_PROPERTIES_8 = "34.333332,1.0,64.666664,64.666664;101.0,1.0,64.666664,64.666664;1.0,67.666664,64.666664,64.666664;67.666664,67.666664,64.666664,64.666664;134.33333,67.666664,64.666664,64.666664;1.0,134.33333,64.666664,64.666664;67.666664,134.33333,64.666664,64.666664;134.33333,134.33333,64.666664,64.666664;";
	private static final String WX_PROPERTIES_9 = "1.0,1.0,64.666664,64.666664;67.666664,1.0,64.666664,64.666664;134.33333,1.0,64.666664,64.666664;1.0,67.666664,64.666664,64.666664;67.666664,67.666664,64.666664,64.666664;134.33333,67.666664,64.666664,64.666664;1.0,134.33333,64.666664,64.666664;67.666664,134.33333,64.666664,64.666664;134.33333,134.33333,64.666664,64.666664;";


    /**
	 * 传入要合成的bitmap的数量
     * @param BipmapCount
	 * @return
	 */
    public static String getWxPropertieCount(int BipmapCount) {
        switch (BipmapCount) {
            case 1:
                return WX_PROPERTIES_1;
            case 2:
                return WX_PROPERTIES_2;
            case 3:
                return WX_PROPERTIES_3;
            case 4:
                return WX_PROPERTIES_4;
            case 5:
                return WX_PROPERTIES_5;
            case 6:
                return WX_PROPERTIES_6;
            case 7:
                return WX_PROPERTIES_7;
            case 8:
                return WX_PROPERTIES_8;
            case 9:
                return WX_PROPERTIES_9;
            default:
                return WX_PROPERTIES_1;
        }
    }

	static public Drawable getScaleDraw(String imgPath, Context mContext) {

		Bitmap bitmap = null;
		try {
			Log.d(TAG, "[getScaleDraw]imgPath is " + imgPath.toString());
			File imageFile = new File(imgPath);
			if (!imageFile.exists()) {
				Log.d(TAG, "[getScaleDraw]file not  exists");
				return null;
			}
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imgPath, opts);

			opts.inSampleSize = computeSampleSize(opts, -1, 800 * 480);
			// Log.d(TAG,"inSampleSize===>"+opts.inSampleSize);
			opts.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(imgPath, opts);

		} catch (OutOfMemoryError err) {
			Log.d(TAG, "[getScaleDraw] out of memory");

		}
		if (bitmap == null) {
			return null;
		}
		Drawable resizeDrawable = new BitmapDrawable(mContext.getResources(),
				bitmap);
		return resizeDrawable;
	}


    public static boolean saveBitmapJPG(String imageUri, Bitmap bitmap) throws IOException {
        File imageFile = new File(imageUri);
        File tmpFile = new File(imageFile.getAbsolutePath() + ".tmp");
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(tmpFile),  32 * 1024); // 32 Kb
        boolean savedSuccessfully = false;

        try {
            savedSuccessfully = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } finally {
			os.close();
            if(savedSuccessfully && !tmpFile.renameTo(imageFile)) {
                savedSuccessfully = false;
            }

            if(!savedSuccessfully) {
                tmpFile.delete();
            }
        }

        bitmap.recycle();
        return savedSuccessfully;
    }

	public static int computeSampleSize(BitmapFactory.Options options,
										int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;

	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
												int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}

	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap decodeBitmap(Resources res, int id) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 通过这个bitmap获取图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
		if (bitmap == null) {
			L.d("bitmap为空");
		}
		float realWidth = options.outWidth;
		float realHeight = options.outHeight;
		L.d("真实图片高度：" + realHeight + "宽度:" + realWidth);
		// 计算缩放比
		int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
		if (scale <= 0) {
			scale = 1;
		}
		L.d("scale=>" + scale);
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		// 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
		bitmap = BitmapFactory.decodeResource(res, id, options);
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		L.d("缩略图高度：" + h + "宽度:" + w);
		return bitmap;
	}


	/**
	 * 获取bitmap宽高数组
	 * @return 0：宽 1：高
	 */
	public static int[] getBitmapWidthAndHeight(String file){
		int [] size = new int[2];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(file,options);
		size[0] = options.outWidth;
		size[1] = options.outHeight;
		options.inJustDecodeBounds = false;
		return size;
	}

	/**
	 * 将多个Bitmap合并成一个图片。
	 *
	 * @param columns 将多个图合成多少列
	 * @param bitmaps
	 *            ... 要合成的图片
	 * @return
	 */
	public static Bitmap combineBitmaps(int columns, Bitmap... bitmaps) {
		if (columns <= 0 || bitmaps == null || bitmaps.length == 0) {
			throw new IllegalArgumentException(
					"Wrong parameters: columns must > 0 and bitmaps.length must > 0.");
		}
		int maxWidthPerImage = 20;
		int maxHeightPerImage = 20;
		for (Bitmap b : bitmaps) {
			maxWidthPerImage = maxWidthPerImage > b.getWidth() ? maxWidthPerImage
					: b.getWidth();
			maxHeightPerImage = maxHeightPerImage > b.getHeight() ? maxHeightPerImage
					: b.getHeight();
		}
		L.d("maxWidthPerImage=>" + maxWidthPerImage
				+ ";maxHeightPerImage=>" + maxHeightPerImage);
		int rows = 0;
		if (columns >= bitmaps.length) {
			rows = 1;
			columns = bitmaps.length;
		} else {
			rows = bitmaps.length % columns == 0 ? bitmaps.length / columns
					: bitmaps.length / columns + 1;
		}
		Bitmap newBitmap = Bitmap.createBitmap(columns * maxWidthPerImage, rows
				* maxHeightPerImage, Config.ARGB_8888);
		L.d("newBitmap=>" + newBitmap.getWidth() + ","
				+ newBitmap.getHeight());
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				int index = x * columns + y;
				if (index >= bitmaps.length)
					break;
				L.d("y=>" + y + " * maxWidthPerImage=>"
						+ maxWidthPerImage + " = " + (y * maxWidthPerImage));
				L.d("x=>" + x + " * maxHeightPerImage=>"
						+ maxHeightPerImage + " = " + (x * maxHeightPerImage));
				newBitmap = mixtureBitmap(newBitmap, bitmaps[index],
						new PointF(y * maxWidthPerImage, x * maxHeightPerImage));
			}
		}
		return newBitmap;
	}

	/**
	 * Mix two Bitmap as one.
	 *
	 * @param first
	 * @param second
	 * @param fromPoint
	 *            where the second bitmap is painted.
	 * @return
	 */
	public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight(), Config.RGB_565);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, 0, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newBitmap;
	}

	public static void getScreenWidthAndHeight(Activity mContext) {
		DisplayMetrics metric = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		L.d("screen width=>" + width + ",height=>" + height);
	}
	/**
	 * 通过ExifInterface类读取图片文件的被旋转角度
	 * @param path ： 图片文件的路径
	 * @return 图片文件的被旋转角度
	 */
	public static int readPicDegree(String path) {
		int degree = 0;

		// 读取图片文件信息的类ExifInterface
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (exif != null) {
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;

				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		}

		return degree;
	}

	/**
	 * 将图片纠正到正确方向
	 *
	 * @param degree ： 图片被系统旋转的角度
	 * @param bitmap ： 需纠正方向的图片
	 * @return 纠向后的图片
	 */
	public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return bm;
	}
	/**
	 * 转换View到Bitmap
	 *
	 * @param v
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View v) {
		v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
		v.buildDrawingCache();
		Bitmap bitmap = v.getDrawingCache();
		return bitmap;
	}
}