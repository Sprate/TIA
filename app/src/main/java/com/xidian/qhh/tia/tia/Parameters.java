package com.xidian.qhh.tia.tia;

/**
 * Created by dell on 2018/4/19.
 */

public class Parameters {
    public static final int DATANUM = 30;
    public static final long RUNPERIOC = 1000;
    public static final int TransferNum=50;
    public static final int TestNum=5;
    //设置运行状态参数，启动的时候就运行，否则暂停
    public static final int TransferSuccess=3;
    public static boolean runing_state = true;
    public static final int GenerateSuccess=2;
    public static final int Initialization_state=4;
    public static int result=Initialization_state; //有三个状态   -1：该样本未训练 0：认证成功 1：认证失败  4：初始化状态
    public static final int entry_state=1;
    public static final int test_state=2;
    public static final int  default_state=0;
    public static int state=default_state; //0:录数据 1：测试
    public static int countnumber=0;

    public static void setState(int state) {
        Parameters.state = state;
    }
}
