package net.earth.antiproxybypass;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiProxyBypass extends JavaPlugin implements CommandExecutor, Listener {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.reloadConfig();
        this.getCommand("apb").setExecutor(this);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1)
            return false;

        switch (args[0].toLowerCase()) {
            case "rl":
            case "reload":
                this.saveDefaultConfig();
                this.reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Configuration successfully reloaded.");
                return true;
            case "ver":
            case "version":
                sender.sendMessage(ChatColor.GRAY + "AntiProxyBypass is running version " + this.getDescription().getVersion() + ".");
                return true;
            default:
                return false;
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (this.getConfig().getStringList("proxy-addresses")
                .stream()
                .anyMatch(address -> event.getAddress().getHostAddress().contains(address)))
            return;

        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kick-message")));
    }
}