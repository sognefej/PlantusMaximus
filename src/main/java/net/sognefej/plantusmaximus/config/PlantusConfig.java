package net.sognefej.plantusmaximus.config;


import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

import net.minecraft.util.ActionResult;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.autoconfig.CustomGuiProviders;
import net.sognefej.plantusmaximus.config.defaults.SeedDefaults;
import net.sognefej.plantusmaximus.config.defaults.ToolDefaults;
import net.sognefej.plantusmaximus.planter.PlanterKeybinding;


@Config.Gui.Background("minecraft:textures/block/farmland.png")
@Config(name = PlantusMaximusMod.MOD_ID)
public class PlantusConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("general")
    @Comment("General configuration options.")
    public General general = new General();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("seeds")
    @Comment("Configure seed")
    public Seeds seeds = new Seeds();

    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Category("tools")
    @Comment("Configure items")
    public Tools tools = new Tools();

    public static void init() {
        ToolDefaults defaultTools = new ToolDefaults();
        SeedDefaults seedDefaults = new SeedDefaults();

        ConfigHolder<PlantusConfig> holder = AutoConfig.register(
                PlantusConfig.class,
                PartitioningSerializer.wrap(JanksonConfigSerializer::new)
        );

        holder.registerSaveListener((manager, data) -> {
            if (data.tools.useDefaults) {
                data.tools.allowedTools = defaultTools.defaultTools;
            }

            if (data.seeds.useDefaults) {
                data.seeds.allowedSeeds = seedDefaults.defaultSeeds;
            }

            return ActionResult.SUCCESS;
        });

        holder.registerLoadListener((manager, data) -> {
            if (data.tools.useDefaults) {
                data.tools.allowedTools = defaultTools.defaultTools;
            }

            if (data.seeds.useDefaults) {
                data.seeds.allowedSeeds = seedDefaults.defaultSeeds;
            }

            return ActionResult.SUCCESS;
        });

        holder.load();
    }

    public static void initClient() {
        CustomGuiProviders customGuiProviders = new CustomGuiProviders();

        customGuiProviders.registerKeyCodeEntry();
    }

    public static PlantusConfig get() {
        return AutoConfig.getConfigHolder(PlantusConfig.class).getConfig();
    }
}


