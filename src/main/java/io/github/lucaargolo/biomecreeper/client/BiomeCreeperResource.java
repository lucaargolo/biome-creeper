package io.github.lucaargolo.biomecreeper.client;

import io.github.lucaargolo.biomecreeper.BiomeCreeper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.InputStream;
import java.util.Optional;

public class BiomeCreeperResource implements SimpleSynchronousResourceReloadListener {

    NativeImageBackedTexture nativeImageBackedTexture = null;

    @Override
    public Identifier getFabricId() {
        return new Identifier(BiomeCreeper.MOD_ID, "biome_creeper");
    }

    @Override
    public void reload(ResourceManager manager) {
        Optional<Resource> optional = manager.getResource(new Identifier("textures/entity/creeper/creeper.png"));
        if(optional.isPresent()) {
            Resource resource = optional.get();
            try {
                InputStream stream = resource.getInputStream();
                NativeImage original = NativeImage.read(stream);
                NativeImage image = null;
                if(nativeImageBackedTexture != null) {
                    image = nativeImageBackedTexture.getImage();
                }
                if(image == null) {
                    image = original;
                }
                for(int x = 0; x < original.getWidth(); x++) {
                    for(int y = 0; y < original.getHeight(); y++) {
                        int abgr = original.getColor(x, y);
                        int a = abgr >> 24 & 0xFF;
                        int b = abgr >> 16 & 0xFF;
                        int g = abgr >> 8 & 0xFF;
                        int r = abgr & 0xFF;
                        float[] hsb = Color.RGBtoHSB(r, g, b, null);
                        hsb[1] = 0f;
                        int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                        int nr = rgb >> 16 & 0xFF;
                        int ng = rgb >> 8 & 0xFF;
                        int nb = rgb & 0xFF;
                        int color = (a << 24) | (nb << 16) | (ng << 8) | nr;
                        image.setColor(x, y, color);
                    }
                }
                if(nativeImageBackedTexture == null) {
                    nativeImageBackedTexture = new NativeImageBackedTexture(image);
                }else{
                    nativeImageBackedTexture.upload();
                }
                if(BiomeCreeperClient.TEXTURE == null) {
                    BiomeCreeperClient.TEXTURE = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("biome_creeper", nativeImageBackedTexture);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
