package net.sognefej.plantusmaximus.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import net.sognefej.plantusmaximus.callback.PlanterCallback;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Shadow
    public ClientPlayerEntity player;
    @Shadow
    public HitResult crosshairTarget;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"), method = "doItemUse", cancellable = true)
    private void doPlant(CallbackInfo ci) {
        BlockHitResult blockHitResult = (BlockHitResult)this.crosshairTarget;
        ActionResult result = PlanterCallback.InteractBlockCallback.EVENT.invoker().plant_area(this.player, blockHitResult);

        if (result.equals(ActionResult.FAIL)) {
            ci.cancel();
        }
    }
}
