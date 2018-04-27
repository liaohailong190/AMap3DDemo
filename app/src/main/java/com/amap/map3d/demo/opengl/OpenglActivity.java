package com.amap.map3d.demo.opengl;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.map3d.demo.R;
import com.amap.map3d.demo.opengl.cube.Cube;
import com.amap.map3d.demo.opengl.cube.CubeMapRender;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 介绍地图中开放Opengl接口功能
 */
public class OpenglActivity extends Activity {

    private MapView mapView;
    private AMap aMap;
    Handler handler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opengl_activity);

        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
        //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (aMap != null) {
                    aMap.runOnDrawFrame();
                }
                sendEmptyMessageDelayed(1, 16);
            }
        };
        handler.sendEmptyMessageDelayed(1, 500);

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

            aMap.showMapText(false);
            aMap.showBuildings(false);
            aMap.setCustomRenderer(new CubeMapRender(this, aMap));
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void onClickBt(View view) {
        if (aMap != null) {
            aMap.runOnDrawFrame();
        }
    }
}