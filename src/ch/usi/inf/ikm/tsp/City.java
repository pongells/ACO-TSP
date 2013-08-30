package ch.usi.inf.ikm.tsp;

import java.util.ArrayList;

/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * A city is a node with an id, a set of coordinates (x,y) and an array 
 * of distances to all the other cities.
 * 
 * @author Stefano Pongelli
 */
public class City {
	private final int id;
	private final double x;
	private final double y;
	private int dist[];
	
	public City(final int id, final double x, final double y, final int num_of_cities) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.dist = new int[num_of_cities];
	}
	
	public final double getX() {
		return x;
	}
	
	public final double getY() {
		return y;
	}
	
	public final void setDist(final City c){
		double dx = this.x - c.x;
		double dy = this.y - c.y;
		int d = (int) Math.round(Math.sqrt(dx*dx + dy*dy));
		dist[c.getId() - 1] = d;
	}
	
	public int dist(final City c) {
		return dist[c.getId() - 1];
	}
	
	public boolean knows(City c) {
		return (dist[c.getId() - 1] != 0);
	}

	public int getId() {
		return id;
	}
	
	public String toString() {
		return ""+id;
	}
	
	public City getNearest(final Problem prob, final Solution sol) {
		final ArrayList<City> cities = prob.cities();
		City nearest = null;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i <cities.size(); i++) {
			City c = cities.get(i);
			int tmp = dist[i];
			if (min > tmp && !sol.contains(c) && c != nearest) {
				min = tmp;
				nearest = c;
			}
		}
		return nearest;
	}

}
