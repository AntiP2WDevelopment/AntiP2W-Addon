package addon.antip2w.modules.griefing;

import addon.antip2w.modules.Categories;
import addon.antip2w.utils.MCUtil;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ServerOpNuke extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("Auto disable")
        .description("Automatically disables the module on server kick.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> opAlt = sgGeneral.add(new BoolSetting.Builder()
        .name("Op alt")
        .description("Give your alt account operator.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> stopServer = sgGeneral.add(new BoolSetting.Builder()
        .name("Stop server")
        .description("Runs the /stop command.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> deopAllPlayers = sgGeneral.add(new BoolSetting.Builder()
        .name("Deop all players")
        .description("Removes all online players operator status'.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> opAllPlayers = sgGeneral.add(new BoolSetting.Builder()
        .name("Op all players")
        .description("Makes Everyone op")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> clearAllPlayersInv = sgGeneral.add(new BoolSetting.Builder()
        .name("Clear all players inventory")
        .description("Will clear all online players inventories.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> banAllPlayers = sgGeneral.add(new BoolSetting.Builder()
        .name("Ban all players")
        .description("Automatically bans all online players except yourself.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> ipBanAllPlayers = sgGeneral.add(new BoolSetting.Builder()
        .name("IP Ban all players")
        .description("Automatically IP bans all online players except yourself.")
        .defaultValue(false)
        .visible(banAllPlayers::get)
        .build()
    );

    private final Setting<String> altNameToOp = sgGeneral.add(new StringSetting.Builder()
        .name("Alt account username.")
        .description("The name of your alt to op.")
        .defaultValue("ItsMeTomTom")
        .visible(opAlt::get)
        .build()
    );

    public ServerOpNuke() {
        super(Categories.GRIEF, "ServerOpNuke","Server Operator Nuker goes brrrrrrrrrrrrrrrrrrrrrrrr");
    }

    public ArrayList<PlayerListEntry> getPlayerList() {
        ArrayList<PlayerListEntry> playerList = new ArrayList<>();

        if (mc.world != null) {
            Collection<PlayerListEntry> players = MCUtil.networkHandler().getPlayerList();
            playerList.addAll(players);
        }

        return playerList;
    }


    @EventHandler
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            info("You are not in a world, toggling.");
            toggle();
            return;
        }

        if (mc.isInSingleplayer()) {
            info("Cannot nuke server in singleplayer, toggling.");
            toggle();
            return;
        }

        if (!mc.player.hasPermissionLevel(4)) {
            return;
        }

        ChatUtils.info("Started nuker.");

        if (deopAllPlayers.get()) {
            List<PlayerListEntry> playerList = getPlayerList();
            for (PlayerListEntry player : playerList) {
                String playerName = player.getProfile().getName();
                if(!Objects.equals(mc.player.getName().toString(), playerName)) {
                    ChatUtils.sendPlayerMsg("/deop " + playerName);
                    ChatUtils.info("Attempted to deop user: " + playerName);
                }
            }
        }

        if (opAlt.get()) {
            ChatUtils.sendPlayerMsg("/op " + altNameToOp.get());
            ChatUtils.info("Attempted to op user: " + altNameToOp.get());
        }

        if (opAllPlayers.get()) {
            List<PlayerListEntry> playerList = getPlayerList();
            for (PlayerListEntry player : playerList) {
                String playerName = player.getProfile().getName();
                if(!Objects.equals(mc.player.getName().toString(), playerName)) {
                    ChatUtils.sendPlayerMsg("/op " + playerName);
                    ChatUtils.info("Attempted to op user: " + playerName);
                }
            }
        }

        if(clearAllPlayersInv.get()) {
            List<PlayerListEntry> playerList = getPlayerList();
            for (PlayerListEntry player : playerList) {
                String playerName = player.getProfile().getName();
                if(!Objects.equals(mc.player.getName().toString(), playerName)) {
                    ChatUtils.sendPlayerMsg("/clear " + playerName);
                    ChatUtils.info("Attempted to clear inventory of user: " + playerName);
                }
            }
        }

        if (banAllPlayers.get()) {
            List<PlayerListEntry> playerList = getPlayerList();
            for (PlayerListEntry player : playerList) {
                if (player.getProfile().getId() == mc.player.getUuid() || Objects.equals(player.getProfile().getName(), altNameToOp.get())) continue;
                String playerName = player.getProfile().getName();
                if (!ipBanAllPlayers.get()) {
                    ChatUtils.sendPlayerMsg("/ban " + playerName);
                    ChatUtils.info("Attempted to ban user: " + playerName);
                } else {
                    ChatUtils.sendPlayerMsg("/ban-ip " + playerName);
                    ChatUtils.info("Attempted to IP ban user: " + playerName);
                }
            }
        }

        if (stopServer.get()) {
            ChatUtils.sendPlayerMsg(String.valueOf(Text.of("/stop")));
        }
        toggle();
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (!autoDisable.get()) {
            return;
        }
        toggle();
    }
}
