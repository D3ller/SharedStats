package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.korastrip.paperplugin.Config;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class Remove extends GroupSubCommand {

    public Remove(SharedStats plugin) {
        super(plugin, "remove", "Remove a player from your group", "remove [Player]");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("remove")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();
                            Entity executor = context.getSource().getExecutor();
                            SharedGroup group = this.plugin.getGroup();

                            if (!(executor instanceof Player player)) {
                                return Command.SINGLE_SUCCESS;
                            }

                            if (group.getPlayersId().isEmpty()) {
                                sender.sendRichMessage("<red>There is no group. Create one with <gold>/sharedgroup create</gold>.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            if (!group.getOwnerUuid().equals(player.getUniqueId())) {
                                sender.sendRichMessage("<red>You do not have permission to use this command.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            if (group.getIsPlaying()) {
                                sender.sendRichMessage("<red>You cannot remove a player during the game.</red>");
                                return 0;
                            }

                            final PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player target;

                            try {
                                target = resolver.resolve(context.getSource()).getFirst();
                            } catch (CommandSyntaxException | NoSuchElementException e) {
                                sender.sendRichMessage("<red>Player not found.</red>");
                                return 0;
                            }

                            if (target.getUniqueId().equals(player.getUniqueId())) {
                                sender.sendRichMessage("<red>You cannot remove yourself. Use <gold>/" + Config.PREFIX + " revoke</gold> instead.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            if (!group.getPlayersId().contains(target.getUniqueId())) {
                                sender.sendRichMessage("<red>This player is not in the group.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            group.removePlayer(target.getUniqueId());
                            sender.sendRichMessage("<green><gold><player></gold> has been removed from your group.</green>", Placeholder.component("player", target.displayName()));
                            target.sendRichMessage("<white>You have been removed from the group by <gold><player></gold>.</white>", Placeholder.component("player", player.displayName()));

                            return Command.SINGLE_SUCCESS;
                        })
                ).requires(source -> source.getSender().isOp());

    }
}
