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

import java.util.Comparator;

public class Stats extends GroupSubCommand {

    public Stats(SharedStats plugin) {
        super(plugin, "leaderboard", "Get the group death leaderboards.", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("leaderboard").executes(context -> {

            ContextResult ctx = this.getContext(context);
            SharedGroup group = this.plugin.getGroup();

            if (!(ctx.entity() instanceof Player player)) {
                return 0;
            }

            if(!group.getPlayersId().contains(player.getUniqueId())) {
                return this.generateError(ErrorMessage.NOT_IN_GROUP, player);
            }

            if(!group.getIsPlaying()) {
                return this.generateError(ErrorMessage.NO_GAME_RUNNING, player);
            }

            var playerPerScore = group.getOnlinePlayers().stream().sorted(Comparator.comparingInt(group::getPlayerDeaths).reversed()).toList();

            player.sendRichMessage("<gold>Group Leaderboard: </gold>\nYour team got <totaldeath> death.", Placeholder.unparsed("totaldeath", String.valueOf(group.getDeadCount())));
            playerPerScore.forEach(p -> {
                player.sendRichMessage("<gold><player></gold> got <red><death></red> death", Placeholder.component("player", p.displayName()), Placeholder.unparsed("death", String.valueOf(group.getPlayerDeaths(p))));
            });

            return Command.SINGLE_SUCCESS;
        });
    }
}
