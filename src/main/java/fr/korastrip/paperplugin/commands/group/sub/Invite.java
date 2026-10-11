package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubcommand;
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

public class Invite implements GroupSubcommand {

    private final SharedStats plugin;

    public Invite(SharedStats plugin) {
        this.plugin = plugin;
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
                                sender.sendRichMessage("<red>There currently no group, you can create one using: /sharedgroup create</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                                sender.sendRichMessage("<red>You cannot invite a player bc you're not the owner.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            if (group.getIsPlaying()) {
                                sender.sendRichMessage("<red>You can't invite someone when the game is already started.</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            final PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player target;

                            try {
                                target = resolver.resolve(context.getSource()).getFirst();
                            } catch (CommandSyntaxException | NoSuchElementException e) {
                                sender.sendRichMessage("<red>Unable to find the player</red>");
                                return 0;
                            }

                            if (group.getPlayersId().contains(target.getUniqueId())) {
                                sender.sendRichMessage("<red>You cannot invite a player who is already in the group</red>");
                                return Command.SINGLE_SUCCESS;
                            }

                            group.addPlayer(target.getUniqueId());
                            target.sendRichMessage("<playername>, you've been added to the group, wait until the owner start the game", Placeholder.component("playername", target.displayName()));

                            sender.sendRichMessage("<green><playername> has been added to the group.</green>", Placeholder.component("playername", target.displayName()));

                            return Command.SINGLE_SUCCESS;
                        }))
                .requires(source -> source.getSender().isOp());
    }
}
