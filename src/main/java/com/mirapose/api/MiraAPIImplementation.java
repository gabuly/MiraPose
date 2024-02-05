package com.mirapose.api;

import com.mirapose.PlayerMira;
import com.mirapose.api.IMiraAPI;

public class MiraAPIImplementation implements IMiraAPI {
    // This should reference your capability or however you're storing mira
    private PlayerMira playerMira;

    public MiraAPIImplementation(PlayerMira playerMira) {
        this.playerMira = playerMira;
    }

    @Override
    public int getMira() {
        return playerMira.getMira();
    }
//    @Override
//    public boolean getSwitch() {
//        return playerMira.canSwitch;
//    }




}
