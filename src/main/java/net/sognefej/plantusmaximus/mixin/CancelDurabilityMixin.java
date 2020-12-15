package net.sognefej.plantusmaximus.mixin;


import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import net.sognefej.plantusmaximus.config.PlantusConfig;
import net.sognefej.plantusmaximus.planter.PlantingPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;


@Mixin(ItemStack.class)
public class CancelDurabilityMixin {
    @Inject(method = "Lnet/minecraft/item/ItemStack;damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void CancelDurability(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player != null && ((PlantingPlayerEntity) player).isPlanting() && PlantusConfig.get().tools.cancelDurability)
            cir.cancel();
    }
}
