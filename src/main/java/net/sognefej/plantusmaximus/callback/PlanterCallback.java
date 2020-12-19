package net.sognefej.plantusmaximus.callback;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.PlantusConfig;
import net.sognefej.plantusmaximus.config.options.LayoutMode;
import net.sognefej.plantusmaximus.planter.PlanterKeybinding;
import net.sognefej.plantusmaximus.planter.PlantingPlayerEntity;


public class PlanterCallback {
    public interface InteractBlockCallback {
        Event<InteractBlockCallback> EVENT = EventFactory.createArrayBacked(InteractBlockCallback.class,
                (listeners) -> (interaction_manager, hit_result) -> {
                    for (InteractBlockCallback listener : listeners) {
                        ActionResult result = listener.plant_area(interaction_manager, hit_result);

                        if(result != ActionResult.PASS) {
                            return result;
                        }
                    }

                    return ActionResult.PASS;
                });

        ActionResult plant_area(ClientPlayerEntity player, BlockHitResult hit_result);
    }

    public static void init() {
        InteractBlockCallback.EVENT.register((player, hit_result) -> {
            boolean pullInventory = PlantusConfig.get().general.pullInventory;

            if (PlantusConfig.get().general.enabled && PlanterKeybinding.isPressed() && !((PlantingPlayerEntity) player).isPlanting()) {

                if (PlantusConfig.get().seeds.allowedSeeds.contains(player.getMainHandStack().getItem().getTranslationKey()) ^ PlantusConfig.get().seeds.useBlacklist) {
                    switch (PlantusConfig.get().seeds.plantingMode) {
                        case COLUMN:
                            int length = PlantusConfig.get().seeds.columnConfigBounds.length;
                            int width = PlantusConfig.get().seeds.columnConfigBounds.width;

                            ServerPacketCallback.sendPlanterPacket(hit_result.getBlockPos(), LayoutMode.COLUMN, length, width, 0, pullInventory);
                            break;
                        case RADIATE:
                            int radius = PlantusConfig.get().seeds.radiusConfigBounds.radius;

                            ServerPacketCallback.sendPlanterPacket(hit_result.getBlockPos(), LayoutMode.RADIATE, 0, 0, radius, pullInventory);
                            break;
                        default:
                            System.out.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                            return ActionResult.PASS;
                    }
                } else if (PlantusConfig.get().tools.allowedTools.contains(player.getMainHandStack().getItem().getTranslationKey()) ^ PlantusConfig.get().tools.useBlacklist) {
                    switch (PlantusConfig.get().tools.harvestMode) {
                        case COLUMN:
                            int length = PlantusConfig.get().tools.columnConfigBounds.length;
                            int width = PlantusConfig.get().tools.columnConfigBounds.width;

                            ServerPacketCallback.sendTimerPacker(PlantusConfig.get().general.headStart);
                            ServerPacketCallback.sendPlanterPacket(hit_result.getBlockPos(), LayoutMode.COLUMN, length, width, 0, pullInventory);
                            break;
                        case RADIATE:
                            int radius = PlantusConfig.get().tools.radiusConfigBounds.radius;

                            ServerPacketCallback.sendTimerPacker(PlantusConfig.get().general.headStart);
                            ServerPacketCallback.sendPlanterPacket(hit_result.getBlockPos(), LayoutMode.RADIATE, 0, 0, radius, pullInventory);
                            break;
                        default:
                            System.out.println(PlantusMaximusMod.MOD_ID + ": unimplemented LayoutMode");
                            return ActionResult.PASS;
                    }
                } else {
                    return ActionResult.PASS;
                }

                // Fail if success need to cancel the first action.
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });
    }
}
