package net.sognefej.plantusmaximus.config;


import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.sognefej.plantusmaximus.config.options.ColumnConfigBounds;
import net.sognefej.plantusmaximus.config.options.LayoutMode;
import net.sognefej.plantusmaximus.config.options.RadiusConfigBounds;

import java.util.ArrayList;
import java.util.List;


@Config(name = "tools")
public class Tools implements ConfigData {
    @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @Comment("Switch between radiate and column mode")
    public LayoutMode harvestMode = LayoutMode.COLUMN;

    @Comment("Cancel tool durability (when using mod)")
    public boolean cancelDurability = false;

    @Comment("Use default seed list (to change the list disable this setting)")
    public boolean useDefaults = true;

    @Comment("Invert list")
    public boolean useBlacklist = false;

    @ConfigEntry.Gui.CollapsibleObject()
    @Comment("Configure column bounds")
    public ColumnConfigBounds columnConfigBounds = new ColumnConfigBounds();

    @ConfigEntry.Gui.CollapsibleObject()
    @Comment("Configure radiate bounds")
    public RadiusConfigBounds radiusConfigBounds = new RadiusConfigBounds();

    @Comment("List of allowed/disallowed tools")
    public List<String> allowedTools = new ArrayList<>();
}
