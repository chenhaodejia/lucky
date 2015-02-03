package com.nwnu.lucky.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//import cn.domob.android.ads.DomobAdEventListener;
//import cn.domob.android.ads.DomobAdView;
//import cn.domob.android.ads.DomobAdManager.ErrorCode;

import com.nwnu.lucky.R;
import com.nwnu.lucky.controller.CustomViewPager;
import com.nwnu.lucky.controller.GridViewAdapter;
import com.nwnu.lucky.controller.ShakeListener;
import com.nwnu.lucky.controller.ShakeListener.OnShakeListener;
import com.nwnu.lucky.controller.ViewPagerAdapter;
import com.nwnu.lucky.model.HouseWorkService;
import com.nwnu.lucky.utils.MainUtils;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnPageChangeListener {
	// ����ViewPager����--ViewPager������--ArrayList���View--����view������Դ--�ײ�pointͼƬ--��¼��ǰλ��
	private CustomViewPager viewpager;
	private ViewPagerAdapter vpAdapter;
	private ArrayList<View> views;
	private static int[] viewLayouts = { R.layout.viewpager_shake,
			R.layout.viewpager_gridview };
	private ImageView[] viewpager_points;
	// ����Ĭ�ϵ�ǰҳ
	private int currentIndex = viewLayouts.length - 1;
	LayoutInflater mInflater;
	// �ײ�������ť--��ʼ��ť
	LinearLayout nav_btn;
	Button nav_btn_start;
	/**
	 * ������gridview
	 * **************************************************************
	 */
	private HouseWorkService service;
	private ArrayList<String> grids;
	private ArrayList<Integer> gridsID;
	public static ArrayList<Integer> deleteGridsID;
	// �ܼ�¼��
	private int count;
	// ��ʾͼƬ��ѡ��־--ȫѡ��־
	private boolean visible_flag = false;
	private boolean selectAll_flag = true;
	private GridViewAdapter mGridViewAdapter;
	// popupwindow
	private PopupWindow popupWindow_top;
	private PopupWindow popupWindow_top_shake;
	private PopupWindow popupWindow_bottom;
	private PopupWindow popupWindow_deletedialogcustom;
	// ѡ����Ŀ��
	private TextView selectedItems;
	// ȫѡ��ť
	private Button btn_select_all;
	// gridview gv
	private GridView gv;
	/**
	 * ������shake **************************************************************
	 */
	private ShakeListener mShakeListener = null;
	private Vibrator mVibrator;
	// ������--���soundID
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	// ҡ�γ������������� viewpager_shake_text
	private TextView viewpager_shake_text;
	// ȫ�� -- ����--data id����
	private ArrayList<String> allDatas;
	private ArrayList<Integer> allIds;
	public static ArrayList<Integer> hitIds;
	private String hitData;
	private Integer hitId;
	// Random����
	private Random mRandom;
	// ��ʼ��־
	private boolean start_flag = true;

	/**
	 * �ٰ�һ���˳� ****************************************************
	 */
	private long waitTime = 2000;
	private long touchTime = 0;
	/**
	 * ���˹��*************************
	 */
	//private RelativeLayout mAdRelativeLayout;
	//private DomobAdView mAdBanner;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ����Ͳ
		mInflater = LayoutInflater.from(this);
		// ActionBarʵ�����ϵ���
		ActionBar actionBar = this.getActionBar();
		actionBar.show();
		// ��ʼ��view
		initView();
	}

	/**
	 * ��ʼ�����
	 */
	private void initView() {
		// ��ȡ��nav_btn_start��ť����
		nav_btn_start = (Button) findViewById(R.id.nav_btn_start);
		// ��ʼ�����ݿ�service
		service = new HouseWorkService(this);
		// ʵ����ArrayList����--ViewPager--ViewPager������
		views = new ArrayList<View>();
		viewpager = (CustomViewPager) findViewById(R.id.viewpager);
		// ����viewpagerҳ���л�ʱ��
		// UpdateViewPagerSpeed(200);
		vpAdapter = new ViewPagerAdapter(views);
		// ��ʼ������
		initData();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		// ��ȡ�ܼ�¼��
		count = (int) service.getCount_selected();
		// ���һ��
		if (views.size() > 0) {
			views.clear();
		}
		// ��ʼ������
		for (int i = 0; i < viewLayouts.length + (count - 1) / 6; i++) {
			if (i >= viewLayouts.length) {
				views.add(mInflater.inflate(
						viewLayouts[viewLayouts.length - 1], null));
			} else {
				views.add(mInflater.inflate(viewLayouts[i], null));
			}
		}
		// ����������
		viewpager.setAdapter(vpAdapter);
		// ���õ�ǰview--gridView
		viewpager.setCurrentItem(currentIndex);
		initGrids(views.get(currentIndex));
		// ���ü�����
		viewpager.setOnPageChangeListener(this);
		// ��ʼ���ײ�point
		initViewPagerPoint();
	}

	/**
	 * ��ʼ��С��
	 */
	private void initViewPagerPoint() {
		LinearLayout linerLayout = (LinearLayout) findViewById(R.id.viewpager_point);
		viewpager_points = new ImageView[viewLayouts.length + (count - 1) / 6];
		// ���linerLayout�е�С��
		if (linerLayout.getChildCount() > 0) {
			linerLayout.removeAllViewsInLayout();
		}
		// ѭ�����С��
		for (int i = 0; i < viewpager_points.length; i++) {
			// inflate��ImageView���� �����ò���
			ImageView point = (ImageView) mInflater.inflate(
					R.layout.viewpager_point_item, null);
			// Ĭ��Ϊ��ɫ
			point.setEnabled(true);
			// ���ü���
			point.setOnClickListener(this);
			// ����λ��tag������ȡ���뵱ǰλ�ö�Ӧ
			point.setTag(i);
			viewpager_points[i] = point;
			linerLayout.addView(point);
		}
		// ���õ�ǰС�� Ϊ��ɫ ��ʾѡ��
		viewpager_points[currentIndex].setEnabled(false);
	}

	/**
	 * ������
	 */
	public void longClickVibrator() {
		// ��ȡ����
		if (mVibrator == null) {
			mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		}
		mVibrator.vibrate(new long[] { 50, 50 }, -1);
		// mVibrator.cancel();
	}

	/**
	 * activity�������ں���--�ָ�
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// �ָ�shake
		if (currentIndex == 0) {
			initShake();
		} else if (hitIds != null) {
			// ���¿�ʼ
			Restart();
			initView();
		}
	}

	/**
	 * ���¿�ʼ
	 */
	private void Restart() {
		if (hitIds != null) {
			hitIds.clear();
			hitIds = null;
		}
		start_flag = true;
		nav_btn_start.setText("��ʼ");
	}

	/**
	 * activity�������ں���--��ͣ
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}

	/**
	 * btn_add���gridѡ��
	 * 
	 * @param view
	 */
	public void nav_btn_add(View view) {
		Intent intent = new Intent(this, SecondActivity.class);
		startActivity(intent);
	}

	/**
	 * nav_btn_start��λ��shakeҳ��
	 * 
	 * @param view
	 */
	public void nav_btn_start(View view) {
		if (!start_flag) {
			start_flag = true;
		}
		setCurView(0);
		setCurDot(0);
	}

	/**
	 * ������״̬�ı�ʱ����
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * ����ǰҳ�汻����ʱ����
	 */
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * ��ǰ��ҳ�汻ѡ���µ���
	 */
	@Override
	public void onPageSelected(int position) {
		// ���õײ�С��ѡ��״̬
		setCurDot(position);
	}

	/**
	 * ͨ������¼��л���ǰҳ��
	 */
	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
	}

	/**
	 * ���õ�ǰҳ���λ��
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= viewLayouts.length + (count - 1) / 6) {
			return;
		}
		viewpager.setCurrentItem(position);
	}

	/**
	 * ���õ�ǰ��С���λ��
	 */
	private void setCurDot(int position) {
		if (position < 0 || position > viewpager_points.length - 1
				|| currentIndex == position) {
			return;
		}
		// ���ɾ��һҳgrid��Ǳ�Խ��bug
		if (currentIndex >= viewpager_points.length) {
			currentIndex = viewpager_points.length - 1;
		}
		viewpager_points[position].setEnabled(false);
		viewpager_points[currentIndex].setEnabled(true);
		currentIndex = position;

		// ����grids
		initGrids(views.get(currentIndex));

		if (!visible_flag) {
			// ������ǽ���ɾ������ ---�ͳ�ʼ��Shake
			if (position == 0) {
				// ��ʼ����
				initShake();
				// ����shake����¼�
				findViewById(R.id.viewpager_shake).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mShakeListener.checkInterval()) {
									mShakeListener.getOnShakeListener()
											.onShake();
								}
							}
						});
				// ���õײ��������ɼ�--����Ч��
				if (nav_btn == null) {
					nav_btn = (LinearLayout) findViewById(R.id.nav_btn);
				}
				nav_btn.startAnimation(AnimationUtils.loadAnimation(this,
						R.anim.out_toptobottom));
				nav_btn.setVisibility(View.GONE);
				// ���ö�������popupwindow_top_shake--����
				initPopupWindow_Top_Shake(nav_btn);
			} else {
				if (mShakeListener != null) {
					mShakeListener.stop();
				}
				if (nav_btn != null) {
					// ����nav_btn_start��ť����
					if (start_flag) {
						nav_btn_start.setText("��ʼ");
					} else {
						nav_btn_start.setText("���¿�ʼ");
					}
					// ���õײ������ɼ�
					nav_btn.setVisibility(View.VISIBLE);
					nav_btn.startAnimation(AnimationUtils.loadAnimation(this,
							R.anim.in_bottomtotop));
					nav_btn = null;
					// ���ö������������ɼ�
					HidePopupWindow_Top_Shake();
				}
			}
		}
	}

	/**
	 * ��ʼ�� popupwindow_top_shake
	 */
	private void initPopupWindow_Top_Shake(View v) {
		// ���popupWindow_top_shake�������ٳ�ʼ��
		if (popupWindow_top_shake != null) {
			return;
		}
		// ��ȡ�Զ���popupWindow_view����
		View popupWindow_view_top_shake = mInflater.inflate(
				R.layout.popupwindow_top_shake, null);
		// ����PopupWindowʵ��,200,LayoutParams.MATCH_PARENT�ֱ��ǿ�Ⱥ͸߶�---����ȡ����
		popupWindow_top_shake = new PopupWindow(popupWindow_view_top_shake,
				LayoutParams.MATCH_PARENT, MainUtils.Dp2Px(this, 50));
		// ���ö���Ч��
		popupWindow_top_shake.setAnimationStyle(R.style.AnimationFade_top);
		// �����ⲿ�ɴ���
		popupWindow_top_shake.setOutsideTouchable(true);
		// ��һ������---��view-������λ����ʾ��ʽ,����Ļ�Ķ���
		popupWindow_top_shake.showAtLocation(v, Gravity.TOP, 0,
				MainUtils.StatusBarHeight(v) - 2);
	}

	/**
	 * ���ö������������ɼ�
	 */
	private void HidePopupWindow_Top_Shake() {
		if (popupWindow_top_shake != null && popupWindow_top_shake.isShowing()) {
			// �ر�popupwindow
			popupWindow_top_shake.dismiss();
			popupWindow_top_shake = null;
		}
	}

	/**
	 * ������gridview
	 * **************************************************************
	 */
	private void initGrids(View gridView) {
		if (currentIndex != 0 || visible_flag) {
			// ��ȡ��ҳ��ʾ������ ����Ӧ��ID
			grids = service.getScrollData_selected((visible_flag ? currentIndex
					: currentIndex - 1) * 6, 6);
			gridsID = service.getScrollDataID_selected(
					(visible_flag ? currentIndex : currentIndex - 1) * 6, 6);
			if (grids.size() == 0) {
				gridView.findViewById(R.id.remind).setVisibility(View.VISIBLE);
			} else {
				gridView.findViewById(R.id.remind).setVisibility(View.GONE);
			}
			gv = (GridView) gridView.findViewById(R.id.gridview);
			mGridViewAdapter = new GridViewAdapter(this, grids, gridsID, gv,
					visible_flag);
			gv.setAdapter(mGridViewAdapter);

			if (!visible_flag) {
				// ������ʾ��ѡ��imageview
				gv.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View v,
							int position, long arg3) {
						// ��
						longClickVibrator();
						// ���õ���PopupWindow����
						initPopupWindow(v);
						// ȥ��shakeҳ��
						viewLayouts = new int[] { R.layout.viewpager_gridview };
						currentIndex--;
						// ���ݸ���
						ToggleSelectImgState_initView(true);
						GridsOnItemClick(v, position);
						return false;
					}
				});
			}
			// ���ѡ��ͼƬ�ɼ� �����õ����¼�
			if (visible_flag) {
				// ����¼� �л�ѡ��״̬
				gv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						GridsOnItemClick(arg1, arg2);
					}
				});
			}
		}
	}

	/**
	 * Grids��ѡ��ͼƬ�ɼ������µĵ����ı�״̬����
	 * 
	 * @param view
	 * @param position
	 */
	private void GridsOnItemClick(View view, int position) {
		// ��ʼ��Ҫɾ����id����
		if (deleteGridsID == null) {
			deleteGridsID = new ArrayList<Integer>();
		}
		Integer temp = gridsID.get(position);
		// ����Ѱ����������ĿID���ͽ����Ƴ����������ͽ�����ӽ�ȥ�������ı�ͼƬ��ʾ״̬
		if (deleteGridsID.contains(temp)) {
			deleteGridsID.remove(temp);
			view.findViewById(R.id.gridview_item_imageview).setEnabled(true);
		} else {
			deleteGridsID.add(temp);
			view.findViewById(R.id.gridview_item_imageview).setEnabled(false);
		}
		// ���û��ȫѡ
		if (deleteGridsID.size() != service.getCount_selected()) {
			selectAll_flag = true;
			btn_select_all.setText("ȫѡ");
			ToggleSelectImgState(true);
		}
		selectedItems.setText("��ѡ��"
				+ (deleteGridsID == null ? 0 : deleteGridsID.size()) + "��");
	}

	/**
	 * ��ʼ��initPopupWindow����
	 */
	private void initPopupWindow(View v) {
		// �ж�popupwindow ������ִ��
		if (null != popupWindow_top && popupWindow_bottom != null) {
			return;
		}

		// ��ȡ�Զ���popupWindow_view����
		View popupWindow_view_top = mInflater.inflate(R.layout.popupwindow_top,
				null);
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
		popupWindow_view_top.findViewById(R.id.btn_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						HidePopupWindow();
					}
				});
		// ���ȫѡ��ť
		btn_select_all = (Button) popupWindow_view_top
				.findViewById(R.id.btn_select_all);
		btn_select_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ȫѡ�Ļ� �ͰѲ�ѯ��������ID ��ֵ��ɾ��ID����--��֮�ÿ�
				if (selectAll_flag) {
					deleteGridsID = service.getAllDataID_selected();
					selectAll_flag = false;
					((Button) v).setText("ȫ��ѡ");
					ToggleSelectImgState(true);
				} else {
					selectAll_flag = true;
					((Button) v).setText("ȫѡ");
					deleteGridsID.clear();
					deleteGridsID = null;
					ToggleSelectImgState(true);
				}
				selectedItems.setText("��ѡ��"
						+ (deleteGridsID == null ? 0 : deleteGridsID.size())
						+ "��");
			}
		});
		// ѡ����Ŀ��
		selectedItems = (TextView) popupWindow_view_top
				.findViewById(R.id.tv_selectitems);
		// ͬ��
		View popupWindow_view_bottom = mInflater.inflate(
				R.layout.popupwindow_bottom, null);
		popupWindow_bottom = new PopupWindow(popupWindow_view_bottom,
				LayoutParams.MATCH_PARENT, MainUtils.Dp2Px(this, 50));
		popupWindow_bottom.setAnimationStyle(R.style.AnimationFade_bottom);
		popupWindow_bottom.setOutsideTouchable(true);
		popupWindow_bottom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		// ɾ����Ŀ����
		popupWindow_view_bottom.findViewById(R.id.btn_cancel)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (deleteGridsID != null && deleteGridsID.size() > 0) {
							DeleteDialogCustom(MainActivity.this.getWindow()
									.getDecorView());
						} else {
							Toast.makeText(MainActivity.this, "����ѡ����Ŀ��",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * �л���ʾgridview_item_imageview
	 * 
	 * @param flag
	 */
	private void ToggleSelectImgState(boolean visible_flag) {
		this.visible_flag = visible_flag;
		mGridViewAdapter.setVisible_flag(visible_flag);
		mGridViewAdapter.notifyDataSetChanged();
	}

	/**
	 * �л���ʾgridview_item_imageview
	 * 
	 * @param flag
	 */
	private void ToggleSelectImgState_initView(boolean visible_flag) {
		this.visible_flag = visible_flag;
		mGridViewAdapter.setVisible_flag(visible_flag);
		initView();
	}

	/**
	 * ɾ������½�������
	 */
	private void MyNotifyDataChange() {
		HidePopupWindow();
	}

	/**
	 * �رյ���
	 */
	private void HidePopupWindow() {
		// ���shakeҳ��
		viewLayouts = new int[] { R.layout.viewpager_shake,
				R.layout.viewpager_gridview };
		currentIndex++;
		if (popupWindow_top != null && popupWindow_top.isShowing()) {
			// �ر�popupwindow
			popupWindow_top.dismiss();
			popupWindow_top = null;
			popupWindow_bottom.dismiss();
			popupWindow_bottom = null;
			// ����gridview_item_imageview--����ȫѡ״̬
			ToggleSelectImgState_initView(false);
			selectAll_flag = true;
			// �ͷ�ɾ��ID������Դ�����ÿ�
			if (deleteGridsID != null) {
				deleteGridsID.clear();
				deleteGridsID = null;
			}
			// gv������¼�
			gv.setOnItemClickListener(null);
		}
		// initView();
	}

	/**
	 * ʹ�÷������ viewpagerҳ���л�ʱ��
	 * 
	 * @param speed
	 */
	/*
	 * private void UpdateViewPagerSpeed(int speed) { try { if (viewpager ==
	 * null) { return; } Field field =
	 * ViewPager.class.getDeclaredField("mScroller"); field.setAccessible(true);
	 * FixedSpeedScroller scroller = new FixedSpeedScroller(
	 * viewpager.getContext(), new AccelerateInterpolator());
	 * field.set(viewpager, scroller); scroller.setmDuration(speed); } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

	/**
	 * �Զ���ɾ��������
	 */
	private void DeleteDialogCustom(View v) {
		// ��ȡpopupwindow�Զ��岼��
		View popupWindow_view_deletedialogcustom = mInflater.inflate(
				R.layout.popupwindow_deletedialogcustom, null);
		// ����PopupWindowʵ��,200,LayoutParams.MATCH_PARENT�ֱ��ǿ�Ⱥ͸߶�---����ȡ����
		popupWindow_deletedialogcustom = new PopupWindow(
				popupWindow_view_deletedialogcustom, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		// ���ö���Ч��
		popupWindow_deletedialogcustom
				.setAnimationStyle(R.style.AnimationFade_bottom);
		// �����ⲿ�ɴ���
		// popupWindow_deletedialogcustom.setOutsideTouchable(true);
		// ��һ������---��view-������λ����ʾ��ʽ,����Ļ�Ķ���
		popupWindow_deletedialogcustom.showAtLocation(v, Gravity.BOTTOM, 0, 0);
		// ����text
		((TextView) popupWindow_view_deletedialogcustom
				.findViewById(R.id.tv_deletedialogcustom)).setText("ȷ��Ҫ�Ƴ���ѡ��"
				+ deleteGridsID.size() + "��ѡ����?");
		// ���ȡ���ô�����ʧ
		popupWindow_view_deletedialogcustom.findViewById(
				R.id.btn_cancle_deletedialogcustom).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ȡ����������
						if (popupWindow_deletedialogcustom != null
								&& popupWindow_deletedialogcustom.isShowing()) {
							popupWindow_deletedialogcustom.dismiss();
							popupWindow_deletedialogcustom = null;
						}
					}
				});
		// ���ȷ����ť
		popupWindow_view_deletedialogcustom.findViewById(
				R.id.btn_ok_deletedialogcustom).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// ȷ��ɾ��
						// ���÷���ķ��� �������ݿ�
						service.delete_selected(deleteGridsID);
						// ����start_flag Ϊtrue---hitIds=null--����Ϊ��ʼ
						Restart();
						// ֪ͨadapter���ݸ���
						MyNotifyDataChange();
						// ȡ����������
						if (popupWindow_deletedialogcustom != null
								&& popupWindow_deletedialogcustom.isShowing()) {
							popupWindow_deletedialogcustom.dismiss();
							popupWindow_deletedialogcustom = null;
						}
					}
				});
	}

	/**
	 * ��ʼ��Shake--ҡһҡ
	 */
	public void initShake() {
		// ��ʼ�������㷨��������
		if (start_flag) {
			allDatas = service.getAllData_selected();
			allIds = service.getAllDataID_selected();
			mRandom = new Random();
			hitIds = new ArrayList<Integer>();
			// start_flag��־��Ϊfalse
			start_flag = false;
		}
		// ��ȡ ҡ�γ���������������--������Ϊ���ɼ�
		viewpager_shake_text = (TextView) findViewById(R.id.viewpager_shake_text);
		viewpager_shake_text.setVisibility(View.GONE);
		// ��������
		loadSound();
		// ��ȡ����
		if (mVibrator == null) {
			mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		}
		// �����𶯼����� Ч��
		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				// ����banner���
//				startAdBanner();
				// �ж�ѡ����ȫ��ѡ��
				if (allIds.size() == 0) {
					if (service.getAllData_selected().size() == 0) {
						Toast.makeText(MainActivity.this, "�������ѡ�",
								Toast.LENGTH_SHORT).show();
						return;
					}
					Toast.makeText(MainActivity.this, "�����¿�ʼ��",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// ����viewpager���ɻ���
				viewpager.setCanScroll(false);
				coreAlgorithm();// ���ú����㷨
				startShakeAnim();// ��ʼ�𶯶���
				startVibrator();// ��ʼ��
				mShakeListener.stop();// ȡ��������
				soundPool.play(soundPoolMap.get(0), 1.0f, 1.0f, 0, 0, 1.0f);// ��������
				//
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// ��������
						soundPool.play(soundPoolMap.get(1), 1.0f, 1.0f, 0, 0,
								1.0f);// ��������
						// ֹͣ��
						mVibrator.cancel();
						// �ٴ�ע�������
						if (currentIndex == 0) {
							mShakeListener.start();
							//����viewpager�ɻ���
							viewpager.setCanScroll(true);
						}
					}
				}, 2000);
			}
		});
	}

	/**
	 * �𶯶���
	 */
	public void startShakeAnim() {
		// shake_top
		Animation ani_shake_top = AnimationUtils.loadAnimation(this,
				R.anim.up_shaketop);
		((ImageView) findViewById(R.id.viewpager_shake_top))
				.startAnimation(ani_shake_top);
		// shake_bottom
		Animation ani_shake_bottom = AnimationUtils.loadAnimation(this,
				R.anim.down_shakebottom);
		((ImageView) findViewById(R.id.viewpager_shake_bottom))
				.startAnimation(ani_shake_bottom);
		// shake_text
		Animation ani_shake_text = AnimationUtils.loadAnimation(this,
				R.anim.down_shaketext);
		// ��������
		viewpager_shake_text.setText(hitData);
		viewpager_shake_text.setVisibility(View.VISIBLE);
		viewpager_shake_text.startAnimation(ani_shake_text);
	}

	/**
	 * ҡһҡ�����㷨
	 */
	private void coreAlgorithm() {
		// ��ȡ����������ķ�Χ--�õ���������
		int max = allIds.size();
		int hitIndex = mRandom.nextInt(max);
		// �����ݴ洢�������������ݼ���--���Ƴ�
		hitId = allIds.remove(hitIndex);
		hitData = allDatas.remove(hitIndex);
		hitIds.add(hitId);
		// int s = random.nextInt(max)%(max-min+1) + min;
		// random.nextInt(max)��ʾ����[0,max]֮����������Ȼ���(max-min+1)ȡģ��
		// ������[10,20]�����Ϊ������������0-20���������Ȼ���(20-10+1)ȡģ�õ�[0-10]֮����������Ȼ�����min=10��������ɵ���10-20�������
	}

	/**
	 * ��ʼ��
	 */
	public void startVibrator() {
		// ��һ�����������ǽ������飬 �ڶ����������ظ�������-1Ϊ���ظ�����-1��pattern��ָ���±꿪ʼ�ظ�
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
	}

	/**
	 * ��������
	 */
	@SuppressLint("UseSparseArrays")
	public void loadSound() {
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		new Thread() {
			public void run() {
				try {
					soundPoolMap.put(
							0,
							soundPool.load(
									getAssets().openFd(
											"sound/shake_sound_male.mp3"), 1));
					soundPoolMap.put(1, soundPool.load(
							getAssets().openFd("sound/shake_match.mp3"), 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * ��дonBackPressed����ֱ�Ӽ������ؼ� ϵͳ����onKeyDown�����return true�ˣ��Ͳ���onBackPressed����
	 */
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, "�ٰ�һ�α�֤�˳���", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			this.finish();
		}
	}

    /**
	 * ���˹��
	 */
	
	/**	
	private void startAdBanner() {
		// ��ȡ���λ��
		if (mAdRelativeLayout == null) {
			mAdRelativeLayout = (RelativeLayout) findViewById(R.id.ad_banner);
		}
		// ������� ������Ӧ ��50
		if (mAdBanner == null) {
			mAdBanner = new DomobAdView(this, "56OJw/souNLPK2iKf/",
					"16TLuASvApfVkNUfnizY7Yvi",
					DomobAdView.INLINE_SIZE_FLEXIBLE);
			mAdBanner.setAdEventListener(new DomobAdEventListener() {

				// �뿪Ӧ�ûص�
				@Override
				public void onDomobLeaveApplication(DomobAdView arg0) {
					// TODO Auto-generated method stub

				}

				// �ɹ����յ���淵�ػص�
				@Override
				public void onDomobAdReturned(DomobAdView arg0) {

				}

				// ���ص�ǰcontext
				@Override
				public Context onDomobAdRequiresCurrentContext() {
					// TODO Auto-generated method stub
					return null;
				}

				// �ɹ��򿪻ص�
				@Override
				public void onDomobAdOverlayPresented(DomobAdView arg0) {
					// TODO Auto-generated method stub

				}

				// �رջص�
				@Override
				public void onDomobAdOverlayDismissed(DomobAdView arg0) {
					// TODO Auto-generated method stub

				}

				// �������ʧ��
				@Override
				public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
					// TODO Auto-generated method stub

				}

				// ������ص�
				@Override
				public void onDomobAdClicked(DomobAdView arg0) {
					// TODO Auto-generated method stub

				}
			});
			// �����view��ӵ����λ
			mAdRelativeLayout.addView(mAdBanner);
		}
		if (mAdRelativeLayout.getVisibility() == View.GONE) {
			mAdRelativeLayout.setVisibility(View.VISIBLE);
			mAdRelativeLayout.startAnimation(AnimationUtils.loadAnimation(this,
					R.anim.in_bottomtotop));
		}
		if (timer == null) {
			timer = new Timer();
			// ��ʱ��--5�������ʧ
			timer.schedule(new TimerTask() {
				// ����ջ
				@Override
				public void run() {
					// ����ui
					// ����Activity.runOnUiThread(Runnable)�Ѹ���ui�Ĵ��봴����Runnable�У�Ȼ������Ҫ����uiʱ�������Runnable���󴫸�Activity.runOnUiThread(Runnable)��
					// ����Runnable���������ui�����б����á������ǰ�߳���UI�߳�,��ô�ж�������ִ�С������ǰ�̲߳���UI�߳�,�����Ƿ������¼����е�UI�߳�
					MainActivity.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							mAdRelativeLayout.startAnimation(AnimationUtils
									.loadAnimation(MainActivity.this,
											R.anim.out_toptobottom));
							mAdRelativeLayout.setVisibility(View.GONE);
						}
					});
					timer.cancel();
					timer = null;
				}
			}, 5000);
		}   
	}  */
}
