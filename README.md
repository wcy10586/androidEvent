# 轻松弄懂android事件分发机制解析
android 中的事件处理一直以来困扰不少刚刚从事android开发的同学，网上也有不少讲解android事件分发的文章，然而讲解的都不够简洁！现在我将用另一种简洁的方式来讲解android事件的分发机制！

android的事件分发可以简单的归位两类：1.view的事件分发和处理 2.ViewGroup的事件分发和处理。我们首先分析View的事件分发和处理。

## View的事件分发和处理
View中的事件分发和处理涉及到了两个方法：
1.public boolean dispatchTouchEvent(MotionEvent event)     2.public boolean onTouchEvent(MotionEvent event)  它们的返回值都为boolean类型。

在View中dispatchTouchEvent(MotionEvent event) 负责将事件分发给onTouchEvent(MotionEvent event)来处理，简单一点理解就是下面的这段代码

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventView======dispatchTouchEvent==" + event.getAction());
        boolean handed ＝ onTouchEvent(event);
        return handed； 
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventView======onTouchEvent==" + event.getAction());
        return true;
}

通过简易模型我们可以看到：  
在dispatchTouchEvent(MotionEvent event)只要返回true就代表这个View消耗了这个事件。

如果在onTouchEvent(MotionEvent event)中返回true

那么这个事件的传递链就是这样的：activity的dispatchTouchEvent（）——>ViewGroup 的dispatchTouchEvent（）——>ViewGroup onInterceptTouchEvent（）——>view 的dispatchTouchEvent（）
——> view的onTouchEvent(）；

如果在dispatchTouchEvent(MotionEvent event)中直接返回true。类似于这样

        public boolean dispatchTouchEvent(MotionEvent event) {
            Log.i("eventTest", "=EventView======dispatchTouchEvent==" + event.getAction());
            return true;
        }
    
那么这个事件依旧是被这个View消耗了是被dispatchTouchEvent（）消耗的，并没有将事件分发给onTouchEvent（），所以这个View的onTouchEvent（）不会执行，这是这个事件的传递链是这样的：activity的dispatchTouchEvent（）——>ViewGroup 的dispatchTouchEvent（）——>ViewGroup onInterceptTouchEvent（——>view 的dispatchTouchEvent（）。

在dispatchTouchEvent(MotionEvent event)只要返回false就代表这个View不消耗了这个事件。
如果在onTouchEvent(MotionEvent event)f中返回false，除了action_down以外后续事件都不会传递到这个View上
当然如果直接在dispatchTouchEvent(MotionEvent event)中直接返回false，这个View的onTouchEvent（）不会执行。原理同直接返回true；

然而在现实中经常会看见这样的写法：

     @Override
     public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventView======dispatchTouchEvent==" + event.getAction());

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventView======onTouchEvent==" + event.getAction());
        return super.onTouchEvent(event);
    }
    
它们调用super的方法是因为在父类中它们各自定义了一些事件分发或者事件处理逻辑。特别是在super.dispatchTouchEvent中定义了将事件交给
onTouchEvent（）处理的逻辑。

##ViewGroup的事件分发和处理
ViewGroup中的事件分发出处理涉及到了三个方法：
1.public boolean dispatchTouchEvent(MotionEvent event)   2.public boolean onInterceptTouchEvent(MotionEvent ev)  3.public boolean onTouchEvent(MotionEvent event)   它们的返回值都是boolean类型。

在ViewGroup中  dispatchTouchEvent(MotionEvent event) 负责先将事件分发给自己onInterceptTouchEvent(MotionEvent ev) 
来判断自己是否拦截事件，如果onInterceptTouchEvent(MotionEvent ev) 返回true，表示自己要拦截这个事件，那么这个事件就会交给自己的
onTouchEvent(MotionEvent event)处理，如果事件被自己的onTouchEvent（）消耗，那么后续事件将直接交给自己的onTouchEvent（）处理，如果事件不消耗，那么ViewGroup的父控件不会再将事件分发过来。如果不拦截事件那么ViewGroup就会将事件分发给子View，如果子View也不处理这个事件，那么它就会将事件交给自己的onTouchEvent(MotionEvent event)来处理。简单一点理解就是下面的这段代码

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
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("eventTest", "=EventViewGroup======onTouchEvent==" + event.getAction());
        return false;
    }
  
 通过简易模型我们可以看到：   
ViewGroup 的onInterceptTouchEvent（）则是用来直接拦截事件的，如果返回true，就代表本ViewGroup要拦截事件并将事件交给自己的onTouchEvent处理。事件一旦被拦截 就不会再分发给当前ViewGroup包含的子控件了。  
ViewGroup 的dispatchTouchEvent（）返回true就代表了这个事件是被这个ViewGroup消耗了。而在这个ViewGroup中我们知道是ViewGroup本身消耗了事件还是它包含的子 view消耗了事件，从而决定了事件是分发给自己还是包含的子 view。


    
通过对ViewGroup的事件分发模型的简单化，我们可以可以看出来，如果ViewGroup的dispatchTouchEvent(MotionEvent ev)直接返回true

    public boolean dispatchTouchEvent(MotionEvent event) {
            Log.i("eventTest", "=EventView======dispatchTouchEvent==" + event.getAction());
            return true;
    }
    
那么这个事件ji hu

##activity的事件分发和处理
activity的事件分发模型和ViewGrooup类似，只是没有onInterceptTouchEvent(MotionEvent ev)这一步。
可以简单概括为

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
        return super.onTouchEvent(event);
    }

#总结
为什么在不拦截不处理事件的情况下，事件的分的过程是有acttivity.dispatchTouchEvent(MotionEvent ev) －> viewGroup.dispatchTouchEvent(MotionEvent ev) －>onInterceptTouchEvent(MotionEvent ev)－> view.dispatchTouchEvent(MotionEvent ev)  －> view.onTouchevent(otionEvent ev)－>viewGroup.onTouchevent(otionEvent ev) －> activity.onTouchevent(otionEvent ev)

从对View和viewGroup以及activity中的事件分析我们可以看到事件的分发处理过程是这样的：首先由activity优先分发给他的根视图，而根视图会优先判断是否拦截事件，不拦截则对事件做继续分发，直到在某一级视图事件被消耗了才会停止分发这个事件，否则，在最后一层视图，因为它没有子视图了，那么事件的分发就结束了，从而转入对onTouchevent（）的调用，当onTouchevent（）执行结束，它的dispatchTouchEvent(ev)执行结束即它的父视图的getChildAt(0).dispatchTouchEvent(ev)执行结束，然后它的父视图就会判断是否调用自己的onTouchevent（）...依次类推，就有了上面的事件分发处理链。

