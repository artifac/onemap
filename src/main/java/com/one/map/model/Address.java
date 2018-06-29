package com.one.map.model;

import java.io.Serializable;

public class Address implements Serializable {
  
  /**
   * 地址经纬度
   */
  public LatLng mAdrLatLng;
  
  /**
   * 定位半径
   */
  public float mRadius;
  
  /**
   * 国家 城市 国家码 和 城市码
   */
  public String mCity;
  public String mCityCode;
  public String mCountry;
  public String mCountryCode;
  
  /**
   * 街道及街道码
   */
  public String mStreet;
  public String mStreetCode;
  
  /**
   * 定位返回时间
   */
  public String mServerBackTime;
  
  /**
   * 地址全名称
   */
  public String mAdrFullName;
  
  /**
   * 地址显示名称
   */
  public String mAdrDisplayName;

  /**
   * 旋转角度
   */
  public float bearing;

  public float speed;

  public float accuracy; // 精度 通常精度为, GPS：<20米，WiFi：30-180米，基站：150-800米.

  public float distance = -1;

  /**
   * 1 起点 2 终点 3 搜索历史 4 home 5 company 6 搜索
   */
  public int type = -1;
  
  @Override
  public String toString() {
    return "Address{" +
        "mAdrLatLng=" + mAdrLatLng +
        ", mRadius=" + mRadius +
        ", mCity='" + mCity + '\'' +
        ", mCityCode='" + mCityCode + '\'' +
        ", mCountry='" + mCountry + '\'' +
        ", mCountryCode='" + mCountryCode + '\'' +
        ", mStreet='" + mStreet + '\'' +
        ", mStreetCode='" + mStreetCode + '\'' +
        ", mServerBackTime='" + mServerBackTime + '\'' +
        ", mAdrFullName='" + mAdrFullName + '\'' +
        ", mAdrDisplayName='" + mAdrDisplayName + '\'' +
        ", bearing=" + bearing +
        ", speed=" + speed +
        ", accuracy=" + accuracy +
        '}';
  }
}
