package at.hugo.papermc.plugin.example.plugin;

import at.hugo.papermc.plugin.example.listener.GUIListener;
import at.hugob.plugin.library.config.YamlFileConfig;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {
    private YamlFileConfig messages;

    @Override
    public void onEnable() {
        getServer().getPluginManager().addPermission(new Permission(
            "example.command.version",
            PermissionDefault.FALSE
        ));
        getServer().getPluginManager().addPermission(new Permission(
            "example.command.reload",
            PermissionDefault.FALSE
        ));
        getServer().getPluginManager().addPermission(new Permission(
            "example.use",
            PermissionDefault.FALSE
        ));
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
    }

    @Override
    public void reloadConfig() {
        saveDefaultConfig();
        super.reloadConfig();
        messages.reload();
    }


    public YamlFileConfig getMessagesConfig() {
        return messages;
    }
}