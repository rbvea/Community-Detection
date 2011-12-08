package edu.hawaii.ics311.amatrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import edu.hawaii.ics311.amatrix.utils.GetMatrix;
import java.util.Comparator;

/**
 * Prints out the in-degrees and out-degrees of a graph. 
 * @author Russell Vea for ICS 311 Fall 2011 Assignment 2
 */
public class VerticeDegrees {

  /**
   * 
   * @param matrix
   * @throws IOException
   */
   public Integer[] getDegreeSequence(AdjacencyMatrix matrix) throws IOException{
    HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
    for (int i = 0; i < matrix.numVertices(); i++) {
      values.put(i, matrix.degree(i));
    }
    Object[] toSort = values.entrySet().toArray();
    Arrays.sort(toSort, new ElementComparator());
    Integer[] toReturn = new Integer[toSort.length];
    for(int i = 0; i < toSort.length; i++) {
      toReturn[i] = ((Map.Entry<Integer, Integer>) toSort[i]).getKey();
    }
    return toReturn;
  }

  /**
   * Calls VerticeDegrees on karate,political-blogs and c-elegans, ti-chats omitted because of runtime.
   * @param args should be empty.
   * @throws IOException when file doesn't exist or there's a error reading file.
   */
  public static void main(String[] args) throws IOException{
    VerticeDegrees test = new VerticeDegrees();
    GetMatrix get = new GetMatrix();
    AdjacencyMatrix matrix = get.parseFile("doc/karate.net");
    Integer[] results = test.getDegreeSequence(matrix);
    for (int i = 0; i < results.length; i++) {
      System.out.print(results[i] + " ");
    }
    
    test.printResults("doc/karate.net");
    test.printResults("doc/c-elegans.net");
    test.printResults("doc/political-blogs.net");
    
    //Set vmargument -Xmx1024m to be able to parse ti-chats
    /* <!--- Remove to run ti-chats
    fn = new BufferedReader(new FileReader("doc/ti-chats.net"));
    test.printResults(fn, "Ti-Chat"); Remove to run ti-chats --->*/ 
  }
  
  /**
   * Parses file and create matrix, 
   * @param fn
   * @param name
   * @throws IOException
   */
  public void printResults(String name) throws IOException{
    GetMatrix get = new GetMatrix();
    AdjacencyMatrix matrix = get.parseFile(name);
    
    int inBest = Integer.MIN_VALUE;
    int outBest = Integer.MIN_VALUE;
    int inWorst = Integer.MAX_VALUE;
    int outWorst = Integer.MAX_VALUE;
    double inTotal = 0.0;
    double outTotal = 0.0;
    for (int i = 0; i < matrix.numVertices(); i++) {
      int in = matrix.inDegree(i);
      if (in < inWorst) {
        inWorst = in;
      } 
      else if (in > inBest) {
        inBest = in;
      }
      inTotal += in;
      int out = matrix.outDegree(i);
      if (out < outWorst) {
        outWorst = out;
      } 
      else if (out > outBest) {
        outBest = out;
      }
      outTotal += out;
    }
    //System.out.println(matrix);

    System.out.format("Graph<%s>", name);
    System.out.format("|V|:  %d", matrix.numVertices());
    System.out.format("|E|: %d%n", matrix.numEdges());
    System.out.format("Degree Distribution:\t minimum\t average\t maximum%n");
    System.out.format("  inDegree(v):\t\t  %d\t%f\t%d%n", inWorst, inTotal / matrix.numVertices(), inBest);
    System.out.format("  outDegree(v):\t\t  %d\t%f\t%d%n", outWorst, outTotal / matrix.numVertices(), outBest);
  }
}
/**
 * 
 * 
 *@author Russell Vea
 */
class ElementComparator implements Comparator<Object> {

  public int compare(Object o1, Object o2) {
    Map.Entry<Integer, Integer> one = (Entry<Integer, Integer>) o1;
    Map.Entry<Integer, Integer> two = (Entry<Integer, Integer>) o2;
    
    if (one.getValue() > two.getValue()) {
      return -1;
    } else if (one.getValue() < two.getValue()) {
      return 1;
    }
    
    return 0;
  }





  
  
}

