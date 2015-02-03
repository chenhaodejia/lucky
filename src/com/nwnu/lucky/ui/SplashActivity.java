package com.nwnu.lucky.ui;

//import com.nwnu.lucky.QManager;
import com.nwnu.lucky.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	//splash ��ʾʱ��
	private final int SPLASH_DISPLAY_LENGTH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_view);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				//��ʼ���
//				startPush(SplashActivity.this);
				//������activity--finish
				Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}
	/**
	 * ��ʼpush���
	 * @param mContext
	 */
//	public void startPush(Context mContext){
//		//��ȡʵ��--���ò�Ʒid--��������id--�������
//		QManager push = QManager.getInstance(mContext);
//		push.setKey(mContext, "432e4d51fb19e2d0084b0d4b5e7f9cc3");
//		push.setChannelId(mContext, "360");
//		push.show(mContext, true);
//	}
}
