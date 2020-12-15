package net.sognefej.plantusmaximus.callback;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

import net.sognefej.plantusmaximus.config.PlantusConfig;
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
            if (PlantusConfig.get().general.enabled && PlanterKeybinding.isPressed() && !((PlantingPlayerEntity) player).isPlanting()) {
                ServerPacketCallback.sendPlanterPacket(hit_result.getBlockPos());
                return ActionResult.FAIL;
            }

            return ActionResult.PASS;
        });
    }
}
