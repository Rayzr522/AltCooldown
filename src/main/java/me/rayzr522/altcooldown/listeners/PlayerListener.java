package me.rayzr522.altcooldown.listeners;

import me.rayzr522.altcooldown.AltCooldown;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {
    private final List<String> recentIps = new ArrayList<>();
    private final AltCooldown plugin;

    public PlayerListener(AltCooldown plugin) {
        this.plugin = plugin;
    }

    public boolean preventMessage(PlayerEvent e) {
        String ipAddress = e.getPlayer().getAddress().getAddress().toString().replace("/", "");

        if (!recentIps.contains(ipAddress)) {
            recentIps.add(ipAddress);

            new BukkitRunnable() {
                @Override
                public void run() {
                    recentIps.remove(ipAddress);
                }
            }.runTaskLater(plugin, plugin.getConfig().getLong("message-cooldown") * 20L);

            return false;
        }

        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (preventMessage(e)) {
            e.setJoinMessage(null);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        if (preventMessage(e)) {
            e.setQuitMessage(null);
        }
    }
}
