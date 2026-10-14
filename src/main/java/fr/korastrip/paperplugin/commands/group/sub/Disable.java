
package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class Disable extends GroupSubCommand {

    public Disable(SharedStats plugin) {
        super(plugin, "disable", "Disable synchronization for a stat", "disable <health|food|potions>");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("disable")
                .requires(source -> source.getSender().isOp())
                .then(Commands.literal("health")
                        .executes(context -> disable("health", context.getSource())))
                .then(Commands.literal("food")
                        .executes(context -> disable("food", context.getSource())))
                .then(Commands.literal("potions")
                        .executes(context -> disable("potions", context.getSource())));
    }

    private int disable(String stat, CommandSourceStack source) {
        var config = plugin.getSynchronizationConfig();

        if (!config.isEnabled(stat)) {
            source.getSender().sendRichMessage("<yellow>" + stat + " synchronization is already disabled.</yellow>");
            return Command.SINGLE_SUCCESS;
        }

        if (!config.setEnabled(stat, false)) {
            source.getSender().sendRichMessage("<red>Could not save the configuration.</red>");
            return 0;
        }

        source.getSender().sendRichMessage("<green>Disabled " + stat + " synchronization.</green>");
        return Command.SINGLE_SUCCESS;
    }
}
