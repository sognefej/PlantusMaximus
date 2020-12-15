package net.sognefej.plantusmaximus.planter;


import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.PlantusConfig;
import net.sognefej.plantusmaximus.util.BlockLayout;

import java.util.List;


public class Planter {
    private final BlockPos startBlockPos;
    private final Identifier startBlockId;
    private final World world;
    private final Hand hand;
    private final ServerPlayerInteractionManager interactionManager;
    private ServerPlayerEntity player;

    public Planter(ServerPlayerInteractionManager interaction_manager, ServerPlayerEntity player, World world, Hand hand, BlockPos pos) {
        this.interactionManager = interaction_manager;
        this.player = player;
        this.world = world;
        this.hand = hand;
        this.startBlockPos = pos;
        this.startBlockId = Registry.BLOCK.getId(world.getBlockState(startBlockPos).getBlock());
        BlockLayout.facing = player.getMovementDirection();
        BlockLayout.placementMode = PlantusConfig.get().tools.columnConfigBounds.placementMode;
    }

    public void plant() {
        List<BlockPos> posList;

        if (PlantusConfig.get().seeds.allowedSeeds.contains(player.getStackInHand(hand).getItem().getTranslationKey()) ^ PlantusConfig.get().seeds.useBlacklist) {
            switch (PlantusConfig.get().seeds.plantingMode) {
                case COLUMN:
                    posList = BlockLayout.getBlocksColumn(this.startBlockPos, PlantusConfig.get().seeds.columnConfigBounds.width, PlantusConfig.get().seeds.columnConfigBounds.length);
                    break;
                case RADIATE:
                    posList = BlockLayout.getBlocksRadius(this.startBlockPos, PlantusConfig.get().seeds.radiusConfigBounds.radius);
                    break;
                default:
                    System.out.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                    return;
            }
        } else if (PlantusConfig.get().tools.allowedTools.contains(player.getStackInHand(hand).getItem().getTranslationKey()) ^ PlantusConfig.get().tools.useBlacklist) {
            switch (PlantusConfig.get().tools.harvestMode) {
                case COLUMN:
                    posList = BlockLayout.getBlocksColumn(this.startBlockPos, PlantusConfig.get().tools.columnConfigBounds.width, PlantusConfig.get().tools.columnConfigBounds.length);
                    break;
                case RADIATE:
                    posList = BlockLayout.getBlocksRadius(this.startBlockPos, PlantusConfig.get().tools.radiusConfigBounds.radius);
                    break;
                default:
                    System.out.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                    return;
            }
        } else {
            return;
        }

        ((PlantingPlayerEntity) player).setPlanting(true);
        for(BlockPos pos : posList) {
            plantAt(pos);
        }
        ((PlantingPlayerEntity) player).setPlanting(false);
    }

    private void plantAt(BlockPos pos) {
        Identifier block = Registry.BLOCK.getId(PlanterHelper.getBlockAt(world, pos));
        BlockHitResult block_hit_result = new BlockHitResult(player.getPos(), Direction.UP, pos, false);
        ItemStack itemStack = this.player.getStackInHand(hand);
        if (PlanterHelper.isTheSameBlock(startBlockId, block, world)) {
            this.interactionManager.interactBlock(this.player, this.world, itemStack, this.hand, block_hit_result);
        }
    }
}