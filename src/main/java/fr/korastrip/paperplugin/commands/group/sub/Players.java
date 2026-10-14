package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class Players extends GroupSubCommand {

    public Players(SharedStats plugin) {
        super(plugin, "players", "Get all the players in your groups", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("players").executes(context -> {

            ContextResult ctx = this.getContext(context);
            SharedGroup group = this.plugin.getGroup();

            if (!(ctx.entity() instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            if (!group.getPlayersId().contains(player.getUniqueId())) {
                return this.generateError(ErrorMessage.NOT_IN_GROUP, player);
            }

            var players = group.getOnlinePlayers();

            player.sendRichMessage("<white>Online group members: <gold>" + players.size() + "</gold>.</white>");
            players.forEach(p -> {
                player.sendRichMessage(
                        "<gold><player></gold>" +
                                (p.getUniqueId().equals(group.getOwnerUuid())
                                        ? " <white>(owner)</white>"
                                        : ""),
                        Placeholder.component("player", p.displayName())
                );
            });

            return Command.SINGLE_SUCCESS;
        });
    }
}
