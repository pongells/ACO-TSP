package ch.usi.inf.ikm.tsp.antcolony;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.usi.inf.ikm.extra.TSP_Plot;
import ch.usi.inf.ikm.extra.Time;
import ch.usi.inf.ikm.tsp.City;
import ch.usi.inf.ikm.tsp.Opt2;
import ch.usi.inf.ikm.tsp.Problem;
import ch.usi.inf.ikm.tsp.Solution;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * An AntColony is the base of the Ant Colony System implementation.
 * It coordinates all the ants, and stops the algorithm if 3 minutes have passed or the optimal solution is found.
 * All the variables of the ant colony system are set here.
 * 
 * @author Stefano Pongelli
 */
public class AntColony {
	private final int numOfAnts;
	private final int numOfCities;
	private final double[][] pheromone;
	private final ArrayList<City> cities;
	private final ArrayList<Ant> ants;
	private final Problem problem;
	
	private double q0;
	private final int beta;
	private final double alpha;
	private final double ro;
	private final double tau0;
	
	private Ant bestSolutionEver;

	private final TSP_Plot p;
	private String title;
	private final Random rnd;
	private final long now;
	
	public AntColony(final Problem problem, final Solution solution, final int numOfAnts, final TSP_Plot plot, final String title, final Random rnd, final long now) {
		//---- variables ----
		this.q0 = 0.9d;
		this.beta = 2;
		this.alpha = 0.1d;
		this.numOfCities = problem.size();
		this.tau0 = 1/new Double(solution.tourLength()*numOfCities);
		this.ro = 0.1d;
		//-------------------
		
		this.problem = problem;
		this.bestSolutionEver = null;
		this.pheromone = new double[numOfCities][numOfCities];
		this.ants = new ArrayList<Ant>(numOfAnts);
		this.numOfAnts = numOfAnts;
		this.cities = problem.cities();
		
		this.p = plot;
		this.title = title;
		this.rnd = rnd;
		this.now = now;
	}
	
	public final Solution optimize() {
		initTaus();
		int solnum = 0;
		while((Time.getCpuTime() - now) < 180000000000l && (bestSolutionEver == null || bestSolutionEver.tourLength() > problem.getOptimalSolution())) {
			//add numOfAnts new ants
			for (int i = 0; i<numOfAnts; i++) {
				ants.add(new Ant(this));
			}
			
			//move all the ants through each city
			for (int i = 0; i<numOfCities-1; i++) {
				for (final Ant ant : ants) {
					final City next = ant.nextCity();
					ant.moveTo(next);
				}
			}
			
			//find local best Ant
			int minL = ants.get(0).tourLength();
			Ant bestAnt = ants.get(0);
			for (final Ant ant : ants) {
				int tmp = ant.tourLength();
				if (tmp <= minL) {
					bestAnt = ant;
					minL = tmp;
				}
			}
			
			//optimize best local ant
			Opt2.optimize(bestAnt, false);
			
			//update pheromone of best ant (ever)
			if (bestSolutionEver == null) globalTrailUpdate(bestAnt);
			else globalTrailUpdate(bestSolutionEver);
			
			//if it is the case, update the best ant (solution) ever
			if (bestSolutionEver == null || bestAnt.tourLength() <= bestSolutionEver.tourLength()) {
				bestSolutionEver = bestAnt;	
				
				if (p != null) {
					plotGraph(bestAnt, solnum++);
				}
				
				System.out.println("Time: "+ ((Time.getCpuTime() - now)/1000000000.0) +" Best tour so far: "+bestSolutionEver.tourLength());
			}

			ants.clear();
		}
		return bestSolutionEver;
	}

	public void globalTrailUpdate(final Ant ant) {
		final double constant = 1/new Double(ant.tourLength());
		City prev = ant.getLast();
		ListIterator<City> itr = ant.getPath().listIterator();
        while (itr.hasNext()) {	
        	City current = itr.next();
        	final double currentTau = pheromone[prev.getId() - 1][current.getId() - 1];
        	double newTau = (1-alpha)*currentTau+alpha*(constant);
        	pheromone[prev.getId() - 1][current.getId() - 1] = newTau;
        	pheromone[current.getId() - 1][prev.getId() - 1] = newTau;
        	prev = current;
        }
	}
	
	private void plotGraph(final Solution sol, final int solNum) {
        final XYSeries series = new XYSeries("path",false,true);
        final LinkedList<City> path = sol.getPath();
        for (final City c : path) {
        	  series.add(c.getX(),c.getY());
		}
        series.add(path.getFirst().getX(),path.getFirst().getY());
        final XYSeriesCollection data = new XYSeriesCollection(series);
        p.getPlot().setDataset(data);
        p.setTitle(title+" (Sol:"+solNum+")");
        p.getChart().setTitle(title + " (Len: "+sol.tourLength()+")");
	}
	
	private final void initTaus() {
		for (int i = 0; i<numOfCities; i++) {
			Arrays.fill(pheromone[i], tau0);
		}
	}

	public Solution getBestSolution() {
		return bestSolutionEver;
	}
	
	public double[][] getTaus() {
		return pheromone;
	}

	public int getNumOfCities() {
		return numOfCities;
	}
	
	public ArrayList<City> getCities() {
		return cities;
	}
	
	public double getQ0() {
		return q0;
	}
	public int getBeta() {
		return beta;
	}
	public double getAlpha() {
		return alpha;
	}
	public double getRo() {
		return ro;
	}
	public double getTau0() {
		return tau0;
	}
	public Random getRandom() {
		return rnd;
	}
}
