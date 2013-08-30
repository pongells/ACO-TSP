package ch.usi.inf.ikm.tsp;

import java.util.ArrayList;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * A Problem is created from a TSP problem file.
 * It contains all the cities, the number of cities, a name and the optimal 
 * tour length used to stop the Ant Colony before the 3 minutes time limit.
 * 
 * @author Stefano Pongelli
 */
public class Problem {
	private final int num_of_cities;
	private final ArrayList<City> cities;
	private final String name;
	private final int optimalSolution;
	
	public Problem(int num_of_cities, String name, int optimalSolution) {
		this.num_of_cities = num_of_cities;
		this.cities = new ArrayList<City>(num_of_cities);
		this.name = name;
		this.optimalSolution = optimalSolution;
	}

	public int size() {
		return num_of_cities;
	}
	
	public ArrayList<City> cities() {
		return cities;
	}
	
	public City getCity(final int id) {
		return cities.get(id-1);
	}
	
	public void add(final City city) {
		cities.add(city);
	}
	
	public void updateDistances() {
		for (final City c : cities)
			for (final City d : cities)
				if (!c.knows(d)) { c.setDist(d); d.setDist(c); }
	}
	
	public String getName() {
		return name;
	}
	
	public void printDist() {
	for (City c :cities) 
		System.out.println(c+" -> ") ;
	}
	
	public void printCoordinates() {
		for (final City c : cities) {
			System.out.println(c.getX()+"\t"+c.getY());
		}
		System.out.println(cities.get(0).getX()+"\t"+cities.get(0).getY());
	}
	
	public String toString() {
		return cities.toString();
	}

	public int getOptimalSolution() {
		return optimalSolution;
	}
}
