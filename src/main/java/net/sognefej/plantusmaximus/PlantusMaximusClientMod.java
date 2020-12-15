package net.sognefej.plantusmaximus;


import net.fabricmc.api.ClientModInitializer;
import net.sognefej.plantusmaximus.callback.PlanterCallback;
import net.sognefej.plantusmaximus.config.PlantusConfig;


public class PlantusMaximusClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlanterCallback.init();
        PlantusConfig.initClient();
    }
}



