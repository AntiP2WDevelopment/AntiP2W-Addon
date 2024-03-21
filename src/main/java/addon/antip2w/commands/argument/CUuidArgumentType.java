package addon.antip2w.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CUuidArgumentType implements ArgumentType<UUID> {
    public static final SimpleCommandExceptionType INVALID_UUID = new SimpleCommandExceptionType(Text.translatable("argument.uuid.invalid"));
    private static final Collection<String> EXAMPLES = List.of("dd12be42-52a9-4a91-a8a1-11c01849e498");
    private static final Pattern VALID_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");

    public static UUID getUuid(CommandContext<?> ctx, String name) {
        return ctx.getArgument(name, UUID.class);
    }

    public static CUuidArgumentType create() {
        return new CUuidArgumentType();
    }

    @Override
    public UUID parse(StringReader stringReader) throws CommandSyntaxException {
        String remaining = stringReader.getRemaining();
        Matcher matcher = VALID_CHARACTERS.matcher(remaining);
        if (!matcher.find()) throw INVALID_UUID.create();
        String match = matcher.group(1);
        try {
            UUID uUID = UUID.fromString(match);
            stringReader.setCursor(stringReader.getCursor() + match.length());
            return uUID;
        } catch (IllegalArgumentException ignored) {
            throw INVALID_UUID.create();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
