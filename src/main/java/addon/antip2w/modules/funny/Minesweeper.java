package addon.antip2w.modules.funny;

import addon.antip2w.modules.Categories;
import addon.antip2w.screen.MinesweeperScreen;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;

/*
onActivate -> randomizate new table
onActivate -> open chest
onActivate -> set opened chest screen's name to "Minesweeper Game: #<number>"
HashMap storage
module description = wool colors explained to numbers
if game ended then replace the game ui with a menu screen thing with the options of "leave" and "restart"
*/


public class Minesweeper extends Module {
    public Minesweeper() {
        super(Categories.FUNNY, "Minesweeper", "");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> playSound = sgGeneral.add(new BoolSetting.Builder().name("playSound").defaultValue(true).build());

    private final Setting<Difficulties> difficulty = sgGeneral.add(new EnumSetting.Builder<Difficulties>()
            .name("Choose difficulty")
            .defaultValue(Difficulties.MEDIUM)
            .build());
    // easy = 8 | medium = 10 | hard = 15

    private Setting<Integer> minesCount = sgGeneral.add(new IntSetting.Builder()
            .name("mines")
            .defaultValue(20)
            .sliderRange(1, 25)
            .visible(() -> difficulty.get().toString().equals("CUSTOM"))
            .build());

    public enum Difficulties {
        EASY(8),
        MEDIUM(10),
        HARD(15),
        CUSTOM(0),
        RANDOM((int) (Math.random()*12+8));

        public byte value = 0;

        Difficulties(int value) {
            this.value = (byte) value;
        }
    }

    @Override
    public void onActivate() {
        mc.setScreen(new MinesweeperScreen());
    }
}
