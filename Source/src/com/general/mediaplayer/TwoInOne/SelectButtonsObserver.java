package com.general.mediaplayer.TwoInOne;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/18/14
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectButtonsObserver {
    protected ArrayList<SelectButton> delegates = new ArrayList<SelectButton>();
    protected ArrayList<View> moveViews = new ArrayList<View>();

    protected ButtonSelectStrategy selectStrategy;
    protected Button btnTapHint;
    protected boolean isMoved = false;

    public void setSelectStrategy(ButtonSelectStrategy strategy) {
        selectStrategy = strategy;
    }

    public void onButtonTouched(SelectButton btn) {

        ListIterator<SelectButton> iterator = delegates.listIterator();
        int nextState = SelectButton.SELBTNSTATE_NORMAL;
        while(iterator.hasNext())
        {
            SelectButton currBtn = iterator.next();
            if (currBtn.equals(btn) == false)
                currBtn.setState(selectStrategy.getNextState(btn, currBtn), true);
            else
            {
                nextState = selectStrategy.getNextState(currBtn);
                currBtn.setState(nextState, true);
            }
        }

        RelativeLayout.LayoutParams tapLayoutParams = (RelativeLayout.LayoutParams)btnTapHint.getLayoutParams();
        int height = tapLayoutParams.height;


        // move to original pos
        ListIterator<View> itV = moveViews.listIterator();
        while(itV.hasNext())
        {
            View v = itV.next();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
            if (btn.isSelectable() == true)
            {
                // move to original pos
                if (isMoved == true)
                    layoutParams.topMargin = layoutParams.topMargin + height;
            }
            else
            {
                // move to top
                if (isMoved == false)
                    layoutParams.topMargin = layoutParams.topMargin - height;
            }
            v.setLayoutParams(layoutParams);
        }

        if (btn.isSelectable() == true)
        {
            isMoved = false;
            btnTapHint.setVisibility(View.INVISIBLE);
        }
        else
        {
            isMoved = true;
            btnTapHint.setVisibility(View.VISIBLE);
        }

    }

    public void addDelegate(SelectButton btn) {
        delegates.add(btn);
    }

    public void addBtnTapHint(Button btnTapHint) {
        this.btnTapHint = btnTapHint;
    }

    public void addMoveView(View v) {
        moveViews.add(v);
    }
}
