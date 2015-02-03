package com.nwnu.lucky.controller;

import java.util.ArrayList;

import com.nwnu.lucky.R;
import com.nwnu.lucky.ui.MainActivity;
import com.nwnu.lucky.utils.MainUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
	private Context mContext;
	// gridҪ��ʾ�����ݼ���--��Ӧ��ID����
	private ArrayList<String> mGrids;
	private ArrayList<Integer> mGridsID;
	private LayoutInflater mInflater;
	private GridView gridView;
	// ��ʾѡ��ͼƬ��־--ȫѡ��־
	private boolean visible_flag;

	/**
	 * get set ����
	 * 
	 * @param visible_flag
	 */
	public void setVisible_flag(boolean visible_flag) {
		this.visible_flag = visible_flag;
	}

	public GridViewAdapter(Context mContext, ArrayList<String> mGrids,
			ArrayList<Integer> mGridsID, GridView gridView, boolean visible_flag) {
		super();
		this.mContext = mContext;
		this.mGrids = mGrids;
		this.mGridsID = mGridsID;
		this.mInflater = LayoutInflater.from(mContext);
		this.gridView = gridView;
		this.visible_flag = visible_flag;
	}

	/**
	 * ����grid����
	 */
	@Override
	public int getCount() {
		return mGrids.size();
	}

	@Override
	public Object getItem(int position) {
		return mGrids.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ��װgird�������Ż��ķ�ʽ
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.viewpager_gridview_item,
					null);
			holder = new ViewHolder();

			holder.text = (TextView) convertView
					.findViewById(R.id.gridview_item_textview);
			holder.hitIndex = (TextView) convertView
					.findViewById(R.id.tv_hit_index);
			holder.image = (ImageView) convertView
					.findViewById(R.id.gridview_item_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.text.setHeight(gridView.getHeight() / 3
				- MainUtils.Dp2Px(mContext, 7 * 2));
		holder.text.setText(mGrids.get(position));
		// ���ݱ�־�ж��Ƿ���ʾgridview_item_imageview
		if (visible_flag) {
			holder.image.setVisibility(View.VISIBLE);
			// ɾ��ID���ϲ�Ϊ��--�ͼ�齫Ҫ��ʼ����ID�Ƿ���������У��ǵĻ��͸���gridview_item_imageview״̬
			if (MainActivity.deleteGridsID != null
					&& MainActivity.deleteGridsID.contains(mGridsID
							.get(position))) {
				holder.image.setEnabled(false);
			} else {
				holder.image.setEnabled(true);
			}
		} else {
			holder.image.setVisibility(View.GONE);
		}
		// hitIds��Ϊ��--�ͼ��Ҫ��ʼ����id�Ƿ����������,�ǵĻ��͸��ı���
		if (MainActivity.hitIds != null
				&& MainActivity.hitIds.contains(mGridsID.get(position))) {
			//�ı䱳����ɫ��͸����Ϊ50%
			holder.text.setBackgroundColor(mContext.getResources().getColor(R.color.gray_main));
			holder.text.setAlpha(0.5f);
			//���û�������
			int hitIndex = MainActivity.hitIds.indexOf(mGridsID.get(position))+1;
			holder.hitIndex.setText(hitIndex+"");
			holder.hitIndex.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView text;
		TextView hitIndex = null;
		ImageView image;
	}
}
