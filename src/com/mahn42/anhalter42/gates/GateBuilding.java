/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.gates;

import com.mahn42.framework.Building;
import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class GateBuilding extends Building {
    public enum GateMode {
        UpDown,
        DownUp,
        LeftRight,
        RightLeft,
        MiddleLeftRight,
        MiddleUpDown
    }

    public boolean open = false;
    public int openCount = 0;
    public GateMode mode = GateMode.UpDown;
    
    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(open);
        aCols.add(openCount);
        aCols.add(mode);
    }

    @Override
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        open = Boolean.parseBoolean(aCols.pop());
        openCount = aCols.popInt();
        String lMode = aCols.pop();
        if (lMode != null && !lMode.isEmpty()) {
            mode = GateMode.valueOf(lMode);
        }
    }
}
