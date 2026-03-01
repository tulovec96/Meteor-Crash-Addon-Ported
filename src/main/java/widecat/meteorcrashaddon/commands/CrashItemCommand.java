package widecat.meteorcrashaddon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.commands.SharedSuggestionProvider;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CrashItemCommand extends Command {

    public CrashItemCommand() {
        super("crashitem", "Gives you crash items.");
    }

    @Override
    public void build(LiteralArgumentBuilder<SharedSuggestionProvider> builder) {
        builder.then(literal("CrashFireball").executes(ctx -> {
            // CrashFireball command disabled for 1.21.11 compatibility
            return SINGLE_SUCCESS;
        }));

        builder.then(literal("OOBEgg").executes(ctx -> {
            // OOBEgg command disabled for 1.21.11 compatibility
            return SINGLE_SUCCESS;
        }));

    }
}
