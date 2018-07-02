package com.amap.map3d.demo.airline;

import android.graphics.Bitmap;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.map3d.demo.util.AMapUtil;

import java.util.List;

/**
 * Describe as : 导航器
 * Created by LHL on 2018/4/28.
 */

class AirNavigator {
    private final AMap aMap;
    private final List<LatLng> mTotalAirLine;
    private final Marker mMarker;
    private int mTempIndex = 0;

    AirNavigator(AMap aMap, Bitmap bitmap, List<LatLng> totalAirLine) {
        this.aMap = aMap;
        mTotalAirLine = totalAirLine;
        MarkerOptions markerOptions = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        mMarker = aMap.addMarker(markerOptions);
    }


    void moveToNext() {
        int footIndex = mTempIndex += 4;
        int headIndex = footIndex + 1;
        //飞到尽头了
        if (headIndex >= mTotalAirLine.size() - 1) {
            headIndex = mTotalAirLine.size() - 1;
            footIndex = headIndex - 1;
            mTempIndex = 0;
        }
        LatLng footLatLng = mTotalAirLine.get(footIndex);
        LatLng headLatLng = mTotalAirLine.get(headIndex);
        float rotate = AMapUtil.getRotate(footLatLng, headLatLng);
        float rotateAngle = AMapUtil.getRotateAngle(rotate, aMap);
        mMarker.setPosition(headLatLng);
        mMarker.setRotateAngle(rotateAngle);
    }

}
