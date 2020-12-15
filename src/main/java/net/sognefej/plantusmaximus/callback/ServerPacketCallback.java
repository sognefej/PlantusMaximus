package net.sognefej.plantusmaximus.callback;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import io.netty.buffer.Unpooled;

import net.sognefej.plantusmaximus.PlantusMaximusMod;
import net.sognefej.plantusmaximus.planter.Planter;


public class ServerPacketCallback {
    private static final Identifier PLANTER_PACKET = new Identifier(PlantusMaximusMod.MOD_ID, "planter_packet");

    @Environment(EnvType.CLIENT)
    public static void sendPlanterPacket(BlockPos blockPos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(blockPos);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(PLANTER_PACKET, new PacketByteBuf(buf)));
    }

    public static void init() {
        ServerSidePacketRegistry.INSTANCE.register(PLANTER_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos blockPos = packetByteBuf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity) packetContext.getPlayer();
                Planter planter = new Planter(player.interactionManager, player, player.getServerWorld(), Hand.MAIN_HAND, blockPos);
                planter.plant();
            });
        });
    }
}