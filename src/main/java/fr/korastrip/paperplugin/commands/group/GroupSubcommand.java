package fr.korastrip.paperplugin.commands.group;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface GroupSubcommand {
    LiteralArgumentBuilder<CommandSourceStack> build();
}
