package com.xidian.qhh.tia.machinelearning;

import java.util.List;
import com.xidian.qhh.tia.tia.FeatureVector;
/**
 * Created by dell on 2018/4/19.
 */

public abstract class Classifier {
    public abstract boolean train(List<FeatureVector> featureVectors);
    public abstract int classify(FeatureVector featureVector);
}