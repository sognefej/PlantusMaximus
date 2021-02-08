package net.sognefej.plantusmaximus.callback;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import io.netty.buffer.Unpooled;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.config.options.LayoutMode;
import net.sognefej.plantusmaximus.config.options.PlacementMode;
import net.sognefej.plantusmaximus.planter.Planter;
import net.sognefej.plantusmaximus.util.GetTime;

import java.util.Objects;


public class ServerPacketCallback {
    private static final Identifier PLANTER_PACKET = new Identifier(PlantusMaximusMod.MOD_ID, "planter_packet");
    private static final Identifier TIMER_PACKET = new Identifier(PlantusMaximusMod.MOD_ID, "timer_packet");

    @Environment(EnvType.CLIENT)
    public static void sendPlanterPacket(BlockPos blockPos, LayoutMode layoutMode, PlacementMode placementMode, int length, int width, int radius, boolean pullInventory) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(blockPos);
        buf.writeEnumConstant(layoutMode);
        buf.writeEnumConstant(placementMode);
        buf.writeInt(length);
        buf.writeInt(width);
        buf.writeInt(radius);
        buf.writeBoolean(pullInventory);
        try {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getConnection().send(new CustomPayloadC2SPacket(PLANTER_PACKET, new PacketByteBuf(buf)));
        } catch (NullPointerException e) {
           e.printStackTrace();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void sendTimerPacker(int headStart) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(headStart);
        try {
            Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getConnection().send(new CustomPayloadC2SPacket(TIMER_PACKET, new PacketByteBuf(buf)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(PLANTER_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos blockPos = packetByteBuf.readBlockPos();
            LayoutMode layoutMode = packetByteBuf.readEnumConstant(LayoutMode.class);
            PlacementMode placementMode = packetByteBuf.readEnumConstant(PlacementMode.class);
            int length = packetByteBuf.readInt();
            int width = packetByteBuf.readInt();
            int radius = packetByteBuf.readInt();
            boolean pullInventory = packetByteBuf.readBoolean();

            packetContext.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                Planter planter = new Planter(player, blockPos, layoutMode, placementMode, length, width, radius, pullInventory);
                planter.plant();
            });
        });

        ServerSidePacketRegistry.INSTANCE.register(TIMER_PACKET, (packetContext, packetByteBuf) -> {
            int headStart = packetByteBuf.readInt();

            packetContext.getTaskQueue().execute(() -> {
                GetTime.startTime = System.currentTimeMillis();
                GetTime.headStart = headStart;
            });
        });
    }
}