package com.xidian.qhh.tia.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xidian.qhh.tia.tia.FeatureVector;
import com.xidian.qhh.tia.tia.SecureBase;
import com.xidian.qhh.tia.tia.SecureBaseInit;
import com.xidian.qhh.tia.entity.UserBehavoir;
import com.xidian.qhh.tia.dao.DatabaseHelper;
import com.xidian.qhh.tia.secure.TouchEventLive;
import com.xidian.qhh.tia.tia.Parameters;


import com.xidian.qhh.tia.R;

import static com.xidian.qhh.tia.tia.Parameters.*;

public class DemoActivity extends AppCompatActivity{

    private SecureBaseInit mTouchAuthInit = null;
    private TextView mtextView,mPositiveTextView;
    private EditText user_uid_text;
    private static final String TAG = "DemoActivity";
    private Button mreturn;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GenerateSuccess){

                mPositiveTextView.setText(String.format("%d",countnumber));
            }
            if(msg.what==TransferSuccess)
            {
                mPositiveTextView.setText(String.format("%d",countnumber));
                Toast.makeText(DemoActivity.this, "录入数据传输成功", Toast.LENGTH_SHORT).show();
            }
            if(msg.what==-1)
            {
                mPositiveTextView.setText(String.format("%d",countnumber));
                mtextView.setText(String.format("认证结果：不存在该用户样本"));
                Toast.makeText(DemoActivity.this, "不存在该用户样本", Toast.LENGTH_SHORT).show();
            }
            if(msg.what==0)
            {   mPositiveTextView.setText(String.format("%d",countnumber));
                mtextView.setText(String.format("认证结果：失败"));
                Toast.makeText(DemoActivity.this, "认证失败", Toast.LENGTH_SHORT).show();
            }
            if(msg.what==1)
            {
                mPositiveTextView.setText(String.format("%d",countnumber));
                mtextView.setText(String.format("认证结果：成功"));
                Toast.makeText(DemoActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG,"dispatchTouchEvent ");
        if(SecureBase.getTouchAuth().getDispatcher().process(ev, this))
        {
           Thread t1= SecureBase.getTouchAuth().getDispatcher().getProcessthread();
            Thread t2= new Thread()
            {
                @Override
                public void run() {
                    Message message=new Message();
                    message.what=result;
                    handler.sendMessage(message);
                    System.out.println("发送message线程中mesage为:"+result);
                    System.out.println("发送message线程完成");
                }
            };
            try {
                t1.join();
                t2.start();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return super.dispatchTouchEvent(ev);

    }//对每一次输入down ,move,up 的处理

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mtextView = (TextView) findViewById(R.id.classifyscore_textview);
        mPositiveTextView = (TextView) findViewById(R.id.positive_textview);
        mreturn=(Button)findViewById(R.id.return_activity) ;
        mreturn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                Intent intent=new Intent(DemoActivity.this,MainActivity.class);
                startActivity(intent);
                Parameters.setState(Parameters.default_state);
            }
        });

        LinearLayout ll = (LinearLayout) findViewById(R.id.paintlayout);
        View view = new PaintView(this);
        ll.addView(view);


        user_uid_text = (EditText) findViewById(R.id.user_uid);

        //初始化
        user_uid_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    user_uid_text.setText("");
                }
                else {
                   // user_uid_text.setText("username");
                    user_uid_text.setHint("请输入你的用户名");
                }
            }
        });

        user_uid_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UserBehavoir.setUid(s.toString());
            }
        });
        //对用户名输入的操作


        Log.d(TAG, "onCreate: sucess");
    }

    @Override
    protected void onStart() {
        super.onStart();


        //设置运行状态
        Parameters.runing_state = true;
        mTouchAuthInit = new SecureBaseInit(new TouchEventLive());//初始化分化器和生成器
       //mTouchAuthInit.start();
        Log.d(TAG, "onStart:sucess ");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Parameters.runing_state = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void onClick_Event(View view){
        System.out.println("....");
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase sqldb = databaseHelper.getWritableDatabase();
        databaseHelper.delete(sqldb);

    }

}
