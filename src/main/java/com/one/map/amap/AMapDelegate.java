package com.one.map.amap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.one.map.IMap.IPoiSearchListener;
import com.one.map.R;
import com.one.map.log.Logger;
import com.one.map.map.BitmapDescriptor;
import com.one.map.map.BitmapDescriptorConvert;
import com.one.map.map.BitmapDescriptorFactory;
import com.one.map.map.CircleOption;
import com.one.map.map.LatLngConvert;
import com.one.map.map.MarkerOption;
import com.one.map.map.MarkerOptionConvert;
import com.one.map.map.PolylineOption;
import com.one.map.map.PolylineOptionConvert;
import com.one.map.map.element.Circle;
import com.one.map.map.element.IMarker;
import com.one.map.map.element.Polyline;
import com.one.map.map.element.amap.AMapInfoWindow;
import com.one.map.map.element.amap.AMapMarker;
import com.one.map.map.element.amap.AMapPolyline;
import com.one.map.model.Address;
import com.one.map.model.BestViewModel;
import com.one.map.model.LatLng;
import com.one.map.model.MapStatusOperation;
import com.one.map.model.MapStatusOperation.Padding;
import com.one.map.util.MapUtils;
import com.one.map.view.IMapDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ludexiang on 2018/6/4.
 */

public class AMapDelegate implements IMapDelegate<AMap> {

  private Context mContext;
  private MapView mMapView;
  private AMap aMap;
  private IMapListener mMapListener;
  private AMapRipple mapRipple;
  private AMapInfoWindow aMapInfoWindow;
  private CenterLatLngParams mCenterLatLngParams = new CenterLatLngParams();
  private Marker mMyMarker;
  private MyLocationStyle myLocationStyle;

  public AMapDelegate(Context context) {
    mContext = context;
    mMapView = new MapView(mContext);
    aMap = mMapView.getMap();

    aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {

      @Override
      public void onCameraChange(CameraPosition cameraPosition) {
        mMapListener.onMapMoveChange();
      }

      @Override
      public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mCenterLatLngParams.center = new LatLng(cameraPosition.target.latitude,
            cameraPosition.target.longitude);
        mMapListener.onMapMoveFinish(mCenterLatLngParams);
      }
    });
  }

  @Override
  public void setMapListener(IMapListener listener) {
    mMapListener = listener;
  }

  @Override
  public View getView() {
    return mMapView;
  }

  @Override
  public void setPadding(Padding padding) {
//    Padding defaultPadding = MapStatusOperation.instance().getDeltaPadding();
//    UiSettings settings = aMap.getUiSettings();
//    aMap.(defaultPadding.left + padding.left, defaultPadding.top + padding.top,
//        defaultPadding.right + padding.right, defaultPadding.bottom + padding.bottom);
  }

  @Override
  public void setTraffic(boolean isShowTraffic) {
    aMap.setTrafficEnabled(isShowTraffic);
  }

  @Override
  public void setUIController(boolean isShowUIController) {
    UiSettings settings = aMap.getUiSettings();
    settings.setZoomControlsEnabled(isShowUIController);
  }

  @Override
  public com.one.map.map.element.Marker addMarker(MarkerOption option) {
    final com.amap.api.maps.model.MarkerOptions options = MarkerOptionConvert.convert2AMapMarkerOption(option);
    com.amap.api.maps.model.Marker marker = aMap.addMarker(options);
    final AMapMarker aMapMarker = new AMapMarker(marker);
    aMapMarker.setMarkerType(option.markerType);
    aMapMarker.setClick(option.isClickable);
    aMapMarker.rotate(option.rotate);
    aMap.setOnMarkerClickListener(option.getMarkerClickListenerAdapter());
    return new com.one.map.map.element.Marker(aMapMarker);
  }

  @Override
  public List<com.one.map.map.element.Marker> addMarkers(List<MarkerOption> options) {
    List<com.one.map.map.element.Marker> markers = new ArrayList<>();
    for (MarkerOption option : options) {
      addMarker(option);
    }
    return markers;
  }

  @Override
  public IMarker myLocationConfig(BitmapDescriptor bitmapDescriptor, LatLng position) {
    // tencent draw Marker
    MarkerOptions op = new MarkerOptions();
    op.position(LatLngConvert.convert2AMapLatLng(position));
    op.icon(BitmapDescriptorConvert.convert2AMapBitmapDesriptor(bitmapDescriptor));
    op.anchor(0.5f, 0.5f);
    mMyMarker = aMap.addMarker(op);
    AMapMarker aMapMarker = new AMapMarker(mMyMarker);
    return aMapMarker;
  }

  @Override
  public void setMyLocationEnable(boolean enable) {
    if (myLocationStyle == null) {
      myLocationStyle = new MyLocationStyle();
      myLocationStyle.myLocationIcon(com.amap.api.maps.model.BitmapDescriptorFactory.fromResource(
          R.drawable.common_map_my_location))
          .radiusFillColor(Color.TRANSPARENT).strokeColor(Color.TRANSPARENT).strokeWidth(0f);
    }
    aMap.setMyLocationStyle(myLocationStyle);
    aMap.setMyLocationEnabled(enable);
  }

  @Override
  public void setLogoPosition(int position, int left, int top, int right, int bottom) {
    UiSettings settings = aMap.getUiSettings();
    settings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
    settings.setLogoBottomMargin(bottom);
    settings.setLogoLeftMargin(left);
  }

  @Override
  public LatLng getCenterPosition() {
    return mCenterLatLngParams.center;
  }

  @Override
  public void geo2Address(final LatLng latLng) {
    GeocodeSearch geocodeSearch = new GeocodeSearch(mContext);
    geocodeSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
      @Override
      public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int code) {
        if (code == 1000) {
          RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
          if (regeocodeAddress != null) {
            mCenterLatLngParams.center = latLng;
            mCenterLatLngParams.detailAddress = regeocodeAddress.getFormatAddress();
            Address centerAdr = new Address();
            centerAdr.mCity = regeocodeAddress.getCity();
            centerAdr.mCountry = regeocodeAddress.getCountry();
            centerAdr.mStreet = regeocodeAddress.getStreetNumber().getStreet();
            centerAdr.mStreetCode = regeocodeAddress.getStreetNumber().getNumber();
            centerAdr.mAdrFullName = regeocodeAddress.getFormatAddress();
            centerAdr.mAdrLatLng = latLng;
            List<PoiItem> pois = regeocodeAddress.getPois();
            Logger.e("ldx", "pois >>>>>>>>>>>>>>>" + pois);
            Collections.sort(pois, new Comparator<PoiItem>() {
              @Override
              public int compare(PoiItem o1, PoiItem o2) {
                if (o1.getDistance() > o2.getDistance()) {
                  return 1;
                } else if (o1.getDistance() < o2.getDistance()) {
                  return -1;
                }
                return 0;
              }
            });
            List<Address> poiAdrs = new ArrayList<Address>();
            for (PoiItem poi : pois) {
              Address poiAdr = new Address();
              poiAdr.mAdrLatLng = new LatLng(poi.getLatLonPoint().getLatitude(), poi.getLatLonPoint().getLongitude());
              poiAdr.mAdrDisplayName = poi.getTitle();
              poiAdr.mAdrFullName = poi.getSnippet();
              poiAdr.distance = poi.getDistance();
              poiAdrs.add(poiAdr);
            }
            if (pois != null && pois.size() > 0) {
              centerAdr.mAdrDisplayName = pois.get(0).getTitle();
            }
            mMapListener.onMapGeo2Address(centerAdr);
            mMapListener.onMapPoiAddresses(0, poiAdrs);
          }
        }
      }

      @Override
      public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

      }
    });
    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude)
    , 0, GeocodeSearch.AMAP);
    geocodeSearch.getFromLocationAsyn(query);

  }

  @Override
  public void poiSearchByKeyWord(String curCity, CharSequence key, final IPoiSearchListener listener) {
    PoiSearch.Query query = new Query((String) key, "", curCity);
    query.setCityLimit(true);
    query.setDistanceSort(true);
    query.setPageSize(10);
    query.setPageNum(1);
    PoiSearch poiSearch = new PoiSearch(mContext, query);
    poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {
      @Override
      public void onPoiSearched(PoiResult poiResult, int code) {
        if (code == 1000) {
          List<PoiItem> poiItems = poiResult.getPois();
          List<Address> searchAddress = new ArrayList<Address>();
          if (poiItems != null && !poiItems.isEmpty()) {
            for (PoiItem item : poiItems) {
              Address address = new Address();
              address.mCity = item.getCityName();
              address.mCityCode = item.getCityCode();
              address.mAdrLatLng = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
              address.mAdrFullName = item.getSnippet();
              address.mAdrDisplayName = item.getTitle();
              address.type = 6;
              searchAddress.add(address);
            }

            if (listener != null) {
              listener.onMapSearchAddress(searchAddress);
            }
          }
        }
      }

      @Override
      public void onPoiItemSearched(PoiItem poiItem, int code) {

      }
    });
    poiSearch.searchPOIAsyn();
  }

  /**
   * 07000 生活服务
   * @param latLng
   * @param city
   */
  @Override
  public void poiNearByWithCity(LatLng latLng, String city) {
    PoiSearch.Query query = new Query("", "070000", city);
    query.setCityLimit(true);
    query.setDistanceSort(true);
    query.setPageSize(10);
    query.setPageNum(1);
    PoiSearch poiSearch = new PoiSearch(mContext, query);
    poiSearch.setBound(new SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 15000, true));
    poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {
      @Override
      public void onPoiSearched(PoiResult poiResult, int code) {
        if (code == 1000) {
          List<PoiItem> poiItems = poiResult.getPois();
          List<Address> searchAddress = new ArrayList<Address>();
          if (poiItems != null && !poiItems.isEmpty()) {
            for (PoiItem item : poiItems) {
              Address address = new Address();
              address.mCity = item.getCityName();
              address.mCityCode = item.getCityCode();
              address.mAdrLatLng = new LatLng(item.getLatLonPoint().getLatitude(),
                  item.getLatLonPoint().getLongitude());
              address.mAdrFullName = item.getSnippet();
              address.mAdrDisplayName = item.getTitle();
              address.type = 6;
              searchAddress.add(address);
            }
            mMapListener.onMapPoiAddresses(1, searchAddress);
          }
        }
      }

      @Override
      public void onPoiItemSearched(PoiItem poiItem, int code) {

      }
    });
    poiSearch.searchPOIAsyn();
  }

  @Override
  public Polyline addPolyline(PolylineOption option) {
    PolylineOptions options = PolylineOptionConvert.convert2AMapPolylineOption(option);
    com.amap.api.maps.model.Polyline polyline = aMap.addPolyline(options);
    AMapPolyline aMapPolyline = new AMapPolyline(polyline);
    return new Polyline(aMapPolyline);
  }

  @Override
  public Circle addCircle(CircleOption option) {
    return null;
  }

  @Override
  public void doBestView(BestViewModel model) {
    // 最佳View
    if (model != null) {
      setPadding(model.padding);
      moveTo(model);

    }
  }

  @Override
  public void clearElements() {
    aMap.clear();
  }

  @Override
  public boolean showInfoWindow(IMarker marker, CharSequence msg) {
    aMapInfoWindow = new AMapInfoWindow<>(mContext, aMap, marker);
    aMapInfoWindow.setMessage(msg);
    return aMapInfoWindow.showInfoWindow();
  }

  @Override
  public void updateInfoWindowMsg(CharSequence msg) {
    if (aMapInfoWindow != null) {
      aMapInfoWindow.updateMessage(msg);
    }
  }

  @Override
  public void removeInfoWindow() {
    if (aMapInfoWindow != null) {
      aMapInfoWindow.remove();
    }
  }

  @Override
  public void startRadarAnim(LatLng latLng) {
    if (mapRipple == null) {
      mapRipple = new AMapRipple(aMap, latLng, mContext)
          .withNumberOfRipples(4)
          .withFillColor(Color.parseColor("#58A3D2E4"))
          .withStrokeColor(Color.parseColor("#7CA3D2E4"))
          .withStrokeWidth(.5f)      // 10dp
          .withDurationBetweenTwoRipples(1000)
          .withDistance(80)      // 1000 metres radius
          .withRippleDuration(5000)    //5000ms
          .withTransparency(0.5f);
    }

    mapRipple.startRippleMapAnimation();
  }

  @Override
  public void stopRadarAnim() {
    if (mapRipple != null && mapRipple.isAnimationRunning()) {
      mapRipple.stopRippleMapAnimation();
      mapRipple = null;
    }
  }

  private void moveTo(BestViewModel model) {
    CameraUpdate cameraUpdate;
    Padding padding = MapStatusOperation.instance().getDeltaPadding();
    int left = model.padding.left + padding.left;
    int top = model.padding.top + padding.top;
    int right = model.padding.right + padding.right;
    int bottom = model.padding.bottom + padding.bottom;
    if (!model.bounds.isEmpty()) {
      if (model.zoomCenter != null) {
        /** 以center为中心点,同时确保所有点都在地图可视区域*/
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < model.bounds.size(); i++) {
          builder.include(new com.amap.api.maps.model.LatLng(model.bounds.get(i).latitude,
              model.bounds.get(i).longitude));
        }
        LatLngBounds bounds = builder.build();

        com.amap.api.maps.model.LatLng sw = bounds.southwest;
        com.amap.api.maps.model.LatLng ne = bounds.northeast;

        LatLng swSymmetry = MapUtils
            .getSymmetry(new LatLng(sw.latitude, sw.longitude), model.zoomCenter);
        LatLng neSymmetry = MapUtils
            .getSymmetry(new LatLng(ne.latitude, ne.longitude), model.zoomCenter);
        double southwestLat = MapUtils
            .min(sw.latitude, swSymmetry.latitude, ne.latitude, neSymmetry.latitude);
        double southwestLng = MapUtils
            .min(sw.longitude, swSymmetry.longitude, ne.longitude, neSymmetry.longitude);
        double northeastLat = MapUtils
            .max(sw.latitude, swSymmetry.latitude, ne.latitude, neSymmetry.latitude);
        double northeastLng = MapUtils
            .max(sw.longitude, swSymmetry.longitude, ne.longitude, neSymmetry.longitude);

        com.amap.api.maps.model.LatLng southwest = new com.amap.api.maps.model.LatLng(southwestLat,
            southwestLng);
        com.amap.api.maps.model.LatLng northeast = new com.amap.api.maps.model.LatLng(northeastLat,
            northeastLng);
        LatLngBounds symmetryBounds = new LatLngBounds(southwest, northeast);

        cameraUpdate = CameraUpdateFactory.newLatLngBoundsRect(symmetryBounds, left,right, top, bottom);
      } else {
        /** 以一组点几何中心为中心点, 同时确保所有点都在地图可视区域*/
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < model.bounds.size(); i++) {
          LatLng latLng = model.bounds.get(i);
          if (latLng != null) {
            builder.include(new com.amap.api.maps.model.LatLng(latLng.latitude, latLng.longitude));
          }
        }
        LatLngBounds bounds = builder.build();
        cameraUpdate = CameraUpdateFactory.newLatLngBoundsRect(bounds, left,right, top, bottom);
      }
    } else {
      cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLngConvert.convert2AMapLatLng(model.zoomCenter), model.zoomLevel);
    }
    aMap.animateCamera(cameraUpdate);
  }

  /************* life cycle begin **************/
  @Override
  public void onCreate(Bundle bundle) {
    if (mMapView != null) {
      mMapView.onCreate(bundle);
    }
  }

  @Override
  public void onResume() {
    if (mMapView != null) {
      mMapView.onResume();
    }
  }

  @Override
  public void onStart() {
  }

  @Override
  public void onPause() {
    if (mMapView != null) {
      mMapView.onPause();
    }
  }

  @Override
  public void onRestart() {
  }

  @Override
  public void onStop() {
  }

  @Override
  public void onDestroy() {
    if (mMapView != null) {
      mMapView.onDestroy();
    }
  }
  /************* life cycle end **************/
}
