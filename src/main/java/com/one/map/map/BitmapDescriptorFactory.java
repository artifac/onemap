package com.one.map.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import java.lang.ref.WeakReference;

public class BitmapDescriptorFactory {

  private static SparseArray<WeakReference<BitmapDescriptor>> cache = new SparseArray<>();

  /**
   * 根据 Bitmap 创建描述信息
   *
   * @param image bitmap数据
   * @return bitmap 描述信息, 如果image为空则返回 null
   */
  public static BitmapDescriptor fromBitmap(Bitmap image) {
    if (image == null) {
      return null;
    }
    int key = image.hashCode();
    WeakReference<BitmapDescriptor> cachedDescriptor = cache.get(key);
    if (cachedDescriptor != null && cachedDescriptor.get() != null && !cachedDescriptor.get()
        .getBitmap().isRecycled()) {
      return cachedDescriptor.get();
    } else {
      cache.remove(key);
    }

    BitmapDescriptor bitmapDescriptor = new BitmapDescriptor();
    bitmapDescriptor.setBitmap(image);
    cache.put(key, new WeakReference<>(bitmapDescriptor));
    return bitmapDescriptor;
  }
  
  public static BitmapDescriptor fromResources(Resources res, int resId) {
    int key = String.valueOf(resId).hashCode();
    WeakReference<BitmapDescriptor> cachedDescriptor = cache.get(key);
    if (cachedDescriptor != null && cachedDescriptor.get() != null && !cachedDescriptor.get()
            .getBitmap().isRecycled()) {
      return cachedDescriptor.get();
    } else {
      cache.remove(key);
    }

    BitmapDescriptor bitmapDescriptor = new BitmapDescriptor();
    Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
    bitmapDescriptor.setBitmap(bitmap);
    cache.put(key, new WeakReference<>(bitmapDescriptor));
    return bitmapDescriptor;
  }
}
