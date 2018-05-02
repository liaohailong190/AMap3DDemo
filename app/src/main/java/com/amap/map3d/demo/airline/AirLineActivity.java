package com.amap.map3d.demo.airline;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.map3d.demo.C;
import com.amap.map3d.demo.R;
import com.amap.map3d.demo.util.AMapUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Describe as : 航线界面
 * Created by LHL on 2018/4/28.
 */

public class AirLineActivity extends Activity {

    private MapView mMapView;
    private ExecutorService mExecutorService;
    private AMap aMap;
    private Bitmap planBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulse);
        stop = false;
        mExecutorService = Executors.newSingleThreadExecutor();

        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();

        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(C.lanZhou, 3.55f));

        planBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plane);

        postAirLine(C.lanZhou, C.shangHai);
        postAirLine(C.danDong, C.foShan);
        postAirLine(C.changSha, C.laSa);
        postAirLine(C.wuLuMuQi, C.kunMing);
        postAirLine(C.heiLongJiang, C.xiZang);
        postAirLine(C.beiJing, C.wuLuMuQi);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                executeAirLine();
            }
        }, 2000);
    }

    private static final long FPS = 1000 / 30;
    private static boolean stop = false;

    private List<AirLineRunnable> mAirLineRunnable = new LinkedList<>();

    private void postAirLine(LatLng from, LatLng to) {
        mAirLineRunnable.add(new AirLineRunnable(from, to));
    }

    private void executeAirLine() {
        if (mAirLineRunnable.isEmpty()) {
            return;
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (stop) {
                        return;
                    }
                    for (AirLineRunnable airLineRunnable : mAirLineRunnable) {
                        airLineRunnable.run();
                    }
                    try {
                        Thread.sleep(FPS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private final class AirLineRunnable implements Runnable {

        private List<AirLine> airLines;
        private AirNavigator airNavigator;

        AirLineRunnable(LatLng from, LatLng to) {
            //生成曲线路径
            int count = (int) (AMapUtils.calculateLineDistance(from, to) / 6000);
            int temp = count % 10;
            count -= temp;
            //生成分段数量
            int section = count / 10;
            //生成模拟轨迹
            List<LatLng> path = AMapUtil.getBezierPathLatLng(from, to, count);

            airLines = new ArrayList<>();
            int startIndex;
            int length = count / section;
            int skipCount = length / 2;
            for (int i = 0; i < section; i++) {
                startIndex = length * i;
                int endIndex = startIndex + length - skipCount;
                if (startIndex >= path.size() - 1) {
                    continue;
                }
                AirLine airLine = new AirLine(aMap, path, startIndex, endIndex);
                airLines.add(airLine);
            }

            airNavigator = new AirNavigator(aMap, planBitmap, path);
        }

        @Override
        public void run() {
            for (AirLine airLine : airLines) {
                airLine.moveToNext();
            }
            airNavigator.moveToNext();
        }
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
        stop = true;
        mMapView.onDestroy();
    }
}
