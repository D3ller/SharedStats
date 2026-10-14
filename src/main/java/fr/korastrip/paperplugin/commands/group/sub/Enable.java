
package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.Locale;
import java.util.Set;

public class Enable extends GroupSubCommand {
    public Enable(SharedStats plugin) {
        super(plugin, "enable", "Enable synchronization for a stat", "enable <health|food|potions>");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("enable")
                .requires(source -> source.getSender().isOp())
                .then(Commands.literal("health")
                        .executes(context -> enable("health", context.getSource())))
                .then(Commands.literal("food")
                        .executes(context -> enable("food", context.getSource())))
                .then(Commands.literal("potions")
                        .executes(context -> enable("potions", context.getSource())));
    }

    private int enable(String stat, CommandSourceStack source) {
        var config = plugin.getSynchronizationConfig();

        if (config.isEnabled(stat)) {
            source.getSender().sendRichMessage("<yellow>" + stat + " synchronization is already enabled.</yellow>");
            return Command.SINGLE_SUCCESS;
        }

        if (!config.setEnabled(stat, true)) {
            source.getSender().sendRichMessage("<red>Could not save the configuration.</red>");
            return 0;
        }

        source.getSender().sendRichMessage("<green>Enabled " + stat + " synchronization.</green>");
        return Command.SINGLE_SUCCESS;
    }
}
