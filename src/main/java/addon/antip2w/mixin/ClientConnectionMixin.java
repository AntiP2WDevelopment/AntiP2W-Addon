package addon.antip2w.mixin;

import addon.antip2w.modules.PacketLoggerPlus;
import io.netty.channel.ChannelHandlerContext;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {
    @Shadow public abstract NetworkSide getSide();

    @Inject(method = "sendInternal", at = @At("HEAD"))
    void onSendInternal(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush, CallbackInfo ci) {
        Modules.get().get(PacketLoggerPlus.class).onPacket(packet, getSide());
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"))
    void onChannelReadInternal(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        Modules.get().get(PacketLoggerPlus.class).onPacket(packet, getSide());
    }
}
