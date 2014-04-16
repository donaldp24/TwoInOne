package com.general.mediaplayer.TwoInOne;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/22/14
 * Time: 7:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class SpecialButtonSelectStrategy implements ButtonSelectStrategy {
    @Override
    public int getNextState(SelectButton btnTouched) {
        switch (btnTouched.getState())
        {
            case SelectButton.SELBTNSTATE_NORMAL:
                if (btnTouched.isSelectable())
                    return SelectButton.SELBTNSTATE_SELECTED;
                else
                    return SelectButton.SELBTNSTATE_DISABLED;
            case SelectButton.SELBTNSTATE_SELECTED:
                if (btnTouched.isSelectable())
                    return SelectButton.SELBTNSTATE_NORMAL;
                else
                    return SelectButton.SELBTNSTATE_DISABLED;
            case SelectButton.SELBTNSTATE_DISABLED:
                return SelectButton.SELBTNSTATE_DISABLED;
            default:
                return SelectButton.SELBTNSTATE_DISABLED;
        }
    }

    @Override
    public int getNextState(SelectButton btnTouched, SelectButton btnCurr) {
        return btnCurr.getState();
    }
}
