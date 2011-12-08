package edu.hawaii.ics311.amatrix;

import java.io.IOException;
import java.util.Iterator;
import edu.hawaii.ics311.amatrix.utils.GetMatrix;

/**
 * Depth First Search implentation using an Adjacency Matrix
 * 
 * @author Russell Vea for ICS 311 Fall 2011.
 */
public class DepthFirstSearch {

  private AdjacencyMatrix matrix;
  private int time;

  /**
   * Constructor with specified matrix 
   * @param matrix
   */
  public DepthFirstSearch(AdjacencyMatrix matrix) {
    this.matrix = matrix;
  }

  /**
   * Depth First Search with specified matrix.
   * @param matrix
   */
  public void search(AdjacencyMatrix matrix){
    //set time and new stack
    this.time = 0;
    Iterator<Vertex> toSearch = matrix.vertices();

    while(toSearch.hasNext()) {
      Vertex v = toSearch.next(); 
      v.setAnnotation("parent", null);
      v.setAnnotation("color", Color.WHITE);
    }

    //go through stack and process vertices
    toSearch = matrix.vertices();
    while (toSearch.hasNext() ){
      Vertex node = toSearch.next();
      if(node.getAnnotation("color") == Color.WHITE) {
        searchVertex(node);
      }
    }
  }
  
  /**
   * Do a DFS search but in the order specified.
   * @param matrix the matrix
   * @param order the order of vertices to search through
   */
  public void search(AdjacencyMatrix matrix, Integer[] order) {
    this.time = 0;
    Iterator<Vertex> toSearch = matrix.vertices();
    //put all vertices (adjacent to v) in stack
    while(toSearch.hasNext()) {
      Vertex v = toSearch.next(); 
      v.setAnnotation("color", Color.WHITE);
      v.setAnnotation("parent", null);
    }
    for (int i = 0; i < order.length; i++) {
      Vertex v = matrix.getVertex(order[i]);
      if(v.getAnnotation("color") == Color.WHITE) {
        searchVertex(v);
      }
    }
  }
  
  /**
   * Recursive method to search node's adjacent vertices.
   * Assigns discover/finish time as well as parents of adjacent vertices.
   * @param node the vertex to search.
   */
  public void searchVertex(Vertex node) {
    Iterator<Vertex> adjacentVertices = null;
    node.setAnnotation("discover", ++time);
    node.setAnnotation("color", Color.GREY);
    adjacentVertices = this.matrix.outAdjacentVertices(node);
    while (adjacentVertices.hasNext()) {
      Vertex vNext = (Vertex) adjacentVertices.next();
      if(vNext.getAnnotation("color") == Color.WHITE) {
          vNext.setAnnotation("parent", node);
          searchVertex(vNext);
      }
    }
    node.setAnnotation("color", Color.BLACK);
    node.setAnnotation("finish", ++time);
  }

  /**
   * For testing porpoises.
   * @param args should be empty
   * @throws IOException when file doesn't exist or there's an error reading file.
   */
  public static void main(String[] args) throws IOException {
    GetMatrix getMatrix = new GetMatrix();
    AdjacencyMatrix matrix = getMatrix.parseFile("doc/karate.net");
    DepthFirstSearch DFS = new DepthFirstSearch(matrix);
    DFS.search(matrix);
    Iterator<Vertex> results = matrix.vertices();
    while(results.hasNext()) {
      Vertex v = (Vertex) results.next();
      Vertex vSub = (Vertex) v.getAnnotation("parent");
      System.out.format("ID: %s, Parent: %s, Start: %d, Finish: %d%n", v, vSub , v.getAnnotation("discover"), v.getAnnotation("finish"));
    }
  }
}
