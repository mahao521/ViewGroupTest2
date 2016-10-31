package com.mahao.viewgrouptest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mahao.viewgrouptest2.custom.VerticalLinearLayout;

public class MainActivity extends AppCompatActivity implements VerticalLinearLayout.OnpageChangeListener, View.OnTouchListener {

    private VerticalLinearLayout verticalLayout;
    private int count;
    private long firClick,secondClick;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verticalLayout = (VerticalLinearLayout) this.findViewById(R.id.vertical_layout);
        verticalLayout.setOnPageChangeListener(this);
        verticalLayout.setOnTouchListener(this);
    }

    @Override
    public void onPageChange(int currentPage) {

        Toast.makeText(this,"第"+currentPage+"页",Toast.LENGTH_SHORT).show();
        if(currentPage == 3){

            if(flag == true){

                Intent intent = new Intent(this,ScrollToAndByActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(MotionEvent.ACTION_DOWN == event.getAction()){

            count++;
            if(count == 1){
               firClick = System.currentTimeMillis();

            }else if(count == 2){

                secondClick = System.currentTimeMillis();
                if(secondClick - firClick < 1000){
                    flag = true;
                }
                count = 0;
                firClick = 0;
                secondClick = 0;
                return true;
            }
        }
        return false;
    }
}
