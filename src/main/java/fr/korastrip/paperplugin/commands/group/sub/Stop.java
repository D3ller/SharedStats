package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.Config;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;

public class Stop extends GroupSubCommand {

    public Stop(SharedStats plugin) {
        super(plugin, "stop", "Stop the current game", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("stop").executes(context -> {

            SharedGroup group = this.plugin.getGroup();

            ContextResult ctx = this.getContext(context);

            if (!(ctx.entity() instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            if(!group.getPlayersId().contains(player.getUniqueId())) {
                return this.generateError(ErrorMessage.NOT_IN_GROUP, ctx.sender());
            }

            if(!player.getUniqueId().equals(group.getOwnerUuid())) {
                return this.generateError(ErrorMessage.NOT_OWNER, ctx.sender());
            }

            if(!group.getIsPlaying()) {
                return this.generateError(ErrorMessage.NO_GAME_RUNNING, ctx.sender());
            }

            player.sendRichMessage("You're game has been stopped.\nTo restart: <gold>/" + Config.PREFIX + " start</gold>");

            group.getOnlinePlayers().forEach(p -> {
                if(p.getUniqueId().equals(player.getUniqueId())) return;
                p.sendRichMessage("<gold><player></gold> has stopped the game. Wait until he restart the game.", Placeholder.component("player", player.displayName()));
            });

            group.resetStats();

            return Command.SINGLE_SUCCESS;

        });
    }
}
