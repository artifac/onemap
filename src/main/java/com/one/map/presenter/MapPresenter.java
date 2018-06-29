package com.one.map.presenter;

import android.content.Context;
import android.graphics.Color;
import com.one.map.IMap;
import com.one.map.R;
import com.one.map.map.PolylineOption;
import com.one.map.map.element.Polyline;
import com.one.map.model.Address;
import com.one.map.model.LatLng;
import com.one.map.model.Route;
import com.one.map.model.Route.TMC;
import com.one.map.poi.IMapPoi;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapPresenter {

  private Context mContext;
  private IMapPoi mMapPoi;
  private IMap mView;
  private StringBuilder builder;
  private DecimalFormat mDecimalFormat;
  private List<IMap.IRoutePlanMsgCallback> mCallbacks = new ArrayList<IMap.IRoutePlanMsgCallback>();
  private Polyline polyline;

  private Address mStart, mEnd;
  private int mDefaultColor = Color.parseColor("#87b2f0");
  private int mLineColor;
  private boolean isArrow;
  private List<LatLng> mLinePath = new ArrayList<>();

  public MapPresenter(Context context, IMap view, IMapPoi poi) {
    mContext = context;
    mView = view;
    mMapPoi = poi;
    mDecimalFormat = new DecimalFormat("#.##");
  }

  public void drivingRoutePlan(Address from, Address to, final boolean arrow) {
    drivingRoutePlan(from, to, 0, arrow);
  }

  public void drivingRoutePlan(Address from, Address to, final int lineColor, final boolean arrow) {
    mStart = from;
    mEnd = to;
    isArrow = arrow;
    mLineColor = lineColor != 0 ? lineColor : mDefaultColor;
    mMapPoi.drivingRoutePlan(from, to, new IMapPoi.IMapCallback() {
      @Override
      public void callback(Object obj) {
        // success obj is List<Route>
        if (obj != null && obj instanceof List) {
          List<Route> points = (List<Route>) obj;
          if (points != null && points.size() > 0) {
            float totalDistance = 0f; // 单位米
            float totalDuration = 0f; // 单位分钟
            mLinePath.clear();
            List<LatLng> latLngs = new ArrayList<>();
            List<TMC> tmcLatLng = new ArrayList<TMC>();
            for (Route route : points) {
              latLngs.addAll(route.polyLine);
              mLinePath.addAll(latLngs);
              totalDistance += route.distance;
              totalDuration += route.duration;
              tmcLatLng.addAll(route.tmc);
            }

            createInfoWindow(totalDistance, totalDuration);
            if (polyline != null) {
              polyline.remove();
            }
            if (!tmcLatLng.isEmpty() && lineColor == 0) {
              PolylineOption colorOptions = colorWayUpdate(tmcLatLng);
              polyline = drawDriverLine(colorOptions);
            } else {
              polyline = drawDriverLine(getDefaultPolylineOptions());
            }
            for (IMap.IRoutePlanMsgCallback callback : mCallbacks) {
              callback.routePlanPoints(latLngs);
            }
          }
        } else if (obj instanceof String) {
          String failMsg = (String) obj;
        }
      }
    });

  }

  private PolylineOption getDefaultPolylineOptions() {
    PolylineOption option = new PolylineOption();
    option.points(mLinePath).color(mLineColor).width(20).arrow(isArrow);
    return option;
  }

  /**
   * 根据不同的路段拥堵情况展示不同的颜色
   */
  private PolylineOption colorWayUpdate(List<TMC> tmcSection) {
    if (tmcSection == null || tmcSection.size() <= 0) {
      return getDefaultPolylineOptions();
    }

    List<LatLng> points = new ArrayList<>();
    PolylineOption polylineOption = new PolylineOption();
    polylineOption.width(20);
    List<Integer> colorList = new ArrayList<Integer>();
    List<Integer> indexLatLng = new ArrayList<Integer>();
    points.add(mStart.mAdrLatLng);
    points.add(tmcSection.get(0).tmcPoints.get(0));
//    colorList.add(tmcSection.ge);
    for (int i = 0; i < tmcSection.size(); i++) {
      TMC tmc = tmcSection.get(i);
      points.addAll(tmc.tmcPoints);
      colorList.add(tmc.mLine.getLineColor());
      indexLatLng.addAll(tmc.indexLatLng);
    }
    points.add(mEnd.mAdrLatLng);
    polylineOption.points(points);
    polylineOption.colorIndex(indexLatLng);
    polylineOption.colors(colorList);
    return polylineOption;
  }

  private Polyline drawDriverLine(PolylineOption options) {
    return mView.addPolyline(options);
  }

  /**
   * remove 路线规划
   */
  public void removeDriverLine() {
    if (polyline != null) {
      polyline.remove();
    }
  }

  /**
   * @param totalDistance
   * @param totalDuration
   */
  private void createInfoWindow(float totalDistance, float totalDuration) {
    int hour = 0;
    int minute;
    if (totalDuration > 60) {
      hour = (int) (totalDuration / 60);
      minute = (int) (totalDuration % 60);
    } else {
      minute = (int) (totalDuration <= 1 ? 1 : totalDuration);
    }

    builder = new StringBuilder();
    String time = mContext.getString(R.string.map_time_about);
    String kil = mContext.getString(R.string.map_kilmeter);
    String h = mContext.getString(R.string.map_hour);
    String min = mContext.getString(R.string.map_minute);
    builder.append("{").append(mDecimalFormat.format(totalDistance / 1000 * 1f)).append("}")
        .append(kil).append(" · ")
        .append(time);
    if (hour > 0) {
      builder.append("{").append(hour).append("}").append(h).append("{").append(minute).append("}")
          .append(min);
    } else {
      builder.append("{").append(minute).append("}").append(min);
    }
    for (IMap.IRoutePlanMsgCallback callback : mCallbacks) {
      callback.routePlanMsg(builder.toString(), polyline != null ? polyline.getPoints() : null);
    }
  }

  public void registerRoutePlanCallback(IMap.IRoutePlanMsgCallback callback) {
    mCallbacks.add(callback);
  }

  public void unRegisterRoutePlanCallback(IMap.IRoutePlanMsgCallback callback) {
    Iterator<IMap.IRoutePlanMsgCallback> iterator = mCallbacks.iterator();
    while (iterator.hasNext()) {
      IMap.IRoutePlanMsgCallback planCallback = iterator.next();
      if (planCallback == callback) {
        iterator.remove();
      }
    }
  }

  public List<LatLng> getLinePoints() {
    if (polyline != null) {
      return polyline.getPoints();
    }
    return null;
  }

}
