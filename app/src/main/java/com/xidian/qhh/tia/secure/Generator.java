package com.xidian.qhh.tia.secure;

import com.xidian.qhh.tia.tia.FeatureVector;
/**
 * Created by dell on 2018/4/19.
 */

public abstract class Generator {
    protected FeatureVector fv;

    public abstract boolean process(Object ev);//返回成功生成一次数据的特征向量

    public FeatureVector getFeatureVector(){
        return fv;
    }
}
//特征向量生成器  基类
