package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
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
                                return this.generateError(ErrorMessage.NO_GROUP, sender);
                            }

                            if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                                return this.generateError(ErrorMessage.NOT_OWNER, sender);
                            }

                            if (group.getIsPlaying()) {
                                return this.generateError(ErrorMessage.GAME_RUNNING, sender);
                            }

                            final PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player target;

                            try {
                                target = resolver.resolve(context.getSource()).getFirst();
                            } catch (CommandSyntaxException | NoSuchElementException e) {
                                return this.generateError(ErrorMessage.PLAYER_NOT_FOUND, sender);
                            }

                            if (group.getPlayersId().contains(target.getUniqueId())) {
                                return this.generateError(ErrorMessage.PLAYER_ALREADY_IN_GROUP, sender);
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
