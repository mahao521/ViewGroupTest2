package com.mahao.viewgrouptest2;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.mahao.viewgrouptest2.custom.MultiViewGroup;

public class MultiScreenActivity extends AppCompatActivity implements View.OnClickListener {

    public Button mBtnLeft,mBtnRight;
    public MultiViewGroup mViewGroup;
    public static int screenWidth;
    public static int screenHeight;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_screen);

        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        screenHeight = point.y ;
        screenWidth = point.x;
        Log.i("mahao",screenHeight+"...."+screenWidth);

        DisplayMetrics matrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(matrics);
        screenWidth = matrics.widthPixels;
        screenHeight = matrics.heightPixels;
        Log.i("mahao",screenHeight+"....2...."+screenWidth);

        mBtnLeft = (Button) findViewById(R.id.btn_scrollLeft);
        mBtnRight = (Button) findViewById(R.id.btn_scrollRight);
        mViewGroup = (MultiViewGroup) findViewById(R.id.viewgroup_custom);

        mBtnRight.setOnClickListener(this);
        mBtnLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){

            case R.id.btn_scrollLeft:
                if(currentPage > 0){
                    currentPage--;
                }
                mViewGroup.scrollTo(currentPage*screenWidth,0);
                break;
            case R.id.btn_scrollRight:
                if(currentPage < 2){
                    currentPage++;
                }
                mViewGroup.scrollBy(20,0);
                Log.i("mahao",currentPage*screenWidth+"");
                break;
        }
    }
}
