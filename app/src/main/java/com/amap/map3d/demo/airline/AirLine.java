package com.amap.map3d.demo.airline;

import android.graphics.Color;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.List;

/**
 * Describe as : 航线模型
 * Created by LHL on 2018/4/28.
 */

class AirLine {
    private final List<LatLng> mTotalAirLine;
    private int mStartIndex = 0;
    private int mEndIndex = 0;
    private int mLength = 0;

    private Polyline mPolyline;

    AirLine(AMap aMap, List<LatLng> totalAirLine, int startIndex, int endIndex) {
        mTotalAirLine = totalAirLine;
        mStartIndex = startIndex;
        mEndIndex = endIndex;
        mLength = endIndex - startIndex;

        mEndIndex = mEndIndex >= totalAirLine.size() - 1 ? totalAirLine.size() - 1 : mEndIndex;
        LatLng footLatLng = totalAirLine.get(mStartIndex);
        LatLng headLatLng = totalAirLine.get(mEndIndex);

        PolylineOptions polylineOptions = new PolylineOptions()
                .width(2.5f)
                .color(Color.BLUE)
                .add(footLatLng)
                .add(headLatLng);
        mPolyline = aMap.addPolyline(polylineOptions);
    }


    void moveToNext() {
        if (mPolyline == null) {
            return;
        }
        int lastIndex = mTotalAirLine.size() - 1;

        mStartIndex++;
        mEndIndex = mStartIndex + mLength;

        mStartIndex = mStartIndex >= lastIndex - 1 ? lastIndex - 1 : mStartIndex;
        mEndIndex = mEndIndex >= lastIndex ? lastIndex : mEndIndex;

        LatLng footLatLng = mTotalAirLine.get(mStartIndex);
        LatLng headLatLng = mTotalAirLine.get(mEndIndex);
        List<LatLng> points = mPolyline.getPoints();
        points.clear();
        points.add(footLatLng);
        points.add(headLatLng);
        mPolyline.setPoints(points);

        //归位重置
        if (mStartIndex >= lastIndex - 1) {
            mStartIndex = 0;
            mEndIndex = mStartIndex + mLength;
        }
    }
}
