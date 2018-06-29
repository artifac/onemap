package com.one.map.model;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ludexiang on 2017/11/27.
 */

public class Route {

  /**
   * 路线总体方向
   */
  public String direction;
  /**
   * 路径的距离，单位为米
   */
  public float distance;
  /**
   * 预计所需要的时间
   */
  public float duration;
  /**
   * 途径的路线点
   */
  public List<LatLng> polyLine = new ArrayList<>();
  /**
   * 路线策略 仅当设置需要返回多条路线时返回，且为非必有值
   */
  public List<String> tags;
  /**
   * 设置的途经点
   */
  public Map<String, LatLng> waypoints = new HashMap<String, LatLng>();

  /**
   * 路径状态
   */
  public List<TMC> tmc = new ArrayList<TMC>();

  public class TMC {

    public List<LatLng> tmcPoints = new ArrayList<>();
    public String trafficStatus;
    public int distance;
    public Line mLine;
    public List<Integer> indexColor = new ArrayList<>();
    public List<Integer> indexLatLng = new ArrayList<>();

    @Override
    public String toString() {
      return "TMC{" +
          "tmcPoints=" + tmcPoints +
          ", trafficStatus='" + trafficStatus + '\'' +
          ", distance=" + distance +
          ", mLine=" + mLine +
          '}';
    }
  }

  public enum Line {

    FLUENT("畅通", 0, Color.parseColor("#6dc08b")),
    SLOW("缓行", 1, Color.parseColor("#edc563")),
    JAM("拥堵", 2, Color.parseColor("#db6c64")),
    CONGESTION("严重拥堵", 3, Color.parseColor("#990033")),
    OTHER("其他", 4, Color.parseColor("#87b2f0"));

    String trafficStatus;
    int trafficCode;
    int lineColor;

    Line(String status, int code, int color) {
      trafficStatus = status;
      trafficCode = code;
      lineColor = color;
    }

    public int getLineColor() {
      return lineColor;
    }
  }
}
