package com.xidian.qhh.tia.tia;

import com.xidian.qhh.tia.secure.Dispatcher;
import com.xidian.qhh.tia.secure.TouchEvent;
import com.xidian.qhh.tia.machinelearning.SVMClassifier;
/**
 * Created by dell on 2018/4/19.
 */

public class SecureBaseInit extends SecureBase {

    public SecureBaseInit(TouchEvent touchEvent) {
        super();
        setDispatcher(new Dispatcher(touchEvent));
//        useClassifier(new BpDeepClassifier(new int[]{25,30,2}, 0.15, 0.8));
//        useClassifier(new SVMClassifier(TouchEvent.NUM_FEATURES));
//        useClassifier(new KNNClassifier(7, TouchEvent.NUM_FEATURES));
    }
}