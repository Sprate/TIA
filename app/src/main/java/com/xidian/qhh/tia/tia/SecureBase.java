package com.xidian.qhh.tia.tia;

import java.util.ArrayList;
import com.xidian.qhh.tia.secure.Dispatcher;
import com.xidian.qhh.tia.machinelearning.Classifier;

/**
 * Created by dell on 2018/4/19.
 */

public class SecureBase extends Thread{
    String TAG = "TouvhAuth";

    private static SecureBase sSecureBase = null;
    private Dispatcher mDispatcher;


    private static ArrayList<Integer> scoresList = new ArrayList<Integer>();


    public SecureBase(){
        sSecureBase = this;
    }


    public static SecureBase getTouchAuth(){
        return sSecureBase;
    }
    public Dispatcher getDispatcher(){
        return mDispatcher;
    }
    public void setDispatcher(Dispatcher dispatcher){
        mDispatcher = dispatcher;
    }


    /*public int getScores(){
        if (scoresList.size()>=1) {
            return scoresList.remove(0);
        }
        else
            return 0;
    }*/

    @Override
    public void run() {

        //while(Parameters.runing_state) {
            System.out.println("tia thread run");

       // }
    }
}