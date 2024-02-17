package com.mirapose.events;

import com.mirapose.PlayerMira;
import com.mirapose.PlayerMiraProvider;
import com.mirapose.client.ClientMiraData;
import com.mirapose.network.C2SPacket;
import com.mirapose.network.ModMessages;
import com.mirapose.network.S2CPacket;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.particle.Particle;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import reascer.wom.world.item.WOMItems;
import yesman.epicfight.api.animation.AnimationPlayer;

import yesman.epicfight.api.animation.ServerAnimator;

import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.Arrays;
import java.util.List;

import static yesman.epicfight.api.client.animation.ClientAnimator.getAnimator;
import static yesman.epicfight.world.capabilities.EpicFightCapabilities.getEntityPatch;
import static yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch.rand;



@Mod.EventBusSubscriber(modid = "mirapose")
public class ModEvents {
    public static List<RegistryObject<Item>> weapons = Arrays.asList(
            WOMItems.AGONY,
            WOMItems.TORMENTED_MIND,
            WOMItems.RUINE,
            WOMItems.NETHERITE_STAFF,
            WOMItems.MOONLESS,
            WOMItems.SATSUJIN,
            WOMItems.ANTITHEUS
    );
    //获取玩家包，奇迹状态
    public static ServerPlayerPatch getPlayerPatch(Player player) {
        if(player!=null){
        LazyOptional<EntityPatch> entityPatchOptional = player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY);
        if (entityPatchOptional.isPresent()) {
            EntityPatch entityPatch = entityPatchOptional.orElse(null);
            if (entityPatch instanceof ServerPlayerPatch playerPatch) {
                return playerPatch;
            }
        }
        }
        return null;
    }


    //客户端粒子METHOD
    private static void spawnFlameCircle(Player player) {
        Level level = player.level();
        double radius = 1.0; // The radius of the circle

        for (int i = 0; i < 360; i += 10) { // Increment i by more than 1 to have fewer particles and improve performance
            double angle = Math.toRadians(i);
            double x = player.getX() + (radius * Math.cos(angle));
            double z = player.getZ() + (radius * Math.sin(angle));
            double y = player.getY();
            //player.sendSystemMessage(Component.literal(  " PPPPPPPPPPPPPPPPPPPPPPPPPPP" ));
            // We spawn a flame particle at each location around the player.
            level.addParticle(ParticleTypes.FLAME, x, y, z,  0, 0, 55);
        }
    }

    public static void  miraWeaponReplace(Player player) {
        CompoundTag playerMiraData = player.getPersistentData();
        CompoundTag modMiraData;

        if (playerMiraData.contains("MiraTag")) {
            modMiraData = playerMiraData.getCompound("MiraTag");
        } else {
            modMiraData = new CompoundTag();
            playerMiraData.put("MiraTag", modMiraData);
        }

        // Record the current held item's CompoundTag
        ItemStack currentItem = player.getMainHandItem();
        if (!currentItem.isEmpty()) { // Ensure the player is holding an item
            CompoundTag itemTag = currentItem.save(new CompoundTag());
            modMiraData.put("OriginalItem", itemTag);
        }

        // Replace with mira weapon
        int randomWoM = rand.nextInt(weapons.size());
        ItemStack miraWeapon = new ItemStack(weapons.get(randomWoM).get()); // Example: Replace with your actual mira weapon
        player.getInventory().setItem(player.getInventory().selected, miraWeapon);
        player.sendSystemMessage(Component.literal(  "REppppppppppppppppppppppppppppp" ));
    }


    public static void  miraWeaponBack(Player player) {
        CompoundTag playerMiraData = player.getPersistentData();
        CompoundTag  modMiraData = playerMiraData.getCompound("MiraTag");
        if(modMiraData.contains("OriginalItem")){
            CompoundTag itemTag = modMiraData.getCompound("OriginalItem");
            //Replace with your actual mira weapon
            ItemStack originalItem = ItemStack.of(itemTag);
            player.setItemInHand(InteractionHand.MAIN_HAND, originalItem);
            modMiraData.remove("OriginalItem");
        }else{
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        //变身后倒计时
        if(event.side == LogicalSide.SERVER) {
            if(getPlayerPatch(event.player)!=null&&getPlayerPatch(event.player).isBattleMode()) {
            event.player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
                //替换mira武器
                if(mira.getMira()== mira.MAX_mira){
                    miraWeaponReplace(event.player);
                }
                //持续消耗mira
                    mira.subMira();
                    event.player.sendSystemMessage(Component.literal(+mira.getMira() + "" ));
                //变回mining
                        if(mira.getMira()<=0){
                            getPlayerPatch(event.player).toggleMode();
                            miraWeaponBack(event.player);
                        }
                    ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), (ServerPlayer)event.player);
                  //  event.player.sendSystemMessage(Component.literal(+ClientMiraData.getMira() + " tick client mira left!! (C)" ));

            });
            }
            //延迟替换逻辑
//            event.player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
//                if(mira.getMira()<0){
//                    mira.subMira();
//                    if(mira.getMira()<-60){
//                       getPlayerPatch(event.player).toMiningMode(true);
//                        miraWeaponBack(event.player);
//                        mira.resetMira();
//                        ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), (ServerPlayer)event.player);
//                    }}
//            });
        }
        //客户端 快结束时粒子触发
        if(event.side == LogicalSide.CLIENT) {
                if(ClientMiraData.getMira()<100){
                    spawnFlameCircle(event.player);
            }
        }

    }



    @SubscribeEvent        //经验值=mira
    public static void onXpGain(PlayerXpEvent.PickupXp event) {
        Player player = (Player) event.getEntity();
        player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
       // event.getEntity().sendSystemMessage(Component.literal("  battleMode is = "+ getPlayerPatch(player).isBattleMode()));
       if(!getPlayerPatch(player).isBattleMode()&&mira.getMira()>=0){

//            if(mira.getMira()>=10) {
//            return;
//            }
            mira.addMira((event.getOrb().getValue())*10);
            ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), ((ServerPlayer) event.getEntity()));
            player.sendSystemMessage(Component.literal("mira = "+ mira.getMira()));
            //LOGGER.info(ANSI_GREEN + "MIRA Gained: " + mira.getMira() + ANSI_RESET);

        }
        });
    }


    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        CompoundTag playerMiraData = event.getEntity().getPersistentData();
        CompoundTag modMiraData = new CompoundTag();
		playerMiraData.put("MiraTag", modMiraData);
        if(!event.getEntity().level().isClientSide()) {
            if(event.getEntity() instanceof ServerPlayer player) {

                player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
                    ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), player);
                });
            }
        }
    }


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
        if (event.isWasDeath()){
            event.getOriginal().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(oldStore -> {
                        event.getOriginal().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(newStore -> {
                            newStore.copyFrom(oldStore);
                            newStore.resetMira();
//                    if(event.getEntity() instanceof ServerPlayer player) {
//                    ModMessages.sendToPlayer(new S2CPacket(mira.getMira()), player);
//                    }
                });
            });
        }
    }
//        if (event.isWasDeath()&&!event.getEntity().level().isClientSide()){
////            if(getPlayerPatch(event.getOriginal()).isBattleMode()){
////                getPlayerPatch(event.getOriginal()).toggleMode();
////            }
//
//
//        }



//            event.getOriginal().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(oldStore -> {
//                getPlayerPatch(event.getOriginal()).toMiningMode(true);
//                oldStore.resetMira();
//                ModMessages.sendToPlayer(new S2CPacket(oldStore.getMira()), event.getEntity());
////                event.getEntity().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(newStore -> {
////                    newStore.copyFrom(oldStore);
////                });
//            });
//        }
//    }

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
