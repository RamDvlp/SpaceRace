package com.example.carrace;

import java.util.Random;

public class GameRules {

    public static final int NOTHING = 0;
    public static final int ROCK = 1;
    public static final int COIN = 2;
    public static final int ROCKET = 3;
    public static final int COLITIONLAYER = 6;

    private int RockAppearOnEven = 0;

    private int [][] gameMesh =
            {{0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0},
            {0,0,0}};

    private int [] rocket = {0,3,0};

    private boolean isColition = false;
    private int currentRocketLocation = 1;

    private Random random;
    private boolean coinColected;

    public GameRules() {}

    public GameRules(Random random) {
        this.random = random;
    }

    public void updateRocketLocation(int loc){

        for (int i = 0; i < rocket.length; i++) {
            rocket[i]=NOTHING;
        }
        this.rocket[loc]=ROCKET;
        currentRocketLocation = loc;

    }

    public void moveRocket(boolean left){

        if(left){
            if(rocket[0]==ROCKET) {
                return;
            } else {
                for (int i =1; i<rocket.length;i++){
                    if(rocket[i] == ROCKET){
                        updateRocketLocation(i-1);
                    }
                }
            }
        } else {
            if(rocket[2]==ROCKET) {
                return;
            } else {
                for (int i =0; i<rocket.length-1;i++){
                    if(rocket[i] == ROCKET){
                        updateRocketLocation(i+1);
                        break;
                    }
                }
            }
        }


    }

    public void updateGameMesh(){

        for(int i=gameMesh.length-2;i >=0 ;i--){
            for (int k=gameMesh[0].length-1;k>=0 ;k--){
                if(gameMesh[i][k]==ROCK){
                    gameMesh[i+1][k] =ROCK;
                    gameMesh[i][k] = NOTHING;
                }
                if(gameMesh[i][k]==COIN){
                    gameMesh[i+1][k] =COIN;
                    gameMesh[i][k] = NOTHING;
                }
            }
        }

        setColition();
        newRockAndCoinAppearance();

        //Reset Colition
        for (int i = 0; i < gameMesh[COLITIONLAYER].length; i++) {
            gameMesh[COLITIONLAYER][i] = NOTHING;
        }

    }

    private void newRockAndCoinAppearance() {
//
//        if(RockAppearOnEven%2!=0) {
//            RockAppearOnEven++;
//            return;
//        }
//        RockAppearOnEven++;

        /*
        Rock can "Override" coin.
         */
        int appearCol = random.nextInt(3);
        gameMesh[0][appearCol] = COIN;
        appearCol = random.nextInt(3);
        gameMesh[0][appearCol] = ROCK;


    }

    public void setColition (){

        if(gameMesh[COLITIONLAYER][currentRocketLocation] == ROCK){
            isColition = true;
        } else {
            isColition = false;
        }


        if(gameMesh[COLITIONLAYER][currentRocketLocation] == COIN){
            coinColected = true;
        } else {
            coinColected = false;
        }

    }

    public boolean isColition() {
        return isColition;
    }

    public boolean isCoinColected() {
        return coinColected;
    }

    public int[][] getGameMesh() {
        return gameMesh;
    }

    public int[] getRocket() {
        return rocket;
    }

    public int getCurrentRocketLocation() {
        return currentRocketLocation;


    }
}
