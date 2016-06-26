package com.changyou.eventtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MainActivity extends Activity {
   boolean viewHanded;
    boolean handed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("eventTest", "=MainActivity======dispatchTouchEvent==" + ev.getAction());
        viewHanded = getWindow().getDecorView().findViewById(android.R.id.content).dispatchTouchEvent(ev);
        if (!viewHanded){
            handed = onTouchEvent(ev);
        }
        return handed || viewHanded;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=MainActivity======onTouchEvent==" + event.getAction());
        return false;
    }
}
