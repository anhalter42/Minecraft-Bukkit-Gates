/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.SyncBlockList;
import com.mahn42.framework.WorldLineWalk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author andre
 */
public class GateTask implements Runnable {

    public Gates plugin;
    public GateBuilding gate;
    public boolean open = true;
    public boolean emergency = false;
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
    
    public void doEmegency() {
        while (fCount > 0) {
            run();
        }
    }
    
    @Override
    public void run() {
        if (!fInRun) {
            fInRun = true;
            try {
                if (!fInit) {
                    fLeftBottom = gate.getBlock("DoorHingeLeftBottom").position.clone();
                    fLeftTop = gate.getBlock("DoorHingeLeftTop").position.clone();
                    fRightBottom = gate.getBlock("DoorHingeLeftBottom").position.clone();
                    fRightTop = gate.getBlock("DoorHingeRightTop").position.clone();
                    fHeight = ( fLeftTop.y - fLeftBottom.y ) + 1;
                    if (open) {
                        fCount = fHeight - 1;
                        gate.openCount = 0;
                    } else {
                        fCount = gate.openCount;
                        fLeftTop.y -= ( fHeight - fCount ) - 1;
                        fRightTop.y -= ( fHeight - fCount ) - 1;
                    }
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
            //plugin.getLogger().info("C: " + new Integer(fCount));
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
                        //plugin.getLogger().info("TopLine no AIR");
                        break;
                    }
                }
            }
            if (lTopLineEmpty) {
                lLeftTop.cloneFrom(fLeftTop);
                lRightTop.cloneFrom(fRightTop);
                SyncBlockList lList = new SyncBlockList(gate.world);
                for(int lDy = 0; lDy < fHeight; lDy++) {
                    for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                        if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                            Block lFrom = lPos.getBlock(gate.world);
                            BlockPosition lTo = lPos.clone();
                            lTo.add(0,1,0);
                            lList.add(lTo, lFrom.getType(), lFrom.getData(), true);
                        }
                    }
                    lLeftTop.y--;
                    lRightTop.y--;
                }
                if (fHeight > 0) {
                    lLeftTop.y++;
                    lRightTop.y++;
                }
                for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                    if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                        lList.add(lPos, Material.AIR, (byte)0, true);
                    }
                }
                lList.execute();
                fLeftTop.add(0,1,0);
                fRightTop.add(0,1,0);
                fCount--;
                gate.open = true;
                gate.openCount++;
            } else {
                plugin.stopGateTask(this);
            }
        } else {
            plugin.stopGateTask(this);
        }
    }

    protected void doClose() {
        if (fCount > 0) {
            //plugin.getLogger().info("C: " + new Integer(fCount));
            BlockPosition lLeftTop = fLeftTop.clone();
            BlockPosition lRightTop = fRightTop.clone();
            lLeftTop.add(0,-1,0);
            lRightTop.add(0,-1,0);
            //check if bottom line has items to drop
            for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                    Block lBlock = lPos.getBlock(gate.world);
                    Material lMat = lBlock.getType();
                    if (!lMat.equals(Material.AIR)) {
                        if (!(lMat.equals(Material.WATER)
                                || lMat.equals(Material.LAVA)
                                || lMat.equals(Material.STATIONARY_LAVA)
                                || lMat.equals(Material.STATIONARY_WATER))) {
                            ItemStack lStack = new ItemStack(lMat, 1, (short)0, lBlock.getData());
                            gate.world.dropItemNaturally(lPos.getLocation(gate.world), lStack);
                        }
                    }
                }
            }
            lLeftTop.cloneFrom(fLeftTop);
            lRightTop.cloneFrom(fRightTop);
            SyncBlockList lList = new SyncBlockList(gate.world);
            for(int lDy = 0; lDy < fHeight; lDy++) {
                for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                    if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                        Block lFrom = lPos.getBlock(gate.world);
                        BlockPosition lTo = lPos.clone();
                        lTo.add(0,-1,0);
                        lList.add(lTo, lFrom.getType(), lFrom.getData(), true);
                    }
                }
                lLeftTop.y++;
                lRightTop.y++;
            }
            if (fHeight > 0) {
                lLeftTop.y--;
                lRightTop.y--;
            }
            for(BlockPosition lPos : new WorldLineWalk(lLeftTop, lRightTop)) {
                if (!lPos.equals(lLeftTop) && !lPos.equals(lRightTop)) {
                    lList.add(lPos, Material.AIR, (byte)0, true);
                }
            }
            lList.execute();
            fLeftTop.add(0,-1,0);
            fRightTop.add(0,-1,0);
            fCount--;
            gate.open = false;
            gate.openCount = 0;
        } else {
            plugin.stopGateTask(this);
        }
    }
}
