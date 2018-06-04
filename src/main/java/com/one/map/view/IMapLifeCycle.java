package com.one.map.view;

import android.os.Bundle;
import android.support.annotation.Keep;

/**
 * Created by ludexiang on 2017/11/27.
 */

@Keep
public interface IMapLifeCycle {

  void onCreate(Bundle bundle);
  
  void onRestart();
  
  void onStart();
  
  void onResume();
  
  void onPause();
  
  void onStop();
  
  void onDestroy();
}
