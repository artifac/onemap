package com.one.map.map;


import com.one.map.model.LatLng;

/**
 * Created by ludexiang on 2017/11/29.
 */

public class LatLngConvert {
  public static com.tencent.tencentmap.mapsdk.maps.model.LatLng convert2TencentLatLng(LatLng position) {
    return new com.tencent.tencentmap.mapsdk.maps.model.LatLng(position.latitude, position.longitude);
  }

  public static com.amap.api.maps.model.LatLng convert2AMapLatLng(LatLng position) {
    return new com.amap.api.maps.model.LatLng(position.latitude, position.longitude);
  }
}
