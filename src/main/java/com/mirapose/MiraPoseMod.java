package com.mirapose;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

@Mod("mirapose")
@Mod.EventBusSubscriber
public class MiraPoseMod {
    public boolean miraSwitch=false;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public MiraPoseMod(){

    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag persistentData = player.getPersistentData();
        CompoundTag modData;

        // Create a new tag to store your mod's data if it doesn't exist
        if (!persistentData.contains("mirapose:mira")) {
            modData = new CompoundTag();
            persistentData.put("mirapose:mira", modData);
        } else {
            modData = persistentData.getCompound("mirapose:mira");
        }

        // Set your data
        modData.putInt("mira", 100); // Just an example; you can set any data you want
    }

//@SubscribeEvent
//    public static void onXpGain(PlayerXpEvent.PickupXp event) {
//    CompoundTag NBTdata =  event.getEntity().getPersistentData();
//    event.getEntity().sendSystemMessage(Component.literal("mira detected ::::: "+NBTdata.getInt("mirapose:mira")));
//}}


//
//
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerMira.class);
   }
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerMiraProvider.PLAYER_Mira).isPresent()) {
                event.addCapability(new ResourceLocation("mirapose", "miraproperty"), new PlayerMiraProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
//
////    @SubscribeEvent
////    public static void onRegisterCapability(RegisterCapabilitiesEvent event) {
////    event.register(PlayerMira.class);
////}

    @SubscribeEvent
    public static void onXpGain(PlayerXpEvent.PickupXp event) {
        Player player = (Player) event.getEntity();
        player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira ->{
        mira.addMira(event.getOrb().getValue());
        //LOGGER.info(ANSI_GREEN + "MIRA Gained: " + mira.getMira() + ANSI_RESET);
            player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(playerMira -> {
                player.sendSystemMessage(Component.literal("mira = "+ mira.getMira()));
            });

            if(mira.getMira()>=10){
                LOGGER.info(ANSI_GREEN + "CANMira!!! " + ANSI_RESET);
               // mira.turnSwitch();
                mira.subMira(10);
            }


        });
    }}


//    @SubscribeEvent
//    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        Player player = event.getEntity();
//        CompoundTag playerData = player.getPersistentData();
//
//        // Check if the mod's player data compound exists, if not create it
//        if (!playerData.contains("mirapose:player_data",TAG_COMPOUND)) {
//            LOGGER.info(ANSI_GREEN + "No tag!!! " + ANSI_RESET);
//            playerData.put("mirapose:player_data", new CompoundTag());
//
//        }
//        CompoundTag modData = playerData.getCompound("mirapose:player_data");
//
//        // Initialize miraSwitch if it doesn't exist
//        if (!modData.contains("miraSwitch")) {
//            modData.putBoolean("miraSwitch", false); // Default value is false
//        }
//
//        // Initialize Mira if it doesn't exist
//        if (!modData.contains("mira")) {
//            modData.putInt("mira", 0); // Default value is 0
//        }
//
//        // No need to put modData back into playerData, as it is already a reference to the nested tag
//    }

//    @SubscribeEvent
//    public static void onXpGain(PlayerXpEvent.PickupXp event) {
//        Player player = (Player) event.getEntity();
//        int miraGain = event.getOrb().getValue();
//        CompoundTag playerData = player.getPersistentData();
//        // Assuming "mira" is already initialized somewhere
//        playerData.putInt("mira", playerData.getInt("mira")+ miraGain);
//        int currentMira = playerData.getInt("mira");
//        player.sendSystemMessage(Component.literal("mira = "+ currentMira));
//            if(currentMira>=10){
//               // switchMira();
//                LOGGER.info(ANSI_GREEN + "CANMira!!! " + ANSI_RESET);
//                playerData.putInt("mira", 0);
//            } }



//            miraSwitch = true;
//
//        }
//
//        }
//
////        public static void switchMira(){
////         if (!miraSwitch){
////             miraSwitch=true;
////             return;
////         }
////         miraSwitch=false;
////        }
////    public boolean getCanSwitch(){
////        return miraSwitch;
////
////    }
//
//    }
//
//
//        // Now 'mira' is guaranteed to exist, either we just created it or it was already there

