package com.general.mediaplayer.TwoInOne;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/18/14
 * Time: 10:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralButtonSelectStrategy implements ButtonSelectStrategy {

    @Override
    public int getNextState(SelectButton btnTouched) {
        /*
        switch (btnTouched.getState())
        {
            case SelectButton.SELBTNSTATE_NORMAL:
                if (btnTouched.isSelectable())
                {
                    return SelectButton.SELBTNSTATE_SELECTED;
                }
                else
                {
                    return SelectButton.SELBTNSTATE_TOUCHED;
                }
            case SelectButton.SELBTNSTATE_SELECTED:
                return SelectButton.SELBTNSTATE_NORMAL;
            case SelectButton.SELBTNSTATE_TOUCHED:
                return SelectButton.SELBTNSTATE_TOUCHED;
            case SelectButton.SELBTNSTATE_LOSTFOCUSTED:
                if (btnTouched.isSelectable())
                {
                    return SelectButton.SELBTNSTATE_SELECTED;
                }
                else
                {
                    return SelectButton.SELBTNSTATE_TOUCHED;
                }
            default:
                return SelectButton.SELBTNSTATE_NORMAL;
        }
        */
        return SelectButton.SELBTNSTATE_SELECTED;
    }

    @Override
    public int getNextState(SelectButton btnTouched, SelectButton btnCurr) {

        /*
        if (btnTouched.isSelectable())
        {
            if (btnCurr.isSelectable())
            {
                if (btnCurr.getState() != SelectButton.SELBTNSTATE_SELECTED)
                    return SelectButton.SELBTNSTATE_NORMAL;
                return btnCurr.getState();
            }
            else
                return SelectButton.SELBTNSTATE_NORMAL;
        }
        else
        {
            if (btnCurr.isSelectable())
            {
                if (btnCurr.getState() == SelectButton.SELBTNSTATE_SELECTED)
                    return btnCurr.getState();
                return SelectButton.SELBTNSTATE_LOSTFOCUSTED;
            }
            return SelectButton.SELBTNSTATE_LOSTFOCUSTED;
        }
        */
        return SelectButton.SELBTNSTATE_SELECTED;
    }
}
