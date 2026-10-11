package fr.korastrip.paperplugin.commands.group;

import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.sub.Create;
import fr.korastrip.paperplugin.commands.group.sub.Invite;
import fr.korastrip.paperplugin.commands.group.sub.Start;
import fr.korastrip.paperplugin.commands.group.sub.Version;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class GroupCommand {

    private final SharedStats plugin;

    public GroupCommand(SharedStats plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("sharedgroup")
                .then(new Invite(plugin).build())
                .then(new Create(plugin).build())
                .then(new Start(plugin).build())
                .then(new Version(plugin).build())
                .build();

    }

}
