package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.Matrix;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.model.LatLng;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zxy94400 on 2016/3/19.
 */
public class CubeMapRender implements CustomRenderer {
    private Context context;
    private LatLng center = new LatLng(39.90403, 116.407525);// 北京市经纬度
    private Cube cube ;
    private AMap aMap;

    public CubeMapRender(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,4));
    }

    float[] mvp = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {

        if(cube != null) {
            cube.drawES20(mvp);
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //创建cube
        cube = new Cube(context,aMap);
    }

    @Override
    public void OnMapReferencechanged() {

    }

}
