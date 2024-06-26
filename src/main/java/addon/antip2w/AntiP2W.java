package addon.antip2w;

import addon.antip2w.commands.*;
import addon.antip2w.commands.CreativeFunnies.CreativeFunnies;
import addon.antip2w.hud.BrandHud;
import addon.antip2w.modules.*;
import addon.antip2w.modules.AntiFunny;
import addon.antip2w.modules.crash.CrashyCrash;
import addon.antip2w.modules.crash.Pl3xmapCrash;
import addon.antip2w.modules.funny.*;
import addon.antip2w.modules.griefing.*;
import addon.antip2w.modules.vulcan.VulcanBoatFly;
import addon.antip2w.modules.vulcan.VulcanGlide;
import addon.antip2w.modules.vulcan.VulcanSpider;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
//this is a very cool comment
public class AntiP2W extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final MinecraftClient MC = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
        LOG.info("Initializing AntiP2W addon");

        registerModules();
        registerWIPModules();
        registerCommands();
        registerHUDs();

        LOG.info("RAT says: lol (this is a joke ofc)");
        LOG.info("AntiP2W addon initalization has finished");
    }

    private static void registerModules() {
        Modules.get().add(new AirstrikePlus());
        Modules.get().add(new AntiFunny());
        Modules.get().add(new AntiP2WRPC());
        Modules.get().add(new AntiVanish());
        Modules.get().add(new AutoLC());
        Modules.get().add(new AutoLogin());
        Modules.get().add(new AutoScoreboard());
        Modules.get().add(new AutoSignPlus());
        Modules.get().add(new AutoTitles());
        Modules.get().add(new BoomPlus());
        Modules.get().add(new ChatFilter());
        Modules.get().add(new ChestDisco());
        Modules.get().add(new CrashyCrash());
        Modules.get().add(new CreativePuke());
        Modules.get().add(new DelayPackets());
        Modules.get().add(new DoomBoom());
        Modules.get().add(new DubCounter());
        Modules.get().add(new ExplosiveHands());
        Modules.get().add(new FidgetSpinner());
        Modules.get().add(new FirstClassFlight());
        Modules.get().add(new GameModeNotifier());
        Modules.get().add(new HandOfGod());
        Modules.get().add(new IPModule());
        Modules.get().add(new ItemGenerator());
        Modules.get().add(new LiriasCaptcha());
        Modules.get().add(new Minesweeper());
        Modules.get().add(new MultiUse());
        Modules.get().add(new NbtEditor());
        Modules.get().add(new NoBlockEntities());
        Modules.get().add(new NoFall());
        Modules.get().add(new NoItem());
        Modules.get().add(new PacketLogger());
        Modules.get().add(new Payall());
        Modules.get().add(new PingSpoof());
        Modules.get().add(new PlayerNotifier());
        Modules.get().add(new Radio());
        Modules.get().add(new RivalsKingdoms());
        Modules.get().add(new RivalsPlayers());
        Modules.get().add(new ServerOpNuke());
        Modules.get().add(new SuperHeroFX());
        Modules.get().add(new Trampoline());
        Modules.get().add(new UwUChat());
        Modules.get().add(new VanillaFlight());
        Modules.get().add(new VulcanBoatFly());
        Modules.get().add(new VulcanGlide());
        Modules.get().add(new VulcanSpider());
        Modules.get().add(new WitherAdvertise());
        Modules.get().add(new Creeper_Alert());
    }

    private static void registerWIPModules() {
        Modules.get().add(new BaseFinder());
        Modules.get().add(new CustomPackets());
        Modules.get().add(new IRC());
        Modules.get().add(new PacketLoggerPlus());
        Modules.get().add(new Pl3xmapCrash());
        Modules.get().add(new RivalsMatek());
    }

    private static void registerCommands() {
        Commands.add(new ClipboardGive());
        Commands.add(new CommandCompleteCrash());
        Commands.add(new CreativeFunnies());
        Commands.add(new FunnyCrash());
        Commands.add(new HologramCommand());
        Commands.add(new HotbarClearCommand());
        Commands.add(new IRCCommand());
        Commands.add(new PlayersCommand());
        Commands.add(new ShulkerCounterCommand());
        Commands.add(new SimulateCommand());
        Commands.add(new SpectatorFunny());
        Commands.add(new WEConsoleClear());
    }

    private static void registerHUDs() {
        Hud.get().register(BrandHud.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(Categories.DEFAULT);
        Modules.registerCategory(Categories.FUNNY);
        Modules.registerCategory(Categories.GRIEF);
        Modules.registerCategory(Categories.WIP);
    }

    @Override
    public String getPackage() {
        return "addon.antip2w";
    }
}
