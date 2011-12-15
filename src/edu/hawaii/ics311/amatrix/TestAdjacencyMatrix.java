package edu.hawaii.ics311.amatrix;

import static org.junit.Assert.assertTrue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.junit.Test;

/**
 * JUnit Test Class for Adjacency Matrix.
 * See doc/Testing.html for more details.
 * 
 *@author Russell Vea for ICS 311 Fall 2011
 */
public class TestAdjacencyMatrix {

  public TestAdjacencyMatrix(){
    
  }
  
  @Test
  public void test() throws FileNotFoundException, IOException{
    BufferedReader fn = new BufferedReader(new FileReader("doc/karate.net"));
    boolean debug = true;
    int numVertices = 0;
    String line = fn.readLine();
    String[] splitLine = line.split(" ");
    int totalVertices = Integer.parseInt(splitLine[splitLine.length - 1]);
    AdjacencyMatrix matrix = new AdjacencyMatrix(totalVertices);
    System.out.println(line);
    for (int i = 0; i < totalVertices; i++) {
      splitLine = fn.readLine().split(" ");
      int id = Integer.parseInt(splitLine[0]);
      if(debug) System.out.println(id);
      matrix.addVertex(new Vertex(id));
      numVertices++;
      assertTrue("Make sure vertex map is growing along linearly",matrix.numVertices() == numVertices);
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
      if(debug) System.out.format("Edge<%d,%d>%n", source + 1, target + 1);
      matrix.insertDirectedEdge(source, target, new Edge(source, target));
      Double weight = Double.parseDouble(splitLine[splitLine.length - 1]);
      Edge e = matrix.getEdge(source, target);
      assertTrue("Check to see if inserted edge has source as start", e.getStart() == source);
      assertTrue("Check to see if inserted edge has target as end",e.getEnd() == target);
      e.setAnnotation("weight", weight);
      if(debug) System.out.println("weight: " + e.getAnnotation("weight"));
      assertTrue("Check to see if edge annotations work", (Double)e.getAnnotation("weight") == weight);
      line = fn.readLine();
    }
    //test outDegree(v)
    assertTrue(matrix.outDegree(29) == 1);
    assertTrue(matrix.outDegree(0) == 2);
    assertTrue(matrix.outDegree(32) == 3);
    
    //test inDegree
    assertTrue(matrix.inDegree(2) == 3);
    assertTrue(matrix.inDegree(33) == 3);
    
    //test outAdjacentVertices
    Vertex v = matrix.getVertex(29);
    Iterator<Vertex> adjacentVertices = matrix.outAdjacentVertices(v);
    int adjacent = 0;
    while(adjacentVertices.hasNext()) {
      assertTrue(adjacentVertices.next().getId() == 23);
      adjacent++;
    }
    assertTrue(adjacent == 1);
    
    v = matrix.getVertex(32);
    adjacentVertices = matrix.outAdjacentVertices(v);
    adjacent = 0;
    int[] adj = {33, 23, 31}; //don't know why this is in this order
    while(adjacentVertices.hasNext()) {
      Vertex adjV = adjacentVertices.next();
      assertTrue(adj[adjacent] == adjV.getId());
      adjacent++;
    }
    assertTrue("Test to see if there are 3 vertices adjacent to 33", adjacent == 3);
    assertTrue("Test to see if areAdjacent works", matrix.areAdjacent(matrix.getVertex(32), matrix.getVertex(33)));
    
    
    //Test remove edge
    Edge e = matrix.getEdge(32, 33);
    Object obj = e.getData();
    Object toCompare = matrix.removeEdge(e);
    Iterator<Edge> edges = matrix.edges();
    while (edges.hasNext()) {
      assertTrue("Test to see if edge is no longer in graph",!edges.next().equals(e));
    }
    assertTrue("Test to see if matrix entry is 0", matrix.getMatrix()[e.getStart()][e.getEnd()] == 0);
    assertTrue("Test to see if matrix entry (opposite) is 0", matrix.getMatrix()[e.getEnd()][e.getStart()] == 0);
    assertTrue("Test to see if object returned in removeEdge is the object that was held by it", obj.equals(toCompare));
    e = matrix.getEdge(32, 23);
    
    Vertex three = matrix.getVertex(2);
    Vertex eight = matrix.getVertex(7);
    matrix.deleteVertex(three);
    assertTrue("Test to see if 3 is not adjacent to three, which was deleted", !matrix.areAdjacent(three, eight));
    assertTrue("Test to see if matrix does not contain three", !matrix.contains(three));
    assertTrue("Test to see if 8 is still adjacent to 9", matrix.areAdjacent(eight, matrix.getVertex(8)));
    
    /*
    //Test reverseDirection
    matrix.reverseDirection(e);
    assertTrue("Test to see if reverseDirection works", e.getStart() == 23);
    assertTrue("Test to see if reverseDirection works", e.getEnd() == 32);
    assertTrue("Test to see if reverseDirection works", matrix.getMatrix()[23][32] == 1);
    assertTrue("Test to see if reverseDirection works", matrix.getMatrix()[32][23] == 0);*/
    
    
    
    
  }
}
