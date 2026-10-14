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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Stop extends GroupSubCommand {

    public Stop(SharedStats plugin) {
        super(plugin, "stop", "Stop the current game", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("stop").executes(context -> {

            SharedGroup group = this.plugin.getGroup();

            CommandSender sender = context.getSource().getSender();
            Entity executor = context.getSource().getExecutor();

            if (!(executor instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            if(!group.getPlayersId().contains(player.getUniqueId())) {
                return this.generateError("You cannot interact with the group, you are not into", sender);
            }

            if(!player.getUniqueId().equals(group.getOwnerUuid())) {
                return this.generateError("Only owners can do this action", sender);
            }

            if(!group.getIsPlaying()) {
                return this.generateError("You cannot stop a game that's not already started", sender);
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
