package org.tpc.uni.algo.exercises.exercises12;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Stellt die Main-Methode zum testen der Algorithmen bereit.
 * 
 * @author Tobias Faller
 *
 */
public class LargestConnectedComponentMain {
	
	/**
	 * Die Main-Methode.
	 * 
	 * @param args Die ignorierten Argumente
	 */
	public static void main(String[] args) {
		PrintStream originalErrorStream = System.out;
		
		try (OutputStream fOut
				= Files.newOutputStream(Paths.get("Graph.private.log"),
					StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
				OutputStream out = new BufferedOutputStream(fOut);
				PrintStream pOut = new PrintStream(out)) {
			System.setOut(pOut);
			System.setErr(pOut);
			
			Graph graph = new Graph();
			try {
				final long startTime = System.currentTimeMillis();
				graph.readFromFile(Paths.get("saarland.private.graph"));
				System.out.println(graph.toString());
				System.out.flush();
				
				long findStartTime = System.currentTimeMillis();
				int size = graph.computeLargestConnectedComponent();
				
				System.out.println();
				System.out.format("Datei in %d Millisekunden gelesen!\r\n\r\n",
								(System.currentTimeMillis() - startTime));
				System.out.format("Groesse des Graphen: %d\r\n", graph.getNumNodes());
				System.out.format("Groesste der groessten Zusammenhangskomponente:"
								+ " %d\r\n", size);
				System.out.format("Benoetigte Zeit zum finden: %d Millisekunden",
								(System.currentTimeMillis() - findStartTime));
				System.out.flush();
			} catch (IOException ioException) {
				ioException.printStackTrace(System.err);
			}
		} catch (IOException ioException) {
			ioException.printStackTrace(originalErrorStream);
		}
	}
}