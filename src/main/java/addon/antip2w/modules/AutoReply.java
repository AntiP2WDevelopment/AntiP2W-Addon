package addon.antip2w.modules;
import addon.antip2w.modules.Categories;

public class AutoReply extends Module {
  private final SettingGroup sgGeneral = settings.getDefaultGroup();

  public AutoReply() {
    super(Categories.DEFAULT, "AutoReply","");
  }

  private final Setting<String> name = sgGeneral.add(new StringSetting.Builder()
        .name("Name")
        .defaultValue("ItsMeTomTom")
        .build()
    );

  @EventHandler
  private void sendmsg(PacketEvent.Send packet) {
    if (packet.event instanceof ChatMessageC2S event) {
      ChatUtils.sendPlayerMsg("/msg "+name+" "+event.content().toString());
      event.cancel();
    }
  }
}
