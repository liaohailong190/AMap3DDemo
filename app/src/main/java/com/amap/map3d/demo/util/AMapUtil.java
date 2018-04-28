/**
 *
 */
package com.amap.map3d.demo.util;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteRailwayItem;
import com.amap.map3d.demo.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AMapUtil {
    /**
     * 判断edittext是否null
     */
    public static String checkEditText(EditText editText) {
        if (editText != null && editText.getText() != null
                && !(editText.getText().toString().trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append("<font color=").append(color).append(">").append(src)
                .append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        final String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append(space);
        }
        return result.toString();
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > 10000) // 10 km
        {
            int dis = lenMeter / 1000;
            return dis + ChString.Kilometer;
        }

        if (lenMeter > 1000) {
            float dis = (float) lenMeter / 1000;
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String dstr = fnum.format(dis);
            return dstr + ChString.Kilometer;
        }

        if (lenMeter > 100) {
            int dis = lenMeter / 50 * 50;
            return dis + ChString.Meter;
        }

        int dis = lenMeter / 10 * 10;
        if (dis == 0) {
            dis = 10;
        }

        return dis + ChString.Meter;
    }

    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AMapUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    /**
     * long类型时间格式化
     */
    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";

    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int miniate = (second % 3600) / 60;
            return hour + "小时" + miniate + "分钟";
        }
        if (second >= 60) {
            int miniate = second / 60;
            return miniate + "分钟";
        }
        return second + "秒";
    }

    //路径规划方向指示和图片对应
    public static int getDriveActionID(String actionName) {
        if (actionName == null || actionName.equals("")) {
            return R.drawable.dir3;
        }
        if ("左转".equals(actionName)) {
            return R.drawable.dir2;
        }
        if ("右转".equals(actionName)) {
            return R.drawable.dir1;
        }
        if ("向左前方行驶".equals(actionName) || "靠左".equals(actionName)) {
            return R.drawable.dir6;
        }
        if ("向右前方行驶".equals(actionName) || "靠右".equals(actionName)) {
            return R.drawable.dir5;
        }
        if ("向左后方行驶".equals(actionName) || "左转调头".equals(actionName)) {
            return R.drawable.dir7;
        }
        if ("向右后方行驶".equals(actionName)) {
            return R.drawable.dir8;
        }
        if ("直行".equals(actionName)) {
            return R.drawable.dir3;
        }
        if ("减速行驶".equals(actionName)) {
            return R.drawable.dir4;
        }
        return R.drawable.dir3;
    }

    public static int getWalkActionID(String actionName) {
        if (actionName == null || actionName.equals("")) {
            return R.drawable.dir13;
        }
        if ("左转".equals(actionName)) {
            return R.drawable.dir2;
        }
        if ("右转".equals(actionName)) {
            return R.drawable.dir1;
        }
        if ("向左前方".equals(actionName) || "靠左".equals(actionName) || actionName.contains("向左前方")) {
            return R.drawable.dir6;
        }
        if ("向右前方".equals(actionName) || "靠右".equals(actionName) || actionName.contains("向右前方")) {
            return R.drawable.dir5;
        }
        if ("向左后方".equals(actionName) || actionName.contains("向左后方")) {
            return R.drawable.dir7;
        }
        if ("向右后方".equals(actionName) || actionName.contains("向右后方")) {
            return R.drawable.dir8;
        }
        if ("直行".equals(actionName)) {
            return R.drawable.dir3;
        }
        if ("通过人行横道".equals(actionName)) {
            return R.drawable.dir9;
        }
        if ("通过过街天桥".equals(actionName)) {
            return R.drawable.dir11;
        }
        if ("通过地下通道".equals(actionName)) {
            return R.drawable.dir10;
        }

        return R.drawable.dir13;
    }

    public static String getBusPathTitle(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        List<BusStep> busSetps = busPath.getSteps();
        if (busSetps == null) {
            return String.valueOf("");
        }
        StringBuffer sb = new StringBuffer();
        for (BusStep busStep : busSetps) {
            StringBuffer title = new StringBuffer();
            if (busStep.getBusLines().size() > 0) {
                for (RouteBusLineItem busline : busStep.getBusLines()) {
                    if (busline == null) {
                        continue;
                    }

                    String buslineName = getSimpleBusLineName(busline.getBusLineName());
                    title.append(buslineName);
                    title.append(" / ");
                }
//					RouteBusLineItem busline = busStep.getBusLines().get(0);

                sb.append(title.substring(0, title.length() - 3));
                sb.append(" > ");
            }
            if (busStep.getRailway() != null) {
                RouteRailwayItem railway = busStep.getRailway();
                sb.append(railway.getTrip() + "(" + railway.getDeparturestop().getName()
                        + " - " + railway.getArrivalstop().getName() + ")");
                sb.append(" > ");
            }
        }
        return sb.substring(0, sb.length() - 3);
    }

    public static String getBusPathDes(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        long second = busPath.getDuration();
        String time = getFriendlyTime((int) second);
        float subDistance = busPath.getDistance();
        String subDis = getFriendlyLength((int) subDistance);
        float walkDistance = busPath.getWalkDistance();
        String walkDis = getFriendlyLength((int) walkDistance);
        return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
    }

    public static String getSimpleBusLineName(String busLineName) {
        if (busLineName == null) {
            return String.valueOf("");
        }
        return busLineName.replaceAll("\\(.*?\\)", "");
    }


    public static float getRotate(LatLng startLatLng, LatLng endLatLng) {
        if (startLatLng != null && endLatLng != null) {
            double sLat = startLatLng.latitude;
            double eLat = endLatLng.latitude;
            double sLng = startLatLng.longitude;
            double eLng = endLatLng.longitude;
            return (float) (Math.atan2(eLng - sLng, eLat - sLat) / 3.141592653589793D * 180.0D);
        } else {
            return 0.0F;
        }
    }

    public static float getRotateAngle(float rotate, AMap aMap) {
        return 360.f - rotate + aMap.getCameraPosition().bearing;
    }

    public static List<LatLng> getGeodesicPath(LatLng sLatLng, LatLng eLatLng, int pointsNum) {
        ArrayList<LatLng> latLngList = new ArrayList<>();
        latLngList.add(sLatLng);
        latLngList.addAll(getGeodesicPoints(sLatLng, eLatLng, pointsNum));
        latLngList.add(eLatLng);
        return latLngList;
    }

    private static List<LatLng> getGeodesicPoints(LatLng start, LatLng end, int pointsNum) {
        pointsNum = pointsNum > 30 ? pointsNum : 30;
        long segments = Math.round(Math.abs(start.longitude - end.longitude));
        if (pointsNum + 1 > segments) {
            segments = pointsNum;
        }
        if (segments <= 0 || Math.abs(start.longitude - end.longitude) < 0.001) {
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
            f = divide(1, segments) * n;
            A = Math.sin((1 - f) * d) / Math.sin(d);
            B = Math.sin(f * d) / Math.sin(d);
            x = A * Math.cos(lat1) * Math.cos(lon1) + B * Math.cos(lat2) * Math.cos(lon2);
            y = A * Math.cos(lat1) * Math.sin(lon1) + B * Math.cos(lat2) * Math.sin(lon2);
            z = A * Math.sin(lat1) + B * Math.sin(lat2);
            double lat = Math.atan2(z, Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
            double lon = Math.atan2(y, x);
            itpLngLats.add(new LatLng(lat * r2d, lon * r2d));
        }
        return itpLngLats;
    }

    private static double divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 18, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    public static List<LatLng> generatePointFByPath(LatLng startPointF, LatLng endPoint, int pointsNum) {
        ArrayList<LatLng> pointsLists = new ArrayList<>();
        if (startPointF.longitude == endPoint.longitude) {
            return pointsLists;
        }
        float k = (float) (endPoint.longitude - startPointF.longitude) / (float) (endPoint.latitude - startPointF.latitude);
        PointF aver = new PointF((float) (endPoint.latitude - startPointF.latitude) / 2.0f, (float) (endPoint.longitude - startPointF.longitude) / 2.0f);

        float endResultY = (float) (endPoint.longitude - startPointF.longitude) / 2 + 900000;
        float startResultX = (endResultY - aver.y) * (-k) + aver.x;
        PointF thirdPointF = new PointF(startResultX, endResultY);
        Path path = new Path();
        path.reset();
        path.cubicTo((float) startPointF.latitude, (float) startPointF.longitude, thirdPointF.x, thirdPointF.y, (float) endPoint.latitude, (float) endPoint.longitude);
        PathMeasure pm = new PathMeasure(path, false);
        float[] re = new float[2];
        double length = Math.sqrt((endPoint.latitude - startPointF.latitude) * (endPoint.latitude - startPointF.latitude) + (endPoint.longitude - startPointF.longitude) * (endPoint.longitude - startPointF.longitude));

        Log.i("length", length + "");

        for (int i = 0; i < pointsNum; i++) {
            pm.getPosTan(i / 200.0f * (float) length, re, null);
            pointsLists.add(new LatLng(re[0], re[1]));
        }
        return pointsLists;
    }

}
