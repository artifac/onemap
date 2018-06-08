package com.one.map.tencent;

import android.content.Context;
import android.os.Bundle;
import com.one.map.IMap.IPoiSearchListener;
import com.one.map.map.BitmapDescriptor;
import com.one.map.map.BitmapDescriptorConvert;
import com.one.map.map.CircleOption;
import com.one.map.map.CircleOptionConverter;
import com.one.map.map.LatLngConvert;
import com.one.map.map.MarkerOption;
import com.one.map.map.MarkerOptionConvert;
import com.one.map.map.PolylineOption;
import com.one.map.map.PolylineOptionConvert;
import com.one.map.map.element.IInfoWindow;
import com.one.map.map.element.IMarker;
import com.one.map.map.element.IPolyline;
import com.one.map.map.element.tencent.TencentCircle;
import com.one.map.map.element.tencent.TencentInfoWindow;
import com.one.map.map.element.tencent.TencentMarker;
import com.one.map.map.element.tencent.TencentPolyline;
import com.one.map.model.Address;
import com.one.map.model.BestViewModel;
import com.one.map.model.LatLng;
import com.one.map.model.MapStatusOperation;
import com.one.map.model.MapStatusOperation.Padding;
import com.one.map.util.MapUtils;
import com.one.map.view.IMapDelegate;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.param.SearchParam.Nearby;
import com.tencent.lbssearch.object.param.SearchParam.Region;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject.ReverseAddressResult.Poi;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.object.result.SearchResultObject.SearchResultData;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnCameraChangeListener;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ludexiang on 2017/11/27.
 */

public class TencentMapDelegate implements IMapDelegate<TencentMap> {

  private Context mContext;
  private MapView mView;
  private TencentMap mTencentMap;
  private Polyline mPolyline;
  private IInfoWindow mTencentInfoWindow;
  private IPolyline mTencentPolyline;
  private SupportMapFragment mMapFragment;
  private Marker mMyMarker;
  private IMapListener mMapListener;

  private CenterLatLngParams mCenterLatLngParams = new CenterLatLngParams();

  public TencentMapDelegate(Context context) {
    mContext = context;
    mView = new MapView(mContext);
    mTencentMap = mView.getMap();
    mTencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
    mTencentMap.setLocationSource(new LocationSource() {
      @Override
      public void activate(OnLocationChangedListener onLocationChangedListener) {

      }

      @Override
      public void deactivate() {

      }
    });

    mTencentMap.getUiSettings().setScaleViewEnabled(false);
    mTencentMap.getUiSettings().setZoomControlsEnabled(false);
    mTencentMap.getUiSettings().setMyLocationButtonEnabled(false);
    mTencentMap.setOnCameraChangeListener(new OnCameraChangeListener() {
      @Override
      public void onCameraChange(CameraPosition cameraPosition) {
        mMapListener.onMapMoveChange();
      }

      @Override
      public void onCameraChangeFinished(CameraPosition cameraPosition) {
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
  public LatLng getCenterPosition() {
    return mCenterLatLngParams.center;
  }

  @Override
  public void geo2Address(final LatLng ll) {
    com.tencent.tencentmap.mapsdk.maps.model.LatLng latLng = LatLngConvert
        .convert2TencentLatLng(ll);
    Geo2AddressParam geo2AddressParam = new Geo2AddressParam()
        .location(new Location((float) latLng.latitude, (float) latLng.longitude)).get_poi(true);
    TencentSearch search = new TencentSearch(mContext);
    search.geo2address(geo2AddressParam, new HttpResponseListener() {
      @Override
      public void onSuccess(int i, BaseObject baseObject) {
        Geo2AddressResultObject result = (Geo2AddressResultObject) baseObject;
        mCenterLatLngParams.center = ll;
        mCenterLatLngParams.detailAddress = result.result.address;
        Address centerAdr = new Address();
        centerAdr.mCity = result.result.address_component.city;
        centerAdr.mCountry = result.result.address_component.nation;
        centerAdr.mStreet = result.result.address_component.street;
        centerAdr.mStreetCode = result.result.address_component.street_number;
        centerAdr.mAdrFullName = result.result.address;
        centerAdr.mAdrLatLng = ll;
        List<Geo2AddressResultObject.ReverseAddressResult.Poi> pois = result.result.pois;
        Collections.sort(pois, new Comparator<Poi>() {
          @Override
          public int compare(Poi o1, Poi o2) {
            if (o1._distance > o2._distance) {
              return 1;
            } else if (o1._distance < o2._distance) {
              return -1;
            }
            return 0;
          }
        });
        List<Address> poiAdrs = new ArrayList<Address>();
        for (Poi poi : pois) {
          Address poiAdr = new Address();
          poiAdr.mAdrLatLng = new LatLng(poi.location.lat, poi.location.lng);
          poiAdr.mAdrDisplayName = poi.title;
          poiAdr.mAdrFullName = poi.address;
          poiAdr.distance = poi._distance;
          poiAdrs.add(poiAdr);
        }
        if (pois != null && pois.size() > 0) {
          centerAdr.mAdrDisplayName = pois.get(0).title;
        }
        mMapListener.onMapGeo2Address(centerAdr);
        mMapListener.onMapPoiAddresses(0, poiAdrs);
      }

      @Override
      public void onFailure(int i, String s, Throwable throwable) {

      }
    });
  }

  @Override
  public void poiNearByWithCity(LatLng latLng, final String city) {
    Region region = new Region().poi(city);
    Nearby nearby = new Nearby()
        .point(new Location((float) latLng.latitude, (float) latLng.longitude)).r(20000);
    SearchParam param = new SearchParam().region(region).keyword("基础设施")
        .boundary(nearby).orderby(true).page_index(1).page_size(15);
    TencentSearch search = new TencentSearch(mContext);
    search.search(param, new HttpResponseListener() {
      @Override
      public void onSuccess(int i, BaseObject baseObject) {
        SearchResultObject result = (SearchResultObject) baseObject;
        if (result != null) {
          List<Address> searchAddress = new ArrayList<Address>();
          List<SearchResultData> searchResultDataList = result.data;
          for (SearchResultData data : searchResultDataList) {
            Address address = new Address();
            address.mAdrFullName = data.address;
            address.mAdrDisplayName = data.title;
            address.mAdrLatLng = new LatLng(data.location.lat, data.location.lng);
            searchAddress.add(address);
          }
          mMapListener.onMapPoiAddresses(1, searchAddress);
        }
      }

      @Override
      public void onFailure(int i, String s, Throwable throwable) {
      }
    });
  }

  @Override
  public void poiSearchByKeyWord(String city, CharSequence key, final IPoiSearchListener listener) {
    final Region region = new Region().poi(city);
    SearchParam param = new SearchParam().keyword(key.toString()).orderby(true).boundary(region);
    TencentSearch search = new TencentSearch(mContext);
    search.search(param, new HttpResponseListener() {
      @Override
      public void onSuccess(int i, BaseObject baseObject) {
        SearchResultObject result = (SearchResultObject) baseObject;
        if (result != null) {
          List<Address> searchAddress = new ArrayList<Address>();
          List<SearchResultData> searchDatas = result.data;
          for (SearchResultData data : searchDatas) {
            Address address = new Address();
            address.mAdrFullName = data.address;
            address.mAdrDisplayName = data.title;
            address.mAdrLatLng = new LatLng(data.location.lat, data.location.lng);
            searchAddress.add(address);
          }

          if (listener != null) {
            listener.onMapSearchAddress(searchAddress);
          }
        }
      }

      @Override
      public void onFailure(int i, String s, Throwable throwable) {

      }
    });
  }

  @Override
  public MapView getView() {
    return mView;
  }

  @Override
  public void setPadding(MapStatusOperation.Padding padding) {
    Padding defaultPadding = MapStatusOperation.instance().getDeltaPadding();
    mTencentMap.setPadding(defaultPadding.left + padding.left, defaultPadding.top + padding.top,
        defaultPadding.right + padding.right, defaultPadding.bottom + padding.bottom);
  }

  @Override
  public IMarker myLocationConfig(BitmapDescriptor bitmapDescriptor, LatLng latLng) {
//    CircleOption circleOption = new CircleOption();
//    circleOption.setCenter(latLng);
//    circleOption.setFillColor(Color.parseColor("#0f0000ff"));
////      circleOption.setStrokeColor(Color.parseColor("#a0000056"));
////      circleOption.setStrokeWidth(1);
//    circleOption.setRadius(50);
//    addCircle(circleOption);

    // tencent draw Marker
    MarkerOptions op = new MarkerOptions(LatLngConvert.convert2TencentLatLng(latLng))
        .icon(BitmapDescriptorConvert.convert2TencentBitmapDescriptor(bitmapDescriptor))
        .anchor(0.5f, 0.5f);
    mMyMarker = mTencentMap.addMarker(op);
    TencentMarker tencentMarker = new TencentMarker(mMyMarker);
    return tencentMarker;
  }

  @Override
  public void setTraffic(boolean isShowTraffic) {
    mTencentMap.setTrafficEnabled(isShowTraffic);
  }

  @Override
  public void setUIController(boolean isShowUIController) {
    UiSettings settings = mTencentMap.getUiSettings();
    settings.setZoomControlsEnabled(isShowUIController);
  }

  @Override
  public void setLogoPosition(int position, int left, int top, int right, int bottom) {
    UiSettings settings = mTencentMap.getUiSettings();
    settings.setLogoPositionWithMargin(position, top, bottom, left, right);
  }

  @Override
  public com.one.map.map.element.Marker addMarker(MarkerOption option) {
    final MarkerOptions options = MarkerOptionConvert.convert2TencentMarkerOption(option);
    Marker marker = mTencentMap.addMarker(options);
    final TencentMarker tencentMarker = new TencentMarker(marker);
    tencentMarker.setMarkerType(option.markerType);
    tencentMarker.setClick(option.isClickable);
    mTencentMap.setOnMarkerClickListener(option.getMarkerClickListenerAdapter());
    return new com.one.map.map.element.Marker(tencentMarker);
  }

  /**
   * 添加一系列的Marker
   */
  @Override
  public List<com.one.map.map.element.Marker> addMarkers(List<MarkerOption> options) {
    List<com.one.map.map.element.Marker> markers = new ArrayList<>();
    for (MarkerOption option : options) {
      MarkerOptions markerOptions = MarkerOptionConvert.convert2TencentMarkerOption(option);
      Marker marker = mTencentMap.addMarker(markerOptions);
      final TencentMarker tencentMarker = new TencentMarker(marker);
      tencentMarker.setMarkerType(option.markerType);
      tencentMarker.setClick(option.isClickable);
      mTencentMap.setOnMarkerClickListener(option.getMarkerClickListenerAdapter());
      markers.add(new com.one.map.map.element.Marker(tencentMarker));
    }
    return markers;
  }

  @Override
  public com.one.map.map.element.Circle addCircle(CircleOption options) {
    CircleOptions circleOptions = CircleOptionConverter.convert2TencentCircleOption(options);
    Circle baiduCircle = mTencentMap.addCircle(circleOptions);
    TencentCircle circle = new TencentCircle(baiduCircle);
    return new com.one.map.map.element.Circle(circle);
  }

  @Override
  public void setMyLocationEnable(boolean enable) {
    mTencentMap.setMyLocationEnabled(enable);
  }

  @Override
  public void doBestView(BestViewModel model) {
    if (model != null) {
      setPadding(model.padding);
      if (model.bounds.size() > 0) {
        moveTo(model);
      } else {
        moveTo(model.zoomCenter, model.zoomLevel);
      }
    }
  }

  @Override
  public com.one.map.map.element.Polyline addPolyline(PolylineOption option) {
    PolylineOptions options = PolylineOptionConvert.convert2TencentPolylineOption(option);
    Polyline polyline = mTencentMap.addPolyline(options);
    TencentPolyline tencentPolyline = new TencentPolyline(polyline);
    return new com.one.map.map.element.Polyline(tencentPolyline);
  }

  @Override
  public boolean showInfoWindow(IMarker marker, CharSequence msg) {
    mTencentInfoWindow = new TencentInfoWindow(mContext, mTencentMap, marker);
    mTencentInfoWindow.setMessage(msg);
    return mTencentInfoWindow.showInfoWindow();
  }

  @Override
  public void updateInfoWindowMsg(CharSequence msg) {
    if (mTencentInfoWindow != null) {
      mTencentInfoWindow.updateMessage(msg);
    }
  }

  @Override
  public void removeInfoWindow() {
    if (mTencentInfoWindow != null) {
      mTencentInfoWindow.remove();
    }
  }

  private void moveTo(LatLng latLng, float zoom) {
    if (latLng != null) {
      com.tencent.tencentmap.mapsdk.maps.model.LatLng center = LatLngConvert
          .convert2TencentLatLng(latLng);
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(center, zoom);
      mTencentMap.animateCamera(cameraUpdate);
    }
  }

  private void moveTo(BestViewModel model) {
    CameraUpdate cameraUpdate;
    Padding padding = MapStatusOperation.instance().getDeltaPadding();
    if (model.zoomCenter != null) {
      /** 以center为中心点,同时确保所有点都在地图可视区域*/
      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      for (int i = 0; i < model.bounds.size(); i++) {
        builder.include(
            new com.tencent.tencentmap.mapsdk.maps.model.LatLng(model.bounds.get(i).latitude,
                model.bounds.get(i).longitude));
      }
      LatLngBounds bounds = builder.build();

      com.tencent.tencentmap.mapsdk.maps.model.LatLng sw = bounds.southwest;
      com.tencent.tencentmap.mapsdk.maps.model.LatLng ne = bounds.northeast;

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

      com.tencent.tencentmap.mapsdk.maps.model.LatLng southwest = new com.tencent.tencentmap.mapsdk.maps.model.LatLng(
          southwestLat, southwestLng);
      com.tencent.tencentmap.mapsdk.maps.model.LatLng northeast = new com.tencent.tencentmap.mapsdk.maps.model.LatLng(
          northeastLat, northeastLng);
      LatLngBounds symmetryBounds = new LatLngBounds(southwest, northeast);

      cameraUpdate = CameraUpdateFactory
          .newLatLngBoundsRect(symmetryBounds, model.padding.left + padding.left,
              model.padding.right + padding.right,
              model.padding.top + padding.top, model.padding.bottom + padding.bottom);
    } else {
      /** 以一组点几何中心为中心点, 同时确保所有点都在地图可视区域*/
      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      for (int i = 0; i < model.bounds.size(); i++) {
        builder.include(
            new com.tencent.tencentmap.mapsdk.maps.model.LatLng(model.bounds.get(i).latitude,
                model.bounds.get(i).longitude));
      }
      LatLngBounds bounds = builder.build();
      cameraUpdate = CameraUpdateFactory
          .newLatLngBoundsRect(bounds, model.padding.left + padding.left,
              model.padding.right + padding.right,
              model.padding.top + padding.top, model.padding.bottom + padding.bottom);
    }
    mTencentMap.animateCamera(cameraUpdate);
  }

  @Override
  public void clearElements() {
    mTencentMap.clear();
  }

  /************* life cycle begin **************/
  @Override
  public void onCreate(Bundle bundle) {
  }

  @Override
  public void onResume() {
    if (mView != null) {
      mView.onResume();
    }
  }

  @Override
  public void onPause() {
    if (mView != null) {
      mView.onPause();
    }
  }

  @Override
  public void onStart() {
    if (mView != null) {
      mView.onStart();
    }
  }


  @Override
  public void onStop() {
    if (mView != null) {
      mView.onStop();
    }
  }

  @Override
  public void onRestart() {
    if (mView != null) {
      mView.onRestart();
    }
  }

  public void onDestroy() {
    if (mView != null) {
      mView.onDestroy();
    }
  }
  /************* life cycle end **************/
}
