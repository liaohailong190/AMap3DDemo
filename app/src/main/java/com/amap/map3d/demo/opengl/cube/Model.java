package com.amap.map3d.demo.opengl.cube;

import android.content.Context;

import com.amap.api.maps.AMap;

/**
 * Created by Administrator on 2018/4/26.
 */

public abstract class Model {
    protected Context context;
    protected MapShader shader;
    protected MapTexture texture;
    protected AMap aMap;
    protected int number; //划线时,总的点数
    public Model(Context context,AMap aMap,int number){
        this.context = context;
        this.aMap = aMap;
        this.number = number;
    }
    public Model(){
    }
    /**
     * 初始化原始数据
     */
    public abstract void initJavaData();

    /**
     * 初始化opengl数据
     */
    public abstract void initBuffer();

    /**
     * 绘制
     * @param mvp
     */
    public abstract void Draw(float[] mvp);
}
