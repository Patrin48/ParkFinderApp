package com.example.parkfinder;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private TextView mTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mTimer = (TextView) findViewById(R.id.tv);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimer.setText("Осталось: "
                        + millisUntilFinished / 1000 + " секунд.");
            }

            public void onFinish() {
                mTimer.setText("Вы не успели!");
            }
        }
                .start();
    }
}
