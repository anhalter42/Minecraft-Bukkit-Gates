package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldDBList;
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
    
    public static void main(String[] args) {
    }

    @Override
    public void onEnable() { 
        framework = Framework.plugin;
        DBs = new WorldDBList<GateBuildingDB>(GateBuildingDB.class, this);
        
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        
        BuildingDescription lDesc;
        BuildingDescription.BlockDescription lBDesc;
        
        lDesc = framework.getBuildingDetector().newDescription("Gates.Door.X");
        lBDesc = lDesc.newBlockDescription("DoorHingeLeftTop");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lBDesc.newRelatedTo(new Vector(0,-10, 0), "DoorHingeLeftBottom");
        lBDesc.newRelatedTo(new Vector(10, 0, 0), "DoorHingeRightTop");
        lBDesc = lDesc.newBlockDescription("DoorHingeLeftBottom");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lBDesc = lDesc.newBlockDescription("DoorHingeRightTop");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lBDesc.newRelatedTo(new Vector(0,-10, 0), "DoorHingeRightBottom");
        lBDesc = lDesc.newBlockDescription("DoorHingeRightBottom");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lDesc.activate();

        lDesc = framework.getBuildingDetector().newDescription("Gates.Door.Z");
        lBDesc = lDesc.newBlockDescription("DoorHingeLeftTop");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lBDesc.newRelatedTo(new Vector(0,-10, 0), "DoorHingeLeftBottom");
        lBDesc.newRelatedTo(new Vector(0, 0, 10), "DoorHingeRightTop");
        lBDesc = lDesc.newBlockDescription("DoorHingeLeftBottom");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lBDesc = lDesc.newBlockDescription("DoorHingeRightTop");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lBDesc.newRelatedTo(new Vector(0,-10, 0), "DoorHingeRightBottom");
        lBDesc = lDesc.newBlockDescription("DoorHingeRightBottom");
        lBDesc.redstoneSensible = true;
        lBDesc.material = Material.IRON_BLOCK;
        lDesc.activate();
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

}
