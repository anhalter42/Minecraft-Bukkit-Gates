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
    public boolean open = false;
    public int openCount = 0;
    
    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(open);
        aCols.add(openCount);
    }

    @Override
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        open = Boolean.parseBoolean(aCols.pop());
        openCount = aCols.popInt();
    }
}
