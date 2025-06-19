package io.arsh;

import io.arsh.bStats.Metrics;
import io.arsh.commands.JustWarpCmd;
import io.arsh.commands.WarpCmd;
import org.bukkit.plugin.java.JavaPlugin;

public final class JustWarp extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("");
        getLogger().info("\u001B[97m  ▀█▀ █ █ █▀▀ ▀█▀  █ █\u001B[91m █▀█\u001B[93m █▀▄\u001B[36m █▀█ \u001B[0m");
        getLogger().info("\u001B[97m ▄ █  █ █ ▀▀█  █   █▄█\u001B[91m █▀█\u001B[93m █▀▄\u001B[36m █▀▀ \u001B[0m");
        getLogger().info("\u001B[97m  ▀▀  ▀▀▀ ▀▀▀  ▀   ▀ ▀\u001B[91m ▀ ▀\u001B[93m ▀ ▀\u001B[36m ▀   \u001B[0m");
        getLogger().info("\u001B[97m By _arsh.io: Thanks for using my plugin!\u001B[0m");
        getLogger().info("\u001B[97m Check Out my Discord server for support and other releases.\u001B[0m");
        getLogger().info("\u001B[36m Discord:\u001B[97m https://discord.gg/aBqZe3dgxW \u001B[0m");
        getLogger().info("");
        WarpManager warpManager = new WarpManager(this);
        new WarpHandler(this, warpManager);
        new Messages(this);
        new Sounds(this);
        new Titles(this);
        getCommand("warp").setExecutor(new WarpCmd(warpManager));
        getCommand("justwarp").setExecutor(new JustWarpCmd(this, warpManager));
        Metrics metrics = new Metrics(this, 26210);

    }

    @Override
    public void onDisable() {
    }

}
