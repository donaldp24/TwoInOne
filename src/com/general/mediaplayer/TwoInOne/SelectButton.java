package com.general.mediaplayer.TwoInOne;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/18/14
 * Time: 10:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectButton extends Button {

    public static final int SELBTNSTATE_NORMAL = 0;
    public static final int SELBTNSTATE_SELECTED = 1;
    public static final int SELBTNSTATE_DISABLED = 2;

    protected int _state = SELBTNSTATE_NORMAL;
    protected boolean _selectable = true;

    protected int _idImgNormal = 0;
    protected int _idImgSelected = 0;
    protected int _idImgDisabled = 0;

    public SelectButton(Context context) {
        this(context, null);
    }

    public SelectButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBackgroundImages(int idImgNormal, int idImgSelected, int idImgDisabled) {
        _idImgNormal = idImgNormal;
        _idImgSelected = idImgSelected;
        _idImgDisabled = idImgDisabled;
    }

    public void setSelectable(boolean selectable)
    {
        _selectable = selectable;
    }

    public boolean isSelectable()
    {
        return _selectable;
    }


    public void setState(int state, boolean changeBk)
    {
        _state = state;
        if (changeBk)
        {
            switch (_state)
            {
                case SELBTNSTATE_NORMAL:
                    super.setBackgroundResource(_idImgNormal);
                    break;
                case SELBTNSTATE_SELECTED:
                    super.setBackgroundResource(_idImgSelected);
                    break;
                case SELBTNSTATE_DISABLED:
                    super.setBackgroundResource(_idImgDisabled);
                    break;
                default:
                    super.setBackgroundResource(_idImgNormal);
            }
        }
    }
    public int getState()
    {
        return _state;
    }
}
