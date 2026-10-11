package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.Config;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubcommand;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Version implements GroupSubcommand {

    private final SharedStats plugin;

    public Version(SharedStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("version").executes(context -> {
            CommandSender sender = context.getSource().getSender();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            sender.sendRichMessage(
                    "<green><pluginname></green> <grey>V.<version> by Kora\nUpdated: " + dateFormat.format(Config.LAST_VERSION) + "</grey>",
                    Placeholder.unparsed("version", plugin.getPluginMeta().getVersion()),
                    Placeholder.unparsed("pluginname", plugin.getPluginMeta().getName())
            );

            return Command.SINGLE_SUCCESS;
        });
    }
}
