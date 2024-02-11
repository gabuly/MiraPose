package com.mirapose.events;

import com.mirapose.PlayerMira;
import com.mirapose.PlayerMiraProvider;
import com.mirapose.client.ClientMiraData;
import com.mirapose.network.C2SPacket;
import com.mirapose.network.ModMessages;
import com.mirapose.network.S2CPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.client.KeyMapping;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.events.SwitchBattleEvent;

import yesman.epicfight.events.SwitchMiningEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import java.util.List;


@Mod.EventBusSubscriber(modid = "mirapose")
public class ModEvents {
    public static  PlayerPatch<?> getPlayerPatch(Player player) {
        LazyOptional<EntityPatch> entityPatchOptional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
        if (entityPatchOptional.isPresent()) {
            EntityPatch entityPatch = entityPatchOptional.orElse(null);
            if (entityPatch instanceof PlayerPatch playerPatch) {
                return playerPatch;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
                if(getPlayerPatch(event.player).isBattleMode()) {
                    mira.subMira();
                    event.player.sendSystemMessage(Component.literal(+mira.getMira() + " mira left!! (S)" ));
                    if(mira.getMira()<=0){
                        getPlayerPatch(event.player).toggleMode();
                    }
                    ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), (ServerPlayer)event.player);
                    event.player.sendSystemMessage(Component.literal(+ClientMiraData.getMira() + " tick client mira left!! (C)" ));
                }
            });
        }

    }


    @SubscribeEvent
    public static void SwitchBattleEvent(SwitchBattleEvent event) {
        event.getEntity().sendSystemMessage(Component.literal(" Swtich eVENTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT" ));
        miraWeaponReplace(event.getEntity());
    }

    @SubscribeEvent
    public static void SwitchMinigEvent(SwitchMiningEvent event) {
        event.getEntity().sendSystemMessage(Component.literal(" Swtich BACK======================" ));
        miraWeaponBack(event.getEntity());
    }
    public static void  miraWeaponReplace(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag modData;

        // Check if our mod's tag already exists
        if (playerData.contains("MiraTag")) {
            modData = playerData.getCompound("MiraTag");
        } else {
            modData = new CompoundTag();
            playerData.put("MiraTag", modData);
        }

        // Record the current held item's CompoundTag
        ItemStack currentItem = player.getMainHandItem();
        if (!currentItem.isEmpty()) { // Ensure the player is holding an item
            CompoundTag itemTag = currentItem.save(new CompoundTag());
            modData.put("OriginalItem", itemTag);
        }

        // Replace with mira weapon
        ItemStack miraWeapon = new ItemStack(Items.DIAMOND_SWORD); // Example: Replace with your actual mira weapon
        player.getInventory().setItem(player.getInventory().selected, miraWeapon);
    }

    public static void  miraWeaponBack(Player player) {
        CompoundTag playerData = player.getPersistentData();
        CompoundTag  modData = playerData.getCompound("MiraTag");
        CompoundTag itemTag = modData.getCompound("OriginalItem");
       // Example: Replace with your actual mira weapon
        ItemStack originalItem = ItemStack.of(itemTag);
        player.getInventory().setItem(player.getInventory().selected, originalItem);
        modData.remove("OriginalItem");

    }

//    @SubscribeEvent
//    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
//        if(event.getObject() instanceof Player) {
//            if(!event.getObject().getCapability(PlayerMiraProvider.PLAYER_Mira).isPresent()) {
//                event.addCapability(new ResourceLocation("mirapose", "properties"), new PlayerThirstProvider());
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public static void onPlayerCloned(PlayerEvent.Clone event) {
//        if(event.isWasDeath()) {
//            event.getOriginal().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(oldStore -> {
//                event.getOriginal().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(newStore -> {
//                    newStore.copyFrom(oldStore);
//                });
//            });
//        }
//    }
//
//    @SubscribeEvent
//    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
//        event.register(PlayerMira.class);
//    }


//    @SubscribeEvent
//    public static void onServerTick(TickEvent.PlayerTickEvent event) {
//        // Check if this is the end phase of the player tick to avoid processing twice per tick
//        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
//            Player player = event.player;
//            // Assuming PlayerPatch is the capability you want to check
//            player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
//                if (cap instanceof PlayerPatch) {
//                    PlayerPatch<?> playerPatch = (PlayerPatch<?>) cap;
//                    // Now you can check if the player is in battle mode
//                    if (playerPatch.isBattleMode()) {
//
//                    } else {
//                        // Player is not in battle mode, do something else
//                    }
//                }
//            });
//        }
//    }
    @SubscribeEvent
    public static void onXpGain(PlayerXpEvent.PickupXp event) {
        Player player = (Player) event.getEntity();
       // event.getEntity().sendSystemMessage(Component.literal("  battleMode is = "+ getPlayerPatch(player).isBattleMode()));
       if(!getPlayerPatch(player).isBattleMode()){
        player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira ->{
//            if(mira.getMira()>=10) {
//            return;
//            }
            mira.addMira((event.getOrb().getValue())*10);
            ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), ((ServerPlayer) event.getEntity()));
            player.sendSystemMessage(Component.literal("mira = "+ mira.getMira()));
            player.sendSystemMessage(Component.literal("Client mira = "+ ClientMiraData.getMira()));
            //LOGGER.info(ANSI_GREEN + "MIRA Gained: " + mira.getMira() + ANSI_RESET);

        });
    }
    }


    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.getEntity().level().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
                    ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), player);
                });
            }
        }
    }

//@SubscribeEvent
//    public static void onXpGain(PlayerXpEvent.PickupXp event) {
//    CompoundTag NBTdata =  event.getEntity().getPersistentData();
//    event.getEntity().sendSystemMessage(Component.literal("mira detected ::::: "+NBTdata.getInt("mirapose:mira")));
//}}


    //
//
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerMira.class);
    }
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerMiraProvider.PLAYER_Mira).isPresent()) {
                event.addCapability(new ResourceLocation("mirapose", "properties"), new PlayerMiraProvider());
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

//    @SubscribeEvent
//    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
//        if(!event.getLevel().isClientSide()) {
//            if(event.getEntity() instanceof ServerPlayer player) {
//                player.getCapability(PlayerThirstProvider.PLAYER_THIRST).ifPresent(thirst -> {
//                    ModMessages.sendToPlayer(new ThirstDataSyncS2CPacket(thirst.getThirst()), player);
//                });
//            }
//        }
//    }
}
