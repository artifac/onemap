/*
 * Copyright (C) 2016 Abhay Raj Singh Yadav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.one.map.amap;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.animation.LinearInterpolator;
import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.one.map.R;
import com.one.map.map.LatLngConvert;
import com.one.map.model.LatLng;

/**
 * Created by abhay yadav on 09-Aug-16.
 */
public class AMapRipple {

  private AMap mAMap;
  private LatLng mLatLng, mPrevLatLng;
  private BitmapDescriptor mBackgroundImageDescriptor;  //ripple image.
  private float mTransparency = 0.5f;                   //transparency of image.
  private volatile double mDistance = 2000;             //distance to which ripple should be shown in metres
  private int mNumberOfRipples = 1;                     //number of ripples to show, max = 4
  private int mFillColor = Color.TRANSPARENT;           //fill color of circle
  private int mStrokeColor = Color.BLACK;               //border color of circle
  private float mStrokeWidth = 10;                        //border width of circle
  private long mDurationBetweenTwoRipples = 4000;       //in microseconds.
  private long mRippleDuration = 12000;                 //in microseconds
  private ValueAnimator mAnimators[];
  private Handler mHandlers[];
  private GroundOverlay mGroundOverlays[];
  private GradientDrawable mBackground;
  private boolean isAnimationRunning = false;

  public AMapRipple(AMap aMap, LatLng latLng, Context context) {
    mAMap = aMap;
    mLatLng = latLng;
    mPrevLatLng = latLng;
    mBackground = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.one_map_radar_background);
    mAnimators = new ValueAnimator[4];
    mHandlers = new Handler[4];
    mGroundOverlays = new GroundOverlay[4];
  }

  /**
   * @param transparency sets transparency for background of circle
   */
  public AMapRipple withTransparency(float transparency) {
    mTransparency = transparency;
    return this;
  }

  /**
   * @param distance sets radius distance for circle
   */
  public AMapRipple withDistance(double distance) {
    if (distance < 200) {
      distance = 200;
    }
    mDistance = distance;
    return this;
  }

  /**
   * @param latLng sets position for center of circle
   */
  public AMapRipple withLatLng(LatLng latLng) {
    mPrevLatLng = mLatLng;
    mLatLng = latLng;
    return this;
  }

  /**
   * @param numberOfRipples sets count of ripples
   */
  public AMapRipple withNumberOfRipples(int numberOfRipples) {
    if (numberOfRipples > 4 || numberOfRipples < 1) {
      numberOfRipples = 4;
    }
    mNumberOfRipples = numberOfRipples;
    return this;
  }

  /**
   * @param fillColor sets fill color
   */
  public AMapRipple withFillColor(int fillColor) {
    mFillColor = fillColor;
    return this;
  }

  /**
   * @param strokeColor sets stroke color
   */
  public AMapRipple withStrokeColor(int strokeColor) {
    mStrokeColor = strokeColor;
    return this;
  }

  /**
   * @deprecated use {@link #withStrokeWidth(float)} instead
   */
  @Deprecated
  public void withStrokewidth(float strokeWidth) {
    mStrokeWidth = strokeWidth;
  }

  /**
   * @param strokeWidth sets stroke width
   */
  public AMapRipple withStrokeWidth(float strokeWidth) {
    mStrokeWidth = strokeWidth;
    return this;
  }

  /**
   * @param durationBetweenTwoRipples sets duration before pulse tick animation
   */
  public AMapRipple withDurationBetweenTwoRipples(long durationBetweenTwoRipples) {
    mDurationBetweenTwoRipples = durationBetweenTwoRipples;
    return this;
  }

  /**
   * @return current state of animation
   */
  public boolean isAnimationRunning() {
    return isAnimationRunning;
  }

  /**
   * @param rippleDuration sets duration of ripple animation
   */
  public AMapRipple withRippleDuration(long rippleDuration) {
    mRippleDuration = rippleDuration;
    return this;
  }

  private final Runnable mCircleOneRunnable = new Runnable() {
    @Override
    public void run() {
      mGroundOverlays[0] = mAMap.addGroundOverlay(new GroundOverlayOptions()
          .position(LatLngConvert.convert2AMapLatLng(mLatLng), (int) mDistance)
          .transparency(mTransparency)
          .image(mBackgroundImageDescriptor));
      startAnimation(0);
    }
  };

  private final Runnable mCircleTwoRunnable = new Runnable() {
    @Override
    public void run() {
      mGroundOverlays[1] = mAMap.addGroundOverlay(new GroundOverlayOptions()
          .position(LatLngConvert.convert2AMapLatLng(mLatLng), (int) mDistance)
          .transparency(mTransparency)
          .image(mBackgroundImageDescriptor));
      startAnimation(1);
    }
  };

  private final Runnable mCircleThreeRunnable = new Runnable() {
    @Override
    public void run() {
      mGroundOverlays[2] = mAMap.addGroundOverlay(new GroundOverlayOptions()
          .position(LatLngConvert.convert2AMapLatLng(mLatLng), (int) mDistance)
          .transparency(mTransparency)
          .image(mBackgroundImageDescriptor));
      startAnimation(2);
    }
  };

  private final Runnable mCircleFourRunnable = new Runnable() {
    @Override
    public void run() {
      mGroundOverlays[3] = mAMap.addGroundOverlay(new GroundOverlayOptions()
          .position(LatLngConvert.convert2AMapLatLng(mLatLng), (int) mDistance)
          .transparency(mTransparency)
          .image(mBackgroundImageDescriptor));
      startAnimation(3);
    }
  };

  private void startAnimation(final int numberOfRipple) {
    ValueAnimator animator = ValueAnimator.ofInt(0, (int) mDistance);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.RESTART);
    animator.setDuration(mRippleDuration);
    animator.setEvaluator(new IntEvaluator());
    animator.setInterpolator(new LinearInterpolator());
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator valueAnimator) {
        int animated = (int) valueAnimator.getAnimatedValue();
        mGroundOverlays[numberOfRipple].setDimensions(animated);
        if (mDistance - animated <= 10) {
          if (mLatLng != mPrevLatLng) {
            mGroundOverlays[numberOfRipple].setPosition(LatLngConvert.convert2AMapLatLng(mLatLng));
          }
        }
      }
    });
    animator.start();
    mAnimators[numberOfRipple] = animator;
  }

  private void setDrawableAndBitmap() {
    mBackground.setColor(mFillColor);
    mBackground.setStroke(AMapUtil.dpToPx(mStrokeWidth), mStrokeColor);
    mBackgroundImageDescriptor = AMapUtil.drawableToBitmapDescriptor(mBackground);
  }

  /**
   * Stops current animation if it running
   */
  public void stopRippleMapAnimation() {
    if (isAnimationRunning) {
      try {
        for (int i = 0; i < mNumberOfRipples; i++) {
          if (i == 0) {
            mHandlers[i].removeCallbacks(mCircleOneRunnable);
            mAnimators[i].cancel();
            mGroundOverlays[i].remove();
          }
          if (i == 1) {
            mHandlers[i].removeCallbacks(mCircleTwoRunnable);
            mAnimators[i].cancel();
            mGroundOverlays[i].remove();
          }
          if (i == 2) {
            mHandlers[i].removeCallbacks(mCircleThreeRunnable);
            mAnimators[i].cancel();
            mGroundOverlays[i].remove();
          }
          if (i == 3) {
            mHandlers[i].removeCallbacks(mCircleFourRunnable);
            mAnimators[i].cancel();
            mGroundOverlays[i].remove();
          }
        }
      } catch (Exception e) {
        //no need to handle it
      }
    }
    isAnimationRunning = false;
  }

  /**
   * Starts animations
   */
  public void startRippleMapAnimation() {
    if (!isAnimationRunning) {
      setDrawableAndBitmap();
      for (int i = 0; i < mNumberOfRipples; i++) {
        if (i == 0) {
          mHandlers[i] = new Handler();
          mHandlers[i].postDelayed(mCircleOneRunnable, mDurationBetweenTwoRipples * i);
        }
        if (i == 1) {
          mHandlers[i] = new Handler();
          mHandlers[i].postDelayed(mCircleTwoRunnable, mDurationBetweenTwoRipples * i);
        }
        if (i == 2) {
          mHandlers[i] = new Handler();
          mHandlers[i].postDelayed(mCircleThreeRunnable, mDurationBetweenTwoRipples * i);
        }
        if (i == 3) {
          mHandlers[i] = new Handler();
          mHandlers[i].postDelayed(mCircleFourRunnable, mDurationBetweenTwoRipples * i);
        }
      }
    }
    isAnimationRunning = true;
  }
}
