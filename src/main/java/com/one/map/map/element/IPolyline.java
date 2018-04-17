package com.one.map.map.element;

import com.one.map.model.LatLng;
import java.util.List;

public interface IPolyline<V> extends IMapElements<V> {
  void setColor(int color);
  List<LatLng> getPoints();
  void remove();
}
