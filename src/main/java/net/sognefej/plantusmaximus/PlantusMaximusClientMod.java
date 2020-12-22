package net.sognefej.plantusmaximus;


import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.sognefej.plantusmaximus.callback.PlanterCallback;
import net.sognefej.plantusmaximus.config.PlantusConfig;
import net.sognefej.plantusmaximus.mixin.MinecraftClientMixin;
import net.sognefej.plantusmaximus.planter.PlantPreview;
import net.sognefej.plantusmaximus.planter.PlanterKeybinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.CallbackI;


public class PlantusMaximusClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PlantusConfig.initClient();
        PlanterCallback.init();
    }
}



