package com.one.map.amap;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
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
import com.one.map.model.MapStatusOperation.Padding;
import com.one.map.view.IMapDelegate;
import java.util.List;

/**
 * Created by ludexiang on 2018/6/4.
 */

public class AMapDelegate implements IMapDelegate<AMap> {

  private Context mContext;
  private MapView mMapView;
  private AMap aMap;
  private IMapListener mMapListener;

  public AMapDelegate(Context context) {
    mContext = context;
    mMapView = new MapView(mContext);
    aMap = mMapView.getMap();
  }

  @Override
  public void setMapListener(IMapListener listener) {
    mMapListener = listener;
  }

  @Override
  public View getView() {
    return mMapView;
  }

  @Override
  public void setPadding(Padding padding) {

  }

  @Override
  public void setTraffic(boolean isShowTraffic) {
    aMap.setTrafficEnabled(isShowTraffic);
  }

  @Override
  public void setUIController(boolean isShowUIController) {
  }

  @Override
  public Marker addMarker(MarkerOption option) {
    return null;
  }

  @Override
  public List<Marker> addMarkers(List list) {
    return null;
  }

  @Override
  public IMarker myLocationConfig(BitmapDescriptor bitmapDescriptor, LatLng position) {
    return null;
  }

  @Override
  public void setMyLocationEnable(boolean enable) {

  }

  @Override
  public void setLogoPosition(int position, int left, int top, int right, int bottom) {

  }

  @Override
  public LatLng getCenterPosition() {
    return null;
  }

  @Override
  public void geo2Address(LatLng latLng) {

  }

  @Override
  public void poiSearchByKeyWord(String curCity, CharSequence key, IPoiSearchListener listener) {
  }

  @Override
  public void poiNearByWithCity(LatLng latLng, String city) {

  }

  @Override
  public Polyline addPolyline(PolylineOption option) {
    return null;
  }

  @Override
  public Circle addCircle(CircleOption option) {
    return null;
  }

  @Override
  public void doBestView(BestViewModel model) {

  }

  @Override
  public void clearElements() {

  }

  @Override
  public boolean showInfoWindow(IMarker marker, CharSequence msg) {
    return false;
  }

  @Override
  public void updateInfoWindowMsg(CharSequence msg) {

  }

  @Override
  public void removeInfoWindow() {

  }

  /************* life cycle begin **************/
  @Override
  public void onCreate(Bundle bundle) {
    if (mMapView != null) {
      mMapView.onCreate(bundle);
    }
  }

  @Override
  public void onResume() {
    if (mMapView != null) {
      mMapView.onResume();
    }
  }

  @Override
  public void onStart() {
  }

  @Override
  public void onPause() {
    if (mMapView != null) {
      mMapView.onPause();
    }
  }

  @Override
  public void onRestart() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void onDestroy() {
    if (mMapView != null) {
      mMapView.onDestroy();
    }
  }
  /************* life cycle end **************/
}
