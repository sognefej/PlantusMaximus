package net.sognefej.plantusmaximus.menu;


import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.PlantusConfig;


@Environment(EnvType.CLIENT)
public class PlantusMenu implements ModMenuApi {
    @Override
    @SuppressWarnings("deprecation")
    public String getModId() {
        return PlantusMaximusMod.MOD_ID;
    }

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(PlantusConfig.class, parent).get();
    }
}