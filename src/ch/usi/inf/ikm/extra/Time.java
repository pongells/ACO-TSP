package ch.usi.inf.ikm.extra;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
/**	
 * IKM-AI CUP - TSP PROBLEM
 * 
 * Compute the current CPU Time.
 * 
 * @author Stefano Pongelli
 */
public class Time {
	 
	/** Get CPU time in nanoseconds. */
	public static long getCpuTime( ) {
	    ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
	    return bean.isCurrentThreadCpuTimeSupported( ) ?
	        bean.getCurrentThreadCpuTime( ) : 0L;
	}

}
