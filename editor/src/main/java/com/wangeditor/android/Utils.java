package com.wangeditor.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.ByteArrayOutputStream;

/**
 * Created by ZQiong on 2018/3/22.
 */

public final class Utils {

    private static boolean debug = false;

    private Utils() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }


    public static void isDebug(boolean isDebug) {
        debug = isDebug;
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap decodeResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }


    public static void logInfo(String info) {
        if (debug) {
            Log.i("WREditor", info);
        }
    }


    private static float density = -1f;

    private static float getDensity() {
        if (density <= 0f) {
            density = Resources.getSystem().getDisplayMetrics().density;
        }
        return density;
    }


    public static int px2dp(float pxValue) {
        return (int) (pxValue / getDensity() + 0.5f);
    }

    public static int dp2px(float dpValue) {
        return (int) (getDensity() * dpValue + 0.5);
    }

    public static int[] getScreenWidthAndHeight(Context context) {
        Point outSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getSize(outSize);

        int[] widthAndHeight = new int[2];
        widthAndHeight[0] = outSize.x;
        widthAndHeight[1] = outSize.y;
        return widthAndHeight;
    }

}
