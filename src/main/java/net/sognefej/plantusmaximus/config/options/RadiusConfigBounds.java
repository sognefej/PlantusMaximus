package net.sognefej.plantusmaximus.config.options;


import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;


public class RadiusConfigBounds {
    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.BoundedDiscrete(min=1, max=10)
    @Comment("Radius in blocks")
    public int radius;

    public RadiusConfigBounds() {
        this.radius = 4;
    }
}

