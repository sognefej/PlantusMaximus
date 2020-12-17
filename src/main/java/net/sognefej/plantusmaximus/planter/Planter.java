package net.sognefej.plantusmaximus.planter;


import net.minecraft.util.ActionResult;
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
import net.sognefej.plantusmaximus.config.options.LayoutMode;
import net.sognefej.plantusmaximus.util.BlockLayout;

import java.util.List;


public class Planter {
    private final BlockPos startBlockPos;
    private final Identifier startBlockId;
    private final World world;
    private final ServerPlayerInteractionManager interactionManager;
    private ServerPlayerEntity player;
    private final LayoutMode layoutMode;
    private int length, width, radius;

    public Planter(ServerPlayerEntity player, BlockPos pos, LayoutMode layoutMode, int length, int width, int radius) {
        this.interactionManager = player.interactionManager;
        this.player = player;
        this.world = player.getServerWorld();
        this.startBlockPos = pos;
        this.startBlockId = Registry.BLOCK.getId(world.getBlockState(startBlockPos).getBlock());
        this.layoutMode = layoutMode;
        this.length = length;
        this.width = width;
        this.radius = radius;
        BlockLayout.facing = player.getMovementDirection();
        BlockLayout.placementMode = PlantusConfig.get().tools.columnConfigBounds.placementMode;
    }

    public void plant() {
        List<BlockPos> posList;

        switch (layoutMode) {
            case COLUMN:
                posList = BlockLayout.getBlocksColumn(this.startBlockPos, length, width);
                break;
            case RADIATE:
                posList = BlockLayout.getBlocksRadius(this.startBlockPos, radius);
                break;
            default:
                System.out.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
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
        ItemStack itemStack = this.player.getMainHandStack();
        if (PlanterHelper.isTheSameBlock(startBlockId, block, world)) {
            this.interactionManager.interactBlock(this.player, this.world, itemStack, Hand.MAIN_HAND, block_hit_result);
        }
    }
}