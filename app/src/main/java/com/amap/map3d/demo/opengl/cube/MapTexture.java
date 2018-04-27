package com.amap.map3d.demo.opengl.cube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Administrator on 2018/4/26.
 */

public class MapTexture {
    public int textureId = 0;
    private Bitmap bitmap;

    public MapTexture(Context context,int sourceId){
        if(context != null)
            bitmap = BitmapFactory.decodeResource(context.getResources(),sourceId);
        textureId = createTexture();
    }

    public MapTexture(Context context,Bitmap bitmap){
        textureId = createTexture();
    }

    /**
     * 根据bitmapC创建纹理
     * @return
     */
    private int createTexture(){
        int[] texture=new int[1];
        if(bitmap != null && !bitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
            bitmap.recycle();
            return texture[0];
        }
        return 0;
    }
}
