package com.one.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.one.map.base.BaseMapFragment;
import com.one.map.factory.MapFactory;
import com.one.map.location.LocationProvider;
import com.one.map.map.BitmapDescriptor;
import com.one.map.map.BitmapDescriptorFactory;
import com.one.map.map.MarkerOption;
import com.one.map.map.MarkerOption.IMarkerClickListener;
import com.one.map.map.PolylineOption;
import com.one.map.map.element.IMarker;
import com.one.map.map.element.Marker;
import com.one.map.map.element.Polyline;
import com.one.map.model.Address;
import com.one.map.model.BestViewModel;
import com.one.map.model.LatLng;
import com.one.map.presenter.MapPresenter;
import com.one.map.view.IMapDelegate.IMapListener;
import com.one.map.view.IMapView;
import java.util.List;

/**
 * created by ludexiang
 */
public class MapFragment extends BaseMapFragment implements IMarkerClickListener {

  private FrameLayout mMapViewContainer;
  private MapPresenter mMapPresenter;
  private IMarker mLocationMarker;
  private IMarkerClickCallback mMarkerListener;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mMapView = MapFactory.newInstance().getMapView(activity, IMapView.TENCENT);
//    mMapPoi = MapFactory.newInstance().getMapPoi(activity, IMapView.TENCENT);

//    mMapView = MapFactory.newInstance().getMapView(activity, IMapView.AMAP);
    mMapPoi = MapFactory.newInstance().getMapPoi(activity, IMapView.AMAP);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMapView.onCreate(savedInstanceState);
  }

  @Nullable
  @Override
  public View onCreateViewImpl(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.map_fragment_layout, container);
    mMapViewContainer = (FrameLayout) view.findViewById(R.id.map_view_container);
    mMapView.attachToRootView(mMapViewContainer);
    int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
        getActivity().getResources().getDisplayMetrics());
    setLogoPosition(85, margin, margin, margin, margin);
    mMapPresenter = new MapPresenter(getContext(), this, mMapPoi);
    mMapView.setUIController(false);
    return view;
  }

  @Override
  public void showMyLocation() {
    int type = mMapView.getMapType();
    if (type == IMapView.TENCENT) {
      Address location = getCurrentLocation();
      if (location != null && mLocationMarker == null) {
        LatLng loc = location.mAdrLatLng;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.common_map_my_location);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        mLocationMarker = mMapView.myLocationConfig(bitmapDescriptor, loc);
      }
    }
    mMapView.setMyLocationEnable(true);
  }

  @Override
  public void hideMyLocation() {
    mMapView.setMyLocationEnable(false);
    if (mLocationMarker != null) {
      mLocationMarker.remove();
      mLocationMarker = null;
    }
  }

  @Override
  protected void onRotate(float degree) {
    if (mLocationMarker != null) {
      mLocationMarker.rotate(degree);
    }
  }

  @Override
  public void setMapListener(IMapListener listener) {
    mMapView.setMapListener(listener);
  }

  @Override
  public void setIMarkerClickCallback(IMarkerClickCallback callback) {
    mMarkerListener = callback;
  }

  @Override
  public void onMarkerClick(IMarker marker) {
    // TODO: 2018/4/16
    // do best view marker and Location

    if (mMarkerListener != null) {
      mMarkerListener.onMarkerClick(marker);
    }
  }

  @Override
  public void setLogoPosition(int position, int left, int top, int right, int bottom) {
    mMapView.setLogoPosition(position, left, top, right, bottom);
  }

  @Override
  public Marker addMarker(MarkerOption option) {
    option.setMarkerClickListener(this);
    return mMapView.addMarker(option);
  }

  @Override
  public List<Marker> addMarkers(List<MarkerOption> options) {
    for (MarkerOption option : options) {
      option.setMarkerClickListener(this);
    }
    return mMapView.addMarkers(options);
  }

  @Override
  public void poiSearchByKeyWord(String curCity, CharSequence key, IPoiSearchListener listener) {
    mMapView.poiSearchByKeyWord(curCity, key, listener);
  }

  @Override
  public void poiNearByWithCity(LatLng latLng, String curCity) {
    mMapView.poiNearByWithCity(latLng, curCity);
  }

  @Override
  public void geo2Address(LatLng latLng) {
    mMapView.geo2Address(latLng);
  }

  @Override
  public LatLng reverseGeo(Address address) {
    return null;
  }

  @Override
  public void drivingRoutePlan(Address from, Address to) {
    drivingRoutePlan(from, to, false);
  }

  @Override
  public void drivingRoutePlan(Address from, Address to, boolean arrow) {
    mMapPresenter.drivingRoutePlan(from, to, arrow);
  }

  @Override
  public void drivingRoutePlan(Address from, Address to, int lineColor, boolean arrow) {
    mMapPresenter.drivingRoutePlan(from, to, lineColor, arrow);
  }

  @Override
  public List<LatLng> getLinePoints() {
    return mMapPresenter.getLinePoints();
  }

  @Override
  public Polyline addPolyline(PolylineOption option) {
    return mMapView.addPolyline(option);
  }

  @Override
  public void doBestView(BestViewModel model) {
    mMapView.doBestView(model);
  }

  @Override
  public void clearElements() {
    if (mLocationMarker != null) {
      mLocationMarker.remove();
      mLocationMarker = null;
    }
    mMapView.clearElements();
  }

  @Override
  public boolean showInfoWindow(IMarker marker, CharSequence msg) {
    return mMapView.showInfoWindow(marker, msg);
  }

  @Override
  public void updateInfoWindowMsg(CharSequence msg) {
    mMapView.updateInfoWindowMsg(msg);
  }

  @Override
  public void removeInfoWindow() {
    mMapView.removeInfoWindow();
  }

  @Override
  public void registerPlanCallback(IRoutePlanMsgCallback callback) {
    mMapPresenter.registerRoutePlanCallback(callback);
  }

  @Override
  public void unRegisterPlanCallback(IRoutePlanMsgCallback callback) {
    mMapPresenter.unRegisterRoutePlanCallback(callback);
  }

  private Address getCurrentLocation() {
    return LocationProvider.getInstance().getLocation();
  }

  @Override
  public LatLng getCenterPosition() {
    return null;
  }

  @Override
  public void removeDriverLine() {
    mMapPresenter.removeDriverLine();
  }

  @Override
  public void startRadarAnim(LatLng latLng) {
    mMapView.startRadarAnim(latLng);
  }

  @Override
  public void stopRadarAnim() {
    mMapView.stopRadarAnim();
  }
}
