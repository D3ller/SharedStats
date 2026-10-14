package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class Help extends GroupSubCommand {

    public Help(SharedStats plugin) {
        super(plugin, "help", "Give you information about command available.", "help");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {

        return Commands.literal("help").executes(context -> {

            CommandSender sender = context.getSource().getSender();
            Entity executor = context.getSource().getExecutor();

            if (!(executor instanceof Player)) {
                return Command.SINGLE_SUCCESS;
            }

            List<GroupSubCommand> commands = List.of(
                    new Create(plugin),
                    new Revoke(plugin),
                    new Invite(plugin),
                    new Remove(plugin),
                    new Start(plugin),
                    new Stop(plugin),
                    new Players(plugin),
                    new Version(plugin),
                    new Stats(plugin)
            );

            commands.forEach(command -> sender.sendRichMessage("<gold><usage></gold><white>: <description></white>", Placeholder.unparsed("description", command.getDescription()), Placeholder.unparsed("usage", command.getUsage())));

            return Command.SINGLE_SUCCESS;
        });
    }
}
