package com.mahao.viewgrouptest2.custom;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by Penghy on 2016/10/20.
 */

public class VerticalLinearLayout extends ViewGroup {

    //屏幕的亮度
    private int mScreenHeight;
    // 收回按下时的getScrollY
    private int mScrollStartY ;
    // 手指抬起的Y
    private int mScrollEndY;
    // 记录手指移动的Y；
    private int mLastY;

    // 滚动的辅助类
    private Scroller mScroller;
    //判断是否在滚动
    private boolean isScrolling;
    //加速度检测
    private VelocityTracker mVelocityTracker;
    //记录当前页
    private int currentPage = 0;

    private OnpageChangeListener onpageListener;

    public VerticalLinearLayout(Context context) {
        super(context);
        initData(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public VerticalLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    private void initData(Context context) {

        // 获取屏幕的宽高
        WindowManager  windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        mScreenHeight = point.y;
        DisplayMetrics outMatrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMatrics);
        mScreenHeight = outMatrics.heightPixels;
        // 初始化
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for(int i = 0; i < count; ++i){

            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

          if(changed){

              int count = getChildCount();
              //设置逐步局的高度
              MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
              lp.height = mScreenHeight * count;
              setLayoutParams(lp);

              for(int i = 0; i < count ; i++ ){

                  View view = getChildAt(i);
                  if(view.getVisibility() != View.GONE){

                      view.layout(l,i*mScreenHeight,r,i*mScreenHeight + mScreenHeight);
                  }
              }
          }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 如果当前正在滚动，调用父类的onTouchEvent
        if(isScrolling){

            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        int y = (int) event.getY(); // getY() 坐标相对于父布局，getRowY（）坐标相对于宽体

        /**
         *  按下之后， 事件触发的y和按下的Y是一样的，但是移动方向上的Y和按下的Y是不同的。
         *
         *   意思就是： 按下之后移动一段距离之后，才会触发move事件；
         *             因此事件触发的Y只是按下的Y的初值。
         */

        // 初始化加速度事件
        obtainVelocity(event);
        switch(action){

            case MotionEvent.ACTION_DOWN:
                mScrollStartY = getScrollY();
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();  //停止动画
                }
                int dy = mLastY - y;  // 滑动的距离
                //边界值检测
                int scrollY = getScrollY(); //由scrollto()触发，该方法是相对控件左上角的位置
                // 已经到达了顶端，下拉多少，就往上滚动多少
                if(dy  < 0 && scrollY + dy < 0){

                    dy = -scrollY;
                }
                // 已经到达了底部，上拉多少，就往下拉多少。
                if(dy > 0 && scrollY + dy > getHeight() - mScreenHeight){

                    dy = getHeight() - mScreenHeight - scrollY;
                }
                scrollBy(0,dy);
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:

                 mScrollEndY = getScrollY();
                int scroll = mScrollEndY - mScrollStartY;  // 滑动的距离
                if(mScrollEndY > mScrollStartY){ // 往上滑动

                    if(shouldScrollToNext()){

                        mScroller.startScroll(0,getScrollY(),0,mScreenHeight - scroll);
                    }else {
                        mScroller.startScroll(0,getScrollY(),0,-scroll);
                    }
                }

                if(mScrollEndY < mScrollStartY){ // 往下滑动

                    if(shouldScrollPre()){

                        mScroller.startScroll(0,getScrollY(),0,-mScreenHeight - scroll);
                    }else{
                        mScroller.startScroll(0,getScrollY(),0,-scroll);
                    }
                }
                isScrolling = true;
                postInvalidate();
                recycleVelocity();
                break;
        }
        return true;

    }

    //释放资源
    private void recycleVelocity() {

        if(mVelocityTracker != null){

            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //初始化加速度的检测
    private void obtainVelocity(MotionEvent event){

        if(mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    // 根据滑动距离判断是否滚动到下一页
    private boolean shouldScrollToNext(){

        return (mScrollEndY - mScrollStartY) > mScreenHeight/2 || Math.abs(getVelocity()) > 600;
    }

    // 根据滑动距离向上滚动
    private boolean shouldScrollPre(){

        return (-mScrollEndY + mScrollStartY) > mScreenHeight/2 || Math.abs(getVelocity()) > 600;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if(mScroller.computeScrollOffset()){ // 获取新的位置和判断动画是否完成

            scrollTo(0,mScroller.getCurrY());
            //?>??  查询scrollTo和scrollBy的区别
            postInvalidate();
        }else{

            int position = getScrollY() / mScreenHeight;
            if(position != currentPage){

                if(onpageListener != null){

                    currentPage = position;
                    onpageListener.onPageChange(position);
                }
            }
            isScrolling = false;
        }
    }

    //获取加速度的方向
    private int getVelocity(){

        mVelocityTracker.computeCurrentVelocity(1000);
        return (int)mVelocityTracker.getYVelocity();
    }

    /**
     *  回调接口
     */
    public interface OnpageChangeListener{


        void onPageChange(int currentPage);
    }

    public void  setOnPageChangeListener(OnpageChangeListener pageChange){

        this.onpageListener  = pageChange;
    }
}
