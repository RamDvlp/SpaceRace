package com.example.carrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final int DELAY = 500;

    private int livescount = 3;
    private Timer timer;
    private int score =0;
    private MaterialTextView score_LBL;

    private MaterialButton btn_Left;
    private MaterialButton btn_Right;
    private AppCompatImageView backGround;

    private AppCompatImageView[][] meteorites;

    private AppCompatImageView[] lives;

    private AppCompatImageView[] rocketShips;

    private GameRules gameRules = new GameRules(new Random());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initViews();
        setTick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer();
    }


    private void setTick() {



        gameRules.updateGameMesh();

        if(gameRules.isCoinColected()) {
            score += 10;
            String displayScore = String.valueOf(score);
            score_LBL.setText(displayScore);

        }

        int[][] rocklocs = gameRules.getGameMesh();
        for(int i=0;i<meteorites.length;i++){
            for(int k=0;k<meteorites[0].length;k++){
                if(rocklocs[i][k] == GameRules.ROCK){
                    meteorites[i][k].setImageResource(R.drawable.meteore);
                    meteorites[i][k].setVisibility(View.VISIBLE);
                }
                if(rocklocs[i][k] == GameRules.COIN){
                    meteorites[i][k].setImageResource(R.drawable.ic_coin);
                    meteorites[i][k].setVisibility(View.VISIBLE);
                }
                if(rocklocs[i][k] == GameRules.NOTHING){
                    meteorites[i][k].setVisibility(View.INVISIBLE);
                }
            }
        }

        if(gameRules.isColition()){
            preformColition();

        }else{
            btn_Right.setEnabled(true);
            btn_Left.setEnabled(true);

            postColition();
        }





    }

    private void postColition() {
        rocketShips[colitionPlace].setImageResource(R.drawable.rocket);
    }


    private void startTimer() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setTick();
                    }
                });
            }
        }, 0, DELAY);
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void initViews() {

        backGround = findViewById(R.id.main_img_back);

        Glide.with(MainActivity.this)
                .load(R.drawable.night_sky)
                .into(backGround);

        score_LBL = findViewById(R.id.game_LBL_score);
        meteorites = new AppCompatImageView[][]{{findViewById(R.id.main_IMG_meteor_00), findViewById(R.id.main_IMG_meteor_01), findViewById(R.id.main_IMG_meteor_02)},
                {findViewById(R.id.main_IMG_meteor_10), findViewById(R.id.main_IMG_meteor_11), findViewById(R.id.main_IMG_meteor_12)},
                {findViewById(R.id.main_IMG_meteor_20),findViewById(R.id.main_IMG_meteor_21),findViewById(R.id.main_IMG_meteor_22)},
                {findViewById(R.id.main_IMG_meteor_30),findViewById(R.id.main_IMG_meteor_31),findViewById(R.id.main_IMG_meteor_32)},
                {findViewById(R.id.main_IMG_meteor_40),findViewById(R.id.main_IMG_meteor_41),findViewById(R.id.main_IMG_meteor_42)},
                {findViewById(R.id.main_IMG_meteor_50),findViewById(R.id.main_IMG_meteor_51),findViewById(R.id.main_IMG_meteor_52)}};

        for (int i =0; i<meteorites.length; i++){
            for(int j=0; j<meteorites[0].length;j++){
                meteorites[i][j].setVisibility(View.INVISIBLE);
            }
        }


        lives = new AppCompatImageView[]{findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)};

        rocketShips = new AppCompatImageView[]{findViewById(R.id.main_IMG_rocket1),
                findViewById(R.id.main_IMG_rocket2),
                findViewById(R.id.main_IMG_rocket3)};

        btn_Left = findViewById(R.id.main_BTN_left);
        btn_Right = findViewById(R.id.main_BTN_right);

        btn_Left.setOnClickListener(view -> moveRocket(true));
        btn_Right.setOnClickListener(view -> moveRocket(false));
    }

    private void moveRocket(boolean left) {
        gameRules.moveRocket(left);
        updateRocketUI();

    }

    private int colitionPlace = 0;
    private void preformColition() {

        colitionPlace = gameRules.getCurrentRocketLocation();
        rocketShips[colitionPlace].setImageResource(R.drawable.explosion);
        livescount--;
        lives[livescount].setVisibility(View.INVISIBLE);

        btn_Right.setEnabled(false);
        btn_Left.setEnabled(false);

        if(livescount==0)
            gameOver();
    }

    private void gameOver() {
        onStop();
        btn_Right.setEnabled(false);
        btn_Left.setEnabled(false);
    }

    private void updateRocketUI() {
        int[] currentRocketLoc = gameRules.getRocket();
        for(int i=0; i<rocketShips.length;i++){
            if(currentRocketLoc[i] == GameRules.ROCKET){
                rocketShips[i].setVisibility(View.VISIBLE);

            }else{
                rocketShips[i].setVisibility(View.INVISIBLE);
            }
        }
    }


}