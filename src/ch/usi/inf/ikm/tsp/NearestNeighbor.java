package ch.usi.inf.ikm.tsp;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * The Nearest Neighbor algorithm used to create an initial solution. 
 * 
 * @author Stefano Pongelli
 */
public class NearestNeighbor {

	public final static Solution getSolution(final Problem problem, final int starting) {
		final Solution solution = new Solution();
		final int size = problem.size();
		int tourLength = 0;
		solution.addCity(problem.getCity(starting));
		for (int i = 0; i<size-1; i++) {
			final City nearest = solution.getLast().getNearest(problem, solution);
			tourLength += solution.getLast().dist(nearest);
			solution.addCity(nearest);
		}
		tourLength += solution.getLast().dist(solution.getFirst());
		solution.tourLength(tourLength);
		return solution;
	}

}
