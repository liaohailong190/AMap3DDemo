package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.map3d.demo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

class Cube {

    private static final LatLng from = new LatLng(36.0613800000, 103.8341700000);//兰州
    private static final LatLng to = new LatLng(31.2303700000, 121.4737000000);//上海

    private AMap aMap;

    private float[] vertexPath;
    private final FloatBuffer mVertexBuffer;

    private float[] navigatorBuffer;
    private final FloatBuffer mNavigatorBuffer;

    private final MapShader mLineShader;//路线
    private final MapShader mNavigatorShader;//导航器

    private final List<LatLng> mGeodesicPath;
    private final Bitmap mBitmap;
    private final int mTextureId;
    //https://blog.csdn.net/junzia/article/details/52842816 绘制纹理

    private int index = 0;

    Cube(Context context, AMap aMap) {
        this.aMap = aMap;
        mLineShader = new MapShader(context, "vsvertex.shader", "fsvertex.shader");
        mNavigatorShader = new MapShader(context, "vsplanevertex.shader", "fsplanevertex.shader");

        //生成曲线路径
        mGeodesicPath = PathSimplifier.getGeodesicPath(from, to, 300);

        //生成曲线路径顶点坐标
        vertexPath = new float[mGeodesicPath.size() * 3];
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertexPath.length * 4);
        mVertexBuffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(vertexPath).position(0);

        //生成导航器顶点坐标和纹理坐标
        navigatorBuffer = new float[5 * 3];
        byteBuffer = ByteBuffer.allocateDirect(navigatorBuffer.length * 4);
        mNavigatorBuffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNavigatorBuffer.put(navigatorBuffer).position(0);

        //生成导航器图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane);
        mTextureId = createTexture();
    }

    /**
     * 根据bitmapC创建纹理
     */
    private int createTexture() {
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            GLES20.glGenTextures(1, texture, 0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            mBitmap.recycle();
            return texture[0];
        }
        return 0;
    }

    void drawES20(float[] mvp) {
        mLineShader.useProgram();

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        int count = mGeodesicPath.size() - 1;
        for (int i = 0; i < count; i++) {
            LatLng from = mGeodesicPath.get(i);
            LatLng to = mGeodesicPath.get(i + 1);

            PointF fromPointF = aMap.getProjection().toOpenGLLocation(from);
            PointF toPointF = aMap.getProjection().toOpenGLLocation(to);

            vertexPath[i * 3] = fromPointF.x;
            vertexPath[i * 3 + 1] = fromPointF.y;
            vertexPath[i * 3 + 2] = 0.0f;

            vertexPath[i * 3] = toPointF.x;
            vertexPath[i * 3 + 4] = toPointF.y;
            vertexPath[i * 3 + 5] = 0.0f;
        }

        mVertexBuffer.clear();
        mVertexBuffer.put(vertexPath).position(0);

        GLES20.glEnableVertexAttribArray(mLineShader.aVertex);
        GLES20.glVertexAttribPointer(mLineShader.aVertex, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
        GLES20.glUniformMatrix4fv(mLineShader.aMVPMatrix, 1, false, mvp, 0);
        GLES20.glUniform4f(mLineShader.aColor, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glLineWidth(2.5f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, count);

        //绘制导航器
        drawNavigator(mvp);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }


    private void drawNavigator(float[] mvp) {
        mNavigatorShader.useProgram();

        if (index > mGeodesicPath.size() - 3) {
            index = 0;
        } else {
            index++;
        }
        LatLng head = mGeodesicPath.get(index + 1);
        LatLng foot = mGeodesicPath.get(index);

        PointF headPointF = aMap.getProjection().toOpenGLLocation(head);
        PointF footPointF = aMap.getProjection().toOpenGLLocation(foot);

        float centerX = (headPointF.x - footPointF.x) / 2f;
        float centerY = (headPointF.y - footPointF.y) / 2f;
        float radius = 2000000;

        navigatorBuffer[0] = centerX - radius * 2f;
        navigatorBuffer[1] = centerY;
        navigatorBuffer[2] = 0.0f;

        navigatorBuffer[3] = 0.0f;
        navigatorBuffer[4] = 1.0f;

        navigatorBuffer[5] = centerX;
        navigatorBuffer[6] = centerY + radius;
        navigatorBuffer[7] = 0.0f;

        navigatorBuffer[8] = 0.5f;
        navigatorBuffer[9] = 0.0f;

        navigatorBuffer[10] = centerX;
        navigatorBuffer[11] = centerY - radius;
        navigatorBuffer[12] = 0.0f;

        navigatorBuffer[13] = 1.0f;
        navigatorBuffer[14] = 1.0f;

        //传入导航器顶点坐标
        mNavigatorBuffer.clear();
        mNavigatorBuffer.put(navigatorBuffer).position(0);

        //绘制导航器
        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glEnableVertexAttribArray(mNavigatorShader.aVertex);
        GLES20.glEnableVertexAttribArray(mNavigatorShader.vCoordinate);
        GLES20.glVertexAttribPointer(mNavigatorShader.aVertex, 3, GLES20.GL_FLOAT, false, 0, mNavigatorBuffer);
        GLES20.glVertexAttribPointer(mNavigatorShader.vCoordinate, 2, GLES20.GL_FLOAT, false, 0, mNavigatorBuffer);
        GLES20.glUniformMatrix4fv(mNavigatorShader.aMVPMatrix, 1, false, mvp, 0);
        GLES20.glUniform4f(mNavigatorShader.aColor, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        //绘制完毕后禁止功能
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisableVertexAttribArray(mNavigatorShader.aVertex);
        GLES20.glDisableVertexAttribArray(mNavigatorShader.vCoordinate);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
}
