package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import addon.antip2w.screen.MinesweeperScreen;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;

import java.util.function.Supplier;

/*
onActivate -> randomizate new table
onActivate -> open chest
onActivate -> set opened chest screen's name to "Minesweeper Game: #<number>"
HashMap storage
module description = wool colors explained to numbers
if game ended then replace the game ui with a menu screen thing with the options of "leave" and "restart"
*/

// TODO make the code better
public class Minesweeper extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> playSound = sgGeneral.add(new BoolSetting.Builder()
        .name("playSound")
        .defaultValue(true)
        .build()
    );

    // easy = 8 | medium = 10 | hard = 15
    private final Setting<Difficulty> difficulty = sgGeneral.add(new EnumSetting.Builder<Difficulty>()
        .name("Choose difficulty")
        .defaultValue(Difficulty.MEDIUM)
        .build()
    );

    private Setting<Integer> mines = sgGeneral.add(new IntSetting.Builder()
        .name("mines")
        .defaultValue(20)
        .sliderRange(1, 25)
        .visible(() -> difficulty.get().toString().equals("CUSTOM"))
        .build()
    );

    public Minesweeper() {
        super(Categories.FUNNY, "Minesweeper", "");
    }

    public enum Difficulty {
        EASY(() -> (byte) 8),
        MEDIUM(() -> (byte) 10),
        HARD(() -> (byte) 15),
        CUSTOM(() -> (byte) 0),
        RANDOM(() -> (byte) (Math.random()*12+8));

        public final Supplier<Byte> value;

        Difficulty(Supplier<Byte> value) {
            this.value = value;
        }
    }

    @Override
    public void onActivate() {
        mc.setScreen(new MinesweeperScreen());
    }
}
