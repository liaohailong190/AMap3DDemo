package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 着色器
 * Created by WX on 2018/4/26.
 */

class MapShader {
    int aVertex, aMVPMatrix, aColor, vCoordinate;
    private int program;

    MapShader(Context context, String vsPath, String fsPath) {
        try {
            vsPath = "shader" + File.separator + vsPath;
            fsPath = "shader" + File.separator + fsPath;
            InputStream vsIn = context.getAssets().open(vsPath);
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = vsIn.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String vsShader = result.toString("UTF-8");
            result.close();
            vsIn.close();
            Log.i("vsShader", vsShader);
            InputStream fsIn = context.getAssets().open(fsPath);
            ByteArrayOutputStream resultFs = new ByteArrayOutputStream();
            while ((length = fsIn.read(buffer)) != -1) {
                resultFs.write(buffer, 0, length);
            }
            String fsShader = resultFs.toString("UTF-8");
            resultFs.close();
            fsIn.close();
            Log.i("vsShader", fsShader);
            create(vsShader, fsShader);
        } catch (IOException e) {
            Log.i("vsShader", "yichang");
            e.printStackTrace();
        }
    }

    void useProgram() {
        GLES20.glUseProgram(program);
    }

    private void create(String vertexShader, String fragmentShader) {
        int vertexLocation = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fragmentLocation = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        GLES20.glShaderSource(vertexLocation, vertexShader);
        GLES20.glCompileShader(vertexLocation);

        GLES20.glShaderSource(fragmentLocation, fragmentShader);
        GLES20.glCompileShader(fragmentLocation);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexLocation);
        GLES20.glAttachShader(program, fragmentLocation);
        GLES20.glLinkProgram(program);
        aVertex = GLES20.glGetAttribLocation(program, "aVertex");
        vCoordinate = GLES20.glGetAttribLocation(program, "vCoordinate");
        aMVPMatrix = GLES20.glGetUniformLocation(program, "aMVPMatrix");
        aColor = GLES20.glGetUniformLocation(program, "aColor");
    }

}
