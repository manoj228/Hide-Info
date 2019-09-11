package com.manojkumar.hideinfo;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.math.BigInteger;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText eti,eto;
    private String publicKey = "";
    private String privateKey = "";
    private byte[] encodeData = null;
    private ImageView ig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eti = findViewById(R.id.editText);
        eto = findViewById(R.id.editText2);
        ig = findViewById(R.id.ivqrcode);

        try {
            Map<String, Object> keyMap = rsa.initKey();
            publicKey = rsa.getPublicKey(keyMap);
            privateKey = rsa.getPrivateKey(keyMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void encrypt(View v){


        byte[] rsaData = eti.getText().toString().getBytes();
        try {
            encodeData= rsa.encryptByPublicKey(rsaData, getPublicKey());
            String encodeStr = new BigInteger(1, encodeData).toString();
            eto.setText(encodeStr);


            String email = eto.getText().toString();
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int x = point.x;
            int y = point.y;

            int icon = x < y ? x : y;
            icon = icon * 3/4;


            //library
           QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(email , null , Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString() , icon);

            // convert image into bitmap
            try {
                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                ig.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decrypt(View v){

        try {
           byte[] decodeData= rsa.encryptByPrivateKey(encodeData, getPrivateKey());
            String decodeStr = new String(decodeData);
            eto.setText(decodeStr);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }


}
