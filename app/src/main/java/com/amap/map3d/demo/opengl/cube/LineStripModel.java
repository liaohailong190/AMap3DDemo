package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * 线模型
 * Created by WZ on 2018/4/26.
 */

public class LineStripModel extends Model {

    //private Vector<Float> vertex = new Vector<Float>(); //存放数据容器
    private float[] vertex = null; //存放数据容器
    private final int[] vbo_ = new int[1];
    private LatLng fromLat, endLat;

    private LineNavigatorModel linePlaneModel;

    LineStripModel(Context context, AMap aMap_, LatLng fromLat, LatLng endLat) {
        super(context, aMap_, 1);
        this.number = (int) (AMapUtils.calculateLineDistance(fromLat, endLat) / 3000);
        vertex = new float[number * 3];
        this.fromLat = fromLat;
        this.endLat = endLat;
        shader = new MapShader(context, "vsvertex.shader", "fsvertex.shader");
        initJavaData();
        initBuffer();
    }

    @Override
    public void initJavaData() {
        //   List<LatLng> lists = PathSimplifier.generatePointFByPath(fromLat,endLat,number);
        List<LatLng> lists = PathSimplifier.getGeodesicPath(fromLat, endLat, number);
        for (int i = 0; i < number; i++) {
            PointF pointF_ = aMap.getProjection().toOpenGLLocation(lists.get(i));
            vertex[i * 3] = pointF_.x;
            vertex[i * 3 + 1] = pointF_.y;
            vertex[i * 3 + 2] = 0.0f;
        }
        linePlaneModel = new LineNavigatorModel(context, aMap, number, lists);
    }


    @Override
    public void initBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer vertexBuffer = byteBuffer.asFloatBuffer();

        //向GPU申请顶点坐标数据区域
        vertexBuffer.put(vertex);
        vertexBuffer.position(0);
        GLES20.glGenBuffers(vbo_.length, vbo_, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        vertexBuffer.clear();
    }

    @Override
    public void Draw(float[] mvp) {
        shader.useProgram();
        Matrix.setIdentityM(mvp, 0);
        //偏移
        PointF pointF = aMap.getProjection().toOpenGLLocation(fromLat);
        Matrix.multiplyMM(mvp, 0, aMap.getProjectionMatrix(), 0, aMap.getViewMatrix(), 0);
        Matrix.translateM(mvp, 0, pointF.x, pointF.y, 0);
        float scale = 1.0f;
        Matrix.scaleM(mvp, 0, scale, scale, scale);

        GLES20.glEnableVertexAttribArray(shader.aVertex);
        GLES20.glUniform4f(shader.aColor,1.0f,1.0f,0.0f,1.0f);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo_[0]);
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);
        GLES20.glLineWidth(2.5f);

//        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, number);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, number);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glDisableVertexAttribArray(shader.aVertex);

        linePlaneModel.Draw(mvp);
    }
}
