package net.sognefej.plantusmaximus.config.autoconfig;


import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import static me.sargunvohra.mcmods.autoconfig1u.util.Utils.getUnsafely;
import static me.sargunvohra.mcmods.autoconfig1u.util.Utils.setUnsafely;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

import net.sognefej.plantusmaximus.config.autoconfig.annotation.CustomConfigEntry;
import net.sognefej.plantusmaximus.config.PlantusConfig;

import java.util.Collections;
import java.util.Optional;


@Environment(EnvType.CLIENT)
public class CustomGuiProviders {
    private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();


    public void registerKeyCodeEntry() {
        AutoConfig.getGuiRegistry(PlantusConfig.class).registerAnnotationProvider(
                (i13n, field, config, defaults, guiProvider) -> Collections.singletonList(
                        ENTRY_BUILDER.startKeyCodeField(
                                new TranslatableText(i13n),
                                InputUtil.fromTranslationKey(getUnsafely(field, config))
                        )
                                .setDefaultValue(() -> InputUtil.fromTranslationKey(getUnsafely(field, defaults)))
                                .setSaveConsumer(
                                newValue -> setUnsafely(
                                        field,
                                        config,
                                        newValue.getTranslationKey()
                                )

                        ).build()
                ),
                field -> field.getType() == String.class,
                CustomConfigEntry.Gui.KeyCodeEntry.class
        );
    }
}