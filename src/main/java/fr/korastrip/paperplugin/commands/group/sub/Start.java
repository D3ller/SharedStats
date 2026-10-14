package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class Start extends GroupSubCommand {


    public Start(SharedStats plugin) {
        super(plugin, "start", "Start the game and the synchronization", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("start").executes(context -> {

            ContextResult ctx = this.getContext(context);
            SharedGroup group = this.plugin.getGroup();

            if (!(ctx.entity() instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                return this.generateError(ErrorMessage.NOT_OWNER, ctx.sender());
            }

            if (group.getIsPlaying()) {
                return this.generateError(ErrorMessage.GAME_RUNNING, ctx.sender());
            }


            List<Player> players = group.getOnlinePlayers();

            if (players.size() < 2) {
                return this.generateError(ErrorMessage.NOT_ENOUGH_PLAYERS, ctx.sender());
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
