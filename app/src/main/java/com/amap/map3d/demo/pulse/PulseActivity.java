package com.amap.map3d.demo.pulse;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.map3d.demo.C;
import com.amap.map3d.demo.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Describe as : 脉冲动画界面
 * Created by LHL on 2018/3/28.
 */

public class PulseActivity extends Activity {

    private static final double maxRadius = 500000D;

    private MapView mMapView;

    private List<PulesLight> mPulesLightList = new LinkedList<>();

    private static final long FPS = 1000 / 30;
    private static boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);
        stop = false;
        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        AMap map = mMapView.getMap();
        //兰州
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(C.lanZhou, 3.55f));

        //生成脉冲光波
        for (int i = 0; i < C.mLatLngList.size(); i++) {
            LatLng center = C.mLatLngList.get(i);
            Integer color = C.mColors.get(i % C.mColors.size());
            PulesLight pulesLight = new PulesLight(map, center, maxRadius, color);
            mPulesLightList.add(pulesLight);
        }
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (stop) {
                        return;
                    }
                    for (PulesLight pulesLight : mPulesLightList) {
                        pulesLight.moveToNext();
                    }
                    try {
                        Thread.sleep(FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
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
        stop = true;
        for (PulesLight pulesLight : mPulesLightList) {
            pulesLight.release();
        }
        mPulesLightList.clear();
    }
}
