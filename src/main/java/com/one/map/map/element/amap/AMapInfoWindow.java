package com.one.map.map.element.amap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.one.map.R;
import com.one.map.map.element.IInfoWindow;
import com.one.map.map.element.IMarker;
import com.one.map.model.LatLng;

/**
 * Created by ludexiang on 2018/6/27.
 */

public class AMapInfoWindow<MAP, MARKER extends IMarker> implements IInfoWindow {

  private Context mContext;
  private AMap mMap;
  private AMapMarker mMarker;
  private LayoutInflater mInflater;
  private CharSequence mMsg;
  private InfoWindowAdapter mInfoWindowAdapter;

  private AMap.OnInfoWindowClickListener infoWindowClickListener = new AMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {

    }
  };

  public AMapInfoWindow(Context context, MAP map, MARKER marker) {
    mContext = context;
    mMap = (AMap) map;
    mMarker = (AMapMarker) marker;
    mInflater = LayoutInflater.from(context);
    mInfoWindowAdapter = new InfoWindowAdapter();
    mMap.setOnInfoWindowClickListener(infoWindowClickListener);
  }

  @Override
  public void setMessage(CharSequence msg) {
    mMsg = msg;
  }

  @Override
  public void setPosition(LatLng location) {

  }

  @Override
  public void updateMessage(CharSequence msg) {

  }

  @Override
  public LatLng getPosition() {
    return null;
  }

  @Override
  public void remove() {
    if (mMarker != null && mMarker.getSourceMarker() != null
        && mMarker.getSourceMarker().isInfoWindowShown()) {
      mMarker.getSourceMarker().hideInfoWindow();
    }
  }

  @Override
  public boolean showInfoWindow() {
    if (mMarker == null) {
      throw new IllegalArgumentException("Marker is null");
    }
    Marker marker = mMarker.getSourceMarker();
    if (marker != null && !marker.isInfoWindowShown()) {
      marker.setTitle(mMsg.toString());
      marker.showInfoWindow();
    }
    mMap.setInfoWindowAdapter(mInfoWindowAdapter);
    return true;
  }

  class InfoWindowAdapter implements AMap.InfoWindowAdapter {
    private View view;
    private TextView info;

    @Override
    public View getInfoWindow(Marker marker) {
      if (mMarker == null) {
        return null;
      }
      Marker sourceMarker = mMarker.getSourceMarker();
      if (sourceMarker == marker) {
        if (view == null) {
          view = mInflater.inflate(R.layout.info_window_layout, null);
          info = (TextView) view.findViewById(R.id.info_window_msg);
          info.setText(mMsg);
        } else {
          info.setText(mMsg);
        }
        return view;
      }
      return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
      if (mMarker != null && marker == mMarker.getSourceMarker()) {
        return view;
      }
      return null;
    }
  }
}
