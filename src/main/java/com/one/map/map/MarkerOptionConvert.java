package com.one.map.map;


/**
 * Created by ludexiang on 2017/11/29.
 */

public class MarkerOptionConvert {
  public static com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions convert2TencentMarkerOption(MarkerOption option) {
    if (option == null) {
      return null;
    }
    com.tencent.tencentmap.mapsdk.maps.model.LatLng latLng = LatLngConvert.convert2TencentLatLng(option.position);
    com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions markerOptions = new com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions(latLng);
    com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor bitmapDescriptor;
    if (option.descriptor == null) {
      bitmapDescriptor = com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory.defaultMarker();
    } else {
      bitmapDescriptor = com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory.fromBitmap(option.descriptor.getBitmap());
    }
    markerOptions.anchor(0.5f, 1f);
    markerOptions.flat(true);
    return markerOptions.icon(bitmapDescriptor).title(option.title).draggable(false);
  }

  public static com.amap.api.maps.model.MarkerOptions convert2AMapMarkerOption(MarkerOption option) {
    if (option == null) {
      return null;
    }
    com.amap.api.maps.model.LatLng latLng = LatLngConvert.convert2AMapLatLng(option.position);
    com.amap.api.maps.model.MarkerOptions markerOptions = new com.amap.api.maps.model.MarkerOptions();
    markerOptions.position(latLng);
    com.amap.api.maps.model.BitmapDescriptor bitmapDescriptor;
    if (option.descriptor == null) {
      bitmapDescriptor = com.amap.api.maps.model.BitmapDescriptorFactory.defaultMarker();
    } else {
      bitmapDescriptor = com.amap.api.maps.model.BitmapDescriptorFactory.fromBitmap(option.descriptor.getBitmap());
    }
    return markerOptions.icon(bitmapDescriptor).title(option.title).draggable(false);
  }
}
