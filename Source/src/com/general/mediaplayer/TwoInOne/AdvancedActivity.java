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
public class AdvancedActivity extends BaseActivity {
    private SelectButtonsObserver observer;
    private SelectButton btnFirst;
    private SelectButton btnSecond;
    private SelectButton btnThird;
    private SelectButton btnForth;
    private Button btnTapHint;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced);

        ImageButton btnContinue = (ImageButton)findViewById(R.id.btnAdvancedContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save current selected option
                int selectedAdvanced = getSelectedState();
                _appPrefs.setSelectedAdvanced(selectedAdvanced);

                Intent intent = new Intent(AdvancedActivity.this, ResultActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                finish();
            }
        });

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnAdvancedBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // reset current selection.
                _appPrefs.setSelectedAdvanced(CommonData.ADVANCED_NONE);

                Intent intent = new Intent(AdvancedActivity.this, BasicActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetBackInAnim(), TransformManager.GetBackOutAnim());
                finish();
            }
        });

        CommonData.SuitableOption _suitableOption = CommonData.getSuitableOption(_appPrefs.getSelectedPrice());

        // Selection Buttons
        btnFirst = (SelectButton)findViewById(R.id.btnAdvancedFirst);
        btnSecond = (SelectButton)findViewById(R.id.btnAdvancedSecond);
        btnThird = (SelectButton)findViewById(R.id.btnAdvancedThird);
        btnForth = (SelectButton)findViewById(R.id.btnAdvancedForth);

        btnFirst.setSelectable(_suitableOption.isAdvancedSuitableWithOne(CommonData.ADVANCED_FIRST));
        btnFirst.setBackgroundImages(R.drawable.advanced_btnfirst_normal, R.drawable.advanced_btnfirst_selected, R.drawable.advanced_btnfirst_disabled);
        btnFirst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnFirst);
            }
        });

        btnSecond.setSelectable(_suitableOption.isAdvancedSuitableWithOne(CommonData.ADVANCED_SECOND));
        btnSecond.setBackgroundImages(R.drawable.advanced_btnsecond_normal, R.drawable.advanced_btnsecond_selected, R.drawable.advanced_btnsecond_disabled);
        btnSecond.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnSecond);
            }
        });

        btnThird.setSelectable(_suitableOption.isAdvancedSuitableWithOne(CommonData.ADVANCED_THIRD));
        btnThird.setBackgroundImages(R.drawable.advanced_btnthird_normal, R.drawable.advanced_btnthird_selected, R.drawable.advanced_btnthird_disabled);
        btnThird.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                observer.onButtonTouched(btnThird);
            }
        });

        btnForth.setSelectable(_suitableOption.isAdvancedSuitableWithOne(CommonData.ADVANCED_FORTH));
        btnForth.setBackgroundImages(R.drawable.advanced_btnforth_normal, R.drawable.advanced_btnforth_selected, R.drawable.advanced_btnforth_disabled);
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
        int selectedAdvanced = _appPrefs.getSelectedAdvanced();
        if (selectedAdvanced != CommonData.ADVANCED_NONE) {
            int _selectedFirst = (selectedAdvanced & (1 << CommonData.ADVANCED_FIRST) ) != 0 ? 1 : 0;
            int _selectedSecond = (selectedAdvanced & (1 << CommonData.ADVANCED_SECOND) ) != 0 ? 1 : 0;
            int _selectedThird = (selectedAdvanced & (1 << CommonData.ADVANCED_THIRD) ) != 0 ? 1 : 0;
            int _selectedForth = (selectedAdvanced & (1 << CommonData.ADVANCED_FORTH) ) != 0 ? 1 : 0;
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
        btnTapHint = (Button)findViewById(R.id.btnAdvancedConstTapHint);
        btnTapHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonData.backFrom = CommonData.PAGE_ADVANCED;

                Intent intent = new Intent(AdvancedActivity.this, PriceActivity.class);
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
        observer.addMoveView(findViewById(R.id.imageViewAdvancedTitle));

        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutAdvanced));

        // if user selected option that is not suitable, show yellow mark to select price again.
        if (!_suitableOption.isAdvancedSuitable(selectedAdvanced))
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
        int selectedAdvanced = 0;
        selectedAdvanced = (selectedAdvanced | ((btnFirst.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.ADVANCED_FIRST));
        selectedAdvanced = (selectedAdvanced | ((btnSecond.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.ADVANCED_SECOND));
        selectedAdvanced = (selectedAdvanced | ((btnThird.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.ADVANCED_THIRD));
        selectedAdvanced = (selectedAdvanced | ((btnForth.getState() == SelectButton.SELBTNSTATE_SELECTED ? 1 : 0) << CommonData.ADVANCED_FORTH));
        return selectedAdvanced;
    }

    // save selected state of buttons
    @Override
    public void onStop() {
        super.onStop();


    }
}