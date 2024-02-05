package com.mirapose;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class PlayerMira {
    private int mira;
    //public boolean  canSwitch=false;
    private final int MIN_mira = 0;
    private int MAX_mira = 10;

    public int getMira() {
        return mira;
    }

    public  void addMira(int add) {
        this.mira = Math.min(mira + add, MAX_mira);
    }

    public void subMira(int sub) {
        this.mira = Math.max(mira - sub, MIN_mira);
    }

//    public void turnSwitch() {
//        if(canSwitch==true){
//            canSwitch=false;
//            return;
//        }
//        canSwitch=true;
//    }
//    public boolean getSwitch() {
//        return canSwitch;
//    }
    public void copyFrom(PlayerMira source) {
        this.mira=source.mira;
        // The implementation is missing here, but it would typically be something like:
        // this.mira = source.mira;
    }
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("mira",mira);

    }

    public void loadNBTData(CompoundTag nbt) {
        mira = nbt.getInt("mira");
    }


}
