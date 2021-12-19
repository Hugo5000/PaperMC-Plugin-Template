package at.hugo.bukkit.plugin.example;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.StringArgument;
import com.advancedkind.plugin.utils.YamlFileConfig;
import com.advancedkind.plugin.utils.command.CommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.runners.model.InitializationError;

import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.text;

public class ExamplePlugin extends JavaPlugin {
    private final static String commandName = "pumpkintrees";
    private YamlFileConfig messages;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        messages = new YamlFileConfig(this, "messages.yml");
        reloadConfig();
        try {
            commandManager = new CommandManager(this, Component.text()
                .append(text("[", NamedTextColor.DARK_GRAY))
                .append(text("Example", NamedTextColor.GOLD))
                .append(text("] ", NamedTextColor.DARK_GRAY)).build(),
                "/" + commandName + " help"
            );
        } catch (InitializationError e) {
            e.printStackTrace();
            setEnabled(false);
        }
        createCommands();
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        messages.reload();
    }

    private void createCommands() {
        var builder = commandManager.manager().commandBuilder(commandName);

        commandManager.command(commandManager.manager().commandBuilder("testcommand", ArgumentDescription.of("poop"))
            .handler(commandContext ->
                commandContext.getSender().sendMessage("test command was successful!")
            )
        );
        commandManager.command(builder.literal("help", ArgumentDescription.of("The main help command"))
            .argument(StringArgument.<CommandSender>newBuilder("query").asOptional().withSuggestionsProvider((context, string) ->
                commandManager.manager().getCommands().stream().map(c -> c.getArguments().stream().findFirst().get().getName()).collect(Collectors.toList())
            ).withDefaultDescription(ArgumentDescription.of("The start of the command to query")))
            .handler(commandContext -> {
                String query = commandContext.getOrDefault("query", "");
                commandManager.queryCommands(query == null ? "" : query, commandContext.getSender());
            })
        );
        commandManager.command(builder.literal("reload", ArgumentDescription.of("Reloads this plugin"))
            .handler(commandContext -> {
                final var sender = commandContext.getSender();
                sender.sendMessage(this.getMessagesConfig().getComponent("commands.reload.start"));
                this.reloadConfig();
                sender.sendMessage(this.getMessagesConfig().getComponent("commands.reload.finish"));
            })
        );
        commandManager.command(builder.literal("player")
            .senderType(Player.class).handler(commandContext -> commandContext.getSender().sendMessage("You're a Player!"))
        );
    }

    public YamlFileConfig getMessagesConfig() {
        return messages;
    }
}
