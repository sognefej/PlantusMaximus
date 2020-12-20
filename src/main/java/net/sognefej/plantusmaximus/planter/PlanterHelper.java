package net.sognefej.plantusmaximus.planter;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.tag.TagRegistry;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.sognefej.plantusmaximus.config.PlantusServerConfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public class PlanterHelper {
    // Just setting a hard limit of 2048 just in case something insane gets set
    static int maxInteractions = Math.min(new PlantusServerConfig().loadConfig().maxInteractions, 2048);

    static Block getBlockAt(World world, BlockPos pos) {
        return (world.getBlockState(pos).getBlock());
    }

    static boolean isTheSameBlock(Identifier start_id, Identifier new_id, World world) {
        Block newBlock = Registry.BLOCK.get(new_id);
        for (Identifier tag_id : getTagsFor(world.getTagManager().getBlocks(), Registry.BLOCK.get(start_id))) {
            if (TagRegistry.block(tag_id).contains(newBlock)) {
                new_id = start_id;
                break;
            }
         }

        return start_id.equals(new_id);
    }

    // copied from: net.minecraft.tag.TagContainer:getTagsFor
    static Collection<Identifier> getTagsFor(TagGroup<Block> container, Block object) {
        List<Identifier> list = Lists.newArrayList();
        for (Map.Entry<Identifier, Tag<Block>> identifierTagEntry : container.getTags().entrySet()) {
            if (identifierTagEntry.getValue().contains(object)) {
                list.add(identifierTagEntry.getKey());
            }
        }
        return list;
    }
}
