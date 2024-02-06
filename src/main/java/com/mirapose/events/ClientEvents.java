package com.mirapose.events;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import com.mirapose.client.ClientMiraData;
import com.mirapose.network.C2SPacket;
import com.mirapose.network.ModMessages;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = "mirapose")
public class ClientEvents {
//    @Mod.EventBusSubscriber(modid = "mirapose",value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.MOD)
//    @SubscribeEvent
//    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event){
//        event.registerAboveAll("mira", MiraOverlay)//mira HUD should be here
//    }


//@SubscribeEvent
//    public static void onTick(TickEvent.PlayerTickEvent event){
//        if(Objects.requireNonNull(getPlayerPatch(event.player)).isBattleMode()){
//            ClientMiraData.sub();
//            event.player.sendSystemMessage(Component.literal(+ClientMiraData.getMira() + " mira left!! (C)" ));
//            if(ClientMiraData.getMira()<=1){
//                ClientMiraData.set(0);
//                getPlayerPatch(event.player).toggleMode();
//                ModMessages.sendToServer(new C2SPacket());
//
//
//        }
//    }
//    }
//@SubscribeEvent
//public static void Attack(ArrowLooseEvent event) {
//        if(isBattleMode(event.getEntity())){
//    event.getEntity().sendSystemMessage(Component.literal("atk client mira = "+ ClientMiraData.getMira()));
//        //event.getEntity().sendSystemMessage(Component.literal("atk server mira = "+ mira.getMira()));
//        event.getEntity().sendSystemMessage(Component.literal(" atk battle is = "+ isBattleMode(event.getEntity())));
//        ClientMiraData.set(0);
//        ModMessages.sendToServer(new C2SPacket());
//        event.getEntity().getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira ->{
//        mira.resetMira();
//        ModMessages.sendToServer(new C2SPacket());
//});
//}
//    }
}




