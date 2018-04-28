package com.amap.map3d.demo;

import android.graphics.Color;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe as : 常量
 * Created by LHL on 2018/4/28.
 */

public class C {
    public static final LatLng lanZhou = new LatLng(36.0613800000, 103.8341700000);
    public static final LatLng shangHai = new LatLng(31.2303700000, 121.4737000000);

    public static final LatLng danDong = new LatLng(40.4242, 124.541);
    public static final LatLng foShan = new LatLng(23.1097, 112.8955);

    public static final LatLng changSha = new LatLng(28.2568, 113.0823);
    public static final LatLng laSa = new LatLng(30.1465, 91.1865);

    public static final LatLng wuLuMuQi = new LatLng(43.8266300000, 87.6168800000);
    public static final LatLng kunMing = new LatLng(24.8796600000, 102.8332200000);

    public static final LatLng heiLongJiang = new LatLng(53.3374334371, 123.8159179688);
    public static final LatLng xiZang = new LatLng(32.1477110660, 78.6566162109);

    public static final LatLng beiJing = new LatLng(39.9046900000, 116.4071700000);

    public static final List<LatLng> mLatLngList = new ArrayList<>();

    static {
        mLatLngList.add(lanZhou);
        mLatLngList.add(shangHai);
        mLatLngList.add(danDong);
        mLatLngList.add(foShan);
        mLatLngList.add(changSha);
        mLatLngList.add(laSa);
        mLatLngList.add(wuLuMuQi);
        mLatLngList.add(kunMing);
        mLatLngList.add(heiLongJiang);
        mLatLngList.add(xiZang);
        mLatLngList.add(beiJing);
    }

    public static final List<Integer> mColors = new ArrayList<>();

    static {
        mColors.add(Color.argb(255, 138, 12, 51));
        mColors.add(Color.argb(255, 128, 240, 12));
        mColors.add(Color.argb(255, 12, 128, 192));
        mColors.add(Color.argb(255, 255, 128, 128));
        mColors.add(Color.argb(255, 128, 12, 88));
    }
}
