/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import java.util.*;

/**
 *
 * @author Brody
 */
public class ProcessTable {
    private ArrayList<PCB> myProgressTable = new ArrayList<>();
    
    public void add(PCB pcb){
        myProgressTable.add(pcb);
    }
    
    public PCB searchTable(int pcbID){
        for(PCB p : myProgressTable ){
            if(pcbID == p.id){
                return p;
            }
        }
        return myProgressTable.get(0);
    }
    
    public void updateState(int pcbID, PCB.ProcessState state){
        for(PCB p : myProgressTable ){
            if(pcbID == p.id){
                p.state = state;
            }
        }
    }
}
