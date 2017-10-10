/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;
import common.Pair;

import scheduling.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

/**
 *
 * @author Brody
 */
public class OS{ 
   
    //Read the txt input file, for each line,create a process and record its arrival time
    //Put each process in New_Q queue initially then put them in Ready_Q
    //Always check whether the CPU is idle or not; if yes, use your scheduler algorithm to select a process from the Ready_Queue for CPU execution 
    //According to the return value of CPU execute(), put the process into the corresponding queue.  
    //Record the time of every operation for computing your latency and response 
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        
        /*
        Necessary variables section. Can change timeslice if needed,
        can't remember what we all agreed on before :'(
        */
        
        final int timeSlice = 9;
        CPU cpu = new CPU(timeSlice);
        cpu.start();
        IODevice io = new IODevice(timeSlice);
        io.start();
        //Keep track of number of processe images
        int numPImages = 0;
        
        /*
        I am just assuming on a lot of this. The way we have the process
        image class set up, it's basically handling all of the needs of
        the process class I would've implemented, so I saw no need in
        creating a process class just to handle stuff that's already
        being handled, but in a different manner. 
        */
        
        boolean isCPUAvailable = cpu.isBusy();
        ProcessTable process_Table = new ProcessTable(); 
        ArrayList<ProcessImage> New_Queue = new ArrayList<>(); 
        ArrayList<ProcessImage> Ready_Queue = new ArrayList<>(); 
        ArrayList<ProcessImage> Wait_Queue = new ArrayList<>(); 
        ArrayList<ProcessImage> Terminated_Queue = new ArrayList<>();
        
        /*
        JFileChooser to get file easily, assuming it is a text file
        as described in the project assignment sheet.
        */
        
        //Check to see if file was chosen, if not, don't run program.
        boolean fileWasChosen = false;
        
        JFileChooser myFileChooser = new JFileChooser();
        File myFile = null;
        try{
            if (myFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                myFile = myFileChooser.getSelectedFile();
                fileWasChosen = true;
            }
        }
        catch (Exception e){
            System.out.println("Somethin' went wrong partner");
        }
        
        if(fileWasChosen){
            /*
            Take in the information from the selected file, parse the file in the
            order of data fields as shown in the assignment and making process
            images from the data adding the images to the new and ready queues
            */
            String[] fileStrings = null;
            int processID, processPriority, processArrivalOrder;
            String processCode;

            Scanner myFileReader = new Scanner(myFile);
            while (myFileReader.hasNextLine()) {
                fileStrings = myFileReader.nextLine().split(", ");
                processID = Integer.parseInt(fileStrings[0]);
                processArrivalOrder = Integer.parseInt(fileStrings[1]);
                processPriority = Integer.parseInt(fileStrings[2]);
                processCode = fileStrings[3];
                ProcessImage pImage = new ProcessImage(processID, processPriority, 
                                    processArrivalOrder, processCode);
                New_Queue.add(pImage);
                numPImages++;
            }

            /*
            Close the file reading scanner becasue 
            Eclipse has told me to in the past
            */
            myFileReader.close();

            //Offer menu for choosing between schedule types
            int choice = 0;
            Scanner myScanner = new Scanner(System.in);
            boolean done = false;
            
            while(!done){
                System.out.println("***Choose one***\n1: First Come First Serve"
                        + "\n2: Round Robin\n3: Static Priority\n4: Exit");
                System.out.print(">");
                //Catch incorrect input and exit program
                try{
                    choice = Integer.parseInt(myScanner.nextLine());
                    System.out.println();
                }
                catch(Exception e){
                    System.out.println("\nIncorrect input, exiting program...");
                    choice = 4;
                }
                
                //Set initial pImage state in new queue to 'ready'
                ProcessImage pImage = New_Queue.remove(0);
                pImage.setState(PCB.ProcessState.READY);
                Ready_Queue.add(pImage);
                
                
                
                //Am just doing FCFS inside the OS class, don't see need
                //for extra class for this alone
                if(choice == 1){
                    System.out.println("First Come First Serve");
                    
                    //Index used for noting code position for PCB
                    int index = 0;
                    int newQueueIndex = 1;
                    while(Terminated_Queue.size() < numPImages){
                        //Check for terminated CPU process before assigning new one
                        if(!cpu.isBusy()){
                            CPU.ExecutionResult result = cpu.getExecutionResult();
                            
                            if (result == null) {
                                if(!Ready_Queue.isEmpty()){
                                    ProcessImage processImage = Ready_Queue.remove(0);
                                    cpu.execute(processImage, timeSlice);
                                }
                            } else {
                                //Check state of process currently used by CPU
                                if(result.state == PCB.ProcessState.WAITING){
                                    result.process.setState(PCB.ProcessState.WAITING);
                                    result.process.setProgramCounter(result.nextPC);
                                    Wait_Queue.add(result.process);
                                }
                                else if(result.state == PCB.ProcessState.TERMINATED){
                                    result.process.setState(PCB.ProcessState.TERMINATED);
                                    result.process.setProgramCounter(result.nextPC);
                                    Terminated_Queue.add(result.process);
                                    if(!New_Queue.isEmpty()){
                                        ProcessImage p_Image = New_Queue.remove(0);
                                        p_Image.setState(PCB.ProcessState.READY);
                                        Ready_Queue.add(p_Image);
                                    }
                                }
                                else if(result.state == PCB.ProcessState.READY){
                                    result.process.setState(PCB.ProcessState.READY);
                                    result.process.setProgramCounter(result.nextPC);
                                    Ready_Queue.add(result.process);
                                }
                            }
                        }
                        
                        //Check for terminated IO process before assigning new one
                        if(!io.isBusy()){
                            IODevice.ExecutionResult result = io.getExecutionResult();
                            
                            if (result == null) {
                                if(!Wait_Queue.isEmpty()){
                                    ProcessImage processImage = Wait_Queue.remove(0);
                                    io.execute(processImage, timeSlice);
                                }
                            } else {
                                //Check state of process currently used by IO
                                if(result.state == PCB.ProcessState.WAITING){
                                    result.process.setState(PCB.ProcessState.WAITING);
                                    result.process.setProgramCounter(result.nextPC);
                                    Wait_Queue.add(result.process);
                                }
                                else if(result.state == PCB.ProcessState.TERMINATED){
                                    result.process.setState(PCB.ProcessState.TERMINATED);
                                    result.process.setProgramCounter(result.nextPC);
                                    Terminated_Queue.add(result.process);
                                    if(!New_Queue.isEmpty()){
                                        ProcessImage p_Image = New_Queue.remove(0);
                                        p_Image.setState(PCB.ProcessState.READY);
                                        Ready_Queue.add(p_Image);
                                    }
                                }
                                else if(result.state == PCB.ProcessState.READY){
                                    result.process.setState(PCB.ProcessState.READY);
                                    result.process.setProgramCounter(result.nextPC);
                                    Ready_Queue.add(result.process);
                                }
                            }
                        }
                    }
                }
                //God help me understand how to use the RR and Static Priority classes
                else if(choice == 2){
                    System.out.println("Round Robin");
                    for(ProcessImage p : New_Queue){
                        p.setState(PCB.ProcessState.READY);
                        Ready_Queue.add(p);
                        process_Table.updateState(pImage.getPCB_ID(), PCB.ProcessState.READY);
                    }
                    RoundRobin roundRobin = new RoundRobin(Ready_Queue, 9);
                    
                    while(Terminated_Queue.size() < numPImages){
                        
                    }
                    
                    /*
                    Preemption, get work needed set work progress
                    */
                    
                }
                else if(choice == 3){
                    System.out.println("Static Priority");
                    int index = 0;
                    for(ProcessImage p : New_Queue){
                        p.setState(PCB.ProcessState.READY);
                        Ready_Queue.add(p);
                        process_Table.updateState(pImage.getPCB_ID(), PCB.ProcessState.READY);
                    }
                    StaticPriority staticPriority = new StaticPriority(Ready_Queue);
                    
                    while(Terminated_Queue.size() < numPImages){
                        if(!cpu.isBusy()){
                            try{
                                ProcessImage processImage = cpu.getProcessImage();
                                process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.WAITING);

                                //see if the process being pulled off is terminated
                                try{
                                    processImage.getInstructionAt(index);
                                    Wait_Queue.add(processImage);
                                }
                                catch (IllegalArgumentException i){
                                    process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.TERMINATED);
                                    Terminated_Queue.add(processImage);
                                    index = 0;
                                    
                                    //Get next process and put in ready queue
                                    ProcessImage p_Image = staticPriority.nextProcess();
                                    Ready_Queue.add(p_Image);
                                    process_Table.updateState(p_Image.getPCB_ID(), PCB.ProcessState.READY);
                                }
                            }
                            catch (NullPointerException n){
                                
                            }

                            if(!Ready_Queue.isEmpty()) {
                                System.out.println("Ready queue size: " + Ready_Queue.size());
                                ProcessImage processImage = Ready_Queue.remove(0);
                                processImage.setState(PCB.ProcessState.RUNNING);
                                process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.RUNNING);
                                
                                
                                /*
                                FIXME I need the next commented line back in some fashion to pass 
                                the processimage to the CPU and IO Device
                                */
                                //cpu.execute(processImage, processImage.getInstructionAt(index));
                                index++;
                                System.out.println("PCB ID: " + processImage.getPCB_ID() + "\nIndex: " + index);
                                System.out.println("CPU is busy");
                               
                            }
                        }
                        
                        if(!io.isBusy()){
                            try{
                                ProcessImage processImage = io.getProcessImage();
                                process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.READY);
                                try{
                                    //If not index out of bounds, process is waiting
                                    processImage.getInstructionAt(index);
                                    Ready_Queue.add(processImage);
                                    
                                }
                                catch (IllegalArgumentException i){
                                    process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.TERMINATED);
                                    Terminated_Queue.add(processImage);
                                    index = 0;
                                    
                                    //Get next process and put in ready queue
                                    ProcessImage p_Image = staticPriority.nextProcess();
                                    Ready_Queue.add(p_Image);
                                    process_Table.updateState(p_Image.getPCB_ID(), PCB.ProcessState.READY);
                                }
                            }
                            catch (NullPointerException n){
                                
                            }
                            
                            if(!Wait_Queue.isEmpty()){
                                System.out.println("Wait queue size: " + Wait_Queue.size());
                                ProcessImage processImage = Wait_Queue.remove(0);
                                processImage.setState(PCB.ProcessState.RUNNING);
                                process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.RUNNING);
                                
                                
                                /*
                                FIXME I need the next commented line back in some fashion to pass 
                                the processimage to the CPU and IO Device
                                */
                                //io.execute(processImage, processImage.getInstructionAt(index));
                                index++;
                                System.out.println("PCB ID: " + processImage.getPCB_ID() + "\nIndex: " + index);
                                System.out.println("IO is busy");
                                
                            }
                            
                        }
                    }
                }
                else
                    done = true;
                
                //Report generation
                if(Terminated_Queue.size() > 0){
                    ArrayList<Long> responseList = new ArrayList<>();
                    ArrayList<Long> latencyList = new ArrayList<>();
                    Reporter myReporter = new Reporter();
                    Pair pair;
                    for(ProcessImage p : Terminated_Queue){
                        System.out.println("Process ID: " + p.getPCB_ID());
                        pair = p.getReport();
                        //Get report, cast variables to longs
                        responseList.add((long)pair.getHead());
                        latencyList.add((long)pair.getTail());
                    }
                    
                    
                    System.out.println("\n***Response Report***");
                    myReporter.handleAll(responseList);
                    
                    System.out.println("\n***Latency Report***");
                    myReporter.handleAll(latencyList);
                    
                }
                //Clear terminated queue for next execution
                Terminated_Queue.clear();
            }
            //Closing scanner because of Eclipse
            myScanner.close();
        }
        System.out.println("Have a nice day :)");   
        cpu.end();
        io.end();
    }
}