package com.one.map.map.element.amap;

import com.amap.api.maps.model.Polyline;
import com.one.map.map.element.IPolyline;
import com.one.map.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class AMapPolyline implements IPolyline<Polyline> {
  private Polyline aMapPolyLine;

  public AMapPolyline(Polyline polyline) {
    aMapPolyLine = polyline;
  }
  
  @Override
  public void setColor(int color) {
    aMapPolyLine.setColor(color);
  }
  
  @Override
  public void remove() {
    aMapPolyLine.remove();
  }
  
  @Override
  public void setPosition(LatLng location) {
  
  }

  @Override
  public List<LatLng> getPoints() {
    List<com.amap.api.maps.model.LatLng> linePoints = aMapPolyLine.getPoints();
    List<LatLng> points = new ArrayList<LatLng>();
    for (com.amap.api.maps.model.LatLng latLng: linePoints) {
      points.add(new LatLng(latLng.latitude, latLng.longitude));
    }
    return points;
  }

  @Override
  public LatLng getPosition() {
    return null;
  }
}
