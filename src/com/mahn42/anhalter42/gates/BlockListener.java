/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 *
 * @author andre
 */
public class BlockListener implements Listener {
    
    protected Gates plugin;
    
    public BlockListener(Gates aPlugin) {
        plugin = aPlugin;
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent aEvent) {
        Block lBlock = aEvent.getBlock();
        //TODO check if block is part of a building
        // if so then delete building record
    }

    @EventHandler
    public void redstoneBlock(BlockRedstoneEvent aEvent) {
        Block lBlock = aEvent.getBlock();
        World lWorld = lBlock.getWorld();
        BlockPosition lPos = new BlockPosition(lBlock.getLocation());
        ArrayList<GateBuilding> lBuildings = new ArrayList<GateBuilding>();
        GateBuildingDB lDB = plugin.DBs.getDB(lWorld);
        lBuildings = lDB.getRedStoneSensibles(lPos);
        for(GateBuilding lB : lBuildings) {
            plugin.getLogger().info("Cur: " + aEvent.getNewCurrent());
            plugin.getLogger().info("B:" + lB.toCSV());
            plugin.getLogger().info("BB:" + lB.getRedStoneSensibles(lPos));
            GateTask aTask = new GateTask(plugin);
            aTask.gate = lB;
            aTask.open = aEvent.getNewCurrent() > 0;
            plugin.startGateTask(aTask);
            //TODO setup task to open/close gate
        }
    }
}
