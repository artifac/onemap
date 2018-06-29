package com.one.map.view;

import android.os.Bundle;
import android.view.View;
import com.one.map.IMap.IPoiSearchListener;
import com.one.map.map.BitmapDescriptor;
import com.one.map.map.CircleOption;
import com.one.map.map.MarkerOption;
import com.one.map.map.PolylineOption;
import com.one.map.map.element.Circle;
import com.one.map.map.element.IMarker;
import com.one.map.map.element.Marker;
import com.one.map.map.element.Polyline;
import com.one.map.model.Address;
import com.one.map.model.BestViewModel;
import com.one.map.model.LatLng;
import com.one.map.model.MapStatusOperation;
import java.util.List;

/**
 * Created by ludexiang on 2017/11/27.
 */

public interface IMapDelegate<MAP> {

  class CenterLatLngParams {
    public LatLng center;
    public String detailAddress;
    public String simpleBuilding;
  }

  interface IMapListener {

    /**
     * 地图加载完成
     */
    void onMapLoaded();

    /**
     * 地图移动中
     */
    void onMapMoveChange();

    /**
     * 地图移动完成
     */
    void onMapMoveFinish(CenterLatLngParams params);

    void onMapGeo2Address(Address address);

    void onMapPoiAddresses(int type, List<Address> addresses);
  }

  void setMapListener(IMapListener listener);
  
  /**
   * 获得MapView 实例
   *
   * @return
   */
  View getView();
  
  void setPadding(MapStatusOperation.Padding padding);
  
  /**
   * 是否显示路况
   * @param isShowTraffic
   */
  void setTraffic(boolean isShowTraffic);
  
  /**
   * 是否展示 + - 控制
   */
  void setUIController(boolean isShowUIController);
  
  /**
   * 添加Marker
   */
  Marker addMarker(MarkerOption option);

  /**
   * 添加一系列marker
   * @return
   */
  List<Marker> addMarkers(List<MarkerOption> options);
  
  
  IMarker myLocationConfig(BitmapDescriptor bitmapDescriptor, LatLng position);
  
  /**
   * 是否展示定位
   */
  void setMyLocationEnable(boolean enable);

  /**
   * 设置logo 位置
   */
  void setLogoPosition(int position, int left, int top, int right, int bottom);

  /**
   * 获取中心店坐标
   * @return
   */
  LatLng getCenterPosition();

  /**
   * 地址翻转
   * @param latLng
   * @return
   */
  void geo2Address(LatLng latLng);

  void poiNearByWithCity(LatLng latLng, String city);

  /**
   * 关键字搜索
   */
  void poiSearchByKeyWord(String curCity, CharSequence key, IPoiSearchListener listener);

  /**
   * 绘制路线
   * @param
   */
  Polyline addPolyline(PolylineOption option);
  
  Circle addCircle(CircleOption option);

  void startRadarAnim(LatLng latLng);

  void stopRadarAnim();
  
  /**
   * 刷新最佳view
   * @param model
   */
  void doBestView(BestViewModel model);
  
  void clearElements();
  
  boolean showInfoWindow(IMarker marker, CharSequence msg);
  void updateInfoWindowMsg(CharSequence msg);
  void removeInfoWindow();
  
  /*** mapview life cycle begin ***/
  void onCreate(Bundle bundle);
  void onResume();
  void onStart();
  void onPause();
  void onRestart();
  void onStop();
  void onDestroy();
  /*** mapview life cycle end ***/
  
}
