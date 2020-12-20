package net.sognefej.plantusmaximus.config.options;


import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;


public class ColumnConfigBounds {
    @ConfigEntry.Gui.EnumHandler(option=ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    @Comment("Place blocks left or right first, used for even width")
    public PlacementMode placementMode;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.BoundedDiscrete(min = 1, max = 21)
    @Comment("Column width in blocks")
    public int width;

    @ConfigEntry.Gui.CollapsibleObject()
    @ConfigEntry.BoundedDiscrete(min = 1, max = 21)
    @Comment("Column length in blocks")
    public int length;


    public ColumnConfigBounds() {
        this.placementMode = PlacementMode.LEFT_FIRST;
        this.width = 9;
        this.length = 9;
    }
}
