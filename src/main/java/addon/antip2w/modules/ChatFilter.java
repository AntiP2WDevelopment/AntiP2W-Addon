package addon.antip2w.modules;

import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ChatFilter extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> filters = sgGeneral.add(new StringListSetting.Builder()
            .name("filters")
            .description("The contents to check if a message matches.")
            .defaultValue()
            .build());

    private final Setting<StringComparisonType> comparisonType = sgGeneral.add(new EnumSetting.Builder<StringComparisonType>()
            .name("comparison")
            .description("How to determine if the message should be hidden.")
            .defaultValue(StringComparisonType.Contains)
            .build());

    private final Setting<Boolean> ignoreCase = sgGeneral.add(new BoolSetting.Builder()
            .name("ignore-case")
            .description("Whether the checking should ignore character casing.")
            .defaultValue(true)
            .build());

    public ChatFilter() {
        super(Categories.DEFAULT, "ChatFilter", "Prevent messages from displaying. Useful for hiding automated messages.");
    }

    @EventHandler
    private void onReceiveMessage(ReceiveMessageEvent event) {
        for (String s : filters.get()) {
            if (comparisonType.get().contains(event.getMessage().getString(), s, ignoreCase.get()))
                event.cancel();
        }
    }

    public enum StringComparisonType {
        Equals,
        Contains,
        StartsWith,
        EndsWith;

        public boolean contains(String from, String to, boolean ignoreCase) {
            switch (this) {
                case Equals -> {
                    return ignoreCase ? StringUtils.equals(from, to) : StringUtils.equalsIgnoreCase(from, to);
                }
                case Contains -> {
                    return ignoreCase ? StringUtils.contains(from, to) : StringUtils.containsIgnoreCase(from, to);
                }
                case StartsWith -> {
                    return ignoreCase ? StringUtils.startsWith(from, to) : StringUtils.startsWithIgnoreCase(from, to);
                }
                case EndsWith -> {
                    return ignoreCase ? StringUtils.endsWith(from, to) : StringUtils.endsWithIgnoreCase(from, to);
                }
                default -> {
                    return false;
                }
            }
        }
    }
}