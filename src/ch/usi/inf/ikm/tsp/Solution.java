package ch.usi.inf.ikm.tsp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * A solution contains a linked list of cities (a path), and a tour length (cost).
 * It can be used to create a random solution if necessary.
 * 
 * @author Stefano Pongelli
 */
public class Solution {
	protected final LinkedList<City> path;
	protected int tourLength;
	
	public Solution() {
		path = new LinkedList<City>();
		tourLength = 0;
	}
	
	public final void generateRandom(final Problem p) {
		path.addAll(p.cities());
		Collections.shuffle(path);
		tourLength = computeLength();
	}
	
	public LinkedList<City> getPath() {
		return path;
	}
	
	private int computeLength() {
		int len = 0;
		City prev = path.getLast();
		ListIterator<City> itr = path.listIterator();
        while (itr.hasNext()) {	
        	City current = itr.next();
        	len += prev.dist(current);
        	prev = current;
        }
       return len;
	}
	
	public void addCity(final City c) {
		path.add(c);
	}
	
	public final int size() {
		return path.size();
	}
	
	public City getFirst() {
		return path.getFirst();
	}
	
	public City getLast() {
		return path.getLast();
	}
	
	public City getCityPos(final int i) {
		return path.get(i);
	}
	
	public String toString() {
		String buffer = "";
		for (final City c : path) {
			buffer += c.toString()+"->";
		}
		return buffer + path.getFirst();
	}
	
	public void printCoordinates() {
		for (final City c : path) {
			System.out.println(c.getX()+"\t"+c.getY());
		}
		System.out.println(path.getFirst().getX()+"\t"+path.getFirst().getY());
	}
	
	public int tourLength() {
		return tourLength;
	}
	
	public void tourLength(final int tourLength) {
		this.tourLength = tourLength;
	}
	
	public boolean contains(final City c) {
		return path.contains(c);
	}
	
	public boolean isEmpty() {
		return path.isEmpty();
	}

}
