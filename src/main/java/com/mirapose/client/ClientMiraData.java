package com.mirapose.client;

public class ClientMiraData {
    private static int playerMira;

    public static void set(int mira){
       ClientMiraData.playerMira=mira;
    }
    public static void sub(){
    ClientMiraData.playerMira--;
    }
    public static int getMira(){
        return playerMira;
    }
}
