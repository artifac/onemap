package com.one.map.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class WindowUtil {
  public static float getScreenDensity() {
    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
    return metrics.density;
  }
}
