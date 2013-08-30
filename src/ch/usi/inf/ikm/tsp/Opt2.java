package ch.usi.inf.ikm.tsp;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * The 2-opt algorithm used to optimize the best ant found each 10 ants.
 * 
 * @author Stefano Pongelli
 */
public class Opt2 {
	
	public final static void optimize(final Solution solution, final boolean firstImprovement) {
		final int fitness = solution.tourLength();
		int best_gain = -1;
		int final_gain = 0;
		final boolean first_improvement = firstImprovement;
		final LinkedList<City> path = solution.getPath();
		
		while (best_gain != 0) {
			best_gain = 0;
			City bestc = path.getFirst();
			City bestb = null;
			City last = path.getLast();
			ListIterator<City> itr = path.listIterator(path.indexOf(bestc));
			itr.add(path.getLast());
	        while (itr.hasNext()) {	
	            final City a = itr.previous();
	            itr.next();
	            final City b = itr.next();
	            
	            if (last == b || a == b) { break;}
	            
	            ListIterator<City> itr2 = path.listIterator(path.indexOf(b)+2);
	            while (itr2.hasNext()) {	
	            	final City c = itr2.previous();
		            itr2.next();
		            final City d = itr2.next();
		            int gain = (d.dist(b) + c.dist(a)) - (a.dist(b) + c.dist(d));
		            if (gain < best_gain) {
		            	best_gain = gain;
		            	bestb = b;
		            	bestc = c;
		            	if (first_improvement) break;
		            }
	            }
	            if (best_gain < 0 && first_improvement) break;
	        }
	        path.remove(path.getFirst());
	        final_gain += best_gain;
	        if (bestb != null && bestc != null) {
		        int posb = path.indexOf(bestb);
		        int posc = path.indexOf(bestc);
		        List<City> l = path.subList(Math.min(posc, posb), Math.max(posc, posb)+1);
		        Collections.reverse(l);
	       }
		}
		solution.tourLength(fitness+final_gain);
	}
	
}