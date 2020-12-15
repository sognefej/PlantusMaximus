package net.sognefej.plantusmaximus.mixin;


import net.minecraft.entity.player.PlayerEntity;

import net.sognefej.plantusmaximus.planter.PlantingPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;


@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements PlantingPlayerEntity {
    private boolean is_planting = false;

    @Override
    public Boolean isPlanting() {
        return is_planting;
    }

    @Override
    public void setPlanting(boolean is_planting) {
        this.is_planting = is_planting;
    }
}