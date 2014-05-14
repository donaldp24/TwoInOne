package com.general.mediaplayer.TwoInOne;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android_serialport_api.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/17/14
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResultActivity extends BaseActivity {

    protected Application mApplication = null;
    protected SerialPort mSerialPort = null;
    protected OutputStream mOutputStream = null;
    private InputStream mInputStream = null;
    private ReadThread mReadThread = null;

    private class ReadThread extends Thread {

        @Override
        public void run() {
            super.run();
            while(!isInterrupted()) {
                int size;
                try {
                    byte[] buffer = new byte[64];
                    if (mInputStream == null) return;
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        onDataReceived(buffer, size);

                        Log.v("ResultActivity", "ReadThread size:" + size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private void DisplayError(int resourceId) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Error");
        b.setMessage(resourceId);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //ResultActivity.this.finish();
            }
        });
        b.show();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        // related with serial
        if (CommonData.USE_SERIAL == 1)
        {
            mApplication = (Application) getApplication();
            try {
                mSerialPort = mApplication.getSerialPort();
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();

                /* Create a receiving thread */
                mReadThread = new ReadThread();
                mReadThread.start();
            } catch (SecurityException e) {
                DisplayError(R.string.error_security);
            } catch (IOException e) {
                DisplayError(R.string.error_unknown);
            } catch (InvalidParameterException e) {
                DisplayError(R.string.error_configuration);
            }
        }


        // transform to Special Offer
        ImageButton btnSpecial = (ImageButton)findViewById(R.id.btnResultSpecial);
        btnSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, SpecialActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                finish();
            }
        });

        // transform to Start Screen
        ImageButton btnStartOver = (ImageButton)findViewById(R.id.btnResultStartOver);
        btnStartOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, ScanMediaActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetBackInAnim(), TransformManager.GetBackOutAnim());
                finish();
            }
        });

        // transform to Legal Screen
        ImageButton btnLegal = (ImageButton)findViewById(R.id.btnResultLegal);
        btnLegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, LegalActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetContinueInAnim(), TransformManager.GetContinueOutAnim());
                //finish();
            }
        });


        CommonData.SuitableOption _optionSelected = CommonData.getSuitableOption(_appPrefs.getSelectedPrice());
        int idMark = R.drawable.result_slide_mark;
        int idExplain = R.drawable.result_slide_explain;
        int idCpu = R.drawable.result_slide_cpu;
        switch (_optionSelected._result)
        {
            case CommonData.RESULT_SLIDE:
                idMark = R.drawable.result_slide_mark;
                idExplain = R.drawable.result_slide_explain;
                idCpu = R.drawable.result_slide_cpu;
                break;
            case CommonData.RESULT_SPIN:
                idMark = R.drawable.result_spin_mark;
                idExplain = R.drawable.result_spin_explain;
                idCpu = R.drawable.result_slide_cpu;
                break;
            case CommonData.RESULT_FLIP:
                idMark = R.drawable.result_flip_mark;
                idExplain = R.drawable.result_flip_explain;
                idCpu = R.drawable.result_slide_cpu;
                break;
            case CommonData.RESULT_DETACH:
                idMark = R.drawable.result_detach_mark;
                idExplain = R.drawable.result_detach_explain;
                idCpu = R.drawable.result_slide_cpu;
                break;
            default:
                idMark = R.drawable.result_slide_mark;
                idExplain = R.drawable.result_slide_explain;
                idCpu = R.drawable.result_detach_cpu;
                break;
        }
        findViewById(R.id.imageViewResultMark).setBackgroundResource(idMark);
        findViewById(R.id.imageViewResultExplain).setBackgroundResource(idExplain);
        findViewById(R.id.imageViewResultCpu).setBackgroundResource(idCpu);
        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutResult));
    }

    @Override
    public void onStart() {
        super.onStart();

        // turn LED on
        if (CommonData.USE_SERIAL == 1)
        {
            CommonData.SuitableOption _optionSelected = CommonData.getSuitableOption(_appPrefs.getSelectedPrice());
            int nSubID = 0;
            switch (_optionSelected._result)
            {
                case CommonData.RESULT_SLIDE:
                    nSubID = 1;
                    break;
                case CommonData.RESULT_SPIN:
                    nSubID = 2;
                    break;
                case CommonData.RESULT_FLIP:
                    nSubID = 3;
                    break;
                case CommonData.RESULT_DETACH:
                    nSubID = 4;
                    break;
                default:
                    nSubID = 2;
                    break;
            }

            SendSubN(nSubID);
        }

    }

    @Override
    public void onDestroy() {

        if (mReadThread != null)
            mReadThread.interrupt();
        //mApplication.closeSerialPort();
        mSerialPort = null;

        super.onDestroy();
    }

    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                    String RecepString = "";
                    RecepString=ByteToHexString(buffer,size);
                    RecepString=RecepString+"\n";
            }
        });
    }

    public static String ByteToHexString( byte[] b,int size)
    {
        String hexString="";
        for (int i = 0; i < size; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            hexString=hexString+" "+hex;
        }
        return hexString;
    }

    protected boolean SendSubN(int SubID)
    {

        int i=0;
        int j=0;
        boolean ret=false;
        byte[] buffer = new byte[5];
        byte SubIDTemp;
        SubIDTemp=(byte)SubID;
        if(SubID>0)
        {

            SubIDTemp=(byte)(SubID-1);

        }
        buffer[i++]=(byte)0xF5;
        buffer[i++]=0x04;
        buffer[i++]=0x01;
        buffer[i++]=SubIDTemp;
        buffer[i]=0x00;
        for(j=0;j<i;j++)
        {
            buffer[i]=(byte)(buffer[i]+buffer[j]);
        }
        try {
            if (mOutputStream != null)
                mOutputStream.write(buffer);
            ret=true;
            //mOutputStream.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}