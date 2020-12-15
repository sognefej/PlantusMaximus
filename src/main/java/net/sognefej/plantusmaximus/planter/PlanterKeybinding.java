package net.sognefej.plantusmaximus.planter;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.PlantusConfig;

import org.lwjgl.glfw.GLFW;

import java.util.Optional;


public class PlanterKeybinding {
    public static boolean isPressed() {
        InputUtil.Key keycode = getKeybinding().orElse(null);
        boolean invert = PlantusConfig.get().general.invert;
        boolean pressed;

        if (keycode == null) { // Invalid key
            return false;
        }

        if (keycode == InputUtil.UNKNOWN_KEY) {
            return true; // Always pressed for empty or explicitly "key.keyboard.unknown"
        }

        if (keycode.getCategory() == InputUtil.Type.MOUSE) {
            pressed = GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
        } else {
            pressed = GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
        }

        if (invert) {
            return !pressed;
        }

        return pressed;
    }

    public static Optional<InputUtil.Key> getKeybinding() {
        String key_binding = PlantusConfig.get().general.keyBinding;

        if (key_binding.isEmpty()) {
            return Optional.of(InputUtil.UNKNOWN_KEY);
        }

        try {
            return Optional.of(InputUtil.fromTranslationKey(key_binding));
        } catch (IllegalArgumentException e) {
            System.out.println(PlantusMaximusMod.MOD_ID + ": unknown key entered");
            return Optional.empty();
        }
    }
}