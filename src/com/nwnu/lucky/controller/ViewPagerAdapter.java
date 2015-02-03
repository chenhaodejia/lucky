package com.nwnu.lucky.controller;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 
 * @author zhi ����������ViewPager�����������������ݺ�view
 */
public class ViewPagerAdapter extends PagerAdapter {
	// �����б�
	private ArrayList<View> mViews;

	public ViewPagerAdapter(ArrayList<View> mViews) {
		super();
		this.mViews = mViews;
	}

	// ��ȡ��ǰ������
	@Override
	public int getCount() {
		if (mViews != null) {
			return mViews.size();
		}
		return 0;
	}

	// ��ʼ��positionλ�õĽ���
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mViews.get(position), 0);
		return mViews.get(position);
	}

	// �ж��Ƿ��ɶ������ɽ���
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	// ����positionλ�õĽ���
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mViews.get(position));
	}
	
}
