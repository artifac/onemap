package com.one.map.map;


import com.one.map.log.Logger;
import com.one.map.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions.ColorType;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions.LineType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class PolylineOptionConvert {
  public static com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions convert2TencentPolylineOption(PolylineOption option) {
    com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions options = new com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions();
    options.width(option.mWidth);
    options.arrow(option.mArrow);
    if (option.mColor != 0) {
      options.color(option.mColor);
    }
    if (!option.mColors.isEmpty()) {
      Logger.e("ldx", "colors " + option.mColors.size() + " " + option.mColorIndex.size());
      options.colors(option.integerToInt(option.mColors), option.integerToInt(option.mColorIndex));
    }
    options.setLineType(LineType.LINE_TYPE_MULTICOLORLINE);
    options.arrowSpacing(100);
    options.borderColor(option.mBorderColor);
    options.borderWidth(option.mBorderWidth);
    List<com.tencent.tencentmap.mapsdk.maps.model.LatLng> points = new ArrayList<com.tencent.tencentmap.mapsdk.maps.model.LatLng>();
    for (LatLng latLng : option.mPoints) {
      points.add(LatLngConvert.convert2TencentLatLng(latLng));
    }
    options.addAll(points);
    return options;
  }

  public static com.amap.api.maps.model.PolylineOptions convert2AMapPolylineOption(PolylineOption option) {
    com.amap.api.maps.model.PolylineOptions options = new com.amap.api.maps.model.PolylineOptions();
    options.color(option.mColor);
    options.width(option.mWidth);
    if (option.mColors != null && !option.mColors.isEmpty()) {
      options.colorValues(option.mColors);
    }
    List<com.amap.api.maps.model.LatLng> points = new ArrayList<com.amap.api.maps.model.LatLng>();
    for (LatLng latLng : option.mPoints) {
      points.add(LatLngConvert.convert2AMapLatLng(latLng));
    }
    options.addAll(points);
    return options;
  }
}
