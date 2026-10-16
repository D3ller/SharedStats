package fr.korastrip.paperplugin.commands.group;

import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.sub.*;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class GroupCommand {

    private final SharedStats plugin;

    public GroupCommand(SharedStats plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> create() {
        return Commands.literal("sharedgroup")
                .then(new Help(plugin).build())
                .then(new Invite(plugin).build())
                .then(new Create(plugin).build())
                .then(new Start(plugin).build())
                .then(new Stop(plugin).build())
                .then(new Version(plugin).build())
                .then(new Remove(plugin).build())
                .then(new Revoke(plugin).build())
                .then(new Players(plugin).build())
                .then(new Stats(plugin).build())
                .then(new Enable(plugin).build())
                .then(new Disable(plugin).build())
                .then(new Configuration(plugin).build())
                .build();

    }

}
