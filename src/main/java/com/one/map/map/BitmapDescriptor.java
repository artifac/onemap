package com.one.map.map;

import android.graphics.Bitmap;

public class BitmapDescriptor {
  public Bitmap getBitmap() {
    return bitmap;
  }
  
  public void setBitmap(Bitmap bitmap) {
    this.bitmap = bitmap;
  }
  
  private Bitmap bitmap;
}
