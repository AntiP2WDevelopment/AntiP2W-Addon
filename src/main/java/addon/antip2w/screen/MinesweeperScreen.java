package addon.antip2w.screen;

import addon.antip2w.modules.funny.Minesweeper;
import addon.antip2w.utils.TimerUtils;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.fabricmc.loader.impl.lib.sat4j.minisat.core.Counter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

// TODO make the code better
public class MinesweeperScreen extends GenericContainerScreen {

    // TODO : methodba rakni a menu updateeket
    private final HashSet<Byte> mines = new HashSet<>();
    private final GameSlot[] gameSlots = new GameSlot[81];
    private final GameSlot[] mapped = new GameSlot[gameSlots.length];
    private byte mineAmount;
    private static long bestTime = 0;
    private static long lastTime = 0;
    private static final Module module = Modules.get().get("Minesweeper");
    private static final EnumSetting<Minesweeper.Difficulty> difficultySetting = (EnumSetting<Minesweeper.Difficulty>) module.settings.get("Choose difficulty");
    private static final IntSetting mineSetting = (IntSetting) module.settings.get("mines");
    private boolean firstClick = true;
    private final TimerUtils timer = new TimerUtils();
    private GameState gameProgress = GameState.IN_PROGRESS;

    public MinesweeperScreen() {
        // new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X6, -69, new PlayerInventory(mc.player), new SimpleInventory(81), 9)
        super(GenericContainerScreenHandler.createGeneric9x6(-69, new PlayerInventory(mc.player)), new PlayerInventory(mc.player), ((MutableText) Text.of("Minesweeper")).formatted(Formatting.UNDERLINE, Formatting.BLACK));
        this.mineAmount = difficultySetting.get() == Minesweeper.Difficulty.CUSTOM ? mineSetting.get().byteValue() : difficultySetting.get().value.get();
        generate();
        update();
    }

    private void runAtSlot(int slotId, Consumer<Integer> consumer) {
        if (slotId >= 0 && slotId < gameSlots.length) consumer.accept(slotId);
    }

    private void runNearby(int slotId, Consumer<Integer> consumer) {
        if (slotId % 9 > 0) {
            runAtSlot(slotId-1, consumer);
            runAtSlot(slotId-10, consumer);
            runAtSlot(slotId+8, consumer);
        }
        if ((slotId+1) % 9 > 0) {
            runAtSlot(slotId+1, consumer);
            runAtSlot(slotId-8, consumer);
            runAtSlot(slotId+10, consumer);
        }
        runAtSlot(slotId+9, consumer);
        runAtSlot(slotId-9, consumer);
    }

    private void update() {
        for (int i = 0; i < gameSlots.length; i++)
            handler.getSlot(i).setStack(mapped[i].itemStack);
        updateMenuSlots();
    }

    private void updateMenuSlots() {
        for (MenuSlot menuSlot : MenuSlot.values())
            handler.getSlot(menuSlot.slotId).setStack(menuSlot.itemStack);
    }

    private void addValue(int slotId) {
        if (slotId >= 0 && slotId < gameSlots.length) gameSlots[slotId] = gameSlots[slotId].next();
    }

    private void playSound(SoundEvent soundEvent, float pitch) {
        mc.world.playSound(mc.player.getX(), mc.player.getY(), mc.player.getZ(), soundEvent, SoundCategory.PLAYERS, 100.0f, pitch, false);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        Text text = switch (gameProgress) {
            // TODO : cleaning lady!!!
            case DEFEAT -> title.copy().formatted(Formatting.BLACK).append(((MutableText) Text.of(" - GAME OVER")).formatted(Formatting.BOLD, Formatting.DARK_RED, Formatting.UNDERLINE));
            case VICTORY -> title.copy().formatted(Formatting.BLACK).append(((MutableText) Text.of(" - VICTORY")).formatted(Formatting.BOLD, Formatting.DARK_GREEN, Formatting.UNDERLINE));
            default -> title;
        };
        context.drawText(this.textRenderer, text, this.titleX, this.titleY, 0x404040, false);
        //context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 0x404040, false);
    }

    private void mine(int slotId) {
        if (gameProgress != GameState.IN_PROGRESS) return;
        if (mapped[slotId] == GameSlot.SPOTTED || mapped[slotId] == GameSlot.FLAG) {
            playSound(SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value(), 1);
            return;
        }
        mapped[slotId] = gameSlots[slotId];
        if (mapped[slotId] == GameSlot.NONE) {
            mapped[slotId] = GameSlot.SPOTTED;
            runNearby(slotId, this::mine);
            playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 3);
        } else if (mapped[slotId] == GameSlot.MINE) {
            for (byte mine : mines) {
                mapped[mine] = GameSlot.MINE;
            }
            ChatUtils.info("GAME OVER!!!");
            ((MutableText) title).formatted(Formatting.RED);
            playSound(SoundEvents.ENTITY_VILLAGER_NO, 1);
            gameProgress = GameState.DEFEAT;
        }
    }

    private void mineAround(int slotId) {
        if (gameSlots[slotId] == GameSlot.NONE) return;
        Counter counter = new Counter();
        runNearby(slotId, a -> {if (mapped[a] == GameSlot.FLAG) counter.inc();});
        if (Integer.parseInt(counter.toString())-1 == mapped[slotId].ordinal()) {
            runNearby(slotId, this::mine);
        }
    }

    private boolean isVictory() {
        if (gameProgress != GameState.IN_PROGRESS) return false;
        int i = 0;
        for (GameSlot value : mapped) {
            if (value == GameSlot.NONE || value == GameSlot.FLAG) i++;
        }
        return i == mines.size();
    }

    public void generate() {
        mines.clear();
        Arrays.fill(gameSlots, GameSlot.NONE);
        Arrays.fill(mapped, GameSlot.NONE);
        firstClick = true;
        gameProgress = GameState.IN_PROGRESS;
        for (; mines.size() < mineAmount; mines.add((byte)Math.round(Math.random()*(gameSlots.length-1)))) {
        }

        for (byte id : mines) {
            gameSlots[id] = GameSlot.MINE;
            runNearby(id, this::addValue);
        }
    }

    @Override
    public void close() {
        super.close();
        if (Modules.get().get("Minesweeper").isActive()) {
            Modules.get().get("Minesweeper").toggle();
        }
    }

    private void updateDifficultyTo(int ordinal) {
        if (ordinal >= Minesweeper.Difficulty.values().length - 1) ordinal = 0;
        if (ordinal < 0) ordinal = Minesweeper.Difficulty.values().length - 1;
        difficultySetting.set(Minesweeper.Difficulty.values()[ordinal]);
        MenuSlot.DIFFICULTY.itemStack.setCustomName(MenuSlot.DIFFICULTY.name.copy().append(" - "+difficultySetting.get().name()));
    }

    private void updateMineAmount() {
        mineAmount = difficultySetting.get() == Minesweeper.Difficulty.CUSTOM ? mineSetting.get().byteValue() : difficultySetting.get().value.get();
        MenuSlot.MINE_AMOUNT.itemStack.setCustomName(MenuSlot.MINE_AMOUNT.name.copy().append(" - "+mineAmount));
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        if (actionType != SlotActionType.PICKUP || slotId < 0 || slotId >= 90) return;

        ChatUtils.info("slotId: "+slotId+" ; "+"button: "+button);

        if (slotId < gameSlots.length) {
            while (firstClick && gameSlots[slotId] != GameSlot.NONE) {
                generate();
            }
            if (firstClick) timer.reset();
            firstClick = false;

            if (mapped[slotId] == GameSlot.NONE) {
                if (button == 1) mapped[slotId] = GameSlot.FLAG;
                if (button == 0) mine(slotId);
            } else if (mapped[slotId].isNumber) {
                mineAround(slotId);
            } else if (mapped[slotId] == GameSlot.FLAG) {
                mapped[slotId] = GameSlot.NONE;
            }

            if (isVictory()) {
                for (byte mine : mines) {
                    mapped[mine] = GameSlot.VICTORY_FLAG;
                }
                ChatUtils.info("VICTORY!!!");
                gameProgress = GameState.VICTORY;
                ChatUtils.info(timer.getDifference() / 1000 + "sec");
                lastTime = timer.getDifference();
                if (bestTime == 0 || lastTime < bestTime) bestTime = lastTime;
                MenuSlot.TIME.itemStack.setCustomName(MenuSlot.TIME.name.copy().append(bestTime / 1000 + " - sec"));
            }
        } else {
            if (MenuSlot.RESET.slotId == slotId) {
                mc.setScreen(new MinesweeperScreen());
                return;
            } else if (MenuSlot.DIFFICULTY.slotId == slotId) {
                int state = difficultySetting.get().ordinal();
                updateDifficultyTo(button == 1 ? state-- : state++);
                updateMineAmount();
                generate();
            } else if (MenuSlot.MINE_AMOUNT.slotId == slotId) {
                if (button == 1) mineAmount--;
                else mineAmount++;
                mineAmount = (byte) MathHelper.clamp(mineAmount, 0, 25);
                updateMineAmount();
                updateDifficultyTo(Minesweeper.Difficulty.CUSTOM.ordinal());
                generate();
            }
        }
        update();
    }

    private enum GameState {
        IN_PROGRESS,
        VICTORY,
        DEFEAT
    }

    private enum MenuSlot {
        RESET(Items.ORANGE_CONCRETE,0, "RESET", Formatting.GOLD),
        TIME(Items.CLOCK, 4,"BEST TIME", Formatting.YELLOW),
        MINE_AMOUNT(Items.TNT_MINECART, 3, "MINES", Formatting.DARK_RED),
        DIFFICULTY(Items.RECOVERY_COMPASS, 8,"DIFFICULTY", Formatting.DARK_RED);

        public final ItemStack itemStack;
        public final int slotId;
        public final MutableText name;

        MenuSlot(Item item, int slotId, String name, Formatting... formattings) {
            this.slotId = slotId+81;
            this.name = ((MutableText) Text.of(name)).formatted(formattings);
            itemStack = item.getDefaultStack().setCustomName(this.name);
        }
    }

    private enum GameSlot {
        NONE(Items.GRAY_STAINED_GLASS_PANE, "???", Formatting.GRAY),
        ONE(Items.CYAN_WOOL, Formatting.AQUA),
        TWO(Items.GREEN_WOOL, Formatting.GREEN),
        THREE(Items.ORANGE_WOOL, Formatting.GOLD),
        FOUR(Items.BLUE_WOOL, Formatting.DARK_BLUE),
        FIVE(Items.RED_WOOL, Formatting.RED),
        SIX(Items.LIGHT_BLUE_WOOL, Formatting.BLUE),
        SEVEN(Items.BLACK_WOOL, Formatting.DARK_GRAY),
        EIGHT(Items.LIGHT_GRAY_WOOL, Formatting.GRAY),
        SPOTTED(Items.AIR),
        FLAG(Items.RED_BANNER, "Flagged"),
        MINE(Items.TNT, "GAME OVER", Formatting.UNDERLINE, Formatting.RED),
        VICTORY_FLAG(Items.GREEN_BANNER, "VICTORY", Formatting.BOLD, Formatting.GREEN);

        public final ItemStack itemStack;
        public final String title;
        public final boolean isNumber;

        GameSlot(Item item) {
            this(item, "", Formatting.RESET);
        }

        GameSlot(Item item, Formatting... formattings) {
            title = this.ordinal()+"";
            isNumber = true;
            itemStack = item.getDefaultStack().setCustomName(getText(formattings));
        }

        GameSlot(Item item, String name, Formatting... formattings) {
            title = name;
            isNumber = false;
            itemStack = item.getDefaultStack().setCustomName(getText(formattings));
        }

        private MutableText getText(Formatting... formattings) {
            MutableText mutableText = (MutableText) Text.of(title);
            mutableText.setStyle(Style.EMPTY.withItalic(false));
            if (isNumber) mutableText.formatted(Formatting.BOLD);
            return mutableText.formatted(formattings);
        }

        private GameSlot next() {
            if (this.ordinal()+1 < 9) return GameSlot.values()[this.ordinal()+1];
            else return this;
        }
    }
}
