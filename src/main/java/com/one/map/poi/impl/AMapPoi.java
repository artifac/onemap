package com.one.map.poi.impl;

import android.content.Context;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.TMC;
import com.amap.api.services.route.WalkRouteResult;
import com.one.map.model.Address;
import com.one.map.model.LatLng;
import com.one.map.model.Route;
import com.one.map.model.Route.Line;
import com.one.map.poi.IMapPoi;
import java.util.ArrayList;
import java.util.List;

public class AMapPoi implements IMapPoi {
  private Context mContext;
  public AMapPoi(Context context) {
    mContext = context;
  }
  
  @Override
  public void drivingRoutePlan(Address from, Address to, final IMapCallback callback) {
    RouteSearch routeSearch = new RouteSearch(mContext);
    RouteSearch.FromAndTo fromAndTo = new FromAndTo(new LatLonPoint(from.mAdrLatLng.latitude, from.mAdrLatLng.longitude),
        new LatLonPoint(to.mAdrLatLng.latitude, to.mAdrLatLng.longitude));
    DriveRouteQuery query = new DriveRouteQuery(fromAndTo, RouteSearch.BUS_DEFAULT, null, null, "");
    routeSearch.setRouteSearchListener(new OnRouteSearchListener() {
      @Override
      public void onBusRouteSearched(BusRouteResult busRouteResult, int code) {

      }

      @Override
      public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int code) {
        if (code == 1000) {
          List<DrivePath> drivePathList = driveRouteResult.getPaths();
          List<Route> routes = new ArrayList<>();
          for (DrivePath drivePath : drivePathList) {

            Route route = new Route();
            float duration = 0f;
            float distance = 0f;
            String direction = "";
            List<LatLng> polyline = new ArrayList<>();
            List<LatLng> tmcPolyline = new ArrayList<>();
            List<DriveStep> stepList = drivePath.getSteps();
            for (DriveStep step: stepList) {
              duration += step.getDuration();
              distance += step.getDistance();
              direction = step.getOrientation();
              List<LatLonPoint> points = step.getPolyline();
              for (LatLonPoint point : points) {
                polyline.add(new LatLng(point.getLatitude(), point.getLongitude()));
              }
              List<TMC> tmcs = step.getTMCs();
              for (int i = 0; i < tmcs.size(); i++) { // TMC info
                TMC tmc = tmcs.get(i);
                com.one.map.model.Route.TMC rTmc = new Route().new TMC();
                rTmc.distance = tmc.getDistance();
                rTmc.trafficStatus = tmc.getStatus();
                if ("畅通".equals(tmc.getStatus())) {
                  rTmc.mLine = Line.FLUENT;
                } else if ("缓行".equals(tmc.getStatus())) {
                  rTmc.mLine = Line.SLOW;
                } else if ("拥堵".equals(tmc.getStatus())) {
                  rTmc.mLine = Line.JAM;
                } else if ("严重拥堵".equals(tmc.getStatus())){
                  rTmc.mLine = Line.CONGESTION;
                } else {
                  rTmc.mLine = Line.OTHER;
                }
                for (int j = 0; j < tmc.getPolyline().size(); j++){
                  LatLonPoint point = tmc.getPolyline().get(j);
                  LatLng pointLatLng = new LatLng(point.getLatitude(), point.getLongitude());
                  rTmc.tmcPoints.add(pointLatLng);
                  tmcPolyline.add(pointLatLng);

                  route.tmc.add(rTmc);
                  if (j == 0) {
                    rTmc.indexColor.add(rTmc.mLine.getLineColor());
                    rTmc.indexLatLng.add(tmcPolyline.indexOf(pointLatLng));
                  }
                }
              }
            }
            route.direction = direction;
            route.distance = distance;
            route.duration = duration * 1f / 60;
            route.polyLine.addAll(polyline);

            routes.add(route);
          }

          if (callback != null) {
            callback.callback(routes);
          }
        } else {

        }
      }

      @Override
      public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int code) {

      }

      @Override
      public void onRideRouteSearched(RideRouteResult rideRouteResult, int code) {

      }
    });
    routeSearch.calculateDriveRouteAsyn(query);
  }
  
  @Override
  public void reverseGeo(Address adr, IMapCallback callback) {
  
  }
}
