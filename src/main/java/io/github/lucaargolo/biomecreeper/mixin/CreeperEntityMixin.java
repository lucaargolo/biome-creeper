package io.github.lucaargolo.biomecreeper.mixin;

import io.github.lucaargolo.biomecreeper.BiomeCreeper;
import io.github.lucaargolo.biomecreeper.mixed.CreeperEntityMixed;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@SuppressWarnings("WrongEntityDataParameterClass")
@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements SkinOverlayOwner, CreeperEntityMixed {

    private static final TrackedData<String> BIOME_STRING = DataTracker.registerData(CreeperEntity.class, TrackedDataHandlerRegistry.STRING);

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void injectSpawnBiome(CallbackInfo ci) {
        if(!world.isClient && BiomeCreeper.CONFIG.persistentBiome) {
            String biomeString = dataTracker.get(BIOME_STRING);
            if (biomeString.isEmpty()) {
                world.getBiome(getBlockPos()).getKey().ifPresent(biomeRegistryKey -> dataTracker.set(BIOME_STRING, biomeRegistryKey.getValue().toString()));
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    public void initDataTracker(CallbackInfo ci) {
        dataTracker.startTracking(BIOME_STRING, "");
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomTrackedDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putString("biomecreeper:biome_string", this.dataTracker.get(BIOME_STRING));
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomTrackedDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        this.dataTracker.set(BIOME_STRING, nbt.getString("biomecreeper:biome_string"));
    }

    @Override
    public String getBiomeString() {
        return this.dataTracker.get(BIOME_STRING);
    }

}
