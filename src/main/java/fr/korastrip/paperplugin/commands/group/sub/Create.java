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

import java.util.UUID;

public class Create extends GroupSubCommand {

    public Create(SharedStats plugin) {
        super(plugin, "create", "Create a sharedgroup.", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal(this.name)
                .requires(source -> source.getSender().isOp())
                .executes(context -> {
                    ContextResult ctx = this.getContext(context);
                    SharedGroup group = this.plugin.getGroup();

                    if (!(ctx.entity() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }

                    if(!group.getPlayersId().isEmpty()) {
                        return this.generateError(ErrorMessage.GROUP_ALREADY_EXISTS, ctx.sender());
                    }

                    UUID playerUUID = player.getUniqueId();
                    group.addPlayer(playerUUID);
                    group.setOwnerUuid(playerUUID);

                    ctx.sender().sendRichMessage("<green>Your group has been created.</green>\n<white>Invite players with <gold>/sharedgroup invite [player]</gold>.</white>");
                    return Command.SINGLE_SUCCESS;
                });
    }
}
