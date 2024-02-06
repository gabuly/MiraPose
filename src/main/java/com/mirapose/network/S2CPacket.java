package com.mirapose.network;

import com.mirapose.PlayerMiraProvider;
import com.mirapose.client.ClientMiraData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class S2CPacket {



    private final int mira;
    public S2CPacket(int mira){

        this.mira=mira;
    }

    public S2CPacket(FriendlyByteBuf buf){

        this.mira=buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf){
        buf.writeInt(mira);
}


    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(()->{
            ClientMiraData.set(mira);
        });

        return true;
}}
