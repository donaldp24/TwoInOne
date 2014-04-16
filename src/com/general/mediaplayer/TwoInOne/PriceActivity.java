package com.general.mediaplayer.TwoInOne;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/17/14
 * Time: 9:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriceActivity extends BaseActivity {

    private ImageView imgPicker = null;
    private int[] resIdPoints = {R.id.btnPricePickPoint1, R.id.btnPricePickPoint2, R.id.btnPricePickPoint3, R.id.btnPricePickPoint4};
    private Point ptCurr = new Point(0, 0);
    private int currSelectedIndex = 0;
    private boolean bGrabbed = false;
    private double moveScope = 100;
    private Point ptDown = new Point(0, 0);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.price);


        // Button Continue
        ImageButton btnContinue = (ImageButton)findViewById(R.id.btnPriceContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save selected option
                _appPrefs.setSelectedPrice(currSelectedIndex);


                // get selected basic option
                int selectedBasic = _appPrefs.getSelectedBasic();
                CommonData.SuitableOption selectedOption = CommonData.getSuitableOption(currSelectedIndex);

                if (CommonData.backFrom == CommonData.PAGE_ADVANCED)
                {
                    CommonData.backFrom = CommonData.PAGE_NONE;

                    // goto next(basic page) when first selecting
                    if (selectedBasic == CommonData.BASIC_NONE)
                    {
                        Intent intent = new Intent(PriceActivity.this, BasicActivity.class);
                        startActivity(intent);
                        overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                        finish();
                    }

                    // if user already selected a option and went into this page through tapping hint.
                    else if (selectedOption.isBasicSuitable(selectedBasic))
                    {
                        // goto advanced page
                        Intent intent = new Intent(PriceActivity.this, AdvancedActivity.class);
                        startActivity(intent);
                        overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                        finish();
                    }
                    else
                    {
                        // goto basic page
                        Intent intent = new Intent(PriceActivity.this, BasicActivity.class);
                        startActivity(intent);
                        overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                        finish();
                    }
                }
                else if (CommonData.backFrom == CommonData.PAGE_BASIC)
                {
                    CommonData.backFrom = CommonData.PAGE_NONE;

                    Intent intent = new Intent(PriceActivity.this, BasicActivity.class);
                    startActivity(intent);
                    overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                    finish();
                }
                else
                {
                    CommonData.backFrom = CommonData.PAGE_NONE;

                    // goto basic page
                    Intent intent = new Intent(PriceActivity.this, BasicActivity.class);
                    startActivity(intent);
                    overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                    finish();
                }
            }
        });

        // Picker
        imgPicker = (ImageView)findViewById(R.id.imageViewPriceSlidePicker);
        imgPicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)imgPicker.getLayoutParams();
                    ptCurr = new Point(layoutParams.leftMargin, layoutParams.topMargin);
                    bGrabbed = true;
                    ptDown = new Point((int)event.getX(), (int)event.getY());
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    // move picker
                    if (bGrabbed == true)
                    {
                        RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)imgPicker.getLayoutParams();
                        int left = layoutParams.leftMargin + (int)event.getX();

                        ImageButton btnPickerMostLeftPos = (ImageButton)findViewById(resIdPoints[0]);
                        RelativeLayout.LayoutParams leftParams =  (RelativeLayout.LayoutParams)btnPickerMostLeftPos.getLayoutParams();

                        ImageButton btnPickerMostRightPos = (ImageButton)findViewById(resIdPoints[resIdPoints.length - 1]);
                        RelativeLayout.LayoutParams rightParams =  (RelativeLayout.LayoutParams)btnPickerMostRightPos.getLayoutParams();
                        left -= ptDown.x;
                        if (left > rightParams.leftMargin)
                            left = rightParams.leftMargin;
                        if (left < leftParams.leftMargin)
                            left = leftParams.leftMargin;

                        layoutParams.leftMargin = left;
                        imgPicker.setLayoutParams(layoutParams);
                    }
                }

                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    bGrabbed = false;
                    RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)imgPicker.getLayoutParams();
                    int correctIndex = -1;
                    int rightIndex = -1;
                    for (int i = 0; i < resIdPoints.length; i++)
                    {
                        ImageButton btnPickerPos = (ImageButton)findViewById(resIdPoints[i]);
                        RelativeLayout.LayoutParams orgParams =  (RelativeLayout.LayoutParams)btnPickerPos.getLayoutParams();
                        if (layoutParams.leftMargin <= orgParams.leftMargin)
                        {
                            rightIndex = i;
                            break;
                        }
                    }

                    if (rightIndex == -1)
                        correctIndex = resIdPoints.length - 1;
                    else if (rightIndex == 0)
                        correctIndex = 0;
                    else
                    {
                        ImageButton btnPickerLeftPos = (ImageButton)findViewById(resIdPoints[rightIndex - 1]);
                        RelativeLayout.LayoutParams leftParams =  (RelativeLayout.LayoutParams)btnPickerLeftPos.getLayoutParams();
                        ImageButton btnPickerRightPos = (ImageButton)findViewById(resIdPoints[rightIndex]);
                        RelativeLayout.LayoutParams rightParams =  (RelativeLayout.LayoutParams)btnPickerRightPos.getLayoutParams();
                        if ((layoutParams.leftMargin - leftParams.leftMargin) <= (rightParams.leftMargin - layoutParams.leftMargin))
                            correctIndex = rightIndex - 1;
                        else
                            correctIndex = rightIndex;
                    }

                    ImageButton btnPickerPos = (ImageButton)findViewById(resIdPoints[correctIndex]);
                    RelativeLayout.LayoutParams orgParams =  (RelativeLayout.LayoutParams)btnPickerPos.getLayoutParams();
                    layoutParams.leftMargin = orgParams.leftMargin;
                    imgPicker.setLayoutParams(layoutParams);

                    currSelectedIndex = correctIndex;
                }
                return true;
            }
        });

        // hide points
        for(int i = 0; i < resIdPoints.length; i++)
        {
            ImageButton btn = (ImageButton)findViewById(resIdPoints[i]);
            //v.setVisibility(View.INVISIBLE);
            final int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout.LayoutParams orgParams =  (RelativeLayout.LayoutParams)v.getLayoutParams();
                    RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)imgPicker.getLayoutParams();
                    layoutParams.leftMargin = orgParams.leftMargin;
                    imgPicker.setLayoutParams(layoutParams);
                    currSelectedIndex = finalI;
                }
            });
        }

        // set default position to Picker.
        currSelectedIndex = _appPrefs.getSelectedPrice();
        ImageButton btnPickerDefaultPos = (ImageButton)findViewById(resIdPoints[currSelectedIndex]);
        RelativeLayout.LayoutParams defaultParams =  (RelativeLayout.LayoutParams)btnPickerDefaultPos.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)imgPicker.getLayoutParams();
        layoutParams.leftMargin = defaultParams.leftMargin;
        imgPicker.setLayoutParams(layoutParams);

        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutPrice));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // save selected state
    @Override
    public void onStop() {
        super.onStop();
    }
}