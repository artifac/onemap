package com.one.map.base;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.one.map.IMap;
import com.one.map.view.IMapView;

/**
 * Created by ludexiang on 2018/4/16.
 */

public abstract class BaseMapFragment extends Fragment implements IMap {

  protected IMapView mMapView;

  private SensorManager mSensorManager;
  private SensorListener listener = new SensorListener();
  private Sensor mSensor;


  @Nullable
  @Override
  public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    initSensor();
    View view = onCreateViewImpl(inflater, container, savedInstanceState);
    return view;
  }

  private void initSensor() {
    //获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
    mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
    /* 获取方向传感器
     * 通过SensorManager对象获取相应的Sensor类型的对象
     */
    mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    //应用在前台时候注册监听器
    mSensorManager.registerListener(listener, mSensor, SensorManager.SENSOR_DELAY_GAME);
  }

  protected abstract View onCreateViewImpl(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState);

  private final class SensorListener implements SensorEventListener {

    private float predegree = 0;

    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {
      /**
       * values[0]: x-axis 方向加速度
       * values[1]: y-axis 方向加速度
       * values[2]: z-axis 方向加速度
       */
      float degree = event.values[0];// 存放了方向值
      onRotate(degree);
      predegree = -degree;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
  }

  protected abstract void onRotate(float degree);

  @Override
  public void onResume() {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  public void onStart() {
    super.onStart();
    mMapView.onStart();
  }

  @Override
  public void onPause() {
    super.onPause();
    mMapView.onPause();
  }

  @Override
  public void onStop() {
    super.onStop();
    mMapView.onStop();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mSensorManager.unregisterListener(listener, mSensor);
    mMapView.onDestroy();
  }
}
