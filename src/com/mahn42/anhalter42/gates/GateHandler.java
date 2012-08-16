/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDB;
import com.mahn42.framework.BuildingHandlerBase;
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
public class GateHandler extends BuildingHandlerBase {

    protected Gates plugin;
    
    public GateHandler(Gates aPlugin) {
        plugin = aPlugin;
    }
    
    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        boolean lOpen = aEvent.getNewCurrent() > 0;
        GateBuilding lGate = (GateBuilding)aBuilding;
        if (!plugin.existsGateTask(lGate)//) {
                && ((lOpen && !lGate.open) || (!lOpen && lGate.open))) {
            GateTask aTask = new GateTask(plugin);
            aTask.gate = lGate;
            aTask.open = lOpen;
            plugin.startGateTask(aTask);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BuildingDB getDB(World aWorld) {
        return plugin.DBs.getDB(aWorld);
    }
    
    @Override
    public void nextConfiguration(Building aBuilding, BlockPosition position, Player aPlayer) {
        super.nextConfiguration(aBuilding, position, aPlayer);
        if (aBuilding instanceof GateBuilding) {
            GateBuilding lGate = (GateBuilding)aBuilding;
            String lMode = null;
            switch (lGate.mode) {
                case UpDown: lGate.mode = GateBuilding.GateMode.DownUp; lMode = "down"; break;
                case DownUp: lGate.mode = GateBuilding.GateMode.UpDown; lMode = "up"; break;
                case LeftRight: lGate.mode = GateBuilding.GateMode.RightLeft; lMode = "right"; break;
                case RightLeft: lGate.mode = GateBuilding.GateMode.LeftRight; lMode = "left"; break;
            }
            if (aPlayer != null) {
                aPlayer.sendMessage("Gate goes on open " + lMode + ".");
            }
        }
    }
    
    @Override
    public Building insert(Building aBuilding) {
        GateBuildingDB lDB = plugin.DBs.getDB(aBuilding.world);
        GateBuilding lGate = new GateBuilding();
        lGate.cloneFrom(aBuilding);
        if (lGate.description.name.matches("Gates.DoorLeft.*")) {
            lGate.mode = GateBuilding.GateMode.LeftRight;
        }
        plugin.getLogger().info("GateMode: " + lGate.mode + " desc " + lGate.description.name);
        lDB.addRecord(lGate);
        return lGate;
    }
}
