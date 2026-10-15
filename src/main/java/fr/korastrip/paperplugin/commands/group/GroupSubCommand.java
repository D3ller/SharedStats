package fr.korastrip.paperplugin.commands.group;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.korastrip.paperplugin.Config;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public abstract class GroupSubCommand {
    public record ContextResult(CommandSender sender, Entity entity) {}
    protected final SharedStats plugin;
    protected final String name;
    protected final String description;
    protected final String usage;

    protected GroupSubCommand(SharedStats plugin, String name, String description, String usage) {
        this.plugin = plugin;
        this.name = name;
        this.description = description;
        this.usage = this.generateUsage(usage);
    }

    public abstract LiteralArgumentBuilder<CommandSourceStack> build();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ContextResult getContext(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        Entity executor = context.getSource().getExecutor();
        return new ContextResult(sender, executor);
    }

    public int generateError(String message, CommandSender player, TagResolver... resolvers) {
        player.sendRichMessage("<red>" + message + "</red>", resolvers);
        return 0;
    }

    public int generateError(ErrorMessage error, CommandSender player, TagResolver... resolvers) {
        return this.generateError(error.getMessage(), player, resolvers);
    }

    public String getUsage() {
        return usage;
    }

    private String generateUsage(String usage) {
        return "/" + Config.PREFIX + " " +
                (usage.isBlank() ? this.name : usage);
    }
}