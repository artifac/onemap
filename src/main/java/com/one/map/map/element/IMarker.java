package com.one.map.map.element;

import android.os.Bundle;
import android.support.annotation.IntDef;
import com.one.map.anim.Animation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

/**
 * B -> BitmapDescription
 * V -> Marker
 */

public interface IMarker<B, V> extends IMapElements<V> {

  int TYPE_NORMAL = 0;
  int TYPE_REDBAG = 1;
  int TYPE_GIFT = 2;
  int TYPE_PARKING = 3;

  @Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
  @Retention(RetentionPolicy.SOURCE)
  @IntDef({TYPE_REDBAG, TYPE_NORMAL, TYPE_GIFT, TYPE_PARKING})
  @interface MarkerType {

  }

  @MarkerType
  int getMarkerType();

  void setMarkerType(@MarkerType int type);

  void setZIndex(int zIndex);

  int getZIndex();

  void setToTop();

  Bundle getExtraInfo();

  void setExtraInfo(Bundle extraInfo);

  void setTitle(String title);

  String getTitle();

  void setIcon(B icon);

  B getIcon();

  void setIcons(ArrayList<B> icons);

  ArrayList<B> getIcons();

  void setPeriod(int period);

  void setAnimation(Animation animation);

  V getSourceMarker();

  V getMarkerInstance();

  void setClick(boolean clickable);

  boolean isClickable();

  void rotate(float angle);
}
