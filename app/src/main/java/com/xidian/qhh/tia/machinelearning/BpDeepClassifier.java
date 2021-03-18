package com.xidian.qhh.tia.machinelearning;

import java.util.List;
import com.xidian.qhh.tia.tia.FeatureVector;
import com.xidian.qhh.tia.utils.FileUtils;

/**
 * Created by dell on 2018/4/19.
 */

public class BpDeepClassifier extends Classifier{
    private BpDeep bpDeep;
    public BpDeepClassifier(int[] layernum, double rate, double mobp){
        bpDeep = new BpDeep(layernum,rate,mobp);
    }
    @Override
    public boolean train(List<FeatureVector> list) {
        list = FileUtils.readFeatureVectors(FileUtils.FILE_FEATUREVECTURE_NAME);
        for(int i=0;i <40;++i){
            for(int j=0;j<list.size();++j){
                bpDeep.train(list.get(j).getAll(),new double[]{list.get(j).getClassLabel()} );
            }
        }
        return true;
    }

    @Override
    public int classify(FeatureVector featureVector) {
        double score = bpDeep.computeOut(featureVector.getAll())[0];
        System.out.println("score:"+score);
        return (int)Math.round(score);
    }
}
