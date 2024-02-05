package com.mirapose;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerMiraProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerMira> PLAYER_Mira = CapabilityManager.get(new CapabilityToken<PlayerMira>() {});
    private PlayerMira Mira = null;
    private final LazyOptional<PlayerMira> optional = LazyOptional.of(this::createPlayerMira);

    private  PlayerMira createPlayerMira() {
        if(this.Mira==null){
            this.Mira = new PlayerMira();
        }
        return this.Mira;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap ==  PLAYER_Mira ? this.optional.cast() :  LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createPlayerMira().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // ...
        createPlayerMira().loadNBTData(nbt);
    }
}
