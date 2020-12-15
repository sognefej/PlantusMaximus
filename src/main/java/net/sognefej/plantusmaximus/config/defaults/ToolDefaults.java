package net.sognefej.plantusmaximus.config.defaults;


import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.List;


public class ToolDefaults {
    public final List<String> defaultTools = Arrays.asList(
            Items.DIAMOND_HOE.getTranslationKey(),
            Items.GOLDEN_HOE.getTranslationKey(),
            Items.IRON_HOE.getTranslationKey(),
            Items.NETHERITE_HOE.getTranslationKey(),
            Items.STONE_HOE.getTranslationKey(),
            Items.WOODEN_HOE.getTranslationKey()
    );
}
