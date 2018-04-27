package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.map3d.demo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Cube {


    private LatLng start = new LatLng(39.90403, 116.407525);// 北京市经纬度
    private LatLng guangzhou = new LatLng(23.1290800000, 113.2643600000); //广州
    private LatLng nanning = new LatLng(22.8167300000, 108.3669000000); //南宁
    private LatLng shagnhai = new LatLng(31.2303700000, 121.4737000000); //上海
    private LatLng haerbin = new LatLng(45.8021600000, 126.5358000000); //haerbin

    private LatLng haerbin1 = new LatLng(30.2211018525, 94.4824218750); //haerbin


    private LatLng end = new LatLng(43.79, 87.62); //新疆经纬度
    //  private LatLng end = new LatLng(43.79,87.62); //新疆经纬度
    List<LineStripModel> array = new ArrayList<LineStripModel>();

    Cube(Context context, AMap aMap) {

        array.add(new LineStripModel(context, aMap, start, end));
        array.add(new LineStripModel(context, aMap, start, guangzhou));
        array.add(new LineStripModel(context, aMap, start, nanning));
        array.add(new LineStripModel(context, aMap, start, shagnhai));
        array.add(new LineStripModel(context, aMap, start, haerbin));
        array.add(new LineStripModel(context, aMap, start, haerbin1));
    }

    void drawES20(float[] mvp) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BITS);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        for (LineStripModel ls : array) {
            ls.Draw(mvp);
        }
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }
}
