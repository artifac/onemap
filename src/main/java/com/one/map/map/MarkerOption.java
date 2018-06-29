package com.one.map.map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.one.map.map.element.IMarker;
import com.one.map.map.element.IMarker.MarkerType;
import com.one.map.map.element.amap.AMapMarker;
import com.one.map.map.element.tencent.TencentMarker;
import com.one.map.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

/**
 * Created by ludexiang on 2017/11/29.
 */

public class MarkerOption {

  /**
   * marker latlng
   */
  public LatLng position;

  /**
   * marker bitmap 描述
   */
  public BitmapDescriptor descriptor;

  /**
   * marker title
   */
  public String title;

  public boolean isClickable = true;

  public float rotate;

  @MarkerType
  public int markerType;

  private MarkerClickListenerAdapter markerClickListener = new MarkerClickListenerAdapter();

  private IMarkerClickListener mListener;

  public void setMarkerClickListener(IMarkerClickListener listener) {
    mListener = listener;
  }

  public MarkerClickListenerAdapter getMarkerClickListenerAdapter() {
    return markerClickListener;
  }

  private class MarkerClickListenerAdapter implements TencentMap.OnMarkerClickListener, AMap.OnMarkerClickListener {

    @Override
    public boolean onMarkerClick(com.tencent.tencentmap.mapsdk.maps.model.Marker marker) {
      TencentMarker tencentMarker = new TencentMarker(marker);
      if (mListener != null && isClickable) {
        mListener.onMarkerClick(tencentMarker);
        return true;
      }
      return false;
    }

    @Override
    public boolean onMarkerClick(com.amap.api.maps.model.Marker marker) {
      AMapMarker aMapMarker = new AMapMarker(marker);
      if (mListener != null && isClickable) {
        mListener.onMarkerClick(aMapMarker);
      }
      return false;
    }
  }

  public interface IMarkerClickListener {
    void onMarkerClick(IMarker marker);
  }

}
