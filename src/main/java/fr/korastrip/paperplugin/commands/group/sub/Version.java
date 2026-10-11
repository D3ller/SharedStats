package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;

public class Version extends GroupSubCommand {

    public Version(SharedStats plugin) {
        super(plugin, "version", "Get information about the version", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("version").executes(context -> {
            CommandSender sender = context.getSource().getSender();

            PluginMeta pluginMeta = plugin.getPluginMeta();

            sender.sendRichMessage(
                    "<white><gold><pluginname></gold> is running version <gold><version></gold>.</white>",
                    Placeholder.unparsed("version", pluginMeta.getVersion()),
                    Placeholder.unparsed("pluginname", pluginMeta.getName())
            );

            return Command.SINGLE_SUCCESS;
        });
    }
}
