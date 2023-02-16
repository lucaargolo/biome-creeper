package io.github.lucaargolo.biomecreeper.mixin;

import io.github.lucaargolo.biomecreeper.client.BiomeCreeperFeatureRenderer;
import net.minecraft.client.render.entity.CreeperEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntityRenderer.class)
public abstract class CreeperEntityRendererMixin extends MobEntityRenderer<CreeperEntity, CreeperEntityModel<CreeperEntity>> {

    public CreeperEntityRendererMixin(EntityRendererFactory.Context context, CreeperEntityModel<CreeperEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void injectBiomeCreeperOverlay(EntityRendererFactory.Context context, CallbackInfo ci) {
        addFeature(new BiomeCreeperFeatureRenderer<>(this, context.getModelLoader()));
    }


}
