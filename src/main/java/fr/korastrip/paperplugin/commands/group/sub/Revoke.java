package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class Revoke extends GroupSubCommand {

    public Revoke(SharedStats plugin) {
        super(plugin, "revoke", "Revoke the group and remove all the players.", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("revoke")
                .executes(context -> {

                    ContextResult ctx = this.getContext(context);
                    SharedGroup group = this.plugin.getGroup();

                    if (!(ctx.entity() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }

                    if (!player.getUniqueId().equals(group.getOwnerUuid())) {
                        return this.generateError(ErrorMessage.NOT_OWNER, player);
                    }

                    group.getOnlinePlayers().forEach(p -> {
                        if(p.getUniqueId().equals(player.getUniqueId())) return;
                        p.sendRichMessage("<white>The group has been revoked by its owner.</white>");
                    });

                    group.resetGroup();
                    player.sendRichMessage("<green>Your group has been revoked.</green>");

                    return Command.SINGLE_SUCCESS;
                }).requires(source -> source.getSender().isOp());
    }
}
