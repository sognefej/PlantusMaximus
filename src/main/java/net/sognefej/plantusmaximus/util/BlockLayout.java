package net.sognefej.plantusmaximus.util;


import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.sognefej.plantusmaximus.config.options.PlacementMode;

import java.util.ArrayList;
import java.util.List;


public class BlockLayout {
    public static Direction facing;
    public static PlacementMode placementMode;

    private static void addIfNotIn(List<BlockPos> blockPosList, BlockPos pos) {
        if (!blockPosList.contains(pos)) {
            blockPosList.add(pos);
        }
    }

    private static BlockPos[] getNeighbors(BlockPos pos) {
        BlockPos[] neighbors = new BlockPos[8];

        neighbors[0] = pos.north();
        neighbors[1] = pos.east();
        neighbors[2] = pos.south();
        neighbors[3] = pos.west();
        neighbors[4] = pos.north().east();
        neighbors[5] = pos.north().west();
        neighbors[6] = pos.south().east();
        neighbors[7] = pos.south().west();

        return neighbors;
    }

    private static void radiate(BlockPos pos, int current_radius, int radius, List<BlockPos> blockPosList) {
        current_radius++;

        if (current_radius <= radius) {

            for(BlockPos p : getNeighbors(pos)) {
                addIfNotIn(blockPosList, p);
            }

            radiate(pos.north().east(), current_radius, radius, blockPosList);
            radiate(pos.north().west(), current_radius, radius, blockPosList);
            radiate(pos.south().east(), current_radius, radius, blockPosList);
            radiate(pos.south().west(), current_radius, radius, blockPosList);
        }
    }

    private static void expand(BlockPos pos, int current_width, int width, List<BlockPos> blockPosList) {
        current_width++;

        if (current_width <= width) {
            blockPosList.add(pos.offset(facing));


            if (current_width % 2 == placementMode.ordinal()) {
                expand(pos.offset(facing.rotateYClockwise(), current_width), current_width, width, blockPosList);
            } else {
                expand(pos.offset(facing.rotateYCounterclockwise(), current_width), current_width, width, blockPosList);
            }
        }
    }

    public static List<BlockPos> getBlocksRadius(BlockPos pos, int radius) {
        List<BlockPos> blockPosList = new ArrayList<>();
        BlockPos origin = pos;

        radiate(pos , 0, radius, blockPosList);

        return blockPosList;
    }

    public static List<BlockPos> getBlocksColumn(BlockPos pos, int width, int column_len) {
        List<BlockPos> blockPosList = new ArrayList<>();
        pos = pos.offset(facing.getOpposite());

        for (int i = 0; i < column_len; i++) {
            expand(pos, 0, width, blockPosList);

            pos = pos.offset(facing);
        }

        return blockPosList;
    }
}