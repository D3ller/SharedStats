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
                            ContextResult ctx = this.getContext(context);
                            SharedGroup group = this.plugin.getGroup();

                            if (!(ctx.entity() instanceof Player player)) {
                                return Command.SINGLE_SUCCESS;
                            }

                            if (group.getPlayersId().isEmpty()) {
                                return this.generateError(ErrorMessage.NO_GROUP, ctx.sender());
                            }

                            if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                                return this.generateError(ErrorMessage.NOT_OWNER, ctx.sender());
                            }

                            if (group.getIsPlaying()) {
                                return this.generateError(ErrorMessage.GAME_RUNNING, ctx.sender());
                            }

                            final PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player target;

                            try {
                                target = resolver.resolve(context.getSource()).getFirst();
                            } catch (CommandSyntaxException | NoSuchElementException e) {
                                return this.generateError(ErrorMessage.PLAYER_NOT_FOUND, ctx.sender());
                            }

                            if (target.getUniqueId().equals(player.getUniqueId())) {
                                return this.generateError(ErrorMessage.CANNOT_REMOVE_YOURSELF, ctx.sender());
                            }

                            if (!group.getPlayersId().contains(target.getUniqueId())) {
                                return this.generateError(ErrorMessage.PLAYER_NOT_IN_GROUP, ctx.sender());
                            }

                            group.removePlayer(target.getUniqueId());
                            ctx.sender().sendRichMessage("<green><gold><player></gold> has been removed from your group.</green>", Placeholder.component("player", target.displayName()));
                            target.sendRichMessage("<white>You have been removed from the group by <gold><player></gold>.</white>", Placeholder.component("player", player.displayName()));

                            return Command.SINGLE_SUCCESS;
                        })
                ).requires(source -> source.getSender().isOp());

    }
}
