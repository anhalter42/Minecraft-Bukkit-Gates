/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDB;
import com.mahn42.framework.BuildingHandler;
import java.util.logging.Logger;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author andre
 */
public class GateHandler implements BuildingHandler {

    protected Gates plugin;
    
    public GateHandler(Gates aPlugin) {
        plugin = aPlugin;
    }
    
    public boolean breakBlock(BlockBreakEvent aEvent, Building aBuilding) {
        World lWorld = aEvent.getBlock().getWorld();
        GateBuildingDB lDB = plugin.DBs.getDB(lWorld);
        lDB.remove(aBuilding);
        return true;
    }
    
    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        GateTask aTask = new GateTask(plugin);
        aTask.gate = (GateBuilding)aBuilding;
        aTask.open = aEvent.getNewCurrent() > 0;
        plugin.startGateTask(aTask);
        return true;
    }

    @Override
    public boolean playerInteract(PlayerInteractEvent aEvent, Building aBuilding) {
        Player lPlayer = aEvent.getPlayer();
        World lWorld = lPlayer.getWorld();
        boolean lFound = false;
        GateBuildingDB lDB = plugin.DBs.getDB(lWorld);
        if (lDB.getBuildings(aBuilding.edge1).isEmpty()
                && lDB.getBuildings(aBuilding.edge2).isEmpty()) {
            GateBuilding lGate = new GateBuilding();
            lGate.cloneFrom(aBuilding);
            lGate.playerName = lPlayer.getName();
            //Logger.getLogger("detect").info(lGate.toCSV());
            lDB.addRecord(lGate);
            lPlayer.sendMessage("Building " + lGate.getName() + " found.");
            lFound = true;
        }
        return lFound;
    }

    @Override
    public BuildingDB getDB(World aWorld) {
        return plugin.DBs.getDB(aWorld);
    }
    
}
