package com.one.map.location.amap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationPurpose;
import com.amap.api.location.AMapLocationListener;
import com.one.map.location.ILocation;
import com.one.map.location.Location;
import com.one.map.model.Address;
import com.one.map.model.LatLng;

/**
 * Created by ludexiang on 2018/6/27.
 */

public class AMapLocation implements ILocation, AMapLocationListener {

  private ILocReceive mLocReceive;
  private Context mContext;
  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption;

  private Address currentLocation;

  private Location location;

  private static final long INTERVAL = 10 * 1000;

  public AMapLocation(Context context) {
    mContext = context;
    mLocationClient = new AMapLocationClient(context.getApplicationContext());
    mLocationOption = new AMapLocationClientOption();
    mLocationOption.setInterval(INTERVAL);
    mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
    mLocationOption.setLocationPurpose(AMapLocationPurpose.Transport);
    mLocationOption.setNeedAddress(true);
    mLocationOption.setMockEnable(false); // 不允许模拟位置
    mLocationOption.setLocationCacheEnable(false);
    mLocationClient.setLocationListener(this);
  }

  @Override
  public int onStart() {
    mLocationClient.setLocationOption(mLocationOption);
    mLocationClient.startLocation();
    return 0;
  }

  @Override
  public void onStop() {
    if (mLocationClient != null) {
      mLocationClient.stopLocation();
    }
  }

  @Override
  public void setLocListener(ILocReceive locReceive) {
    mLocReceive = locReceive;
  }

  @Override
  public void onLocationChanged(com.amap.api.location.AMapLocation aMapLocation) {
    if (aMapLocation.getErrorCode() == 0) {
      location = getLocation(aMapLocation);
      currentLocation = buildAddress(location);
      if (mLocReceive != null) {
        mLocReceive.onLocReceive(currentLocation);
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


  private Location getLocation(com.amap.api.location.AMapLocation amapLocation) {
    if (amapLocation == null) {
      return null;
    }
    Location location = new Location();
    location.city = amapLocation.getCity();
    location.accuracy = amapLocation.getAccuracy();
    location.bearing = amapLocation.getBearing();
    location.speed = amapLocation.getSpeed();
    location.cityCode = amapLocation.getCityCode();
    location.country = amapLocation.getCountry();
    location.streetCode = amapLocation.getStreetNum();
    location.street = amapLocation.getStreet();
    location.latitude = amapLocation.getLatitude();
    location.longitude = amapLocation.getLongitude();
    location.adrFullName = amapLocation.getAddress(); // 位置详细信息
    location.adrDisplayName = amapLocation.getAoiName();
    location.locationDetail = amapLocation.getLocationDetail();
    return location;
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
