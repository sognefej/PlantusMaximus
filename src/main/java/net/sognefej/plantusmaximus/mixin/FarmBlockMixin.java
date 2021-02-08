package net.sognefej.plantusmaximus.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import net.sognefej.plantusmaximus.util.GetTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(FarmlandBlock.class)
public class FarmBlockMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"), method = "randomTick", cancellable = true)
    private void doNotDirt(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (System.currentTimeMillis() / 1000 < GetTime.startTime / 1000 + GetTime.headStart) {
            ci.cancel();
        }
    }
}
