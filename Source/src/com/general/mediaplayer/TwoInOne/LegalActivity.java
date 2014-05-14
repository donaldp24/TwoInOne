package com.general.mediaplayer.TwoInOne;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/17/14
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class LegalActivity extends BaseActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal);

        ImageButton btnBack = (ImageButton)findViewById(R.id.btnLegalBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(LegalActivity.this, ResultActivity.class);
                //startActivity(intent);
                finish();
                overridePendingTransition(TransformManager.GetBackInAnim(), TransformManager.GetBackOutAnim());
            }
        });

        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutLegal));
    }
}