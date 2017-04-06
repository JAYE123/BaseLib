package core.base.views.imageview.clipbitmap;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * @ClassName: ClipImageUtils 把bitmap切成一个带尖角的聊天背景图片，仿微信的图片消息效果
 * @说明:
 * @author 吴桂业与王敏
 * @date 2015-2-9 下午5:11:46
 */
public class ClipImageUtils {

	public static Bitmap clipBitmap(Context context, Bitmap bitmap_src,ClipOption clipOption, Config bitmapConfig) {
		int width = bitmap_src.getWidth();
		int height = bitmap_src.getHeight();
		
		clipOption.check(width,height);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap_des = Bitmap.createBitmap(width, height, bitmapConfig);
		Canvas canvas = new Canvas(bitmap_des);
		Path clipPath = createClipPath(context, width, height, clipOption,clipOption.getTriAngleDirection());
		canvas.clipPath(clipPath);
		canvas.drawBitmap(bitmap_src, 0, 0, null);
		return bitmap_des;
	}

	private static Path createClipPath(Context context, int width, int height,
			ClipOption clipOption, ClipOption.TriAngleDirection direction) {

		int cornerDipInPix =clipOption.getCornerDip();
		int triangleMaginTopInPix =clipOption.getTriangleMaginTop();
		int triangleMaginLeftInPix =clipOption.getTriangleMaginLeft();
		int triangleWidthInPix = clipOption.getTriangleHeight();
		int triangleHightInPix =clipOption.getTriangleBottomLength();

		Path clipPath = new Path();
		clipPath.reset();
		
		if(direction == ClipOption.TriAngleDirection.RIGHT){
			/*clipPath.moveTo(cornerDipInPix, 0);
			clipPath.lineTo(width - cornerDipInPix - triangleWidthInPix, 0);
			clipPath.quadTo(width - triangleWidthInPix, 0, width- triangleWidthInPix, cornerDipInPix);
			clipPath.lineTo(width - triangleWidthInPix, triangleMaginTopInPix);
			
			clipPath.lineTo(width, triangleMaginTopInPix + triangleHightInPix/2.0f);
			
			clipPath.quadTo(width- triangleWidthInPix, triangleMaginTopInPix+triangleHightInPix/2.0f,width- triangleWidthInPix,triangleMaginTopInPix + triangleHightInPix);
			
			clipPath.lineTo(width - triangleWidthInPix, height - cornerDipInPix);
			clipPath.quadTo(width - triangleWidthInPix, height, width-cornerDipInPix-triangleWidthInPix, height);
			clipPath.lineTo(cornerDipInPix, height);
			clipPath.quadTo(0, height, 0, height - cornerDipInPix);
			clipPath.lineTo(0, cornerDipInPix);
			clipPath.quadTo(0, 0, cornerDipInPix, 0);*/
			clipPath.moveTo((float) cornerDipInPix, 0.0F);
			clipPath.lineTo((float) (width - cornerDipInPix - triangleWidthInPix), 0.0F);
			clipPath.quadTo((float) (width - triangleWidthInPix), 0.0F, (float) (width - triangleWidthInPix), (float) cornerDipInPix);
			clipPath.lineTo((float) (width - triangleWidthInPix), (float) triangleMaginTopInPix);
			clipPath.lineTo((float) (width - 4 * triangleWidthInPix / (triangleHightInPix / 2)), (float) triangleMaginTopInPix + (float) triangleHightInPix / 2.0F - 4F);//4F为小尖角的弧度，可以随意修改
			clipPath.quadTo((float)width,(float)triangleMaginTopInPix + (float)triangleHightInPix / 2.0F,(float)(width-4*triangleWidthInPix/(triangleHightInPix/2)),(float)triangleMaginTopInPix + (float)triangleHightInPix / 2.0F+4F);
			clipPath.lineTo((float)(width-triangleWidthInPix),(float)(triangleMaginTopInPix+triangleHightInPix));
			clipPath.lineTo((float)(width - triangleWidthInPix), (float)(height - cornerDipInPix));
			clipPath.quadTo((float)(width - triangleWidthInPix), (float)height, (float)(width - cornerDipInPix - triangleWidthInPix), (float)height);
			clipPath.lineTo((float)cornerDipInPix, (float)height);
			clipPath.quadTo(0.0F, (float)height, 0.0F, (float)(height - cornerDipInPix));
			clipPath.lineTo(0.0F, (float)cornerDipInPix);//0.0,10
			clipPath.quadTo(0.0F, 0.0F, (float)cornerDipInPix, 0.0F);//0,0;10,0;
			
		}else if (direction == ClipOption.TriAngleDirection.LEFT) {
		/*	clipPath.moveTo(cornerDipInPix + triangleWidthInPix, 0);
			clipPath.lineTo(width - cornerDipInPix, 0);
			clipPath.quadTo(width, 0, width, cornerDipInPix);
			clipPath.lineTo(width, height - cornerDipInPix);
			clipPath.quadTo(width, height, width - cornerDipInPix, height);
			clipPath.lineTo(cornerDipInPix + triangleWidthInPix, height);
			clipPath.quadTo(triangleWidthInPix, height, triangleWidthInPix,height - cornerDipInPix);
			
			clipPath.lineTo(triangleWidthInPix, triangleMaginTopInPix+triangleHightInPix);
			clipPath.lineTo(0, triangleMaginTopInPix + triangleHightInPix / 2.0f);
			
			clipPath.quadTo(triangleWidthInPix, triangleMaginTopInPix+triangleHightInPix/2.0f,triangleWidthInPix, triangleMaginTopInPix);
			
			clipPath.quadTo(triangleWidthInPix, 0, triangleWidthInPix+ cornerDipInPix, 0);*/

			clipPath.moveTo((float) (cornerDipInPix + triangleWidthInPix), 0.0F);//10+30=40,0;
			clipPath.lineTo((float) (width - cornerDipInPix), 0.0F);
			clipPath.quadTo((float) width, 0.0F, (float) width, (float) cornerDipInPix);
			clipPath.lineTo((float) width, (float) (height - cornerDipInPix));
			clipPath.quadTo((float) width, (float) height, (float) (width - cornerDipInPix), (float) height);
			clipPath.lineTo((float) (cornerDipInPix + triangleWidthInPix), (float) height);
			clipPath.quadTo((float) triangleWidthInPix, (float) height, (float) triangleWidthInPix, (float) (height - cornerDipInPix));
			clipPath.lineTo((float) triangleWidthInPix, (float) (triangleMaginTopInPix + triangleHightInPix));
			clipPath.lineTo((float)(3*triangleWidthInPix/(triangleHightInPix/2)), (float) triangleMaginTopInPix + (float) triangleHightInPix / 2.0F +3.0F);//3F为小尖角的弧度，可以随意修改F
			clipPath.quadTo(0.0f, (float) triangleMaginTopInPix + (float) triangleHightInPix / 2.0F, (float)(3*triangleWidthInPix/(triangleHightInPix/2)), (float) triangleMaginTopInPix + (float) triangleHightInPix / 2.0F-6F );//由于对称所欲终点需要减去2*3=6的高度
			clipPath.lineTo((float) triangleWidthInPix, (float) triangleMaginTopInPix);
			clipPath.lineTo((float)triangleWidthInPix,(float)cornerDipInPix);
			//clipPath.quadTo((float)triangleWidthInPix, (float)triangleMaginTopInPix + (float)triangleHightInPix / 2.0F, (float)triangleWidthInPix, (float)triangleMaginTopInPix);
			//30, 60+30/2=75;30,60;
			clipPath.quadTo((float)triangleWidthInPix, 0.0F, (float)(triangleWidthInPix + cornerDipInPix), 0.0F);//30,0;30+10=40,0
			
		}else if(direction == ClipOption.TriAngleDirection.TOP){
			
			clipPath.moveTo(cornerDipInPix, triangleWidthInPix);
			
			clipPath.lineTo(triangleMaginLeftInPix , triangleWidthInPix);
			
			clipPath.lineTo(triangleMaginLeftInPix+triangleHightInPix/2.0f , 0);
			
			//clipPath.lineTo(triangleMaginLeftInPix+triangleHightInPix , triangleWidthInPix);
			clipPath.quadTo(triangleMaginLeftInPix , triangleWidthInPix,triangleMaginLeftInPix+triangleHightInPix , triangleWidthInPix);
			
			clipPath.lineTo(width-cornerDipInPix , triangleWidthInPix);
			
			//画右边拐角
			clipPath.quadTo(width, triangleWidthInPix,width,triangleWidthInPix+cornerDipInPix);
			clipPath.lineTo(width, height-cornerDipInPix);
			clipPath.quadTo(width, height,width-cornerDipInPix,height);
			
			//画下面
			clipPath.lineTo(cornerDipInPix, height);
			clipPath.quadTo(0, height,0,height-cornerDipInPix);
			
			//画左边
			clipPath.lineTo(0, triangleWidthInPix+cornerDipInPix);
			clipPath.quadTo(0,  triangleWidthInPix,cornerDipInPix,triangleWidthInPix);

			
			
			
		}else if(direction == ClipOption.TriAngleDirection.BOTTOM){
			
			//画上方加拐角
			clipPath.moveTo(cornerDipInPix, 0);
			clipPath.lineTo(width-cornerDipInPix , 0);
			clipPath.quadTo(width, 0,width,cornerDipInPix);
			
			
			//画右边拐角
			clipPath.lineTo(width , height-triangleWidthInPix-cornerDipInPix);
			clipPath.quadTo(width, height-triangleWidthInPix,width-cornerDipInPix,height-triangleWidthInPix);
			
			
			//画下面需要改动
			
			clipPath.lineTo(triangleMaginLeftInPix+triangleHightInPix , height-triangleWidthInPix);
			clipPath.lineTo(triangleMaginLeftInPix+triangleHightInPix/2 , height);
			//clipPath.lineTo(triangleMaginLeftInPix , height-triangleWidthInPix);
			clipPath.quadTo(triangleMaginLeftInPix+triangleHightInPix , height-triangleWidthInPix,triangleMaginLeftInPix , height-triangleWidthInPix);
			clipPath.lineTo(cornerDipInPix , height-triangleWidthInPix);
			clipPath.quadTo(0, height-triangleWidthInPix,0,height-triangleWidthInPix-cornerDipInPix);
			
			//画左边
			clipPath.lineTo(0, cornerDipInPix);
			clipPath.quadTo(0, 0,cornerDipInPix,0);
		}
		
		return clipPath;

	}

}
