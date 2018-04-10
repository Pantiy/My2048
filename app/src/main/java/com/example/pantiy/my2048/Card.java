package com.example.pantiy.my2048;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Pantiy on 2016/9/11.
 */
public class Card extends FrameLayout {

    private int num = 0;
    private TextView mNumber;

    public Card(Context context) {

        super(context);

        mNumber = new TextView(getContext());
        mNumber.setTextSize(28);
        mNumber.setGravity(Gravity.CENTER);
        mNumber.setBackgroundColor(0x33ffffff);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(10,10,0,0);
        addView(mNumber, lp);
        setNum(0);
    }

    public boolean equals(Card o) {
        return this.getNum() == o.getNum();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if(num <= 0)
            mNumber.setText("");
        else
            mNumber.setText(num+"");
        setColor(num);
    }

    private void setColor(int num){

        switch (num)
        {
            case 0:
                mNumber.setBackgroundColor(0x33ffffff);
                break;

            case 2:
                mNumber.setBackgroundColor(0x33ffffff);
                break;

            case 4:
                mNumber.setBackgroundColor(0xffCC66FF);
                break;

            case 8:
                mNumber.setBackgroundColor(0xffCC00FF);
                break;

            case 16:
                mNumber.setBackgroundColor(0xffCC00FF);
                break;

            case 32:
                mNumber.setBackgroundColor(0xff6633FF);
                break;

            case 64:
                mNumber.setBackgroundColor(0xff6633FF);
                break;

            case 128:
                mNumber.setBackgroundColor(0xff9933FF);
                break;

            case 256:
                mNumber.setBackgroundColor(0xff9933FF);
                break;

            default:
                mNumber.setBackgroundColor(0xff6600CC);
                break;
        }
    }
}
