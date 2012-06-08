/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.BlockPosition;
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
        
        plugin.getLogger().info("up:" + lPos.getBlockAt(lWorld, 0, 1, 0).isBlockPowered());
        plugin.getLogger().info("down:" + lPos.getBlockAt(lWorld, 0, -1, 0).isBlockPowered());
        plugin.getLogger().info("left:" + lPos.getBlockAt(lWorld, 1, 0, 0).isBlockPowered());
        plugin.getLogger().info("right:" + lPos.getBlockAt(lWorld, -1, 0, 0).isBlockPowered());
        plugin.getLogger().info("front:" + lPos.getBlockAt(lWorld, 0, 0, 1).isBlockPowered());
        plugin.getLogger().info("back:" + lPos.getBlockAt(lWorld, 0, 0, -1).isBlockPowered());
        //TODO check if block is part of a building
        // if so then delete building record
    }
}
