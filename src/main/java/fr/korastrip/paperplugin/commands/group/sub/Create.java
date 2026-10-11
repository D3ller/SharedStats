package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubcommand;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Create implements GroupSubcommand {

    private final SharedStats plugin;

    public Create(SharedStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("create")
                .requires(source -> source.getSender().isOp())
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();
                    Entity executor = context.getSource().getExecutor();
                    SharedGroup group = this.plugin.getGroup();

                    if (!(executor instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }

                    if(!group.getPlayersId().isEmpty()) {
                        sender.sendRichMessage("<red>A group already exist you can't create a new one.</red>");
                        return Command.SINGLE_SUCCESS;
                    }

                    UUID playerUUID = player.getUniqueId();
                    group.addPlayer(playerUUID);
                    group.setOwnerUuid(playerUUID);

                    sender.sendRichMessage("Your group has been created, if you want to add player: use <green>/sharedgroup invite [player]</green>");
                    return Command.SINGLE_SUCCESS;
                });
    }
}
