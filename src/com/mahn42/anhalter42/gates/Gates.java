package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldDBList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andre
 */
public class Gates extends JavaPlugin {

    public Framework framework;

    public WorldDBList<GateBuildingDB> DBs;
    
    protected HashMap<GateBuilding, GateTask> fGateTasks = new HashMap<GateBuilding, GateTask>();
    
    public static void main(String[] args) {
    }

    @Override
    public void onEnable() { 
        framework = Framework.plugin;
        DBs = new WorldDBList<GateBuildingDB>(GateBuildingDB.class, this);
        
        framework.registerSaver(DBs);
        
        GateHandler lHandler = new GateHandler(this);
        
        BuildingDescription lDesc;
        BuildingDescription.BlockDescription lBDesc;
        BuildingDescription.RelatedTo lRel;
        
        lDesc = framework.getBuildingDetector().newDescription("Gates.Door");
        lDesc.typeName = "Gate";
        lDesc.handler = lHandler;
        lBDesc = lDesc.newBlockDescription("DoorHingeLeftTop");
        lBDesc.redstoneSensible = true;
        lBDesc.nameSensible = true;
        lBDesc.detectSensible = true;
        lBDesc.materials.add(Material.IRON_BLOCK);
        lRel = lBDesc.newRelatedTo(new Vector(0,-10, 0), "DoorHingeLeftBottom");
        lRel.materials.add(Material.SMOOTH_BRICK);
        lRel.materials.add(Material.BRICK);
        lRel.minDistance = 1;
        lRel = lBDesc.newRelatedTo(new Vector(10, 0, 0), "DoorHingeRightTop");
        lRel.minDistance = 1;
        lBDesc = lDesc.newBlockDescription("DoorHingeLeftBottom");
        lBDesc.redstoneSensible = true;
        lBDesc.materials.add(Material.IRON_BLOCK);
        lBDesc = lDesc.newBlockDescription("DoorHingeRightTop");
        lBDesc.redstoneSensible = true;
        lBDesc.nameSensible = true;
        lBDesc.materials.add(Material.IRON_BLOCK);
        lRel = lBDesc.newRelatedTo(new Vector(0,-10, 0), "DoorHingeRightBottom");
        lRel.materials.add(Material.SMOOTH_BRICK);
        lRel.materials.add(Material.BRICK);
        lRel.minDistance = 1;
        lBDesc = lDesc.newBlockDescription("DoorHingeRightBottom");
        lBDesc.redstoneSensible = true;
        lBDesc.materials.add(Material.IRON_BLOCK);
        lDesc.createAndActivateXZ();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        /*for(GateTask lTask : fGateTasks.values()) {
            lTask.doEmegency();
        }*/
    }

    public boolean existsGateTask(GateBuilding aGate) {
        return fGateTasks.containsKey(aGate);
    }
    
    public void startGateTask(GateTask aTask) {
        aTask.taskId = getServer().getScheduler().scheduleAsyncRepeatingTask(this, aTask, 1, 15);
        fGateTasks.put(aTask.gate, aTask);
        //getLogger().info("start task " + new Integer(aTask.taskId));
    }
    
    public void stopGateTask(GateTask aTask) {
        getServer().getScheduler().cancelTask(aTask.taskId);
        fGateTasks.remove(aTask.gate);
        //getLogger().info("stop task " + new Integer(aTask.taskId));
    }
    
}
