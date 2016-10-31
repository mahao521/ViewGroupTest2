package com.mahao.viewgrouptest2.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mahao.viewgrouptest2.MultiScreenActivity;
/**
 * Created by Penghy on 2016/10/27.
 */
public class MultiViewGroup extends ViewGroup {
    
    private Context mContext;
    
    public MultiViewGroup(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MultiViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        
        //初始化3个LinearLayout控件
        LinearLayout layout = new LinearLayout(mContext);
        layout.setBackgroundColor(Color.RED);
        addView(layout);
        
        LinearLayout layoutOne = new LinearLayout(mContext);
        layoutOne.setBackgroundColor(Color.BLUE);
        addView(layoutOne);
        
        LinearLayout layoutTwo = new LinearLayout(mContext);
        layoutTwo.setBackgroundColor(Color.GREEN);
        addView(layoutTwo);
    }

    // 测量measure
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //设置viewGroup的大小
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize,hightSize);

        int childCount = getChildCount();
        Log.i("mahao","layout_count"+childCount);
        for(int i = 0; i < childCount;i++){

            View child = getChildAt(i);
           //设置每个子视图的大小
            child.measure(MultiScreenActivity.screenWidth,MultiScreenActivity.screenHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int startLeft = 0;
        int startTop = 10;
        int childCount = getChildCount();
        for(int i = 0; i < childCount ; i++){

            Log.i("mahao",startLeft+"...");
            View child = getChildAt(i);
            // 设置每个视图的位置
            child.layout(startLeft,startTop,startLeft + MultiScreenActivity.screenWidth,startTop + MultiScreenActivity.screenHeight);
            startLeft = startLeft + MultiScreenActivity.screenWidth;
            Log.i("mahao",MultiScreenActivity.screenHeight+"");
        }
    }
}









