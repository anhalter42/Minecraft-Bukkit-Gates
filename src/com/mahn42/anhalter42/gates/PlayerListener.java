/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.Building;
import com.mahn42.framework.Framework;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author andre
 */
public class PlayerListener implements Listener {

    protected Gates plugin;
    
    public PlayerListener(Gates aPlugin) {
        plugin = aPlugin;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent event) {
        Player lPlayer = event.getPlayer();
        World lWorld = lPlayer.getWorld();
        Block lBlock = event.getClickedBlock();
        Material lInHand = null;
        if (event.hasItem()) {
          lInHand = event.getItem().getType();
        }
        if (lBlock != null
                && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.hasItem()
                && (lInHand.equals(Material.BOOK))) {
            ArrayList<Building> lBuildings = Framework.plugin.getBuildingDetector().detect(
                    lWorld,
                    new BlockPosition(lBlock.getLocation().add(-5, -5, -5)),
                    new BlockPosition(lBlock.getLocation().add(5, 5, 5)));
            for(Building lBuilding : lBuildings) {
                Logger.getLogger("detect").info(lBuilding.toCSV());
            }
        }
    }
}
