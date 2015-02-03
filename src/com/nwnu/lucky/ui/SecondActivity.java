package com.nwnu.lucky.ui;

import java.util.ArrayList;

import com.nwnu.lucky.R;
import com.nwnu.lucky.controller.MyListViewAdapter;
import com.nwnu.lucky.model.HouseWorkService;
import com.nwnu.lucky.utils.MainUtils;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends Activity {
	private ListView mListView;
	private HouseWorkService service;
	// popupwindow
	private PopupWindow popupWindow_top;
	private PopupWindow popupWindow_bottom;
	private PopupWindow popupWindow_deletedialogcustom;

	private LayoutInflater mInflater;
	// ѡ��ͼƬ--ȫѡ��־
	private boolean selectAll_flag = true;
	// ���ݼ���--��Ӧ��ID����--adapter
	private ArrayList<String> mList;
	private ArrayList<Integer> mListID;
	private MyListViewAdapter mMyListViewAdapter;
	// ��ӵ�grids�ļ���
	public static ArrayList<Integer> mAddToGridsID;
	// ��ʾѡ��������
	private TextView selectedItems;
	// ȫѡ��ť
	private Button btn_select_all_second;
	// hw_selected ���� ����hwsid
	private ArrayList<Integer> mGridsHWSID;
	// ���Գ�ʼ��popupwindow�ı�־
	private boolean popupwindow_show = true;
	// ������
	private Vibrator mVibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		// ActionBarʵ�����ϵ���
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// ��ʼ��listview
		initListView();
	}

	/**
	 * activityû����ȫ�����ǲ��ܵ���PopupWindow�ģ���
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// ��ʼ��popupwindow
		if (hasFocus && popupwindow_show) {
			initPopupWindow(this.getWindow().getDecorView());// �����������Ļ
			ToggleSelectImgState(true);
		}
	}

	/**
	 * �Զ��尴ť ����¼�
	 * 
	 * @param v
	 */
	public void btn_custom(View v) {
		if (mInflater == null) {
			return;
		}
		View alertDialogCustom = mInflater.inflate(R.layout.alertdialogcustom,
				null);
		final TextView et_input = (TextView) alertDialogCustom
				.findViewById(R.id.et_input);
		// ����popupwindow���ɼ�
		popupwindow_show = false;
		final AlertDialog b = new AlertDialog.Builder(this).setView(
				alertDialogCustom).show();
		// ���ð�ť����¼�
		alertDialogCustom.findViewById(R.id.btn_alertdialog_okadd)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String str = et_input.getText().toString().trim();
						if (!str.equals("") && str != null) {
							service.add_hourseworks(str);
							initListView();
						}
						// ȡ��������
						b.dismiss();
					}
				});
	}

	/**
	 * ��ʼ��listview
	 */
	private void initListView() {
		// �½�service
		service = new HouseWorkService(this);
		// ��ȡ��listview
		mListView = (ListView) findViewById(R.id.second_listview);
		mList = service.getAllData_hourseworks();
		mListID = service.getAllDataID_hourseworks();
		mGridsHWSID = service.getAllDataHWSID_selected();
		mMyListViewAdapter = new MyListViewAdapter(this, mList, mListID,
				mGridsHWSID);
		mListView.setAdapter(mMyListViewAdapter);
		// ��������popupwindow
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����popupwindow
				initPopupWindow(view);
				// ѡ��ͼƬ�ɼ�
				ToggleSelectImgState(true);
				return false;
			}
		});
	}

	/**
	 * ��ʼ��popupwindow
	 */
	private void initPopupWindow(View v) {
		// �ж�popupWindow_top�������ٳ�ʼ��
		if (popupWindow_top != null) {
			return;
		}
		//������
		longClickVibrator();
		// ����л�ѡ��ͼƬ��״̬
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ʼ��mAddToGridsID
				if (mAddToGridsID == null) {
					mAddToGridsID = new ArrayList<Integer>();
				}
				Integer temp = mListID.get(position);
				// ����Ѱ����������ĿID���ͽ����Ƴ����������ͽ�����ӽ�ȥ�������ı�ͼƬ��ʾ״̬
				if (mAddToGridsID.contains(temp)) {
					mAddToGridsID.remove(temp);
					view.findViewById(R.id.second_listview_item_imageview)
							.setEnabled(true);
				} else {
					mAddToGridsID.add(temp);
					view.findViewById(R.id.second_listview_item_imageview)
							.setEnabled(false);
				}
				// ���δȫѡ
				if (mAddToGridsID.size() != (int) service
						.getCount_hourseworks()) {
					selectAll_flag = true;
					btn_select_all_second.setText("ȫѡ");
					ToggleSelectImgState(true);
				}
				selectedItems.setText("��ѡ��"
						+ (mAddToGridsID == null ? 0 : mAddToGridsID.size())
						+ "��");
			}
		});
		mInflater = LayoutInflater.from(this);
		// ��ȡpopupwindow�Զ��岼��
		View popupWindow_view_top = mInflater.inflate(
				R.layout.popupwindow_top_second, null);
		// ����PopupWindowʵ��,200,LayoutParams.MATCH_PARENT�ֱ��ǿ�Ⱥ͸߶�---����ȡ����
		popupWindow_top = new PopupWindow(popupWindow_view_top,
				LayoutParams.MATCH_PARENT, MainUtils.Dp2Px(this, 50));
		// ���ö���Ч��
		popupWindow_top.setAnimationStyle(R.style.AnimationFade_top);
		// �����ⲿ�ɴ���
		popupWindow_top.setOutsideTouchable(true);
		// ��һ������---��view-������λ����ʾ��ʽ,����Ļ�Ķ���
		popupWindow_top.showAtLocation(v, Gravity.TOP, 0,
				MainUtils.StatusBarHeight(v) - 2);
		// ���ȡ���ô�����ʧ
		popupWindow_view_top.findViewById(R.id.btn_cancel_second)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						HidePopupWindow();
					}
				});
		// ���ȫѡ��ť
		btn_select_all_second = (Button) popupWindow_view_top
				.findViewById(R.id.btn_select_all_second);
		btn_select_all_second.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȫѡ�Ļ� �ͰѲ�ѯ��������ID ��ֵ��ɾ��ID����--��֮�ÿ�
				if (selectAll_flag) {
					mAddToGridsID = service.getAllDataID_hourseworks();
					selectAll_flag = !selectAll_flag;
					((Button) v).setText("ȫ��ѡ");
					ToggleSelectImgState(true);
				} else {
					selectAll_flag = !selectAll_flag;
					((Button) v).setText("ȫѡ");
					if (mAddToGridsID != null) {
						mAddToGridsID.clear();
						mAddToGridsID = null;
					}
					ToggleSelectImgState(true);
				}
				selectedItems.setText("��ѡ��"
						+ (mAddToGridsID == null ? 0 : mAddToGridsID.size())
						+ "��");
			}
		});
		// ����ѡ������Ŀ
		selectedItems = (TextView) popupWindow_view_top
				.findViewById(R.id.tv_selectitems_second);
		// ͬ��
		View popupWindow_view_bottom = mInflater.inflate(
				R.layout.popupwindow_bottom_second, null);
		popupWindow_bottom = new PopupWindow(popupWindow_view_bottom,
				LayoutParams.MATCH_PARENT, MainUtils.Dp2Px(this, 50));
		popupWindow_bottom.setAnimationStyle(R.style.AnimationFade_bottom);
		popupWindow_bottom.setOutsideTouchable(true);
		popupWindow_bottom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		// ȷ�����
		popupWindow_view_bottom.findViewById(R.id.btn_okadd_second)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mAddToGridsID != null && mAddToGridsID.size() > 0) {
							service.addToGrids(mAddToGridsID);
							HidePopupWindow();
							//initListView();
							BackMainActivity();
						} else {
							Toast.makeText(SecondActivity.this, "����ѡ����Ŀ��",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		// ȷ��ɾ��
		popupWindow_view_bottom.findViewById(R.id.btn_okdelete_second)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mAddToGridsID != null && mAddToGridsID.size() > 0) {
							//����ȷ��ɾ���Ի���
							DeleteDialogCustom(SecondActivity.this.getWindow().getDecorView());
						} else {
							Toast.makeText(SecondActivity.this, "����ѡ����Ŀ��",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * ActionBar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ����ActionBar itemѡ���¼�
		switch (item.getItemId()) {
		case android.R.id.home:
			BackMainActivity();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * �ص�MainActivity
	 */
	private void BackMainActivity(){
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		this.finish();
	}

	/**
	 * activity���ٵ�ʱ��
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		popupwindow_show = true;
	}

	/**
	 * �л���ʾgridview_item_imageview
	 * 
	 * @param flag
	 */
	private void ToggleSelectImgState(boolean visible_flag) {
		mMyListViewAdapter.setVisible_flag(visible_flag);
		mMyListViewAdapter.notifyDataSetChanged();
	}

	/**
	 * �رյ���
	 */
	private void HidePopupWindow() {
		if (popupWindow_top != null && popupWindow_top.isShowing()) {
			// �ر�popupwindow
			popupWindow_top.dismiss();
			popupWindow_top = null;
			popupWindow_bottom.dismiss();
			popupWindow_bottom = null;
			// ��ղ��ÿ�mAddToGridsID
			if (mAddToGridsID != null) {
				mAddToGridsID.clear();
				mAddToGridsID = null;
			}
			// ȫѡ��־��Ϊture
			selectAll_flag = true;
			// ѡ��ͼƬ���ɼ�
			ToggleSelectImgState(false);
			// ȡ��listview�ĵ���¼�
			mListView.setOnItemClickListener(null);
		}
	}

	/**
	 * �Զ���ɾ��������
	 */
	private void DeleteDialogCustom(View v) {
		// ��ȡpopupwindow�Զ��岼��
		View popupWindow_view_deletedialogcustom = mInflater.inflate(
				R.layout.popupwindow_deletedialogcustom_second, null);
		// ����PopupWindowʵ��,200,LayoutParams.MATCH_PARENT�ֱ��ǿ�Ⱥ͸߶�---����ȡ����
		popupWindow_deletedialogcustom = new PopupWindow(popupWindow_view_deletedialogcustom,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
		// ���ö���Ч��
		popupWindow_deletedialogcustom.setAnimationStyle(R.style.AnimationFade_bottom);
		// �����ⲿ�ɴ���
		//popupWindow_deletedialogcustom.setOutsideTouchable(true);
		// ��һ������---��view-������λ����ʾ��ʽ,����Ļ�Ķ���
		popupWindow_deletedialogcustom.showAtLocation(v, Gravity.BOTTOM, 0,0);
		//����text
		((TextView)popupWindow_view_deletedialogcustom.findViewById(R.id.tv_deletedialogcustom_second)).setText("ȷ��Ҫɾ����ѡ��"+mAddToGridsID.size()+"��ѡ����?");
		// ���ȡ���ô�����ʧ
		popupWindow_view_deletedialogcustom.findViewById(R.id.btn_cancle_deletedialogcustom_second)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//ȡ����������
						if(popupWindow_deletedialogcustom!=null && popupWindow_deletedialogcustom.isShowing()){
							popupWindow_deletedialogcustom.dismiss();
							popupWindow_deletedialogcustom=null;
						}
					}
				});
		// ���ȫѡ��ť
		popupWindow_view_deletedialogcustom.findViewById(R.id.btn_ok_deletedialogcustom_second)
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ȷ��ɾ��
				service.deleteReal(mAddToGridsID);
				Toast.makeText(SecondActivity.this, "ѡ��ɾ���ɹ�����",
						Toast.LENGTH_SHORT).show();
				HidePopupWindow();
				initListView();
				//ȡ����������
				if(popupWindow_deletedialogcustom!=null && popupWindow_deletedialogcustom.isShowing()){
					popupWindow_deletedialogcustom.dismiss();
					popupWindow_deletedialogcustom=null;
				}
			}
		});
		
	}
	/**
	 * ������
	 */
	public void longClickVibrator() {
		// ��ȡ����
		if (mVibrator == null) {
			mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		}
		mVibrator.vibrate(new long[]{50,50}, -1);
		//mVibrator.cancel();
	}
}
