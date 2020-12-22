package net.sognefej.plantusmaximus.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;

import net.sognefej.plantusmaximus.planter.PlantPreview;
import net.sognefej.plantusmaximus.planter.PlanterKeybinding;
import net.sognefej.plantusmaximus.util.DataCache;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

    @Inject(method = "render", at = @At(value = "TAIL"))
        private void render(MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci){
            if(PlanterKeybinding.isPressed() && DataCache.plantusConfig.general.highlightPreview) {
                PlantPreview.render(cameraX, cameraY, cameraZ);
            }
        }
}
