package net.sognefej.plantusmaximus.config;


import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.sognefej.plantusmaximus.config.autoconfig.annotation.CustomConfigEntry;


@Config(name = "general")
public class General implements ConfigData {
    @Comment("Enable mod")
    public boolean enabled = true;

    @CustomConfigEntry.Gui.KeyCodeEntry
    @Comment("Planter Keybinding")
    public String keyBinding = "key.keyboard.r";

    @Comment("Invert Keybinding")
    public boolean invert = false;
}
