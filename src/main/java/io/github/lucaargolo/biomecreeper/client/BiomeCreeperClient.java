package io.github.lucaargolo.biomecreeper.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BiomeCreeperClient implements ClientModInitializer {

    public static BiomeCreeperResource RESOURCE = new BiomeCreeperResource();
    public static Identifier TEXTURE = null;

    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(RESOURCE);
    }
}
