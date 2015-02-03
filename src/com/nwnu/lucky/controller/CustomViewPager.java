package com.nwnu.lucky.controller;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * �Զ���Viewpager
 * 
 * @author zhi
 * 
 */
public class CustomViewPager extends ViewPager {

	private boolean isCanScroll = true;

	/**
	 * ���Է���
	 * @return
	 */
	public boolean isCanScroll() {
		return isCanScroll;
	}

	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	/**
	 * ���Ƿ���
	 * @param context
	 */
	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * ������ķ���ֵ��true����ô˵�����δ����¼������ѵ��ˣ��ᴫ����һ���µĴ����¼��������false����ô˵��û�б����ѵ����Ͳ��ỻ��һ���µ��¼���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			try {
				return super.onTouchEvent(arg0);
			} catch (Exception e) {
				return true;
			}
		}else{
			return true;
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (isCanScroll) {
			try {
				return super.onInterceptTouchEvent(arg0);
			} catch (Exception e) {
				return true;
			}
		}else{
			return true;
		}
	}
}
