package io.github.lucaargolo.biomecreeper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class BiomeCreeper implements ModInitializer {

    public static final String MOD_ID = "biomecreeper";
    public static final String MOD_NAME = "Biome Creeper";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static ModConfig CONFIG;

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @Override
    public void onInitialize() {
        Path configPath = FabricLoader.getInstance().getConfigDir();
        File configFile = new File(configPath + File.separator + MOD_ID +".json");

        LOGGER.info("["+MOD_NAME+"] Trying to read config file...");
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("["+MOD_NAME+"] No config file found, creating a new one...");
                String json = GSON.toJson(JsonParser.parseString(GSON.toJson(new ModConfig())));
                try (PrintWriter out = new PrintWriter(configFile)) {
                    out.println(json);
                }
                CONFIG = new ModConfig();
                LOGGER.info("["+MOD_NAME+"] Successfully created default config file.");
            } else {
                LOGGER.info("["+MOD_NAME+"] A config file was found, loading it..");
                CONFIG = GSON.fromJson(new String(Files.readAllBytes(configFile.toPath())), ModConfig.class);
                if(CONFIG == null) {
                    throw new NullPointerException("["+MOD_NAME+"] The config file was empty.");
                }else{
                    LOGGER.info("["+MOD_NAME+"] Successfully loaded config file.");
                }
            }
        }catch (Exception exception) {
            LOGGER.error("["+MOD_NAME+"] There was an error creating/loading the config file!", exception);
            CONFIG = new ModConfig();
            LOGGER.warn("["+MOD_NAME+"] Defaulting to original config.");
        }
    }
}
