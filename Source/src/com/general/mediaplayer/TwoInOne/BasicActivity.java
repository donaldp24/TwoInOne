package com.general.mediaplayer.TwoInOne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/17/14
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class BasicActivity extends BaseActivity {
    private SelectButtonsObserver observer;
    private SelectButton btnFirst;
    private SelectButton btnSecond;
    private SelectButton btnThird;
    private SelectButton btnForth;
    private Button btnTapHint;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic);

        // Button Continue
        ImageButton btnContinue = (ImageButton)findViewById(R.id.btnBasicContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save current selected option
                int selectedBasic = getSelectedState();
                _appPrefs.setSelectedBasic(selectedBasic);

                Intent intent = new Intent(BasicActivity.this, AdvancedActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                finish();
            }
        });

        // Button Back
        ImageButton btnBack = (ImageButton)findViewById(R.id.btnBasicBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // reset current selection
                _appPrefs.setSelectedBasic(CommonData.BASIC_NONE);

                Intent intent = new Intent(BasicActivity.this, PriceActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetBackInAnim(), TransformManager.GetBackOutAnim());
                finish();
            }
        });

        CommonData.SuitableOption _suitableOption = CommonData.getSuitableOption(_appPrefs.getSelectedPrice());

        // Selection Buttons
        btnFirst = (SelectButton)findViewById(R.id.btnBasicFirst);
        btnSecond = (SelectButton)findViewById(R.id.btnBasicSecond);
        btnThird = (SelectButton)findViewById(R.id.btnBasicThird);
        btnForth = (SelectButton)findViewById(R.id.btnBasicForth);

        btnFirst.setSelectable(_suitableOption.isBasicSuitableWithOne(CommonData.BASIC_FIRST));
        btnFirst.setBackgroundImages(R.drawable.basic_btnfirst_normal, R.drawable.basic_btnfirst_selected, R.drawable.basic_btnfirst_disabled);
        btnFirst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnFirst);
            }
        });

        btnSecond.setSelectable(_suitableOption.isBasicSuitableWithOne(CommonData.BASIC_SECOND));
        btnSecond.setBackgroundImages(R.drawable.basic_btnsecond_normal, R.drawable.basic_btnsecond_selected, R.drawable.basic_btnsecond_disabled);
        btnSecond.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnSecond);
            }
        });

        btnThird.setSelectable(_suitableOption.isBasicSuitableWithOne(CommonData.BASIC_THIRD));
        btnThird.setBackgroundImages(R.drawable.basic_btnthird_normal, R.drawable.basic_btnthird_selected, R.drawable.basic_btnthird_disabled);
        btnThird.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnThird);
            }
        });

        btnForth.setSelectable(_suitableOption.isBasicSuitableWithOne(CommonData.BASIC_FORTH));
        btnForth.setBackgroundImages(R.drawable.basic_btnforth_normal, R.drawable.basic_btnforth_selected, R.drawable.basic_btnforth_disabled);
        btnForth.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnForth);
            }
        });

        observer = new SelectButtonsObserver();
        observer.setSelectStrategy(new SpecialButtonSelectStrategy());
        observer.addDelegate(btnFirst);
        observer.addDelegate(btnSecond);
        observer.addDelegate(btnThird);
        observer.addDelegate(btnForth);

        // Reflect Selected State
        int selectedBasic = _appPrefs.getSelectedBasic();
        if (selectedBasic != CommonData.BASIC_NONE) {
            int _selectedFirst = (selectedBasic & (1 << CommonData.BASIC_FIRST) ) != 0 ? 1 : 0;
            int _selectedSecond = (selectedBasic & (1 << CommonData.BASIC_SECOND) ) != 0 ? 1 : 0;
            int _selectedThird = (selectedBasic & (1 << CommonData.BASIC_THIRD) ) != 0 ? 1 : 0;
            int _selectedForth = (selectedBasic & (1 << CommonData.BASIC_FORTH) ) != 0 ? 1 : 0;
            setInitialState(_selectedFirst, btnFirst);
            setInitialState(_selectedSecond, btnSecond);
            setInitialState(_selectedThird, btnThird);
            setInitialState(_selectedForth, btnForth);
        }
        else {
            setInitialState(0, btnFirst);
            setInitialState(0, btnSecond);
            setInitialState(0, btnThird);
            setInitialState(0, btnForth);
        }

        //tap hint
        btnTapHint = (Button)findViewById(R.id.btnBasicConstTapHint);
        btnTapHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonData.backFrom = CommonData.PAGE_BASIC;

                Intent intent = new Intent(BasicActivity.this, PriceActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetBackInAnim(), TransformManager.GetBackOutAnim());
                finish();
            }
        });

        btnTapHint.setVisibility(View.INVISIBLE);
        observer.addBtnTapHint(btnTapHint);

        observer.addMoveView(btnFirst);
        observer.addMoveView(btnSecond);
        observer.addMoveView(btnThird);
        observer.addMoveView(btnForth);
        observer.addMoveView(findViewById(R.id.imageViewBasicTitle));

        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutBasic));

        // if user selected option that is not suitable, show yellow mark to select price again.
        if (!_suitableOption.isBasicSuitable(selectedBasic))
        {
            if (!btnFirst.isSelectable())
                observer.onButtonTouched(btnFirst);
            else if (!btnSecond.isSelectable())
                observer.onButtonTouched(btnSecond);
            else if (!btnThird.isSelectable())
                observer.onButtonTouched(btnThird);
            else if (!btnForth.isSelectable())
                observer.onButtonTouched(btnForth);
        }
    }

    // set buttons' initial state(selected or normal)
    private void setInitialState(int selectedState, SelectButton btn) {
        if (selectedState == 0)
        {
            //btn.setState(SelectButton.SELBTNSTATE_NORMAL, true);
            if (btn.isSelectable())
                btn.setState(SelectButton.SELBTNSTATE_NORMAL, true);
            else
                btn.setState(SelectButton.SELBTNSTATE_DISABLED, true);
        }
        else
        {
            if (btn.isSelectable())
                btn.setState(SelectButton.SELBTNSTATE_SELECTED, true);
            else
                btn.setState(SelectButton.SELBTNSTATE_DISABLED, true);
        }
    }

    // get buttons' selected state
    private int getSelectedState() {
        int selectedBasic = 0;
        selectedBasic = (selectedBasic | ((btnFirst.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.BASIC_FIRST));
        selectedBasic = (selectedBasic | ((btnSecond.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.BASIC_SECOND));
        selectedBasic = (selectedBasic | ((btnThird.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.BASIC_THIRD));
        selectedBasic = (selectedBasic | ((btnForth.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.BASIC_FORTH));
        return selectedBasic;
    }

    // save selected state of buttons
    @Override
    public void onStop() {
        super.onStop();


    }
}