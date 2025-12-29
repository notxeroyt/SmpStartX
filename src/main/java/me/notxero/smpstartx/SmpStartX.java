package me.notxero.smpstartx;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SmpStartX extends JavaPlugin {

    private boolean started = false;

    @Override
    public void onEnable() {
        World world = Bukkit.getWorlds().get(0);
        WorldBorder border = world.getWorldBorder();

        border.setCenter(world.getSpawnLocation());
        border.setSize(16); // 16x16 spawn area
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;

        if (!player.hasPermission("smpstart.exe")) {
            player.sendMessage(ChatColor.RED + "You don't have permission!");
            return true;
        }

        if (started) {
            player.sendMessage(ChatColor.RED + "SMP already started!");
            return true;
        }

        started = true;

        new BukkitRunnable() {
            int time = 5;

            @Override
            public void run() {
                if (time > 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                ChatColor.RED + "" + ChatColor.BOLD + time,
                                "",
                                0, 20, 0
                        );
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    }
                    time--;
                } else {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                ChatColor.GREEN + "" + ChatColor.BOLD + "SMP START",
                                "",
                                10, 40, 10
                        );
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                    }

                    World world = Bukkit.getWorlds().get(0);
                    world.getWorldBorder().setSize(60000000);

                    cancel();
                }
            }
        }.runTaskTimer(this, 0, 20);

        return true;
    }
}
