package com.nwnu.lucky.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * ����ֻ�ҡ�εļ�����
 * 
 * @author zhi
 * 
 */
public class ShakeListener implements SensorEventListener {
	// �ٶȷ�ֵ����ҡ���ٶȵ�����ֵ���������
	private static final int SPEED_THRESHOLD = 2000;
	// ���μ������ʱ��
	private static final int UPDATE_INTERVAL_TIME = 100;
	// ���и�Ӧ��
	private SensorManager sensorManager;
	// ������
	private Sensor sensor;
	// ������Ӧ������
	private OnShakeListener onShakeListener;
	// ������
	private Context mContext;
	// �ֻ���һ��λ��ʱ������Ӧ����
	private float lastX;
	private float lastY;
	private float lastZ;
	// �ϴμ��ʱ��
	private long lastUpdateTime;
	//ʱ����
	long timeInterval;
	// ������
	public ShakeListener(Context c) {
		// ��ü�������
		mContext = c;
		start();
	}

	/**
	 * ��ʼ
	 */
	public void start() {
		// ��ô��и�Ӧ��
		if(sensorManager==null){
			sensorManager = (SensorManager) mContext
					.getSystemService(Context.SENSOR_SERVICE);
		}
		if (sensorManager != null) {
			// ��ȡ������Ӧ��
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		// ע��
		if (sensor != null) {
			// ��������������������Ӧ������Ӧ����
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
	}

	/**
	 * ֹͣ���
	 */
	public void stop() {
		sensorManager.unregisterListener(this);
	}

	// ����������Ӧ������
	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
	}

	// ��ȡ������Ӧ������
	public OnShakeListener getOnShakeListener() {
		return onShakeListener;
	}

	/**
	 * ������Ӧ��ȡ�仯����
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		//������ε���ʱ����
		if(checkInterval()){
			return;
		}
		// ���xyz����
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		// ���xyz�仯ֵ
		float deltaX = x - lastX;
		float deltaY = x - lastY;
		float deltaZ = x - lastZ;
		// ���ڵ����� ���last����
		lastX = x;
		lastY = y;
		lastZ = z;

		// ����
		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		// �ﵽ�ٶȷ�ֵ��������ʾ
		if (speed >= SPEED_THRESHOLD) {
			onShakeListener.onShake();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	// ҡ�μ����ӿ�
	public interface OnShakeListener {
		public void onShake();
	}

	/**
	 * ������ε���onShake����ʱ����
	 * 
	 * @return
	 */
	public boolean checkInterval() {
		// ���ڼ��ʱ��
		long currentUpdateTime = System.currentTimeMillis();
		// ����ʱ����
		timeInterval = currentUpdateTime - lastUpdateTime;
		// �ж��Ƿ���ʱ����
		if (timeInterval < UPDATE_INTERVAL_TIME) {
			return true;
		}
		// ���ڵ�ʱ�����ϴ�ʱ��
		lastUpdateTime = currentUpdateTime;
		return false;
	}
}
