package com.amap.map3d.demo.util;

import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fj on 2017/11/30.
 */

public class PathSimplifier {

    public static List<LatLng> getGeodesicPath(LatLng sLatLng,LatLng eLatLng,int pointsNum){
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(sLatLng);
        latLngs.addAll(getGeodesicPoints(sLatLng,eLatLng,pointsNum));
        latLngs.add(eLatLng);
        return latLngs;
    }

    private static List<LatLng> getGeodesicPoints(LatLng start, LatLng end, int pointsNum){
        pointsNum = pointsNum>30?pointsNum:30;
        long segments = Math.round(Math.abs(start.longitude - end.longitude));
        if (pointsNum+1 > segments){
            segments = pointsNum;
        }
        if (segments <=0 || Math.abs(start.longitude - end.longitude) < 0.001){
            return null;
        }
        int n;
        double f, A, B, x, y, z;
        ArrayList<LatLng> itpLngLats = new ArrayList<>();
        double PI = Math.PI, d2r = PI / 180, r2d = 180 / PI;
        double lat1 = start.latitude * d2r,
                lon1 = start.longitude * d2r,
                lat2 = end.latitude * d2r,
                lon2 = end.longitude * d2r,
                d = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin((lat1 - lat2) / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin((lon1 - lon2) / 2), 2)));
        for (n = 1; n < segments; n += 1) {
            f = divide(1,segments) * n;
            A = Math.sin((1 - f) * d) / Math.sin(d);
            B = Math.sin(f * d) / Math.sin(d);
            x = A * Math.cos(lat1) * Math.cos(lon1) + B * Math.cos(lat2) * Math.cos(lon2);
            y = A * Math.cos(lat1) * Math.sin(lon1) + B * Math.cos(lat2) * Math.sin(lon2);
            z = A * Math.sin(lat1) + B * Math.sin(lat2);
            double lat = Math.atan2(z, Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
            double lon = Math.atan2(y, x);
            itpLngLats.add(new LatLng(lat * r2d,lon * r2d));
        }
        return itpLngLats;
    }

    public static double divide(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 18, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }
}
