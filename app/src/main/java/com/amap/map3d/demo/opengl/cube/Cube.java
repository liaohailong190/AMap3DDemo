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

    private float[] navigatorVertex;
    private FloatBuffer mNavigatorVertexBuffer;
    private FloatBuffer mNavigatorCoordinateBuffer;

    private final MapShader mLineShader;//路线
    private final MapShader mNavigatorShader;//导航器

    private final List<LatLng> mGeodesicPath;
    private final Bitmap mBitmap;
    private final int mTextureId;

    private int index = 0;

    Cube(Context context, AMap aMap) {
        this.aMap = aMap;
        mLineShader = new MapShader(context, "line_vertex.shader", "line_fragment.shader");
        mNavigatorShader = new MapShader(context, "navigator_vertex.shader", "navigator_fragment.shader");

        //生成曲线路径
        mGeodesicPath = PathSimplifier.getGeodesicPath(from, to, 300);

        //生成曲线路径顶点坐标
        vertexPath = new float[mGeodesicPath.size() * 3];
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertexPath.length * 4);
        mVertexBuffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        mVertexBuffer.put(vertexPath).position(0);

        //生成导航器顶点坐标和纹理坐标
        navigatorVertex = new float[4 * 3];
        byteBuffer = ByteBuffer.allocateDirect(navigatorVertex.length * 4);
        mNavigatorVertexBuffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNavigatorVertexBuffer.put(navigatorVertex).position(0);

        float[] navigatorCoordinate = new float[]{
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
        };
        byteBuffer = ByteBuffer.allocateDirect(navigatorCoordinate.length * 4);
        mNavigatorCoordinateBuffer = byteBuffer.order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNavigatorCoordinateBuffer.put(navigatorCoordinate).position(0);

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
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
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

    private float[] nMVP = new float[16];

    private void drawNavigator(float[] mvp) {
        System.arraycopy(mvp, 0, nMVP, 0, nMVP.length);

        mNavigatorShader.useProgram();

        if (index > mGeodesicPath.size() - 3) {
            index = 0;
        } else {
            index++;
        }

        LatLng head = mGeodesicPath.get(index + 1);
//        LatLng foot = mGeodesicPath.get(index);
        PointF headPointF = aMap.getProjection().toOpenGLLocation(head);
//        float rotate = PathSimplifier.getRotate(foot, head);
//        Matrix.rotateM(nMVP, 0, rotate, 0.0f, 0.0f, 0.0f);

        float radius = 400000;
        //左上角
        navigatorVertex[0] = headPointF.x - radius;
        navigatorVertex[1] = headPointF.y + radius;
        navigatorVertex[2] = 0;
        //左下角
        navigatorVertex[3] = headPointF.x - radius;
        navigatorVertex[4] = headPointF.y - radius;
        navigatorVertex[5] = 0;
        //右上角
        navigatorVertex[9] = headPointF.x + radius;
        navigatorVertex[10] = headPointF.y + radius;
        navigatorVertex[11] = 0;
        //右下角
        navigatorVertex[6] = headPointF.x + radius;
        navigatorVertex[7] = headPointF.y - radius;
        navigatorVertex[8] = 0;

        mNavigatorVertexBuffer.clear();
        mNavigatorVertexBuffer.put(navigatorVertex).position(0);
        //绘制导航器
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glEnableVertexAttribArray(mNavigatorShader.aVertex);
        GLES20.glEnableVertexAttribArray(mNavigatorShader.vCoordinate);
        GLES20.glVertexAttribPointer(mNavigatorShader.aVertex, 3, GLES20.GL_FLOAT, false, 0, mNavigatorVertexBuffer);
        GLES20.glVertexAttribPointer(mNavigatorShader.vCoordinate, 2, GLES20.GL_FLOAT, false, 0, mNavigatorCoordinateBuffer);
        GLES20.glUniformMatrix4fv(mNavigatorShader.aMVPMatrix, 1, false, nMVP, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, navigatorVertex.length / 3);
        //绘制完毕后禁止功能
        GLES20.glDisableVertexAttribArray(mNavigatorShader.aVertex);
        GLES20.glDisableVertexAttribArray(mNavigatorShader.vCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
