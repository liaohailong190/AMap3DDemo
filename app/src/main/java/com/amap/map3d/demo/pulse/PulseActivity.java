package com.amap.map3d.demo.pulse;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.map3d.demo.R;

/**
 * Describe as : 脉冲动画界面
 * Created by LHL on 2018/3/28.
 */

public class PulseActivity extends Activity {

    private int fillColor = Color.argb(0, 237, 17, 116);
    private static final double maxRadius = 500d;
    private static final long duration = 4000;
    private Circle mPulse1;//一号脉冲--->大圈
    private Circle mPulse2;//二号脉冲--->中圈
    private Circle mPulse3;//三号脉冲--->小圈
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);

        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        AMap map = mMapView.getMap();
        //定位坐标(湖南省-长沙市-天心区-鑫远微中心)
        LatLng latLng = new LatLng(28.1137500000, 112.9734300000);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
        //添加脉冲
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(maxRadius)
                .fillColor(fillColor)
                .zIndex(2.0f)
                .strokeColor(Color.TRANSPARENT);
        mPulse1 = map.addCircle(circleOptions);
        mPulse2 = map.addCircle(circleOptions);
        mPulse3 = map.addCircle(circleOptions);
        //创建脉冲属性动画
        ValueAnimator pulseAnim1 = ValueAnimator.ofFloat(0, 1f);
        ValueAnimator pulseAnim2 = ValueAnimator.ofFloat(0, 1f);
        ValueAnimator pulseAnim3 = ValueAnimator.ofFloat(0, 1f);
        //脉冲持续时间
        pulseAnim1.setDuration(duration);
        pulseAnim2.setDuration(duration);
        pulseAnim3.setDuration(duration);
        //平缓的插值器
        pulseAnim1.setInterpolator(new LinearInterpolator());
        pulseAnim2.setInterpolator(new LinearInterpolator());
        pulseAnim3.setInterpolator(new LinearInterpolator());
        //无限循环
        pulseAnim1.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnim2.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnim3.setRepeatCount(ValueAnimator.INFINITE);
        //重新开始
        pulseAnim1.setRepeatMode(ValueAnimator.RESTART);
        pulseAnim2.setRepeatMode(ValueAnimator.RESTART);
        pulseAnim3.setRepeatMode(ValueAnimator.RESTART);
        //回调进度，更新脉冲
        pulseAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                int fillColor = getFillColor(animatedValue);
                double radius = getRadius(animatedValue);
                mPulse1.setRadius(radius);
                mPulse1.setFillColor(fillColor);
            }
        });
        pulseAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                int fillColor = getFillColor(animatedValue);
                double radius = getRadius(animatedValue);
                mPulse2.setRadius(radius);
                mPulse2.setFillColor(fillColor);
            }
        });
        pulseAnim3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                int fillColor = getFillColor(animatedValue);
                double radius = getRadius(animatedValue);
                mPulse3.setRadius(radius);
                mPulse3.setFillColor(fillColor);
            }
        });
        //模拟延迟，计划重叠脉冲
        pulseAnim1.setStartDelay(0);
        pulseAnim2.setStartDelay(duration / 3);
        pulseAnim3.setStartDelay(duration * 2 / 3);
        //开启脉冲动画
        pulseAnim1.start();
        pulseAnim2.start();
        pulseAnim3.start();
    }

    private int getFillColor(float progress) {
        float percent = 1 - progress;
        percent = percent < 0f ? 0 : percent;
        percent = percent > 1.0f ? 1.0f : percent;
        int alpha = (int) (160 * percent);
        return Color.argb(alpha, 237, 17, 116);
    }

    private double getRadius(float progress) {
        float percent = progress;
        percent = percent < 0f ? 0 : percent;
        percent = percent > 1.0f ? 1.0f : percent;
        return maxRadius * percent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
