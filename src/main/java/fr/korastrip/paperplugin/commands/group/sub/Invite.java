package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class Invite extends GroupSubCommand {


    public Invite(SharedStats plugin) {
        super(plugin, "invite", "Add a new player to your group", "invite [Player]");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("invite")
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

                            if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                                sender.sendRichMessage("<red>You cannot invite a player because you are not the owner.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            if (group.getIsPlaying()) {
                                sender.sendRichMessage("<red>You cannot invite a player after the game has started.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            final PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player target;

                            try {
                                target = resolver.resolve(context.getSource()).getFirst();
                            } catch (CommandSyntaxException | NoSuchElementException e) {
                                sender.sendRichMessage("<red>Player not found.</red>");
                                return 0;
                            }

                            if (group.getPlayersId().contains(target.getUniqueId())) {
                                sender.sendRichMessage("<red>This player is already in the group.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            group.addPlayer(target.getUniqueId());

                            TagResolver.Single playerName = Placeholder.component("player", target.displayName());

                            target.sendRichMessage("<green>You have been added to the group.</green> <white>Wait for the owner to start the game.</white>");
                            sender.sendRichMessage("<green><gold><player></gold> has been added to the group.</green>", playerName);

                            return Command.SINGLE_SUCCESS;
                        }))
                .requires(source -> source.getSender().isOp());
    }
}
