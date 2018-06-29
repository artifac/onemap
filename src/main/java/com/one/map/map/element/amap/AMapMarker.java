package com.one.map.map.element.amap;

import android.os.Bundle;
import com.amap.api.maps.model.Marker;
import com.one.map.anim.Animation;
import com.one.map.map.BitmapDescriptor;
import com.one.map.map.BitmapDescriptorConvert;
import com.one.map.map.element.IMarker;
import com.one.map.model.LatLng;
import java.util.ArrayList;

/**
 * Created by ludexiang on 2018/6/27.
 */

public class AMapMarker implements IMarker<BitmapDescriptor, Marker> {
  private Marker aMapMarker;
  private @MarkerType int markerType;

  private boolean isClickable;

  public AMapMarker(Marker marker) {
    this.aMapMarker = marker;
  }

  @Override
  public void setPosition(LatLng location) {

  }

  @Override
  public LatLng getPosition() {
    return null;
  }

  @Override
  public void remove() {
    if (aMapMarker != null && !aMapMarker.isRemoved()) {
      aMapMarker.remove();
    }
  }

  @Override
  public int getMarkerType() {
    return markerType;
  }

  @Override
  public void setMarkerType(int type) {
    markerType = type;
  }

  @Override
  public void setZIndex(int zIndex) {
    aMapMarker.setZIndex(zIndex);
  }

  @Override
  public int getZIndex() {
    return 0;
  }

  @Override
  public void setToTop() {

  }

  @Override
  public Bundle getExtraInfo() {
    return null;
  }

  @Override
  public void setExtraInfo(Bundle extraInfo) {

  }

  @Override
  public void setTitle(String title) {
    aMapMarker.setTitle(title);
  }

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public void setIcon(BitmapDescriptor icon) {
    aMapMarker.setIcon(BitmapDescriptorConvert.convert2AMapBitmapDesriptor(icon));
  }

  @Override
  public BitmapDescriptor getIcon() {
    return null;
  }

  @Override
  public void setIcons(ArrayList<BitmapDescriptor> icons) {

  }

  @Override
  public ArrayList<BitmapDescriptor> getIcons() {
    return null;
  }

  @Override
  public void setPeriod(int period) {

  }

  @Override
  public void setAnimation(Animation animation) {
  }

  @Override
  public Marker getSourceMarker() {
    return aMapMarker;
  }

  @Override
  public Marker getMarkerInstance() {
    return aMapMarker;
  }

  @Override
  public void setClick(boolean clickable) {
    isClickable = clickable;
  }

  @Override
  public boolean isClickable() {
    return isClickable;
  }

  @Override
  public void rotate(float angle) {
    if (aMapMarker != null) {
      aMapMarker.setRotateAngle(angle);
    }
  }
}
