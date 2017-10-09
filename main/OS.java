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
        
        final int timeSlice = 4;
        CPU cpu = new CPU(timeSlice);
        IODevice io = new IODevice(timeSlice);
        
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
                fileStrings = myFileReader.nextLine().split(" ");
                processID = Integer.parseInt(fileStrings[0]);
                processArrivalOrder = Integer.parseInt(fileStrings[1]);
                processPriority = Integer.parseInt(fileStrings[2]);
                processCode = fileStrings[3];
                ProcessImage pImage = new ProcessImage(processID, processPriority, 
                                    processArrivalOrder, processCode);
                New_Queue.add(pImage);
                Ready_Queue.add(pImage);
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
            
            /*
            Am adding this becasue queue.remove() is a lot easier 
            than managing index values, and temp will hold initial ready 
            queue values
            */
            ArrayList<ProcessImage> Temp_Ready_Queue = Ready_Queue;
            while(!done){
                System.out.println("***Choose one***\n1: First Come First Serve"
                        + "\n2: Round Robin\n3: Static Priority\n4: Exit\n>");
                //Catch incorrect input and exit program
                try{
                    choice = Integer.parseInt(myScanner.nextLine());
                }
                catch(Exception e){
                    System.out.println("Incorrect input, exiting program...");
                    choice = 4;
                }
                
                //If we've removed all from ready, repopulate it
                if(Ready_Queue.isEmpty()){
                    Ready_Queue = Temp_Ready_Queue;
                }    
                
                //Set pImage states in ready queue to 'ready'
                for(ProcessImage pImage : Ready_Queue){
                    pImage.setState(PCB.ProcessState.READY);
                    process_Table.updateState(pImage.getPCB_ID(), PCB.ProcessState.READY);
                }
                
                //Am just doing FCFS inside the OS class, don't see need
                //for extra class for this alone
                if(choice == 1){
                    System.out.println("First Come First Serve");
                    
                    //FIXME cpu.start();
                    //FIXME io.start();
                    //FIXME Have no idea what I am doing here
                    while(Terminated_Queue.size() < numPImages){
                        //Check for terminated CPU process before assigning new one
                        if(!cpu.isBusy()){
                            //Get finished process and place in terminated queue
                            if(cpu.getProcessState() == PCB.ProcessState.TERMINATED){
                                process_Table.updateState(cpu.getProcessID(), PCB.ProcessState.TERMINATED);
                                Terminated_Queue.add(cpu.getProcessImage());
                            }
                            if(Ready_Queue.size() > 0){
                                //Add new process to CPU and run
                                ProcessImage processImage = Ready_Queue.remove(0);
                                processImage.setState(PCB.ProcessState.RUNNING);
                                process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.RUNNING);
                                cpu.execute(processImage, numPImages);
                                cpu.run();
                            }
                        }
                        
                        //Check for terminated IO process before assigning new one
                        if(!io.isBusy()){
                            //Get process and place in terminated queue
                            if(io.getProcessState() == PCB.ProcessState.WAITING){
                                process_Table.updateState(io.getProcessID(), PCB.ProcessState.READY);
                                Ready_Queue.add(io.getProcessImage());
                            }
                            if(Wait_Queue.size() > 0){
                                ProcessImage processImage = Wait_Queue.remove(0);
                                
                                processImage.setState(PCB.ProcessState.RUNNING);
                                process_Table.updateState(processImage.getPCB_ID(), PCB.ProcessState.RUNNING);
                                io.execute(processImage, numPImages);
                                io.run();
                            }
                        }
                    }
                    //FIXME cpu.end();
                    //FIXME io.end();
                }
                //God help me understand how to use the RR and Static Priority classes
                else if(choice == 2){
                    System.out.println("Round Robin");
                    while(Terminated_Queue.size() < numPImages){
                        
                    }
                }
                else if(choice == 3){
                    System.out.println("Static Priority");
                    while(Terminated_Queue.size() < numPImages){
                        
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
    }
}