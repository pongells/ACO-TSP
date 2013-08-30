package ch.usi.inf.ikm.tsp.antcolony;

import java.util.ArrayList;
import java.util.Random;

import ch.usi.inf.ikm.tsp.City;
import ch.usi.inf.ikm.tsp.Solution;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * An ant is an object that moves through the cities following the Ant Colony System rules.
 * 
 * @author Stefano Pongelli
 */
public class Ant extends Solution {
	private final AntColony colony;
	private final double taus[][];
	private ArrayList<City> citiesToVisit;
	private final Random rnd;
	private final int size;

	public Ant(final AntColony colony) {
		super();
		this.rnd = colony.getRandom();
		
		this.colony = colony;
		this.taus = colony.getTaus();
		this.size = colony.getNumOfCities();
		
		this.citiesToVisit = new ArrayList<City>(colony.getCities());
		moveTo(citiesToVisit.get(rnd.nextInt(size)), true);
	}

	public void moveTo(final City city) {
		moveTo(city, false);
	}
	
	public void moveTo(final City city, boolean start) {
		if (!start) {
			localUpdate(path.getLast(), city);
			tourLength += path.getLast().dist(city);
		}
		path.add(city);
		citiesToVisit.remove(city);
		if (citiesToVisit.isEmpty()) {
			tourLength += city.dist(path.getFirst());
			localUpdate(city,path.getFirst());
		}
	}
	
	private final void localUpdate(final City current, final City city) {
		double newTau = (1-colony.getRo())*getTau(current,city)+colony.getRo()*colony.getTau0();
		taus[current.getId() - 1][city.getId() - 1] = newTau;
		taus[city.getId() - 1][current.getId() - 1] = newTau;
	}
	
	public City nextCity() {
		City max = null;
		if (rnd.nextDouble() <= colony.getQ0()) {
			double last = -1;
			max = citiesToVisit.get(0);
			for (final City c : citiesToVisit) {
				double tmp = (getTau(path.getLast(), c) * Math.pow(getEta(path.getLast(), c), colony.getBeta()));
				if (tmp > last) {
					last = tmp;
					max = c;
				}
			}
		} else {
			double sum = 0;
			double params[] = new double[citiesToVisit.size()];
			
			for (int i = 0; i<citiesToVisit.size(); i++) {
				params[i] = (getTau(path.getLast(), citiesToVisit.get(i)) * Math.pow(getEta(path.getLast(),citiesToVisit.get(i)), colony.getBeta()));
				sum += params[i];
			}
			int i = 0;
			final double rand = rnd.nextDouble();
			double p = 0;
			max = citiesToVisit.get(params.length-1);
			while (i < params.length && rand >= (p += params[i]/sum)) {
				max = citiesToVisit.get(++i);
			}
		}
		return max;
	}
	
	private double getEta(final City a, final City b) {
		return 1/new Double(a.dist(b));
	}
	
	private double getTau(final City a, final City b) {
		return taus[a.getId() - 1][b.getId() - 1];
	}

}
