/*
    ShengDao Android Client, BitmapUtils
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.aion.axframe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;

import com.aion.axframe.utils.log.ALog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * [A brief description]
 * 
 * @author huxinwu
 * @version 1.0
 * @date 2014-3-18
 * 
 **/
public class BitmapUtils {

	/**
	 * 从资源文件中获取图片资源转化成bitmap
	 * 
	 * @param context
	 * @param resId
	 *            图片id
	 * @return
	 */
	public static Bitmap getResBitmap(Context context, int resId) {
		Resources res = context.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	/**
	 * 屏幕截屏方法 获取当前屏幕bitmap
	 * 
	 * @param activity
	 * @return
	 */
	public Bitmap printscreen(Activity activity) {
		View view = activity.getWindow().getDecorView();
		int width = CommonUtils.getScreenHeight(activity);
		int height = CommonUtils.getScreenHeight(activity);
		view.layout(0, 0, width, height);
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
		return bitmap;
	}

	/**
	 * 图片缩放
	 * 
	 * @param photo
	 * @param newHeight
	 * @param context
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap photo, int newHeight, Context context) {
		final float densityMultiplier = context.getResources().getDisplayMetrics().density;
		int h = (int) (newHeight * densityMultiplier);
		int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));
		photo = Bitmap.createScaledBitmap(photo, w, h, true);
		return photo;
	}

	/**
	 * byte[]转Bitmap
	 * 
	 * @param bytes
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] bytes) {
		if (bytes.length != 0) {
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}
		return null;
	}

	/**
	 * Bitmap转byte[]
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 将Bitmap转换成InputStream
	 * 
	 * @param bm
	 * @return
	 */
	public InputStream Bitmap2InputStream(Bitmap bm) {
		return Bitmap2InputStream(bm, 100);
	}

	/**
	 * 将Bitmap转换成InputStream
	 * 
	 * @param bm
	 * @param quality
	 * @return
	 */
	public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	/**
	 * 将InputStream转换成Bitmap
	 * 
	 * @param is
	 * @return
	 */
	public Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}


	/**
	 * Bitmap转换成Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable drawable = (Drawable) bd;
		return drawable;
	}

	/**
	 * Drawable转换成Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 字符串转换成Bitmap类型
	 * 
	 * @param  imgBase64Str 图片通过Base64编码成字符串
	 * @return
	 */
	public static Bitmap stringtoBitmap(String imgBase64Str) {
		Bitmap bitmap = null;
		byte[] bitmapArray = null;
		try {
			bitmapArray = Base64.decode(imgBase64Str, Base64.DEFAULT);  
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 将Bitmap转换成字符串
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String bitmaptoString(Bitmap bitmap) {
		String result = null;
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, bStream);
			byte[] bytes = bStream.toByteArray();
			result = Base64.encodeToString(bytes, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据图片路径读取经过压缩处理过的图片
	 * 如果图片很大，不能全部加在到内存中处理，要是全部加载到内存中会内存溢出
	 * 
	 * @param filePath 图片路径
	 * @param reqWidth 要求的宽
	 * @param reqHeight 要求的高
	 * @return
	 */
	public static Bitmap decodeBitmapPath(String filePath, int reqWidth, int reqHeight) {
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	/**
	 * 读取资源文件中经过压缩处理过的图片
	 * 使用方法：decodeBitmapResource(getResources(), R.id.myimage, 100, 100));
	 * @param res Resources对象
	 * @param resId 资源id
	 * @param reqWidth 要求的宽
	 * @param reqHeight 要求的高
	 * @return
	 */
	public static Bitmap decodeBitmapResource(Resources res, int resId, int reqWidth, int reqHeight) {
		
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 计算压缩的值，谷歌官方方法
	 * @param options BitmapFactory.Options
	 * @param reqWidth 要求的宽
	 * @param reqHeight 要求的高
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			
			// Calculate the largest inSampleSize value that is a power of 2 and  keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		
		return inSampleSize;
	}
	
	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap 需要修改的图片
	 * @param pixels 圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels){
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 把图片按照另一个图片的有效图像截取，并加边框（如微信聊天中的图片显示效果）
	 *
	 * @param bitmap_bg 边框形状图
	 * @param bitmap_in 需要处理的源图片
	 * @param shard 边框宽度
	 * @return 处理完成的图片
	 */
	public static Bitmap getShardAndRoundImage(Bitmap bitmap_bg, Bitmap bitmap_in, int shard) {
		return getShardAndRoundImage(bitmap_bg, bitmap_in, 0, 0, shard);
	}

	/**
	 * 把图片按照另一个图片的有效图像截取，并加边框（如微信聊天中的图片显示效果）
	 *
	 * @param bitmap_bg 边框形状图
	 * @param bitmap_in 需要处理的源图片
	 * @param width 最终处理的宽
	 * @param height 最终处理的高
	 * @param shard 边框宽度
	 * @return 处理完成的图片
	 */
	public static Bitmap getShardAndRoundImage(Bitmap bitmap_bg, Bitmap bitmap_in, int width, int height, int shard) {
		return getShardImage(bitmap_bg, getRoundCornerImage(bitmap_bg, bitmap_in, width, height), width, height, shard);
	}

	/**
	 * 将两张图片叠加，并将上面的图缩小一定宽度，形成带有底图边框的图片
	 *
	 * @param bitmap_bg 作为边框的底图
	 * @param bitmap_in 需要缩小并加边框的图片
	 * @param width 最终要得到的宽
	 * @param height 最终要得到的高
	 * @param shard 边框的宽度
	 * @return 最终图片的Bitmap
	 */
	public static Bitmap getShardImage(Bitmap bitmap_bg, Bitmap bitmap_in, int width, int height, int shard) {
		if (width<1 || height<1) {
			width = bitmap_in.getWidth();
			height = bitmap_in.getHeight();
		}
		if (shard<1 || shard>width/2 || shard>height/2) {
			ALog.e("边框宽度在0~width/2或者0~height/2之间");
			return null;
		}
		Bitmap roundConcerImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(roundConcerImage);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, width, height);
		paint.setAntiAlias(true);
		NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
		patch.draw(canvas, rect);
		Rect rect2 = new Rect(shard, shard, width-shard, height-shard);
		canvas.drawBitmap(bitmap_in, rect, rect2, paint);
		return roundConcerImage;
	}

	/**
	 * 把图片按照另一个图片的有效图像截取（如微信聊天中的图片显示效果）
	 *
	 * @param bitmap_bg 最终截取形状的图片
	 * @param bitmap_in 需要截取的图片
	 * @param width 最终要得到的宽
	 * @param height 最终要得到的高
	 * @return 最终图片的Bitmap
	 */
	public static Bitmap getRoundCornerImage(Bitmap bitmap_bg, Bitmap bitmap_in, int width, int height) {
		if (width<1 || height<1) {
			width = bitmap_in.getWidth();
			height = bitmap_in.getHeight();
		}
		Bitmap roundConcerImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(roundConcerImage);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, width, height);
		Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
		paint.setAntiAlias(true);
		NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
		patch.draw(canvas, rect);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap_in, rectF, rect, paint);
		return roundConcerImage;
	}
}
