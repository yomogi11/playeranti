package com.naka.playeranti;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import  org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Playeranti extends JavaPlugin implements Listener{

    private  Map<UUID, Integer> diamondBreakCountMap;
    private void resetCounts() {
        diamondBreakCountMap.clear();
    }



    @Override
    public void onEnable() {
        // Pluginが起動したときの処理
        getLogger().info("playerantiが起動しました.");
        diamondBreakCountMap =new HashMap<>();
        getServer().getPluginManager().registerEvents(this, this);

        new BukkitRunnable() {
            @Override
            public void run() {
                resetCounts();
            }

        }.runTaskTimer(this, 0, 23 * 60 * 15);


    }

    @Override
    public void onDisable() {
        // Pluginがシャットダウンした時の処理
        getLogger().info("playerantiがシャットダウンしました");
        diamondBreakCountMap.clear();

    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIAMOND_BLOCK) {
            UUID playerUUID = event.getPlayer().getUniqueId();
            int currentDiamondBreakCount = diamondBreakCountMap.getOrDefault(playerUUID, 0);
            diamondBreakCountMap.put(playerUUID, currentDiamondBreakCount + 1);

            Player player = event.getPlayer();

            if (currentDiamondBreakCount + 1 >= 30 && player.isOp()) {
                //opを持ってるplayerに通知
                for (Player op : Bukkit.getServer().getOnlinePlayers()) {
                    if(op.isOp()) {
                        op.sendMessage(player.getName() + "が不正行為をしている可能性があります。スペクで監視するなどの行為をとってください" + (currentDiamondBreakCount + 1) + "diamond blocks");
                    }
                }
            }
            event.getPlayer().sendMessage("あなたは、ブロックを壊しました " + (currentDiamondBreakCount + 1) + " diamond blocks ");
        }
    }

}


