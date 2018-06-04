package com.one.map.location.tencent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import com.one.map.location.ILocation;
import com.one.map.location.Location;
import com.one.map.model.Address;
import com.one.map.model.LatLng;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * Created by ludexiang on 2017/11/27.
 */
@Keep
public class TencentMapLocation implements ILocation, TencentLocationListener {

  private ILocReceive mLocReceive;
  private TencentLocationManager mManager;
  private Context mContext;
  private TencentLocationRequest locationRequest;

  private Address currentLocation;

  private Location location;

  public TencentMapLocation(Context context) {
    mContext = context;
    mManager = TencentLocationManager.getInstance(mContext);
    mManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
    locationRequest = TencentLocationRequest.create();
    locationRequest.setAllowGPS(true);
    locationRequest.setAllowDirection(true);
  }

  @Override
  public int onStart() {
    locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
//    0	注册位置监听器成功
//    1	设备缺少使用腾讯定位SDK需要的基本条件
//    2	配置的 Key 不正确
//    3	自动加载libtencentloc.so失败，可能由以下原因造成：
//    1、这往往是由工程中的so与设备不兼容造成的，应该添加相应版本so文件;
//    2、如果您使用AndroidStudio,可能是gradle没有正确指向so文件加载位置，可以按照这里配置您的gradle;
    mManager.removeUpdates(this);
    return mManager.requestLocationUpdates(locationRequest, this);
  }

  @Override
  public void onStop() {
    if (mManager != null) {
      mManager.removeUpdates(this);
    }
  }

  @Override
  public void setLocListener(ILocReceive locReceive) {
    mLocReceive = locReceive;
  }

  /**
   * location	新的位置
   * error	错误码
   * reason	错误描述
   */
  @Override
  public void onLocationChanged(TencentLocation tencentLocation, int errNo, String reason) {
    if (TencentLocation.ERROR_OK == errNo) {
      // 定位成功
      // 位置更新时的回调
      location = getLocation(tencentLocation);
      currentLocation = buildAddress(location);
      if (mLocReceive != null) {
        mLocReceive.onLocReceive(currentLocation);
      }
    } else {
      // 定位失败
      if (mLocReceive != null) {
        mLocReceive.onLocReceive(null);
      }
    }

  }

  private Address buildAddress(Location location) {
    if (location == null) {
      return null;
    }
    Address locAdr = new Address();
    locAdr.mCity = location.city;
    locAdr.bearing = location.bearing;
    locAdr.speed = location.speed;
    locAdr.mCityCode = location.cityCode;
    locAdr.mCountry = location.country;
    locAdr.mStreetCode = location.streetCode;
    locAdr.mStreet = location.street;
    locAdr.mAdrLatLng = new LatLng(location.latitude, location.longitude);
    locAdr.mAdrFullName = location.adrFullName;
    locAdr.mAdrDisplayName = location.adrDisplayName;
    locAdr.accuracy = location.accuracy;
    return locAdr;
  }


  private Location getLocation(TencentLocation tencentLocation) {
    if (tencentLocation == null) {
      return null;
    }
    Location location = new Location();
    location.city = tencentLocation.getCity();
    location.accuracy = tencentLocation.getAccuracy();
    location.bearing = tencentLocation.getBearing();
    location.speed = tencentLocation.getSpeed();
    location.cityCode = tencentLocation.getCityCode();
    location.country = tencentLocation.getNation();
    location.streetCode = tencentLocation.getStreetNo();
    location.street = tencentLocation.getStreet();
    location.latitude = tencentLocation.getLatitude();
    location.longitude = tencentLocation.getLongitude();
    location.adrFullName = tencentLocation.getAddress();
    location.adrDisplayName = tencentLocation.getName();
    return location;
  }


  /**
   * name	GPS，Wi-Fi等
   * status	新的状态, 启用或禁用
   * desc	状态描述
   */
  @Override
  public void onStatusUpdate(String name, int status, String desc) {
    // GPS和Wi-Fi的状态变化回调
  }

  @Override
  public Address getCurrentLocation() {
    return currentLocation;
  }

  public void onSaveInstanceState(Bundle outState) {
    if (location != null && outState != null) {
      outState.putParcelable(Location.TAG, location);
    }
  }

  public void setLocation(Location location) {
    this.location = location;
  }
}
