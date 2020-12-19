package net.sognefej.plantusmaximus;


import net.fabricmc.api.ModInitializer;

import net.sognefej.plantusmaximus.callback.ServerPacketCallback;
import net.sognefej.plantusmaximus.config.PlantusConfig;


public class PlantusMaximusMod implements ModInitializer {
	public static final String MOD_ID = "plantusmaximus";


	@Override
	public void onInitialize() {
		PlantusConfig.init();
		ServerPacketCallback.init();
	}
}
