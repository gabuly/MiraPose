//package com.mirapose.network;
//
//import com.mirapose.MiraPoseMod;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.nio.channels.Channel;
//import java.util.function.Supplier;
//
//// ... imports
//
//public class NetworkHandler {
//    private final int mira;
//
//    public NetworkHandler(int mira) {
//        this.mira = mira;
//    }
//    public NetworkHandler(FriendlyByteBuf buf) {
//        this.mira = buf.readInt();
//    }
//
//    public void toBytes(FriendlyByteBuf buf){
//        buf.writeInt(mira);
//    }
//    public boolean handle(Supplier<NetworkEvent.Context> supplier){
//        NetworkEvent.Context context = supplier.get();
//        context.enqueueWork(()->{
//            ClientMira.set(mira);
//        });
//        return true;
//    }
//}
