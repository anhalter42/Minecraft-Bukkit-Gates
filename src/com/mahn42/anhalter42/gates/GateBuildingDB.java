/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BuildingDB;
import java.io.File;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class GateBuildingDB extends BuildingDB<GateBuilding> {

    public GateBuildingDB() {
        super(GateBuilding.class);
    }

    public GateBuildingDB(World aWorld, File aFile) {
        super(GateBuilding.class, aWorld, aFile);
    }
    
}
