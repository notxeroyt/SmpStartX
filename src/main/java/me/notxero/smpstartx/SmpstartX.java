package me.notxero.smpstartx;

import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SmpstartX extends JavaPlugin {

    private boolean started = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("smpstart").setExecutor(this::onCommand);
    }

    private boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;
        if (!p.isOp() && !p.hasPermission("smpstart.exe")) {
            p.sendMessage(ChatColor.RED + "No permission!");
            return true;
        }

        if (started) {
            p.sendMessage(ChatColor.YELLOW + "SMP already started!");
            return true;
        }

        started = true;

        World world = p.getWorld();
        WorldBorder wb = world.getWorldBorder();
        wb.setCenter(world.getSpawnLocation());
        wb.setSize(16);

        BossBar bar = Bukkit.createBossBar(
                ChatColor.GOLD + "" + ChatColor.BOLD + "SMP Starting...",
                BarColor.PURPLE,
                BarStyle.SEGMENTED_6
        );

        for (Player pl : Bukkit.getOnlinePlayers()) {
            bar.addPlayer(pl);
        }

        new BukkitRunnable() {
            int time = 5;

            @Override
            public void run() {
                if (time == 0) {
                    bar.setTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "SMP START!");
                    wb.setSize(world.getWorldBorder().getSize() * 10, 1);
                    Bukkit.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "🌈 SMP START 🌈");
                    cancel();
                    return;
                }
                bar.setProgress(time / 5.0);
                bar.setTitle(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Starting in " + time);
                time--;
            }
        }.runTaskTimer(this, 0L, 20L);

        return true;
    }
}
