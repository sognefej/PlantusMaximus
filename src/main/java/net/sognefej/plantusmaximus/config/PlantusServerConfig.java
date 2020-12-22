package net.sognefej.plantusmaximus.config;


import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Jankson;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.JsonObject;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.impl.SyntaxError;

import net.fabricmc.loader.FabricLoader;

import net.sognefej.plantusmaximus.PlantusMaximusMod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class PlantusServerConfig {
    @SuppressWarnings("deprecation")
    private final File configFile = new File(FabricLoader.INSTANCE.getConfigDirectory().toString() + "/" + PlantusMaximusMod.MOD_ID + "/server.json5");

    public static class ServerConfig {
        @Comment("Max size of requested block interactions.\nYou will need to restart the server to change this value.\nHard limit of 2048.")
        public int maxInteractions = 441;
    }

    public ServerConfig loadConfig() {
        Jankson jankson = null;
        JsonObject configJson = null;

        try {
            jankson = Jankson.builder().build();

            configJson = jankson.load(configFile);
        } catch (IOException | SyntaxError e) {
            e.printStackTrace();
        }

        return jankson.fromJson(configJson, ServerConfig.class);
    }

    public void createDefaultConfig() {
        Jankson jankson = Jankson.builder().build();
        String result = jankson
                .toJson(new ServerConfig())          //The first call makes a JsonObject
                .toJson(true, true, 0);     //The second turns the JsonObject into a String -
                                                     //in this case, preserving comments and pretty-printing with newlines
        try {
            if(!configFile.exists()) {
                if(!configFile.createNewFile()) {
                    System.err.println(PlantusMaximusMod.MOD_ID + ": failed to create server config");
                }

                FileOutputStream out = new FileOutputStream(configFile, false);

                out.write(result.getBytes());
                out.flush();
                out.close();
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


