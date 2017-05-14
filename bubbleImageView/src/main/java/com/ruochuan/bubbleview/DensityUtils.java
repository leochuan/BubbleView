package com.ruochuan.bubbleview;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by taofangxin on 16/10/27.
 */

public class DensityUtils {
    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Display display = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        // Point p = new Point();
        // display.getSize(p);//need api13
        return display.getWidth();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
