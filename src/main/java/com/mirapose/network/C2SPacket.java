package com.mirapose.network;

import com.mirapose.PlayerMira;
import com.mirapose.PlayerMiraProvider;
import com.mirapose.client.ClientMiraData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

    public class C2SPacket {



    public  C2SPacket() {

}

    public C2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            player.getCapability(PlayerMiraProvider.PLAYER_Mira).ifPresent(mira -> {
            mira.resetMira();
            ModMessages.sendToPlayer(new S2CPacket(mira.getMira()),player);
           // });
     });
    });
    return true;
    }
    }