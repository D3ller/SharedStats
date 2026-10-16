package fr.korastrip.paperplugin.commands.group.sub;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.commands.group.GroupSubCommand;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import fr.korastrip.paperplugin.inventory.ConfigurationInventory;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

public class Configuration extends GroupSubCommand {

    public Configuration(SharedStats plugin) {
        super(plugin, "configuration", "Configure SharedStats using an interface.", "");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("configuration").executes(context -> {
            ContextResult ctx = this.getContext(context);
            SharedGroup group = this.plugin.getGroup();

            if (!(ctx.entity() instanceof Player player)) {
                return this.generateError("<red>This command cannot be used by the console :)</red>", ctx.entity());
            }

            if(!player.getUniqueId().equals(group.getOwnerUuid())) {
                return this.generateError(ErrorMessage.NOT_OWNER, player);
            }

            if(group.getIsPlaying()) {
                return this.generateError(ErrorMessage.GAME_RUNNING, player);
            }

            ConfigurationInventory configurationInventory = new ConfigurationInventory(this.plugin);
            player.openInventory(configurationInventory.getInventory());

            return Command.SINGLE_SUCCESS;
        }).requires(source -> source.getSender().isOp());
    }
}
