package net.sognefej.plantusmaximus.planter;


import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.PlantusConfig;
import net.sognefej.plantusmaximus.util.BlockLayout;
import net.sognefej.plantusmaximus.util.DataCache;

import static org.lwjgl.opengl.GL11.*;


// Totally safe render class :)
@SuppressWarnings("deprecation")
public class PlantPreview {
    public static void render(double camX, double camY, double camZ) {
        int length, width, radius;

        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hitResult = client.crosshairTarget;
        PlayerEntity player = client.player;
        World world = client.world;

        if (hitResult == null || player == null || world == null) return; // Just to be safe

        // Let's just stick to blocks
        if (hitResult.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult blockHitResult = (BlockHitResult)hitResult;
        BlockPos pos = blockHitResult.getBlockPos();
        Identifier block = Registry.BLOCK.getId(PlanterHelper.getBlockAt(world, pos));
        BlockLayout.facing = player.getMovementDirection();

        if (DataCache.blockPos == null) {
            DataCache.blockPos = pos.mutableCopy();
        }

        if (DataCache.plantusConfig.seeds.allowedSeeds.contains(player.getMainHandStack().getItem().getTranslationKey()) ^ DataCache.plantusConfig.seeds.useBlacklist) {
            switch (PlantusConfig.get().seeds.plantingMode) {
                case COLUMN:
                    length = DataCache.plantusConfig.seeds.columnConfigBounds.length;
                    width = DataCache.plantusConfig.seeds.columnConfigBounds.width;
                    BlockLayout.placementMode = DataCache.plantusConfig.seeds.columnConfigBounds.placementMode;

                    if (DataCache.posList == null || !pos.equals(DataCache.blockPos)) {
                        DataCache.posList = BlockLayout.getBlocksColumn(pos, length, width);
                        DataCache.blockPos = pos.mutableCopy();
                    }
                    break;
                case RADIATE:
                    radius = DataCache.plantusConfig.seeds.radiusConfigBounds.radius;

                    if (DataCache.posList == null || !pos.equals(DataCache.blockPos)) {
                        DataCache.posList = BlockLayout.getBlocksRadius(pos, radius);
                        DataCache.blockPos = pos.mutableCopy();
                    }
                    break;
                default:
                    System.err.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                    return;
            }
        } else if (DataCache.plantusConfig.tools.allowedTools.contains(player.getMainHandStack().getItem().getTranslationKey()) ^ DataCache.plantusConfig.tools.useBlacklist) {
            switch (PlantusConfig.get().tools.harvestMode) {
                case COLUMN:
                    length = DataCache.plantusConfig.tools.columnConfigBounds.length;
                    width = DataCache.plantusConfig.tools.columnConfigBounds.width;
                    BlockLayout.placementMode = DataCache.plantusConfig.tools.columnConfigBounds.placementMode;

                    if (DataCache.posList == null || !pos.equals(DataCache.blockPos)) {
                        DataCache.posList = BlockLayout.getBlocksColumn(pos, length, width);
                        DataCache.blockPos = pos.mutableCopy();
                    }
                    break;
                case RADIATE:
                    radius = DataCache.plantusConfig.tools.radiusConfigBounds.radius;

                    if (DataCache.posList == null || !pos.equals(DataCache.blockPos)) {
                        DataCache.posList = BlockLayout.getBlocksRadius(pos, radius);
                        DataCache.blockPos = pos.mutableCopy();
                    }
                    break;
                default:
                    System.err.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                    return;
            }
        } else {
            return;
        }

        // Render system magic I don't understand fully

        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        if (DataCache.plantusConfig.general.smoothPreviewLines) {
            glEnable(GL_LINE_SMOOTH);
        }
        //glLineWidth(3.0f);
        RenderSystem.translated(-camX, -camY, -camZ);

        for (BlockPos cPos : DataCache.posList) {
            if (PlanterHelper.isTheSameBlock(block, Registry.BLOCK.getId(PlanterHelper.getBlockAt(world, cPos)), world)) {
                RenderSystem.pushMatrix();
                double x = cPos.getX();
                // Want our cube on top of the block we are looking at
                double y = cPos.offset(Direction.Axis.Y, 1).getY() + 0.03;
                double z = cPos.getZ();
                glTranslated(x, y, z);
                drawSquare();
                // Let's just put things back and hope we don't break anything
                RenderSystem.popMatrix();
            }
        }

        RenderSystem.enableTexture();
    }

    public static void drawSquare() {
        glBegin(GL_LINE_LOOP);

        glColor3f(0.0f, 1.0f, 0.0f); // make this blue
        glVertex3f( 0.0f, 0.0f, 0.0f);
        glVertex3f( 1.0f, 0.0f, 0.0f);
        glVertex3f( 1.0f, 0.0f, 1.0f);
        glVertex3f( 0.0f, 0.0f, 1.0f);

        glEnd();
    }
}
