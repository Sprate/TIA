package com.xidian.qhh.tia.secure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;

import com.xidian.qhh.tia.tia.FeatureVector;
import com.xidian.qhh.tia.dao.DatabaseHelper;
import com.xidian.qhh.tia.entity.UserBehavoir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.xidian.qhh.tia.tia.Parameters.*;

/**
 * Created by dell on 2018/4/19.
 */

//分化器 提取特征向量并存入数据库
public class Dispatcher {
    private Generator mGenerator;
    private Thread processthread;
    private FeatureVector fv = null;
    private List<UserBehavoir> userBehavoirs=new ArrayList<>();
    private static int num = 0;

    public Thread getProcessthread() {
        return processthread;
    }

    public Dispatcher(Generator generator){
        this.mGenerator = generator;
    }
    public  boolean process(Object ev, Context context){
        if(state!=default_state)
        {

            if (mGenerator.process(ev))
            {
                fv = mGenerator.getFeatureVector();//得到的特征向量

            /*DatabaseHelper dbh = new DatabaseHelper(context);*/
                final UserBehavoir userBehavoir = UserBehavoir.setValues(UserBehavoir.getUid(), fv.getAll());
                userBehavoir.setTouch_time(new Date());
                userBehavoirs.add(userBehavoir);
                countnumber=userBehavoirs.size();
                if (state == entry_state&&userBehavoirs.size() == TransferNum)
                {
                    processthread=new Thread(){
                        @Override
                        public void run() {
                            String urlPath="http://192.168.137.1:8080/usertouch/insertdata";
                            // String urlPath="http://10.0.2.2:8080/usertouch/insertdata";
                            URL url;

                            try {
                                JSONArray jsarray = new JSONArray();
                                for (int i = 0; i < TransferNum; i++) {
                                    JSONObject js = new JSONObject();

                                    js.put("userid", userBehavoirs.get(i).getUid());
                                    js.put("startX", userBehavoirs.get(i).getStart_x());
                                    js.put("startY", userBehavoirs.get(i).getStart_y());
                                    js.put("endX", userBehavoirs.get(i).getEnd_x());
                                    js.put("endY", userBehavoirs.get(i).getEnd_y());
                                    js.put("directEndToEndDistance", userBehavoirs.get(i).getDirect_end_to_end_distance());
                                    js.put("duration", userBehavoirs.get(i).getDuration());
                                    js.put("meanLength", userBehavoirs.get(i).getMean_length());
                                    js.put("twentyVelocity", userBehavoirs.get(i).getTwenty_velocity());
                                    js.put("fiftyVelocity", userBehavoirs.get(i).getFifty_velocity());
                                    js.put("eightyVelocity", userBehavoirs.get(i).getEighty_velocity());
                                    js.put("meanVelocity", userBehavoirs.get(i).getMean_velocity());
                                    js.put("twentyAcceleration", userBehavoirs.get(i).getTwenty_acceleration());
                                    js.put("fiftyAcceleration", userBehavoirs.get(i).getFifty_acceleration());
                                    js.put("eightyAcceleration", userBehavoirs.get(i).getEighty_acceleration());
                                    js.put("directionEndToEndLine", userBehavoirs.get(i).getDirection_end_to_end_line());
                                    js.put("trajectoryLength", userBehavoirs.get(i).getTrajectory_length());
                                    js.put("pressureMiddleStroke", userBehavoirs.get(i).getPressure_middle_stroke());
                                    js.put("middleStrokeArea", userBehavoirs.get(i).getMiddle_stroke_area());
                                    js.put("ratioDistanceTraj", userBehavoirs.get(i).getRatio_distance_traj());
                                    js.put("phoneOrientation", userBehavoirs.get(i).getPhone_orientation());
                                    js.put("flagDirection", userBehavoirs.get(i).getFlag_direction());
                                    js.put("largestDeviation", userBehavoirs.get(i).getLargest_deviation());
                                    js.put("twentyDeviation", userBehavoirs.get(i).getTwenty_deviation());
                                    js.put("fiftyDeviation", userBehavoirs.get(i).getFifty_deviation());
                                    js.put("eightyDeviation", userBehavoirs.get(i).getEighty_deviation());
                                    js.put("timestamp", userBehavoirs.get(i).getTouch_time().getTime());
                                    jsarray.put(js);
                                    System.out.println("发送数据线程:"+i);
                                }

                                String content = jsarray.toString();
                                url = new URL(urlPath);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(5000);
                                conn.setDoOutput(true);// 设置允许输出
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("User-Agent", "Fiddler");
                                conn.setRequestProperty("Content-Type", "application/json");
                                conn.setRequestProperty("Charset", "UTF-8");
                                OutputStream os = conn.getOutputStream();
                                os.write(content.getBytes());
                                os.close();


                                if (conn.getResponseCode() == 200) {
                                    InputStream in = conn.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        response.append(line);
                                    }
                                    conn.disconnect();
                                    System.out.println("发送数据线程：trasfer...");
                                    if (response.toString().equals("insert sucess!")) {
                                        result = TransferSuccess;
                                        System.out.println("发送数据线程：result+"+result);

                                    }
                                    userBehavoirs.clear();

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("发送数据线程完成");
                        }

                    };
                    processthread.start();
                        return true;
                }


           /* SQLiteDatabase db = dbh.getWritableDatabase();
//                dbh.delete(db);
            dbh.onCreate(db);

            dbh.insert(userBehavoir);*/
                //System.out.println("success!");
                //System.out.println(userBehavoir.getUid());

                if (userBehavoirs.size() == TestNum && state == test_state) {
                   processthread= new Thread() {
                        @Override
                        public void run() {
                            String urlPath="http://192.168.137.1:8080/usertouch/common";
                            //String urlPath = "http://10.0.2.2:8080/usertouch/common";
                            URL url;
                            try {
                                JSONArray jsarray = new JSONArray();
                                for (int i = 0; i < TestNum; i++) {
                                    JSONObject js = new JSONObject();
                                    js.put("userid", userBehavoirs.get(i).getUid());
                                    js.put("startX", userBehavoirs.get(i).getStart_x());
                                    js.put("startY", userBehavoirs.get(i).getStart_y());
                                    js.put("endX", userBehavoirs.get(i).getEnd_x());
                                    js.put("endY", userBehavoirs.get(i).getEnd_y());
                                    js.put("directEndToEndDistance", userBehavoirs.get(i).getDirect_end_to_end_distance());
                                    js.put("duration", userBehavoirs.get(i).getDuration());
                                    js.put("meanLength", userBehavoirs.get(i).getMean_length());
                                    js.put("twentyVelocity", userBehavoirs.get(i).getTwenty_velocity());
                                    js.put("fiftyVelocity", userBehavoirs.get(i).getFifty_velocity());
                                    js.put("eightyVelocity", userBehavoirs.get(i).getEighty_velocity());
                                    js.put("meanVelocity", userBehavoirs.get(i).getMean_velocity());
                                    js.put("twentyAcceleration", userBehavoirs.get(i).getTwenty_acceleration());
                                    js.put("fiftyAcceleration", userBehavoirs.get(i).getFifty_acceleration());
                                    js.put("eightyAcceleration", userBehavoirs.get(i).getEighty_acceleration());
                                    js.put("directionEndToEndLine", userBehavoirs.get(i).getDirection_end_to_end_line());
                                    js.put("trajectoryLength", userBehavoirs.get(i).getTrajectory_length());
                                    js.put("pressureMiddleStroke", userBehavoirs.get(i).getPressure_middle_stroke());
                                    js.put("middleStrokeArea", userBehavoirs.get(i).getMiddle_stroke_area());
                                    js.put("ratioDistanceTraj", userBehavoirs.get(i).getRatio_distance_traj());
                                    js.put("phoneOrientation", userBehavoirs.get(i).getPhone_orientation());
                                    js.put("flagDirection", userBehavoirs.get(i).getFlag_direction());
                                    js.put("largestDeviation", userBehavoirs.get(i).getLargest_deviation());
                                    js.put("twentyDeviation", userBehavoirs.get(i).getTwenty_deviation());
                                    js.put("fiftyDeviation", userBehavoirs.get(i).getFifty_deviation());
                                    js.put("eightyDeviation", userBehavoirs.get(i).getEighty_deviation());
                                    js.put("timestamp", userBehavoirs.get(i).getTouch_time().getTime());
                                    jsarray.put(js);
                                    System.out.println(i);
                                }
                                String content = jsarray.toString();
                                url = new URL(urlPath);
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(5000);
                                conn.setDoOutput(true);// 设置允许输出
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("User-Agent", "Fiddler");
                                conn.setRequestProperty("Content-Type", "application/json");
                                conn.setRequestProperty("Charset", "UTF-8");
                                OutputStream os = conn.getOutputStream();
                                os.write(content.getBytes());
                                os.close();

                                if (conn.getResponseCode() == 200) {
                                    InputStream in = conn.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = reader.readLine()) != null) {
                                        response.append(line);
                                    }
                                    conn.disconnect();
                                    System.out.println(response.toString());
                                    if (response.toString().equals("untraining")) {
                                        result =-1;
                                    }
                                    if (response.toString().equals("fail")) {
                                        result =0;
                                    }
                                    if (response.toString().equals("success")) {
                                        result = 1;
                                    }
                                    userBehavoirs.clear();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    };
                    processthread.start();
                    return true;
                }


                else{
                    processthread=new Thread();
                    result=GenerateSuccess;
                    return true;
                }

            }

         return false;
        }

        return false;
    }


}


