package com.changyou.eventtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import android.widget.FrameLayout;

/**
 * Created by houchunyu on 16/6/26.
 */
public class EventViewGroup extends FrameLayout {
    boolean inter;
    boolean childHanded;
    boolean handed;

    public EventViewGroup(Context context) {
        super(context);
    }

    public EventViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("eventTest", "=EventViewGroup======dispatchTouchEvent==" + ev.getAction());
        if (!inter) {
            inter = onInterceptTouchEvent(ev);
        }
        if (inter) {
            handed = onTouchEvent(ev);
        } else {
            childHanded = getChildAt(0).dispatchTouchEvent(ev);
            if (!childHanded) {
                handed = onTouchEvent(ev);
            }
        }
        return handed || childHanded;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("eventTest", "=EventViewGroup======onInterceptTouchEvent==" + ev.getAction());
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventViewGroup======onTouchEvent==" + event.getAction());
        return true;
    }
}
