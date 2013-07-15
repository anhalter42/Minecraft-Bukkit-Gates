/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDB;
import com.mahn42.framework.BuildingHandlerBase;
import com.mahn42.framework.Framework;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
                case FlatLeftRight: lGate.mode = GateBuilding.GateMode.FlatRightLeft; lMode = "right"; break;
                case FlatRightLeft: lGate.mode = GateBuilding.GateMode.FlatLeftRight; lMode = "left"; break;
            }
            if (aPlayer != null) {
                aPlayer.sendMessage(Gates.plugin.getText(aPlayer, "Gate goes on open %s.", Framework.plugin.getText(aPlayer, lMode.toString())));
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
        } else if (lGate.description.name.matches("Gates.Flat.*")) {
            lGate.mode = GateBuilding.GateMode.FlatLeftRight;
        }
        plugin.getLogger().info("GateMode: " + lGate.mode + " desc " + lGate.description.name);
        lDB.addRecord(lGate);
        return lGate;
    }

    @Override
    public JavaPlugin getPlugin() {
        return Gates.plugin;
    }
    
}
