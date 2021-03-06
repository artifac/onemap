package com.one.map.view.impl;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import com.one.map.IMap.IPoiSearchListener;
import com.one.map.amap.AMapDelegate;
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
import com.one.map.tencent.TencentMapDelegate;
import com.one.map.view.IMapDelegate;
import com.one.map.view.IMapDelegate.IMapListener;
import com.one.map.view.IMapView;
import java.util.List;

public class MapView implements IMapView {

  private IMapDelegate mMapDelegate;
  private @MapType int mMapType;

  /**
   * @param context getApplicationContext()
   */
  public MapView(Context context, @MapType int type) {
    mMapType = type;
    if (type == TENCENT) {
      mMapDelegate = new TencentMapDelegate(context);
    } else if (type == AMAP) {
      mMapDelegate = new AMapDelegate(context);
    }
  }

  @Override
  public void attachToRootView(ViewGroup viewGroup) {
    viewGroup.addView(mMapDelegate.getView());
  }

  @Override
  public void setPadding(MapStatusOperation.Padding padding) {
    mMapDelegate.setPadding(padding);
  }

  @Override
  public void setTraffic(boolean isShowTraffic) {
    mMapDelegate.setTraffic(isShowTraffic);
  }

  @Override
  public void setUIController(boolean isShowUIController) {
    mMapDelegate.setUIController(isShowUIController);
  }

  @Override
  public int getMapType() {
    return mMapType;
  }

  @Override
  public void geo2Address(LatLng latLng) {
     mMapDelegate.geo2Address(latLng);
  }

  @Override
  public void poiSearchByKeyWord(String curCity, CharSequence key, IPoiSearchListener listener) {
    mMapDelegate.poiSearchByKeyWord(curCity, key, listener);
  }

  @Override
  public void poiNearByWithCity(LatLng latLng, String city) {
    mMapDelegate.poiNearByWithCity(latLng, city);
  }

  @Override
  public LatLng getCenterPosition() {
    return mMapDelegate.getCenterPosition();
  }

  @Override
  public void startRadarAnim(LatLng latLng) {
    mMapDelegate.startRadarAnim(latLng);
  }

  @Override
  public void stopRadarAnim() {
    mMapDelegate.stopRadarAnim();
  }

  @Override
  public void setMapListener(IMapListener listener) {
    mMapDelegate.setMapListener(listener);
  }

  @Override
  public void setLogoPosition(int position, int left, int top, int right, int bottom) {
    mMapDelegate.setLogoPosition(position, left, top, right, bottom);
  }

  @Override
  public IMarker myLocationConfig(BitmapDescriptor bitmapDescriptor, LatLng latLng) {
    return mMapDelegate.myLocationConfig(bitmapDescriptor, latLng);
  }

  @Override
  public void setMyLocationEnable(boolean enable) {
    mMapDelegate.setMyLocationEnable(enable);
  }

  @Override
  public void doBestView(BestViewModel model) {
    mMapDelegate.doBestView(model);
  }

  @Override
  public Marker addMarker(MarkerOption option) {
    return mMapDelegate.addMarker(option);
  }

  @Override
  public List<Marker> addMarkers(List<MarkerOption> options) {
    return mMapDelegate.addMarkers(options);
  }

  @Override
  public boolean showInfoWindow(IMarker marker, CharSequence msg) {
    return mMapDelegate.showInfoWindow(marker, msg);
  }

  @Override
  public void updateInfoWindowMsg(CharSequence msg) {
    mMapDelegate.updateInfoWindowMsg(msg);
  }

  @Override
  public void removeInfoWindow() {
    mMapDelegate.removeInfoWindow();
  }

  /**
   * 驾车路线规划
   */
  @Override
  public Polyline addPolyline(PolylineOption option) {
    return mMapDelegate.addPolyline(option);
  }

  @Override
  public Circle addCircle(CircleOption option) {
    return mMapDelegate.addCircle(option);
  }

  @Override
  public void clearElements() {
    mMapDelegate.clearElements();
  }

  @Override
  public void onCreate(Bundle bundle) {
    mMapDelegate.onCreate(bundle);
  }

  @Override
  public void onRestart() {
    mMapDelegate.onRestart();
  }

  @Override
  public void onStart() {
    mMapDelegate.onStart();
  }

  @Override
  public void onResume() {
    mMapDelegate.onResume();
  }

  @Override
  public void onPause() {
    mMapDelegate.onPause();
  }

  @Override
  public void onStop() {
    mMapDelegate.onStop();
  }

  @Override
  public void onDestroy() {
    mMapDelegate.onDestroy();
  }
}
