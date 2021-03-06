package com.one.map.location;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import com.one.map.location.ILocation.ILocReceive;
import com.one.map.location.amap.AMapLocation;
import com.one.map.location.tencent.TencentMapLocation;
import com.one.map.log.Logger;
import com.one.map.model.Address;
import com.one.map.view.IMapView;
import com.one.map.view.IMapView.MapType;
import java.util.concurrent.CopyOnWriteArraySet;

@Keep
public class LocationProvider implements ILocReceive {

  private ILocation locationService;
  private final CopyOnWriteArraySet<OnLocationChangedListener> mListeners = new CopyOnWriteArraySet<>();
  /**
   * 缓存的定位
   */
  private Address currentAddress;

  private LocationProvider() {

  }

  public static final class LocationFactory {
    private static LocationProvider instance;
    public static LocationProvider getInstance() {
      synchronized (LocationProvider.class) {
        if (instance == null) {
          instance = new LocationProvider();
        }
        return instance;
      }
    }
  }

  public static synchronized LocationProvider getInstance() {
    return LocationFactory.getInstance();
  }

  public void buildLocation(Context context, @MapType int type) {
    switch (type) {
      case IMapView.TENCENT: {
        locationService = new TencentMapLocation(context);
        break;
      }
      case IMapView.AMAP: {
        locationService = new AMapLocation(context);
        break;
      }
    }
    if (locationService != null) {
      locationService.setLocListener(this);
    }
  }

  public int start() {
    if (locationService != null) {
      return locationService.onStart();
    }
    return -1;
  }

  public void addLocationChangeListener(OnLocationChangedListener listener) {
    mListeners.add(listener);
  }

  public void removeLocationChangedListener(OnLocationChangedListener listener) {
    mListeners.remove(listener);
  }

  public Address getLocation() {
    if (locationService != null) {
      return locationService.getCurrentLocation();
    }
    return null;
  }

  public void stop() {
    if (locationService != null) {
      locationService.onStop();
    }
  }

  @Override
  public void onLocReceive(Address adr) {
    try {
      for (OnLocationChangedListener listener : mListeners) {
        if (listener != null) {
          listener.onLocationChanged(adr);
        }
      }
    } catch (Exception e) {

    }
  }

  public String getCityCode() {
    Address address = getLocation();
    if (address != null) {
      return CityCodeConverter.transQQ2AmapCityCode(address.mCityCode);
    }
    return "";
  }

  @Keep
  public interface OnLocationChangedListener {

    /**
     * 当获得新位置后，调用此接口, 更新定位点位置
     *
     * @param location 新位置信息
     * @throws IllegalArgumentException xxxxx
     */
    void onLocationChanged(Address location);
  }

  public void onSaveInstanceState(Bundle outState) {
    if (locationService != null) {
      locationService.onSaveInstanceState(outState);
    }
  }

  public void setMochaLocation(Location location) {
    if (locationService != null) {
      locationService.setLocation(location);
    }
  }
}
