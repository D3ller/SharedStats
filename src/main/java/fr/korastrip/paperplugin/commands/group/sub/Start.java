package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.Config;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Start extends GroupSubCommand {


    public Start(SharedStats plugin) {
        super(plugin, "start", "Start the game and the synchronization", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("start").executes(context -> {

            CommandSender sender = context.getSource().getSender();
            Entity executor = context.getSource().getExecutor();
            SharedGroup group = this.plugin.getGroup();

            if (!(executor instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                return this.generateError("You do not have permission to use this command.", sender);
            }

            if (group.getIsPlaying()) {
                return this.generateError("The game has already started. Stop it with <gold>/<prefix> stop</gold>.", sender, Placeholder.unparsed("prefix", Config.PREFIX));
            }


            List<Player> players = group.getOnlinePlayers();

            if (players.size() < 2) {
                return this.generateError("You cannot start a game alone. Invite at least one player.", sender);
            }

            group.setHealth(20.0);
            group.syncHealth();

            players.forEach(p -> {

                p.playSound(
                        p.getLocation(),
                        Sound.UI_TOAST_CHALLENGE_COMPLETE,
                        1f,
                        1f
                );

                p.sendRichMessage("<green>The game has started.</green> <white>Do not die!</white>");
            });

            group.startGame();

            return Command.SINGLE_SUCCESS;
        });
    }
}
