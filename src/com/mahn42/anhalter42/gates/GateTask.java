/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.WorldLineWalk;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author andre
 */
public class GateTask implements Runnable {

    public Gates plugin;
    public GateBuilding gate;
    public boolean open = true;
    public int taskId;
    
    protected boolean fInRun = false;
    protected boolean fInit = false;
    protected BlockPosition fLeftBottom;
    protected BlockPosition fLeftTop;
    protected BlockPosition fRightBottom;
    protected BlockPosition fRightTop;
    protected int fHeight = 0;
    protected int fCount = 0;
    
    public GateTask(Gates aPlugin) {
        plugin = aPlugin;
    }
    
    @Override
    public void run() {
        if (!fInRun) {
            fInRun = true;
            try {
                if (!fInit) {
                    fLeftBottom = gate.getBlock("DoorHingeLeftBottom").position;
                    fLeftTop = gate.getBlock("DoorHingeLeftTop").position;
                    fRightBottom = gate.getBlock("DoorHingeLeftBottom").position;
                    fRightTop = gate.getBlock("DoorHingeRightTop").position;
                    fHeight = ( fLeftTop.y - fLeftBottom.y ) + 1;
                    fCount = fHeight;
                    fInit = true;
                }
                if (open) {
                    doOpen();
                } else {
                    doClose();
                }
            } finally {
                fInRun = false;
            }
        }
    }
    
    protected void doOpen() {
        if (fCount > 0) {
            plugin.getLogger().info("C: " + new Integer(fCount));
            BlockPosition lLeftTop = fLeftTop.clone();
            BlockPosition lRightTop = fRightTop.clone();
            lLeftTop.add(0,1,0);
            lRightTop.add(0,1,0);
            //check if top line is empty
            boolean lTopLineEmpty = true;
            for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                    if (!lPos.getBlockType(gate.world).equals(Material.AIR)) {
                        lTopLineEmpty = false;
                        plugin.getLogger().info("TopLine no AIR");
                        break;
                    }
                }
            }
            if (lTopLineEmpty) {
                lLeftTop.cloneFrom(fLeftTop);
                lRightTop.cloneFrom(fRightTop);
                for(int lDy = 0; lDy < fHeight; lDy++) {
                    for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                        if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                            Block lFrom = lPos.getBlock(gate.world);
                            plugin.framework.setTypeAndData(lPos.getBlockAt(gate.world, 0, 1, 0).getLocation(), lFrom.getType(), lFrom.getData(), true);
                        }
                    }
                    lLeftTop.y--;
                    lRightTop.y--;
                }
                for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                    if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                        plugin.framework.setTypeAndData(lPos.getBlock(gate.world).getLocation(), Material.AIR, (byte)0, true);
                    }
                }
                fLeftTop.add(0,1,0);
                fRightTop.add(0,1,0);
                fCount--;
            } else {
                plugin.stopGateTask(this);
            }
        } else {
            plugin.stopGateTask(this);
        }
    }

    protected void doClose() {
        
    }
}
