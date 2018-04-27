package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.Matrix;

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

    private LatLng center = new LatLng(39.90403, 116.407525);// 北京市经纬度

    private Cube cube;

    private AMap aMap;

    private Context context;

    public CubeMapRender(Context context, AMap aMap) {
        this.aMap = aMap;
        this.context = context;

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 4));
    }

    private float[] mvp = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {

        if (cube != null) {
            Matrix.setIdentityM(mvp, 0);
            //偏移
            Matrix.multiplyMM(mvp, 0, aMap.getProjectionMatrix(), 0, aMap.getViewMatrix(), 0);
            Matrix.translateM(mvp, 0, 0, 0, 0);
            int scale = 1;
            Matrix.scaleM(mvp, 0, scale, scale, scale);
            //绘制资源
            cube.drawES20(mvp);
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //创建cube
        cube = new Cube(context, aMap);
    }

    @Override
    public void OnMapReferencechanged() {

    }

}
