package com.one.map.map;

import com.amap.api.maps.model.*;

public class BitmapDescriptorConvert {

  public static com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor
  convert2TencentBitmapDescriptor(BitmapDescriptor icon) {
    if (icon == null) {
      return null;
    }
    com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor descriptor =
        com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory
            .fromBitmap(icon.getBitmap());
    return descriptor;
  }

  public static com.amap.api.maps.model.BitmapDescriptor convert2AMapBitmapDesriptor(BitmapDescriptor icon) {
    if (icon == null) {
      return null;
    }
    com.amap.api.maps.model.BitmapDescriptor descriptor = com.amap.api.maps.model.BitmapDescriptorFactory .fromBitmap(icon.getBitmap());
    return descriptor;
  }

}
