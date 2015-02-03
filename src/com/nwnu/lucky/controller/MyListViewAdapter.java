package com.nwnu.lucky.controller;

import java.util.ArrayList;

import com.nwnu.lucky.R;
import com.nwnu.lucky.ui.SecondActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListViewAdapter extends BaseAdapter {
	private Context mContext;
	// listviewҪ��ʾ�����ݼ���--��Ӧ��ID����--hw_selected ���� ����hwsid
	private ArrayList<String> mList;
	private ArrayList<Integer> mListID;
	private ArrayList<Integer> mGridsHWSID;
	// inflater
	private LayoutInflater mInflater;
	// ѡ��ͼƬ �ɼ���־
	private boolean visible_flag;

	/**
	 * get set ����
	 * 
	 * @param visible_flag
	 */
	public void setVisible_flag(boolean visible_flag) {
		this.visible_flag = visible_flag;
	}

	/**
	 * ���캯��
	 * 
	 * @param mContext
	 * @param mList
	 * @param mListID
	 */
	public MyListViewAdapter(Context mContext, ArrayList<String> mList,
			ArrayList<Integer> mListID, ArrayList<Integer> mGridsHWSID) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		this.mListID = mListID;
		this.mInflater = LayoutInflater.from(mContext);
		this.mGridsHWSID = mGridsHWSID;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/**
	 * �Ż����ܷ�ʽ
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		//if (convertView == null) {
			// ���listview_item
			convertView = mInflater
					.inflate(R.layout.second_listview_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView
					.findViewById(R.id.second_listview_item_textview);
			holder.image = (ImageView) convertView
					.findViewById(R.id.second_listview_item_imageview);
			convertView.setTag(holder);
		/*} else {
			holder = (ViewHolder) convertView.getTag();
		}*/
		holder.text.setText(mList.get(position));
		// �ж�mGridsHWSID������mListID ��ı䱳����ɫ
		System.out.println(mGridsHWSID.toString()+";;;;");
		System.out.println(mListID.toString());
		if (mGridsHWSID.size() > 0
				&& mGridsHWSID.contains(mListID.get(position))) {
			holder.text.setTextColor(mContext.getResources().getColor(
					R.color.white));
			holder.text.setBackgroundColor(mContext.getResources().getColor(
					R.color.pink_main));
			holder.text.setAlpha(0.6f);
		}
		// ��ѡ��ͼƬ���ɵ��
		holder.image.setClickable(false);
		if (visible_flag) {
			holder.image.setVisibility(View.VISIBLE);
			// mAddToGridsID���ϲ�Ϊ��--�ͼ�齫Ҫ��ʼ����ID�Ƿ���������У��ǵĻ��͸���gridview_item_imageview״̬
			if (SecondActivity.mAddToGridsID != null
					&& SecondActivity.mAddToGridsID.contains(mListID
							.get(position))) {
				holder.image.setEnabled(false);
			} else {
				holder.image.setEnabled(true);
			}
		} else {
			holder.image.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView text;
		ImageView image;
	}
}
