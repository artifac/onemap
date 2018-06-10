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
import com.one.map.poi.IMapPoi;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobike on 2017/11/27.
 */

public class MapPresenter {

  private Context mContext;
  private IMapPoi mMapPoi;
  private IMap mView;
  private StringBuilder builder;
  private DecimalFormat mDecimalFormat;
  private IMap.IRoutePlanMsgCallback mCallback;
  private Polyline polyline;

  public MapPresenter(Context context, IMap view, IMapPoi poi) {
    mContext = context;
    mView = view;
    mMapPoi = poi;
    mDecimalFormat = new DecimalFormat("#.##");
  }

  public void drivingRoutePlan(Address from, Address to) {
    mMapPoi.drivingRoutePlan(from, to, new IMapPoi.IMapCallback() {
      @Override
      public void callback(Object obj) {
        // success obj is List<Route>
        if (obj != null && obj instanceof List) {
          List<Route> points = (List<Route>) obj;
          if (points != null && points.size() > 0) {
            float totalDistance = 0f; // 单位米
            float totalDuration = 0f; // 单位分钟
            List<LatLng> latLngs = new ArrayList<>();
            for (Route route : points) {
              latLngs.addAll(route.polyLine);
              totalDistance += route.distance;
              totalDuration += route.duration;
            }

            createInfoWindow(totalDistance, totalDuration);
            if (polyline != null) {
              polyline.remove();
            }
            polyline = drawlines(latLngs);
            if (mCallback != null) {
              mCallback.routePlanPoints(latLngs);
            }
          }
        } else if (obj instanceof String) {
          String failMsg = (String) obj;
        }
      }
    });
  }

  private Polyline drawlines(List<LatLng> points) {
    PolylineOption option = new PolylineOption();
    option.points(points).color(Color.parseColor("#bed2f9")).width(20);
//        .border(Color.parseColor("#0000FF")).borderWidth(5);
    return mView.addPolyline(option);
  }

  private void createInfoWindow(float totalDistance, float totalDuration) {
    int hour = 0;
    int minute;
    if (totalDuration > 60) {
      hour = (int) (totalDuration / 60);
      minute = (int) (totalDuration % 60);
    } else {
      minute = (int) totalDuration;
    }

    builder = new StringBuilder();
//    String time = mContext.getString(R.string.mocha_order_map_time_about);
//    String kil = mContext.getString(R.string.mocha_order_map_kilmeter);
//    String h = mContext.getString(R.string.mocha_order_map_hour);
//    String min = mContext.getString(R.string.mocha_order_map_minute);
//    builder.append("{").append(mDecimalFormat.format(totalDistance / 1000 * 1f)).append("}")
//        .append(kil).append(" · ")
//        .append(time);
//    if (hour > 0) {
//      builder.append("{").append(hour).append("}").append(h).append("{").append(minute).append("}")
//          .append(min);
//    } else {
//      builder.append("{").append(minute).append("}").append(min);
//    }
//    if (mCallback != null) {
//      mCallback.routePlanMsg(builder.toString(), polyline != null ? polyline.getPoints() : null);
//    }
  }

  public void setRoutePlanCallback(IMap.IRoutePlanMsgCallback callback) {
    mCallback = callback;
  }

  public List<LatLng> getLinePoints() {
    if (polyline != null) {
      return polyline.getPoints();
    }
    return null;
  }

}
