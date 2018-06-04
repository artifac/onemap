package com.one.map.view;

import android.support.annotation.IntDef;
import android.view.ViewGroup;
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
import com.one.map.view.IMapDelegate.IMapListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

/**
 * 地图的接口Map
 * Created by ludexiang on 2017/11/26.
 */
public interface IMapView extends IMapLifeCycle {
  
  int BAIDU = 0x001;
  int TENCENT = 0x002;
  int AMAP = 0x003;
  
  @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER})
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({BAIDU, TENCENT, AMAP})
  @interface MapType{
  
  }
  
  /**
   * 将MapView attach to root view
   * @param viewGroup
   */
  void attachToRootView(ViewGroup viewGroup);
  
  /**
   * @param padding
   */
  void setPadding(MapStatusOperation.Padding padding);
  
  /**
   * 是否显示路况
   * @param isShowTraffic
   */
  void setTraffic(boolean isShowTraffic);

  void setMapListener(IMapListener listener);
  
  
  IMarker myLocationConfig(BitmapDescriptor bitmapDescriptor, LatLng latLng);
  
  /**
   * 显示定位
   */
  void setMyLocationEnable(boolean enable);

  void setLogoPosition(int position, int left, int top, int right, int bottom);
  
  /**
   * 是否展示 + - 控制
   */
  void setUIController(boolean isShowUIController);
  
  void clearElements();
  
  Marker addMarker(MarkerOption option);

  List<Marker> addMarkers(List<MarkerOption> options);
  
  Polyline addPolyline(PolylineOption option);
  
  Circle addCircle(CircleOption option);
  
  /**
   * info window
   * @param msg
   */
  boolean showInfoWindow(IMarker marker, CharSequence msg);
  
  void updateInfoWindowMsg(CharSequence msg);

  void removeInfoWindow();

  void geo2Address(LatLng latLng);
  
  /**
   * 刷新最佳view
   * @param model
   */
  void doBestView(BestViewModel model);

  /**
   * 获取中心店位置坐标
   * @return
   */
  LatLng getCenterPosition();
}
