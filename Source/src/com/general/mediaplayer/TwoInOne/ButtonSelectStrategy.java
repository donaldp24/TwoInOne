package com.general.mediaplayer.TwoInOne;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/18/14
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ButtonSelectStrategy {
    public int getNextState(SelectButton btnTouched);
    public int getNextState(SelectButton btnTouched, SelectButton btnCurr);
}
