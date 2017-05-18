package org.tpc.uni.algo.exercises.exercises13;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.System.gc;

public class RoutePlannerMain {
	
	private static PathElement longest(Map<Integer, PathElement> elements) {
		Optional<PathElement> element = elements
						.values()
						.stream()
						.collect(Collectors.maxBy(
							Comparator.comparingLong(PathElement::getDistance)));
		
		return element.orElseGet(() -> null);
	}
	
	private static PathElement longestTime(Map<Integer, PathElement> elements) {
		Optional<PathElement> element = elements
						.values()
						.stream()
						.collect(Collectors.maxBy(
							Comparator.comparingDouble(PathElement::getTime)));
		
		return element.orElseGet(() -> null);
	}
	
	/**
	 * Stellt die Hauptmethode fuer das Programm bereit.
	 * 
	 * @param args Die ignorierten Parameter
	 */
	public static void main(String[] args) {
		try (PrintStream resultOut = new PrintStream(new BufferedOutputStream(
						Files.newOutputStream(Paths.get("logs", "results.no-git.log"),
						StandardOpenOption.TRUNCATE_EXISTING,
						StandardOpenOption.CREATE)))) {
			
			final Graph graph = new Graph();
			final Timer timer = new Timer();
			try {
				timer.start();
				graph.readFromFile(Paths.get("data", "bawue_bayern.no-git.graph"));
				resultOut.format("Loaded Graph in %.2f seconds!\r\n",
								timer.stopAndGetS());
				resultOut.flush();
				gc();
				
				try (MapBbConverter bbConverter = new MapBbConverter(
											Paths.get("data", "nuremberg.no-git.bb"))) {
					
					timer.start();
					PathElement shortestRouteToNuremberg
									= graph.computeShortestPaths(5508637).get(4435496);
					timer.stop();
					gc();
					
					resultOut.format("Nuremberg: Distance: %d m, Time: %.2f h, "
									+ "Calculation-Time: %.2f s\r\n",
									shortestRouteToNuremberg.getDistance(),
									shortestRouteToNuremberg.getTime(), timer.getTimeS());
					bbConverter.addPath(shortestRouteToNuremberg, null, "blue", "length");
					resultOut.flush();
					
					timer.start();
					shortestRouteToNuremberg
									= graph.computeFastestPaths(5508637, 130).get(4435496);
					timer.stop();
					gc();
					
					resultOut.format("Nuremberg (max 130 m/h): Distance: %d m, "
									+ "Time: %.2f h, Calculation-Time: %.2f s\r\n",
									shortestRouteToNuremberg.getDistance(),
									shortestRouteToNuremberg.getTime(), timer.getTimeS());
					bbConverter.addPath(shortestRouteToNuremberg, null, "red", "car");
					resultOut.flush();
					
					timer.start();
					shortestRouteToNuremberg
									= graph.computeFastestPaths(5508637, 100).get(4435496);
					timer.stop();
					gc();
					
					resultOut.format("Nuremberg (max 100 m/h): Distance: %d m, "
									+ "Time: %.2f h, Calculation-Time: %.2f s\r\n",
									shortestRouteToNuremberg.getDistance(),
									shortestRouteToNuremberg.getTime(), timer.getTimeS());
					bbConverter.addPath(shortestRouteToNuremberg, null, "green", "moped");
					resultOut.flush();
				}
				
				try (MapBbConverter bbConverter = new MapBbConverter(
						Paths.get("data", "longest-dist.no-git.bb"))) {	
					
					timer.start();
					PathElement longestRoute
									= longest(graph.computeShortestPaths(5508637));
					timer.stop();
					gc();
					
					resultOut.format("Longest: Distance: %d m, Time: %.2f h, "
									+ "Calculation-Time: %.2f s, Node %d\r\n",
									longestRoute.getDistance(), longestRoute.getTime(),
									timer.getTimeS(), longestRoute.getId());
					bbConverter.addPath(longestRoute, null, "blue", "length");
					resultOut.flush();
					
					timer.start();
					longestRoute
									= longestTime(graph.computeFastestPaths(5508637, 130));
					timer.stop();
					gc();
					
					resultOut.format("Longest (max 130 m/h): Distance: %d m, "
									+ "Time: %.2f h, Calculation-Time: %.2f s, Node %d\r\n",
									longestRoute.getDistance(),
									longestRoute.getTime(), timer.getTimeS(),
									longestRoute.getId());
					bbConverter.addPath(longestRoute, null, "red", "car");
					resultOut.flush();
					
					timer.start();
					longestRoute
								= longestTime(graph.computeFastestPaths(5508637, 100));
					timer.stop();
					gc();
					
					resultOut.format("Longest (max 100 m/h): Distance: %d m, "
									+ "Time: %.2f h, Calculation-Time: %.2f s, Node %d\r\n",
									longestRoute.getDistance(),
									longestRoute.getTime(), timer.getTimeS(),
									longestRoute.getId());
					bbConverter.addPath(longestRoute, null, "green", "moped");
					resultOut.flush();
				}
			} catch (Exception exception) {
				exception.printStackTrace(resultOut);
				resultOut.flush();
				
				exception.printStackTrace();
				
				if (exception instanceof IOException) {
					throw exception;
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}