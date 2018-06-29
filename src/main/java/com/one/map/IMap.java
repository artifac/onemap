package com.one.map;

import com.one.map.map.MarkerOption;
import com.one.map.map.PolylineOption;
import com.one.map.map.element.IMarker;
import com.one.map.map.element.Marker;
import com.one.map.map.element.Polyline;
import com.one.map.model.Address;
import com.one.map.model.BestViewModel;
import com.one.map.model.LatLng;
import com.one.map.view.IMapDelegate.IMapListener;
import java.util.List;


/**
 * 地图对外提供的操作
 */

public interface IMap {

  Marker addMarker(MarkerOption option);

  /**
   * 添加一系列 Marker
   */
  List<Marker> addMarkers(List<MarkerOption> options);

  void showMyLocation();

  void hideMyLocation();

  /**
   * geo to address
   */
  void geo2Address(LatLng latLng);

  /**
   * reverse geo
   */
  LatLng reverseGeo(Address address);

  /**
   * 路线规划
   */
  void drivingRoutePlan(Address from, Address to);

  void drivingRoutePlan(Address from, Address to, boolean arrow);

  void drivingRoutePlan(Address from, Address to, int lineColor, boolean arrow);

  List<LatLng> getLinePoints();

  /**
   * show info window
   */
  boolean showInfoWindow(IMarker marker, CharSequence msg);

  /**
   * update info window msg
   */
  void updateInfoWindowMsg(CharSequence msg);

  void poiSearchByKeyWord(String curCity, CharSequence key, IPoiSearchListener listener);

  void poiNearByWithCity(LatLng latLng, String curCity);

  /**
   * remove info window
   */
  void removeInfoWindow();

  /**
   * draw line
   */
  Polyline addPolyline(PolylineOption option);

  /**
   * logo 位置
   */
  void setLogoPosition(int position, int left, int top, int right, int bottom);

  LatLng getCenterPosition();

  void setMapListener(IMapListener listener);

  /**
   * 路径规划返回的消息
   *
   * @return
   */
  interface IRoutePlanMsgCallback {

    void routePlanPoints(List<LatLng> points);

    void routePlanMsg(String msg, List<LatLng> points);
  }

  void registerPlanCallback(IRoutePlanMsgCallback callback);

  void unRegisterPlanCallback(IRoutePlanMsgCallback callback);

  interface IMarkerClickCallback {

    void onMarkerClick(IMarker marker);
  }

  interface IPoiSearchListener {

    void onMapSearchAddress(List<Address> address);
  }

  void setIMarkerClickCallback(IMarkerClickCallback callback);

  void removeDriverLine();

  /**
   * 清空所有地图元素
   */
  void clearElements();

  /**
   * 刷新最佳view
   * 根据BesetViewModel 来判断是回到定位点还是框view
   * 1.回到定位点 设置zoomCenter 并清空bounds
   * 2.框元素 通过设置bounds
   */
  void doBestView(BestViewModel model);

  void startRadarAnim(LatLng latLng);

  void stopRadarAnim();
}
