package org.tpc.uni.algo.exercises.exercises13;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Eine Klasse um OpenMapBB-Dateien zu erzeugen.
 * 
 * @author Tobias Faller
 *
 */
public class MapBbConverter implements Closeable {
	
	private BufferedWriter writer;
	
	public MapBbConverter(Path path) throws IOException {
		writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		writer.write("[map]\r\n");
	}
	
	/**
	 * Fuegt einen Pfad zu dieser Datei hinzu.
	 * 
	 * @param start Der Startknoten
	 * @param end Der Endknoten oder <code>null</code> um alle Knoten zu iterieren
	 * @param color Die Farbe des Pfades
	 * @param name Der Name des Pfades
	 * @throws IOException Wird geworfen, wenn ein Datei-Fehler auftritt
	 */
	public void addPath(PathElement start, PathElement end, String color,
					String name) throws IOException {
		PathElement current = start;
		while (current != null) {
			writer.write(current.getCoordinates());
			
			if (current == end) {
				break;
			}
			
			if (current.previous != null) {
				writer.append(' ');
			}
			current = current.previous;
		}
		
		writer.append('(')
			.append(color)
			.append('|')
			.append(name)
			.append(");\r\n");
		writer.flush();
	}
	
	@Override
	public void close() throws IOException {
		writer.write("[/map]\r\n");
		writer.flush();
		writer.close();
	}
}