package edu.hawaii.ics311.amatrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import edu.hawaii.ics311.amatrix.utils.GetMatrix;

/**
 * Implementation of the Kosaraju's algorithm for computing Strongly Connected Components of a graph.
* @author Russell Vea for ICS 311 Fall 2011.
 */
public class StronglyConnectedComponents {

  private AdjacencyMatrix matrix;
  private AdjacencyMatrix tMatrix;
  private DepthFirstSearch DFS;
  private Integer[] order;
  private Map<Integer, Vertex> toSort;
  private Map<Vertex, Integer> sccMap;
  private String name;


  public StronglyConnectedComponents (AdjacencyMatrix matrix, AdjacencyMatrix tMatrix, String name) {
    this.matrix = matrix;
    this.tMatrix = tMatrix;
    this.DFS = new DepthFirstSearch(matrix);
    this.toSort = new HashMap<Integer, Vertex>();
    this.order = null;
    this.name = name;
  }

  /**
   * 1. Performs a DFS on the matrix.
   * 2. Sorts matrix in order of finishing time (getAnnotation("finish")).
   * 3. Calls DFS on tMatrix but in order calculated in step 2.
   * 4. Calculates the size/root of each SCC.
   * 5. Sorts the HashMap by size with MapComparator and outputs results.
   */
  public void findSCC() {
    //compute finishing times
    DFS.search(this.matrix);

    //sort finishing times in matrix
    this.order = new Integer[this.matrix.numVertices()];
    Iterator<Vertex> vertices = this.matrix.vertices();
    while (vertices.hasNext()) {
      Vertex v = vertices.next();
      this.toSort.put((Integer) v.getAnnotation("finish"), v);
    }
    Object[] unsorted = this.toSort.keySet().toArray();
    Arrays.sort(unsorted, Collections.reverseOrder());
    for (int i = 0; i < unsorted.length; i++) {
      Vertex sorted = this.toSort.get(unsorted[i]);
      this.order[i] = sorted.getId();
    }

    //reset DFS and search with new order
    this.DFS = new DepthFirstSearch(tMatrix);
    DFS.search(this.tMatrix, order);
    //keep track of all roots then sort it
    this.sccMap = new HashMap<Vertex, Integer>();
    Iterator<Vertex> forest = tMatrix.vertices();
    while (forest.hasNext()) {
      Vertex v = forest.next();
      int highDegree = v.getId();
      int degree = matrix.degree(highDegree);
      while(v.getAnnotation("parent") != null) {
        v = (Vertex)v.getAnnotation("parent");
      }
      if (v.getAnnotation("highestDegree") == null) {
        v.setAnnotation("highestDegree", degree);
        v.setAnnotation("highDegree", highDegree);
      } else {
        if ((Integer) v.getAnnotation("highestDegree") < degree) {
          v.setAnnotation("highestDegree", degree);
          v.setAnnotation("highDegree", highDegree);
        }
      }
      if(!this.sccMap.containsKey(v)) {
        this.sccMap.put(v, 1);
      } else {
        int inc = this.sccMap.get(v);
        this.sccMap.remove(v);
        this.sccMap.put(v, ++inc);
      }
    }
    Object[] entries = this.sccMap.entrySet().toArray();
    Arrays.sort(entries, new MapComparator());

    System.out.println("# of SCC in " + name + ": " + entries.length);
    for(int i = 0; i < 20 && i < entries.length; i++) {
      Entry<Vertex, Integer> ent = (Entry<Vertex, Integer>) entries[i];
      Vertex v = ent.getKey();
      System.out.format("size: %s \t Root: %s \t degree(%s): %s%n", ent.getValue(), v, v.getAnnotation("highDegree"), v.getAnnotation("highestDegree"));
    }
  }

  /**
   * Uses GetMatrix class to read in matrix and transposed matrix of files, then call SCC.findSCC() on them.
   * @param args should be empty.
   * @throws IOException if File does not exist or an error occurs when reading. 
   */
  public static void main(String[] args) throws IOException{
    GetMatrix getMatrix = new GetMatrix();
    
    AdjacencyMatrix matrix = getMatrix.parseFile("doc/karate.net");
    AdjacencyMatrix tMatrix = getMatrix.getTransposedMatrix("doc/karate.net");
    StronglyConnectedComponents SCC = new StronglyConnectedComponents(matrix,tMatrix, "karate.net");
    SCC.findSCC();

    matrix = getMatrix.parseFile("doc/c-elegans.net");
    tMatrix = getMatrix.getTransposedMatrix("doc/c-elegans.net");
    SCC = new StronglyConnectedComponents(matrix,tMatrix, "c-elegans.net");
    SCC.findSCC();

    matrix = getMatrix.parseFile("doc/political-blogs.net");
    tMatrix = getMatrix.getTransposedMatrix("doc/political-blogs.net");
    SCC = new StronglyConnectedComponents(matrix,tMatrix, "political-blogs.net");
    SCC.findSCC();
    /*
    AdjacencyMatrix matrix = getMatrix.parseFile("doc/ti-chats.net");
    AdjacencyMatrix tMatrix = getMatrix.getTransposedMatrix("doc/ti-chats.net");
    StronglyConnectedComponents SCC = new StronglyConnectedComponents(matrix,tMatrix, "ti-chats.net");
    SCC.findSCC();*/
  }

  class MapComparator implements Comparator {

    public int compare(Object arg0, Object arg1) {
      Entry<Vertex, Integer> o1 = (Entry<Vertex, Integer>) arg0;
      Entry<Vertex, Integer> o2 = (Entry<Vertex, Integer>) arg1;
      if (o1.getValue() > o1.getValue()) {
        return -1;
      } 
      else if(o1.getValue() < o2.getValue()) {
        return 1;
      }
      return 0;
    }

  }
}
