package com.amap.map3d.demo.pulse;

import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;

/**
 * Describe as : 脉冲光波
 * Created by LHL on 2018/4/28.
 */

class PulesLight {
    private final int red;
    private final int green;
    private final int blue;

    private final double maxRadius;

    private Circle mPulse1;//一号脉冲--->大圈
    private Circle mPulse2;//二号脉冲--->中圈
    private Circle mPulse3;//三号脉冲--->小圈

    private int mProgress1 = 0;//当前进度
    private int mProgress2 = 0;//当前进度
    private int mProgress3 = 0;//当前进度

    PulesLight(AMap aMap, LatLng center, double maxRadius, int fillColor) {
        //解析颜色值
        this.red = Color.red(fillColor);
        this.green = Color.green(fillColor);
        this.blue = Color.blue(fillColor);
        int endColor = Color.argb(0, red, green, blue);
        //最大半径
        this.maxRadius = maxRadius;
        //添加脉冲
        CircleOptions circleOptions = new CircleOptions()
                .center(center)
                .radius(maxRadius)
                .fillColor(endColor)
                .zIndex(2.0f)
                .strokeColor(Color.TRANSPARENT);
        mPulse1 = aMap.addCircle(circleOptions);
        mPulse2 = aMap.addCircle(circleOptions);
        mPulse3 = aMap.addCircle(circleOptions);
        //初始化延迟进度
        mProgress1 = 0;
        mProgress2 = 25;
        mProgress3 = 50;
    }

    void moveToNext() {

        if (mProgress1 > 100) {
            mProgress1 = 0;
        }
        if (mProgress2 > 100) {
            mProgress2 = 0;
        }
        if (mProgress3 > 100) {
            mProgress3 = 0;
        }

        move(mProgress1, mPulse1);
        move(mProgress2, mPulse2);
        move(mProgress3, mPulse3);

        mProgress1++;
        mProgress2++;
        mProgress3++;
    }

    private void move(int progress, Circle pulse) {
        float factor = progress / 100f;
        int fillColor = getFillColor(factor);
        double radius = getRadius(factor);

        pulse.setFillColor(fillColor);
        pulse.setRadius(radius);
    }


    private int getFillColor(float progress) {
        float percent = 1 - progress;
        percent = percent < 0.0f ? 0.0f : percent;
        percent = percent > 1.0f ? 1.0f : percent;
        int alpha = (int) Math.floor(160 * percent);
        return Color.argb(alpha, red, green, blue);
    }

    private double getRadius(float progress) {
        float percent = progress;
        percent = percent < 0f ? 0 : percent;
        percent = percent > 1.0f ? 1.0f : percent;
        double radius = Math.floor(maxRadius * percent);
        Log.i("TAG", "radius = " + radius + " progress = " + progress + "   percent = " + percent);
        return radius;
    }

    void release() {
        if (mPulse1 != null) {
            mPulse1.remove();
            mPulse1 = null;
        }
        if (mPulse2 != null) {
            mPulse2.remove();
            mPulse2 = null;
        }
        if (mPulse3 != null) {
            mPulse3.remove();
            mPulse3 = null;
        }
    }
}
