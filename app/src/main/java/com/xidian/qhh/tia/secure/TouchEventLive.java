package com.xidian.qhh.tia.secure;

import android.view.MotionEvent;
import com.xidian.qhh.tia.tia.FeatureVector;
import java.util.ArrayList;

/**
 * Created by dell on 2018/4/19.
 */

public class TouchEventLive extends TouchEvent{

    private ArrayList<TouchPoint> touchPoints = new ArrayList<TouchPoint>();

    public TouchEventLive(){
        fv = new FeatureVector(NUM_FEATURES);
    }

    @Override
    public boolean process(Object ev) {
        MotionEvent event = (MotionEvent) ev;
        int action = event.getAction();
        System.out.println("beginning process");

        switch(action) {
            case MotionEvent.ACTION_DOWN:/* primary pointer */
                System.out.println("按下去了");
            case MotionEvent.ACTION_POINTER_DOWN: /* any subsequent pointer */
				/*No need for a swipe ID*/
                break;
            case MotionEvent.ACTION_MOVE: /* any number of pointers move */
                System.out.println("在滑动");
               for (int hIndx = 0; hIndx < event.getHistorySize(); hIndx++) {
                    for (int pIndex = 0; pIndex < event.getPointerCount();
                         pIndex++) {
                        TouchPoint tp = new TouchPoint();
                        tp.xVal = event.getHistoricalX(pIndex, hIndx);
                        tp.yVal = event.getHistoricalY(pIndex, hIndx);
                        tp.pressure = event.getHistoricalPressure(pIndex, hIndx);
                        tp.width = event.getHistoricalSize(pIndex, hIndx);
                        tp.orientation = event.getHistoricalOrientation(pIndex, hIndx);
                        tp.eventTimestamp = event.getHistoricalEventTime(hIndx);
                        this.touchPoints.add(tp);
                    }
                }//缓存的历史点和多指触控

                for (int pIndex = 0; pIndex < event.getPointerCount();
                     pIndex++) {
                    TouchPoint tp = new TouchPoint();
                    tp.xVal = event.getX(pIndex);
                    tp.yVal = event.getY(pIndex);
                    tp.pressure = event.getPressure(pIndex);
                    tp.width = event.getSize(pIndex);
                    tp.eventTimestamp = event.getEventTime();
                    tp.orientation = event.getOrientation(pIndex);
                    this.touchPoints.add(tp);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: /* all pointers are up */
            case MotionEvent.ACTION_UP:
                System.out.println("抬起来了");
            case MotionEvent.ACTION_CANCEL:
				/* XXX return if length of swipe is less than 6 touchpoints*/

                if (this.touchPoints.size() < 10) {
                    this.touchPoints.clear();
                    return false;
                }
                else {
                    fv = computeFeatureVector(touchPoints);
                    this.touchPoints.clear();
                    return true;
                }
        }
        return false;
    }
}

