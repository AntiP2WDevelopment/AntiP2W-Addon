package addon.antip2w.modules.vulcan;

import addon.antip2w.modules.Categories;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;


public class VulcanGlide extends Module{
    /*
var module = rise.registerModule(
    "Vulcan Glide",
    "Ported from Nerbles1 Liquidbounce glide."
);
script.handle("onUnload", function () {
  module.unregister();
});

 {
  module.handle("onTick", function () {
    if (!player.isOnGround() && player.getFallDistance() > 0) {
      if (player.getTicksExisted() % 2 === 0) {
        player.setMotionY(-0.155);
      } else {
        player.setMotionY(-0.1);
      }
    }
  });
}
     */

    private final SettingGroup sgGeneral = settings.createGroup("General");
    private final SettingGroup sgFirst = settings.createGroup("First");
    private final SettingGroup sgSecond = settings.createGroup("Second");
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay (in ticks)")
        .description("Delay between velocity changes.")
        .defaultValue(3)
        .build()
    );

    //Executing the 2nd sat of Velocity
    private final Setting<Boolean> booleanthing = sgGeneral.add(new BoolSetting.Builder()
        .name("2nd type of Velocity")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> VeloDebug = sgGeneral.add(new BoolSetting.Builder()
        .name("Velocity Debug")
        .defaultValue(false)
        .build()
    );

    //First sat of Velocity values
    private final Setting<Double> firstx = sgFirst.add(new DoubleSetting.Builder()
        .name("X")
        .defaultValue(0)
        .build()
    );
    private final Setting<Boolean> firstXRelative = sgFirst.add(new BoolSetting.Builder()
        .name("Relative X")
        .defaultValue(true)
        .build()
    );

    private final Setting<Double> firsty = sgFirst.add(new DoubleSetting.Builder()
        .name("Y")
        .defaultValue(-0.15000000001)
        .build()
    );

    private final Setting<Boolean> firstYRelative = sgFirst.add(new BoolSetting.Builder()
        .name("Relative Y")
        .defaultValue(false)
        .build()
    );

    private final Setting<Double> firstz = sgFirst.add(new DoubleSetting.Builder()
        .name("Z")
        .defaultValue(0)
        .build()
    );

    private final Setting<Boolean> firstZRelative = sgFirst.add(new BoolSetting.Builder()
        .name("Relative Z")
        .defaultValue(true)
        .build()
    );

    //Second sat of Velocity Change
    private final Setting<Double> secx = sgSecond.add(new DoubleSetting.Builder()
        .name("X")
        .defaultValue(0)
        .build()
    );
    private final Setting<Boolean> secXRelative = sgSecond.add(new BoolSetting.Builder()
        .name("Relative X")
        .defaultValue(true)
        .build()
    );

    private final Setting<Double> secy = sgSecond.add(new DoubleSetting.Builder()
        .name("Y")
        .defaultValue(-0.1)
        .build()
    );

    private final Setting<Boolean> secYRelative = sgSecond.add(new BoolSetting.Builder()
        .name("Relative Y")
        .defaultValue(false)
        .build()
    );

    private final Setting<Double> secz = sgSecond.add(new DoubleSetting.Builder()
        .name("Z")
        .defaultValue(0)
        .build()
    );

    private final Setting<Boolean> secZRelative = sgSecond.add(new BoolSetting.Builder()
        .name("Relative Z")
        .defaultValue(true)
        .build()
    );

    public VulcanGlide() {
        super(Categories.DEFAULT, "Vulcan Glide", "");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!VulcanSpider.spoofOnGround && !mc.player.isOnGround() && !mc.player.isTouchingWater() && !mc.player.inPowderSnow && mc.player.fallDistance > 0) {
            if (mc.player.age % delay.get() == 0) {
                double x = firstx.get();
                double y = firsty.get();
                double z = firstz.get();
                if (firstXRelative.get()) x += mc.player.getVelocity().x;
                if (firstYRelative.get()) y += mc.player.getVelocity().y;
                if (firstZRelative.get()) z += mc.player.getVelocity().z;
                mc.player.setVelocity(x, y, z);
                if (VeloDebug.get()) {info("first sat Velocity at: x: " + firstx.get() + " , y= " + firsty.get() + " , z= " + firsty.get());}
            } else if (booleanthing.get()) {
                double x = secx.get();
                double y = secy.get();
                double z = secz.get();
                if (secXRelative.get()) x += mc.player.getVelocity().x;
                if (secYRelative.get()) y += mc.player.getVelocity().y;
                if (secZRelative.get()) z += mc.player.getVelocity().z;
                mc.player.setVelocity(x, y, z);
                if (VeloDebug.get()) {info("Second sat Velocity at: " + "x: " + secx.get() + " , y: " + secy.get() + " , z: " + secz.get());}
            }
        }
    }
}
