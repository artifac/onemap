package com.one.map.map;

import com.one.map.model.LatLng;
import java.util.ArrayList;
import java.util.List;

public class PolylineOption {
  /**
   * 折线宽度
   */
  int mWidth;
  
  /**
   * 折线颜色
   */
  int mColor = 0;

  /**
   * 描边颜色
   */
  int mBorderColor = 0xFF0000FF;

  /**
   * 描边颜色
   */
  int mBorderWidth;
  /**
   * 折线顶点集
   */
  List<LatLng> mPoints;

  List<Integer> mColors = new ArrayList<>();
  List<Integer> mColorIndex = new ArrayList<>();

  boolean mArrow;
  
  /**
   * 设置折形坐标点列表
   *
   * @param points 折形坐标点列表 数目大于2，且不能含有 null
   * @return 该折形选项对象
   */
  public PolylineOption points(List<LatLng> points) {
    this.mPoints = points;
    if (points == null) {
      throw new IllegalArgumentException("points list can not be null");
    }
    if (points.size() < 2) {
      throw new IllegalArgumentException(
              "points count can not less than 2");
    }
    if (points.contains(null)) {
      throw new IllegalArgumentException(
              "points list can not contains null");
    }
    return this;
  }
  
  
  /**
   * 设置折线色
   *
   * @param color 折线颜色
   * @return 该折线选项对象
   */
  public PolylineOption color(int color) {
    this.mColor = color;
    return this;
  }

  public PolylineOption colors(List<Integer> colors) {
    mColors.addAll(colors);
    return this;
  }

  public PolylineOption colorIndex(List<Integer> index) {
    mColorIndex.addAll(index);
    return this;
  }

  public int[] integerToInt(List<Integer> integers) {
    int[] ints = new int[integers.size()];
    for (int i = 0 ;i < integers.size(); i++) {
      ints[i] = integers.get(i);
    }
    return ints;
  }
  
  /**
   * 设置折线宽度
   *
   * @param width 宽度
   * @return 该折线选项对象
   */
  public PolylineOption width(int width) {
    this.mWidth = width;
    return this;
  }

  public PolylineOption arrow(boolean arrow) {
    mArrow = arrow;
    return this;
  }

  public PolylineOption border(int color) {
    this.mBorderColor = color;
    return this;
  }

  public PolylineOption borderWidth(int width) {
    this.mBorderWidth = width;
    return this;
  }
}
