package org.tpc.uni.algo.exercises.exercises13;

public class Timer {
	
	private long startTime;
	private long time;
	private boolean finished;
	private boolean started;
	
	public Timer() {
		finished = true;
		started = false;
	}
	
	/**
	 * Gibt die gestoppte Zeit in Millisekunden zurrueck.
	 * Ansonsten ist das Ergebnis 0.
	 * 
	 * @return Die vergangene Zeit
	 */
	public long getTimeMs() {
		if (!started && !finished) {
			return 0;
		}
		
		if (finished) {
			return time;
		}
		
		return (System.currentTimeMillis() - startTime);
	}
	
	public double getTimeS() {
		return getTimeMs() / 1000.0D;
	}
	
	/**
	 * Started den Timer.
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		started = true;
		finished = false;
	}
	
	/**
	 * Stoppt den Timer.
	 */
	public void stop() {
		time = getTimeMs();
		finished = true;
	}
	
	/**
	 * Stoppt den Timer und gibt die vergangene Zeit in Millisekunden zurrueck.
	 * 
	 * @return Die vergangene Zeit
	 */
	public long stopAndGetMs() {
		stop();
		return getTimeMs();
	}
	
	/**
	 * Stoppt den Timer und gibt die vergangene Zeit in Sekunden zurrueck.
	 * 
	 * @return Die vergangene Zeit
	 */
	public double stopAndGetS() {
		stop();
		return getTimeS();
	}
}