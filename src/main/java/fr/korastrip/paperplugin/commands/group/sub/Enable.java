
package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

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
        var group = plugin.getGroup();

        if (source.getSender() instanceof Player player) {

            if (group.getIsPlaying()) {
                return this.generateError(ErrorMessage.GAME_RUNNING, player);
            }

            if (config.isEnabled(stat)) {
                return this.generateError(ErrorMessage.SYNC_ALREADY_ENABLED, player, Placeholder.unparsed("stat", stat));
            }

            if (!config.setEnabled(stat, true)) {
                return this.generateError(ErrorMessage.CONFIG_SAVE_FAILED, player);
            }

            player.sendRichMessage("<green>Enabled " + stat + " synchronization.</green>");
            return Command.SINGLE_SUCCESS;
        }
        return 0;
    }

}
