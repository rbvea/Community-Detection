package edu.hawaii.ics311.amatrix.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import edu.hawaii.ics311.amatrix.AdjacencyMatrix;
import edu.hawaii.ics311.amatrix.Edge;
import edu.hawaii.ics311.amatrix.Vertex;

/**
 * A utility class to parse and generate an AdjacencyMatrix.
 * 
 * @author Russell Vea for ICS 311 Fall 2011.
 */
public class GetMatrix {

  /**
   * Parses .net file and constructs an Adjacency Matrix.
   * @param filename the path to the .net file relative to the root folder
   *        Several files are included in the doc/ folder 
   *        i.e.: /doc/karate.net
   * @return matrix an Adjacency Matrix specified by the file read.
   * @throws IOException when file doesn't exist or there is an error when reading it.
   */
  public AdjacencyMatrix parseFile(String filename) throws IOException{
    boolean debug = false;
    BufferedReader fn = new BufferedReader(new FileReader(filename));
    String line = fn.readLine();
    String[] splitLine = line.split(" ");
    int totalVertices = Integer.parseInt(splitLine[splitLine.length - 1]);
    AdjacencyMatrix matrix = new AdjacencyMatrix(totalVertices);
    if (debug) System.out.println(line);
    for (int i = 0; i < totalVertices; i++) {
      splitLine = fn.readLine().split(" ");
      int id = Integer.parseInt(splitLine[0]);
      matrix.addVertex(new Vertex(id - 1));
    }
    fn.readLine();
    line = fn.readLine();
    while (line != null) {
      try {
        splitLine = line.split(" ");
      } catch (NullPointerException e) {
        break;
      }
      int source = Integer.parseInt(splitLine[0]) - 1;
      int target = Integer.parseInt(splitLine[1]) - 1;
      matrix.insertDirectedEdge(source, target, new Edge(source, target));
      /* no need for weights just yet
      Double weight = Double.parseDouble(splitLine[splitLine.length - 1]);
      Edge e = matrix.getEdge(source, target);
      e.setAnnotation("weight", weight);*/
      line = fn.readLine();
    }
    return matrix;
  }
  
  /**
   * Constructs a Transposed Adjacency Matrix with the original matrix's edges reversed.
   * @param filename the path to the .net file relative to the root folder
   *        Several files are included in the doc/ folder 
   *        i.e.: /doc/karate.net
   * @return matrix an Adjacency Matrix specified by the file read.
   * @throws IOException when file doesn't exist or there is an error when reading it.   * 
   */
  public AdjacencyMatrix getTransposedMatrix(String filename) throws IOException{
    BufferedReader fn = new BufferedReader(new FileReader(filename));
    String line = fn.readLine();
    String[] splitLine = line.split(" ");
    int totalVertices = Integer.parseInt(splitLine[splitLine.length - 1]);
    AdjacencyMatrix matrix = new AdjacencyMatrix(totalVertices);
    for (int i = 0; i < totalVertices; i++) {
      splitLine = fn.readLine().split(" ");
      int id = Integer.parseInt(splitLine[0]);
      matrix.addVertex(new Vertex(id - 1));
    }
    fn.readLine();
    line = fn.readLine();
    while (line != null) {
      try {
        splitLine = line.split(" ");
      } catch (NullPointerException e) {
        break;
      }
      int source = Integer.parseInt(splitLine[0]) - 1;
      int target = Integer.parseInt(splitLine[1]) - 1;
      matrix.insertDirectedEdge(target, source, new Edge(target, source));
      /*
      Double weight = Double.parseDouble(splitLine[splitLine.length - 1]);
      Edge e = matrix.getEdge(source, target);
      e.setAnnotation("weight", weight);*/
      line = fn.readLine();
    }
    return matrix;
  }
  
}
