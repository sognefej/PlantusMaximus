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
import net.sognefej.plantusmaximus.config.options.LayoutMode;
import net.sognefej.plantusmaximus.config.options.PlacementMode;
import net.sognefej.plantusmaximus.util.BlockLayout;

import java.util.Set;


public class Planter {
    private final BlockPos startBlockPos;
    private final Identifier startBlockId;
    private final World world;
    private final ServerPlayerInteractionManager interactionManager;
    private final ServerPlayerEntity player;
    private final LayoutMode layoutMode;
    private final int length, width, radius;
    private int interactions = 0;
    private final ItemStack targetStack;
    private final boolean pullInventory;

    public Planter(ServerPlayerEntity player, BlockPos pos, LayoutMode layoutMode, PlacementMode placementMode, int length, int width, int radius, boolean pullInventory) {
        this.interactionManager = player.interactionManager;
        this.player = player;
        this.world = player.getServerWorld();
        this.startBlockPos = pos;
        this.targetStack = player.getMainHandStack().copy();
        this.startBlockId = Registry.BLOCK.getId(world.getBlockState(startBlockPos).getBlock());
        this.layoutMode = layoutMode;
        this.length = length;
        this.width = width;
        this.radius = radius;
        this.pullInventory = pullInventory;
        BlockLayout.facing = player.getMovementDirection();
        BlockLayout.placementMode = placementMode;
    }

    public void plant() {
        Set<BlockPos> posList;

        switch (layoutMode) {
            case COLUMN:
                posList = BlockLayout.getBlocksColumn(this.startBlockPos, length, width);
                break;
            case RADIATE:
                posList = BlockLayout.getBlocksRadius(this.startBlockPos, radius);
                break;
            default:
                System.err.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                return;
        }

        ((PlantingPlayerEntity) player).setPlanting(true);
        for(BlockPos pos : posList) {
            interactions++;
            plantAt(pos);
        }
        ((PlantingPlayerEntity) player).setPlanting(false);
    }

    private void plantAt(BlockPos pos) {
        if (interactions > PlanterHelper.maxInteractions) {
            return;
        }

        Identifier block = Registry.BLOCK.getId(PlanterHelper.getBlockAt(world, pos));
        BlockHitResult block_hit_result = new BlockHitResult(player.getPos(), Direction.UP, pos, false);
        if (this.player.getMainHandStack().isEmpty() && pullInventory) {
            int slot = this.player.inventory.method_7371(targetStack);
            if (slot != -1) {
                this.player.setStackInHand(Hand.MAIN_HAND, this.player.inventory.getStack(slot));
                this.player.inventory.removeStack(slot);
            }
        }

        if (PlanterHelper.isTheSameBlock(startBlockId, block, world)) {
            this.interactionManager.interactBlock(this.player, this.world, this.player.getMainHandStack(), Hand.MAIN_HAND, block_hit_result);
        }
    }
}