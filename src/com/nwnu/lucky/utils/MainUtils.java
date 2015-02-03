package com.nwnu.lucky.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

public class MainUtils {
	/**
	 * dpת��Ϊpx
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * pxת��Ϊdp
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int Px2Dp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
	/**
	 * ��ȡ״̬���߶�
	 * @param view
	 * @return
	 */
	public static int StatusBarHeight(View view){
		Rect outRect = new Rect();
		view.getWindowVisibleDisplayFrame(outRect);
		return outRect.top;
	}
}
