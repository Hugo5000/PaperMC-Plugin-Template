package at.hugo.papermc.plugin.example.plugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ExamplePluginBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        var lifecycleEventManager = context.getLifecycleManager();
        lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            final var command = Commands.literal("feacatfidetdebdeimfps");
            commands.register(command
                    .then(Commands.literal("reload").requires(css -> css.getSender().hasPermission("example.command.reload"))
                        .executes(ctx -> {
                            final var sender = ctx.getSource().getSender();
                            final var plugin = JavaPlugin.getPlugin(ExamplePlugin.class);
                            sender.sendMessage(plugin.getMessagesConfig().getComponent("commands.reload.start"));
                            plugin.reloadConfig();
                            sender.sendMessage(plugin.getMessagesConfig().getComponent("commands.reload.finish"));
                            return Command.SINGLE_SUCCESS;
                        }))
                    .then(Commands.literal("version")
                        .requires(css -> css.getSender().hasPermission("example.command.version"))
                        .executes(ctx -> {
                            final var sender = ctx.getSource().getSender();
                            final var plugin = JavaPlugin.getPlugin(ExamplePlugin.class);
                            sender.sendMessage(Component.text(plugin.getName() + " version " + plugin.getPluginMeta().getVersion()));
                            return Command.SINGLE_SUCCESS;
                        }))
                    .requires(css -> css.getSender().hasPermission("example.use"))
                    .build()
            );
        });
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new ExamplePlugin();
    }
}
