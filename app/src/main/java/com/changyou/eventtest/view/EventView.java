package com.changyou.eventtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by houchunyu on 16/6/26.
 */
public class EventView extends View {
    public EventView(Context context) {
        super(context);
    }

    public EventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventView======dispatchTouchEvent==" + event.getAction());

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventView======onTouchEvent==" + event.getAction());
        return true;
    }
}
