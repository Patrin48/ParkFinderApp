package com.example.parkfinder;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mTimer = (TextView) findViewById(R.id.tv);
        ImageView Logo = (ImageView) findViewById(R.id.imageView2);
        EditText SaleDescription = (EditText) findViewById(R.id.editText);
        EditText SecretCode = (EditText) findViewById(R.id.editText2);

        Bundle b = getIntent().getExtras();
        int LogoImage = b.getInt("logotype");
        String SaleText = b.getString("saledescript");
        String MyOwnCode = b.getString("mycode");
        //Set Image from Java Code
        Logo.setImageDrawable(ContextCompat.getDrawable(this, LogoImage));
        Logo.getLayoutParams().height=1000;
        Logo.getLayoutParams().width=1000;
        SaleDescription.setText(SaleText);
        SecretCode.setText(MyOwnCode);
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimer.setText("Осталось: "
                        + millisUntilFinished / 1000 + " секунд.");
            }

            public void onFinish() {
                mTimer.setText("Вы не успели!");
                finish();
            }
        }
                .start();
    }
}
