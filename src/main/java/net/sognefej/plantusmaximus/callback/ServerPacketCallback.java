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
import net.sognefej.plantusmaximus.planter.Planter;


public class ServerPacketCallback {
    private static final Identifier PLANTER_PACKET = new Identifier(PlantusMaximusMod.MOD_ID, "planter_packet");

    @Environment(EnvType.CLIENT)
    public static void sendPlanterPacket(BlockPos blockPos, LayoutMode mode, int length, int width, int radius) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(blockPos);
        buf.writeEnumConstant(mode);
        buf.writeInt(length);
        buf.writeInt(width);
        buf.writeInt(radius);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(PLANTER_PACKET, new PacketByteBuf(buf)));
    }

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(PLANTER_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos blockPos = packetByteBuf.readBlockPos();
            LayoutMode mode = packetByteBuf.readEnumConstant(LayoutMode.class);
            int length = packetByteBuf.readInt();
            int width = packetByteBuf.readInt();
            int radius = packetByteBuf.readInt();

            packetContext.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                Planter planter = new Planter(player, blockPos, mode, length, width, radius);
                planter.plant();
            });
        });
    }
}