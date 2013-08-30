package ch.usi.inf.ikm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.ui.RefineryUtilities;

import ch.usi.inf.ikm.extra.TSP_Plot;
import ch.usi.inf.ikm.extra.Time;
import ch.usi.inf.ikm.tsp.City;
import ch.usi.inf.ikm.tsp.NearestNeighbor;
import ch.usi.inf.ikm.tsp.Problem;
import ch.usi.inf.ikm.tsp.Solution;
import ch.usi.inf.ikm.tsp.antcolony.AntColony;

/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * This is the main class for the TSP. After selecting a problem in "probNum",
 * a Problem object containing the set of cities will be created, it will be performed a
 * Nearest Neighbor starting from the first city and then it will be optimized using
 * the Ant Colony System hybridized with the 2-opt for each best ant found.
 * 
 * Optionally, it can plot the best solutions found on a graph created using JFreeChart.
 * To do this, please download the JFreeChart library.
 * 
 * @author Stefano Pongelli
 */
public class Main {
	public static void main(String args[]) {
		Problem problem;
		Random rnd;
		
		//problem number from 0 to 9, see problems array below
		final int probNum = 0;
		//if true shows the best solution I found
		final boolean useSeed = true;
		//if true JFreeChart will be used to plot the path
		final boolean toPlot = false;
		
		//data
		String[] problems = {"ch130", "d198", "eil76", "fl1577", "kroA100", "lin318", "pcb442", "pr439", "rat783", "u1060"};
		int[] optSolutions= { 6110, 15780, 538, 22249, 21282, 42029, 50778, 107217, 8806, 224094 };
		long[] seeds = {1269618360222l, 1269618148808l, 1269618120505l, 1270062658359l, 1269617293222l, 1270036777369l, 1270067431833l, 1270038830402l, 1269716761457l, 1269866310739l};
		
		try {
			//random uses those seeds if useSeed is true
			BufferedReader in = new BufferedReader(new FileReader("src/problems/"+problems[probNum]+".tsp"));
			
			String line;
			//find problem size and create problem
			while (!(line = in.readLine()).contains("DIMENSION")){ }
			String[] inta = line.split(":");
			int num_of_cities = Integer.parseInt(inta[inta.length-1].trim());
			problem = new Problem(num_of_cities, problems[probNum], optSolutions[probNum]);

			//find and add nodes
			Pattern patternLine = Pattern.compile("(\\d+) (.+?) (.+?)$");
            while (!(line = in.readLine()).contains("EOF")) {
                Matcher regex = patternLine.matcher(line);
	            if(regex.find()) {
	            	  Integer id = new Integer(regex.group(1));
	                  Double x = new Double(regex.group(2));
	                  Double y = new Double(regex.group(3));
	                  City city = new City(id,x,y,num_of_cities);
	                  problem.add(city);
	             }
            }
            //create distances (each city knows how far from the other cities it is)
            problem.updateDistances(); 
            in.close();
           
            //start to compute CPU Time (to stay <= 3min)
            long startCpuTimeNano = Time.getCpuTime();
            
            //generate or use seed (generated seeds are time of found solution)
    		long seed;
    		if(useSeed) {
    			seed = seeds[probNum];
    			System.out.println("Solving problem: "+problems[probNum]+" using seed: "+seed);
    		} else {
    			seed = System.currentTimeMillis();
    			System.out.println("Solving problem: "+problems[probNum]+" using a random seed..");
    		}
    		rnd = new Random(seed);     
 
            //generate NN from city 1 by default
    		Solution initialSolution = NearestNeighbor.getSolution(problem, 1); 
    		
    		//AntColony init
    		AntColony colony;
    		
    		//plot solution using JFreeChart library (needs JFreeChart library!)
    		if (toPlot) {
    			final TSP_Plot plot = new TSP_Plot(problems[probNum], initialSolution);
		    	plot.pack();
		    	RefineryUtilities.centerFrameOnScreen(plot);
		    	plot.setVisible(true);
		    	//Ant Colony Optimization with Plot
		    	colony = new AntColony(problem, initialSolution, 10, plot, problems[probNum], rnd, startCpuTimeNano);
    		} else {
    			//Ant Colony Optimization without Plot
        		colony = new AntColony(problem, initialSolution, 10, null, problems[probNum], rnd, startCpuTimeNano);
    		}
    		
    		//either way, let's optimize!
    		Solution finalSolution = colony.optimize();
    		
    		//Out\End
    		System.out.println("Solved problem: "+problems[probNum]+" using seed: "+seed+". Tour length: "+finalSolution.tourLength());
           	System.out.println("Solution: "+finalSolution);
           
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
