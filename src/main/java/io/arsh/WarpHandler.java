package io.arsh;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class WarpHandler implements Listener {

    private static JavaPlugin plugin;
    private static WarpManager warpManager;
    private static final HashMap<UUID, Integer> teleportTasks = new HashMap<>();
    private static final HashMap<UUID, BukkitRunnable> particleTasks = new HashMap<>();

    public WarpHandler(JavaPlugin plugin, WarpManager warpManager) {
        WarpHandler.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        WarpHandler.warpManager = warpManager;
    }

    public static void warp(Player player, String warp) {

        UUID uuid = player.getUniqueId();

        Messages.send(player, Messages.Message.TELEPORTING, "{warp}", warp, "{delay}", String.valueOf(warpManager.getWarpDelay(warp)));
        Titles.sendTitle(player, Titles.Title.TELEPORTING,  "{warp}", warp, "{delay}", String.valueOf(warpManager.getWarpDelay(warp)));
        Sounds.playSound(player, Sounds.Sound.TELEPORTING);

        int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            teleportTasks.remove(uuid);
            warpManager.warp(player, warp);
            Messages.send(player, Messages.Message.TELEPORTED);
            Titles.sendTitle(player, Titles.Title.TELEPORTED);
            Sounds.playSound(player, Sounds.Sound.TELEPORTED);
        }, warpManager.getWarpDelay(warp) * 20L);

        teleportTasks.put(uuid, taskID);
        if (plugin.getConfig().getBoolean("Settings.Particles")) spawnEnchantParticleRing(player, warpManager.getWarpDelay(warp));
        if (plugin.getConfig().getBoolean("Settings.Blindness")) player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (warpManager.getWarpDelay(warp) + 1) * 20, 1));
    }

    public static void spawnEnchantParticleRing(Player player, int seconds) {
        final int totalTicks = seconds * 20;
        final Location center = player.getLocation().clone();
        final World world = player.getWorld();
        List<Location> ringPoints = new ArrayList<>();
        double radius = 0.7;
        for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 16) {
            double x = center.getX() + Math.cos(angle) * radius;
            double z = center.getZ() + Math.sin(angle) * radius;
            double y = center.getY() + 1;
            ringPoints.add(new Location(world, x, y, z));
        }
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= totalTicks || !player.isOnline()) {
                    this.cancel();
                    particleTasks.remove(player.getUniqueId());
                    return;
                }
                for (Location loc : ringPoints) {
                    world.spawnParticle(Particle.ENCHANT, loc, 1, 0, 0, 0, 0);
                }
                ticks += 5;
            }
        };
        bukkitRunnable.runTaskTimer(plugin, 0L, 5L);
        particleTasks.put(player.getUniqueId(), bukkitRunnable);
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getConfig().getBoolean("Settings.Allow-Movements")) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!teleportTasks.containsKey(uuid)) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
            if (particleTasks.containsKey(player.getUniqueId())) {
                BukkitRunnable runnable = particleTasks.get(uuid);
                runnable.cancel();
                particleTasks.remove(player.getUniqueId());
            }
            int taskID = teleportTasks.remove(uuid);
            Bukkit.getScheduler().cancelTask(taskID);
            Messages.send(player, Messages.Message.TELEPORTATION_CANCELED);
            Titles.sendTitle(player, Titles.Title.TELEPORTATION_CANCELED);
            Sounds.playSound(player, Sounds.Sound.TELEPORTATION_CANCELED);
        }
    }

}
