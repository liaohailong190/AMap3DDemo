package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.map3d.demo.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * 线上的模型
 * Created by wz on 2018/4/26.
 */

public class LineNavigatorModel extends Model {
    private float[] vertex = null; //存放数据容器
    private int vbo;
    private FloatBuffer vertextBuffer = null;
    private final int[] vbo_ = new int[1];
    private List<LatLng> lists;
    private int i = 0;
    private float rotate = 0.0f;
    private PointF pointF_after = null;

    LineNavigatorModel(Context context, AMap aMap_, int number_, List<LatLng> lists_) {
        super(context, aMap_, number_);
        lists = lists_;
        vertex = new float[5 * 3];
        texture = new MapTexture(context, R.drawable.plane);
        shader = new MapShader(context, "vsplanevertex.shader", "fsplanevertex.shader");
        initJavaData();
        initBuffer();
    }


    @Override
    public void initJavaData() {

        float start_x = aMap.getProjection().toOpenGLLocation(lists.get(0 * 3 + 1)).x;
        float start_y = aMap.getProjection().toOpenGLLocation(lists.get(0 * 3 + 1)).y;
        vertex[0 * 5] = start_x - 500000;
        vertex[0 * 5 + 1] = start_y;
        vertex[0 * 5 + 2] = 0.0f;

        vertex[0 * 5 + 3] = 0.5f;
        vertex[0 * 5 + 4] = 0.0f;


        vertex[1 * 5] = start_x;
        vertex[1 * 5 + 1] = start_y + 200000;
        vertex[1 * 5 + 2] = 0.0f;

        vertex[1 * 5 + 3] = 1.0f;
        vertex[1 * 5 + 4] = 1.0f;

        vertex[2 * 5] = start_x;
        vertex[2 * 5 + 1] = start_y - 200000;
        vertex[2 * 5 + 2] = 0.0f;

        vertex[2 * 5 + 3] = 0.0f;
        vertex[2 * 5 + 4] = 1.0f;
    }

    @Override
    public void initBuffer() {
        if (vertextBuffer == null) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            vertextBuffer = byteBuffer.asFloatBuffer();
        }
        vertextBuffer.clear();
        vertextBuffer.put(vertex);


        vertextBuffer.position(0);

        GLES20.glGenBuffers(vbo_.length, vbo_, 0);
        vbo = vbo_[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertextBuffer.capacity() * 4, vertextBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        vertextBuffer.clear();
    }

    @Override
    public void Draw(float[] mvp) {
        shader.useProgram();

        int fromIndex = i % lists.size();
        int toIndex = (i + 3) % lists.size();
        pointF_after = aMap.getProjection().toOpenGLLocation(lists.get(fromIndex));
        rotate = PathSimplifier.getRotate(lists.get(fromIndex), lists.get(toIndex));

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo);
        GLES20.glVertexAttribPointer(shader.aVertex, 3, GLES20.GL_FLOAT, false, 5 * 4, 0);
        GLES20.glVertexAttribPointer(shader.vCoordinate, 2, GLES20.GL_FLOAT, false, 5 * 4, 3 * 4);
        GLES20.glEnableVertexAttribArray(shader.aVertex);
        GLES20.glEnableVertexAttribArray(shader.vCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.textureId);

        Matrix.setIdentityM(mvp, 0);
        Matrix.multiplyMM(mvp, 0, aMap.getProjectionMatrix(), 0, aMap.getViewMatrix(), 0);
        Matrix.translateM(mvp, 0, pointF_after.x, pointF_after.y, 0);
        Matrix.rotateM(mvp, 0, rotate, 0.0f, 0.0f, 1.0f);
        GLES20.glUniformMatrix4fv(shader.aMVPMatrix, 1, false, mvp, 0);
        GLES20.glLineWidth(1);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glDisableVertexAttribArray(shader.aVertex);
        GLES20.glDisableVertexAttribArray(shader.vCoordinate);
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);

        if (i > number - 3) {
            i = 0;
        } else {
            i += 1;
        }
    }
}
