package com.example.parkhanee.mytravelapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by parkhanee on 2016. 8. 22..
 */
public class ViewPagerIndicator extends LinearLayout { //왜 레이아웃을 상속받지 ?
    private Context context;
    int totalCount;
    private ImageView[] imageDot;
    private int currentItem=0;

    public ViewPagerIndicator(Context context) {
        super(context);
        this.context = context;
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;
        //is there any attrs to set @ nearby_d3.xml and get them here?
    }

    public void createDot (int totalCount){
        this.totalCount = totalCount;
        imageDot = new ImageView[totalCount];

        for (int i=0;i<totalCount;i++){
            imageDot[i] = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40,40);
            int itemMargin = 5;
            params.leftMargin= itemMargin;
            params.rightMargin= itemMargin;

            imageDot[i].setLayoutParams(params);
            imageDot[i].setImageResource(R.drawable.dots_default);
            imageDot[i].setTag(imageDot[i].getId(),false);
            this.addView(imageDot[i]);
        }
        selectDot(currentItem); //select default
    }

    public void selectDot(int position){
        for (int i=0;i<totalCount;i++){
            if (i==position){ // this item has just selected
                currentItem=position;
                imageDot[i].setImageResource(R.drawable.dots_selected);
                imageDot[i].setTag(imageDot[i].getId(),true);
                selectedAnimation(imageDot[i],1f,1.5f); // adjust float value
            }else if((boolean)imageDot[i].getTag(imageDot[i].getId())){
                imageDot[i].setImageResource(R.drawable.dots_default);
                imageDot[i].setTag(imageDot[i].getId(),false);
                defaultAnimation(imageDot[i],1.5f,1f); // adjust float value
            }
        }
        invalidate();
        requestLayout();
    }

    public void selectedAnimation(View view, float startScale, float endScale){ //선택되었을때

    }

    public void defaultAnimation(View view, float startScale, float endScale){ //선택되었다가 디폴트로 돌아갈때

    }

}
