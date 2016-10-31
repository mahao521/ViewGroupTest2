package com.mahao.viewgrouptest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScrollToAndByActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtContent;
    private Button btnScrollTo,btnScrollBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_to_and_by);

        txtContent = (TextView) this.findViewById(R.id.txt_scroll_content);
        btnScrollTo = (Button) this.findViewById(R.id.btn_scroll_to);
        btnScrollBy = (Button) this.findViewById(R.id.btn_scroll_by);

        btnScrollTo.setOnClickListener(this);
        btnScrollBy.setOnClickListener(this);
        txtContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {  // 向右移动在减小，右上角为0，想左移动是变大

        int id = v.getId();
        switch (id){

            case R.id.btn_scroll_by:

                txtContent.scrollBy(20,0);   // scrollBy是在原来View位置上偏移X,Y；
                int tvScrllx = txtContent.getScrollX();
                int tvScrlly = txtContent.getScrollY();
                Log.i("mahao",tvScrllx + "...." + tvScrlly);
                break;
            case R.id.btn_scroll_to:

                txtContent.scrollTo(-100,0);  // ScrollTo是移动到X，Y;
                int tvScrollX = txtContent.getScrollX();
                int tvScrollY = txtContent.getScrollY();
                Log.i("mahao",tvScrollX+ "...."+ tvScrollY);
                break;

            case R.id.txt_scroll_content:

                Intent intent = new Intent(this,MultiScreenActivity.class);
                startActivity(intent);
                break;
        }
    }
}
