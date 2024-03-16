package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Payall extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("The command to use.")
        .defaultValue("/pay")
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Delay between command usage.")
        .defaultValue(20)
        .min(0)
        .sliderMax(200)
        .build()
    );

    private final Setting<String> money = sgGeneral.add(new StringSetting.Builder()
        .name("money")
        .description("Money to be used in the command.")
        .defaultValue("69420")
        .build()
    );

    private final Setting<Boolean> ignorefriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore friends")
        .description("it will not send money to friends")
        .defaultValue(false)
        .build()
    );

    private static int ticks = 0;
    private static Iterator<String> playerList;

    public Payall() {
        super(Categories.DEFAULT, "/payall", "Pays the specified amount of money to others (ignore-self is fucked due to 1.20.4. Add yourself as friend and voila)");
    }

    @Override
    public void onActivate() {
        getPlayerList();
        ticks = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null) return;

        if (!playerList.hasNext()) {
            getPlayerList();
            return;
        }

        if (ticks >= delay.get()+(Math.random()*10)) {
            String message = command.get() + " " + playerList.next() + " " + money.get();
            ChatUtils.sendPlayerMsg(message);
            ticks = 0;
        } else {
            ticks++;
        }
    }

    private void getPlayerList() {
        playerList = Objects.requireNonNull(mc.getNetworkHandler()).getPlayerList().stream()
                .filter(name -> !name.equals(mc.player.getGameProfile().getName()))
                .filter(name -> !ignorefriends.get() || getFriendsList().contains(name))
                .map(entry -> entry.getProfile().getName())
                .iterator();
    }

    private List<String> getFriendsList() {
        List<String> friendsList = new ArrayList<>();
        for (Friend friend : Friends.get()) {
            friendsList.add(friend.name);
        }
        return friendsList;
    }
}
