package com.example.yolo.capp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;

public class PatternLock extends AppCompatActivity {

    String PATTERN_KEY = "pattern_code";
    PatternLockView patternLockView;
    Button button;
    TextView textView;
    String finalPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock);
        button = (Button) findViewById(R.id.button_set_pattern);
        textView = (TextView)findViewById(R.id.text_set_pattern);
        patternLockView = (PatternLockView)findViewById(R.id.pattern_lock_view);

        Paper.init(this);
        final String savedPattern = Paper.book().read(PATTERN_KEY);
        if (savedPattern!=null && !savedPattern.equals("null")){
            button.setVisibility(View.GONE);
            textView.setText("Draw Pattern");
            patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {

                    finalPassword = PatternLockUtils.patternToString(patternLockView,pattern);

                    if (finalPassword.equals(savedPattern)){
                        Intent intent = new Intent(getBaseContext(),MainActivity.class);
                        startActivity(intent);

                    }else {

                        Toast.makeText(getBaseContext(),"Wrong Password",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCleared() {

                }
            });
        }
        else {
           button.setVisibility(View.VISIBLE);
            textView.setText("Set Pattern");
            patternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {

                    finalPassword = PatternLockUtils.patternToString(patternLockView,pattern);
                }

                @Override
                public void onCleared() {

                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Paper.book().write(PATTERN_KEY,finalPassword);
                    Snackbar snackbar = Snackbar.make(patternLockView,"Password Saved ! ",Snackbar.LENGTH_LONG)
                            .setAction("Continue", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                    snackbar.show();

                }
            });
        }
    }
}
