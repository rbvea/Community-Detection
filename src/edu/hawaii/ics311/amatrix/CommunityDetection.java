package edu.hawaii.ics311.amatrix;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import edu.hawaii.ics311.amatrix.utils.GetMatrix;

/**
 * Implementation of the algorithm specified by Palla et al.
 * Algorithm:
 * 
 *  Construct Clique-clique matrix:
 *    - Use the BronKerbosch algorithm with degeneracy order to determine cliques.
 *    - Insert vertices where each vertex represents a clique.   
 *    - M_ij = number of common nodes between clique i and j
 *  Find k-clique communities:
 *    - Disregard all elements of k - 1
 *    - Relabel remaining cliques with 1
 * 
 * @author Russell Vea for Assignment 3 ICS 311 Fall 2011
 */
public class CommunityDetection {

  VerticeDegrees degrees;
  BronKerbosch bronbron;
  public AdjacencyMatrix matrix;
  AdjacencyMatrix ccmatrix;
  Integer[] degreeVertices;
  List<Clique> cliques;
  Set<Set<Vertex>> communities;

  public CommunityDetection (AdjacencyMatrix matrix) {
    this.matrix = matrix;
    this.cliques = new ArrayList<Clique>();
    this.communities = new HashSet<Set<Vertex>>();
  }

  public Set<Set<Vertex>> find(int k) throws IOException, CloneNotSupportedException {
    this.degrees = new VerticeDegrees();
    this.degreeVertices = degrees.getDegreeSequence(matrix);
    Set<Vertex> thisSet = new HashSet<Vertex>(this.matrix.vertices.values());
    this.bronbron = new BronKerbosch(this.matrix.clone(), this.degreeVertices);
    this.bronbron.find(thisSet);
    this.ccmatrix = bronbron.getCliqueCliqueMatrix();

    for(int i = 0; i < this.ccmatrix.numVertices(); i++) {
      for(int j = 0; j < this.ccmatrix.numVertices(); j++){
        if(i != j) {  
          Set<Vertex> one = new HashSet(((Clique)this.ccmatrix.getVertex(i).getData()).getMembers());
          Set<Vertex> two = new HashSet(((Clique)this.ccmatrix.getVertex(j).getData()).getMembers());
          one.retainAll(two);
          if(one.size() == k) {
            this.communities.add(one);
          }
        } else if (i == j) {
          Set<Vertex> vertex =((Clique)this.ccmatrix.getVertex(i).getData()).getMembers();
          if(vertex.size() == k)
            this.communities.add(vertex);
        }
      }
    }
    return this.communities;
  }

  private int commonVertices(Object object, Object object2) {
    Set<Vertex> c1 =  new HashSet<Vertex>( (HashSet<Vertex>) object);
    Set<Vertex> c2 =  new HashSet<Vertex>( (HashSet<Vertex>) object2);
    c1.retainAll(c2);
    return c1.size(); 
  }

  public static void main(String[] args) throws IOException, CloneNotSupportedException{
    GetMatrix get = new GetMatrix();
    VerticeDegrees vDegrees = new VerticeDegrees();
    AdjacencyMatrix matrix = get.parseFile("doc/artificial-20-mixing.net");
    //vDegrees.printResults("doc/artificial-20-mixing.net");
    Integer[] order = vDegrees.getDegreeSequence(matrix);
    int highOrder = order[order.length - 1];
    CommunityDetection com = new CommunityDetection(matrix);
    Set<Set<Vertex>> communities = new HashSet<Set<Vertex>>();
    
    for(int i = 0; i < highOrder; i++)
      communities.addAll(com.find(i));
    
    Object[] toSort = communities.toArray();
    Arrays.sort(toSort, new CommunityComparator());
    
    System.out.println("Amount communities found: " + communities.size());
    
    int id = 1;
    System.out.println("id   size");
    for(int i = 0; i < toSort.length; i++) {
      Set<Vertex> thisSet = (Set<Vertex>) toSort[i];
      Vertex highestDegree = null;
      int hiDeg = 0;
      for(Iterator<Vertex> ti = thisSet.iterator(); ti.hasNext();) {
        Vertex v = ti.next();
        if(highestDegree == null) {
          highestDegree = v;
          hiDeg = matrix.degree(v.getId());
        } else if(matrix.degree(v.getId()) > matrix.degree(highestDegree.getId())) {
          highestDegree = v;
          hiDeg = matrix.degree(v.getId());
        }
      }
      System.out.format("%d:   %d  deg(%d) = %d%n", id++, thisSet.size(), highestDegree.getId(), hiDeg);
      if(id > 20) 
        break;
    }
  }
}

class CommunityComparator implements Comparator<Object> {

  public int compare(Object one, Object two) {
    Set<Vertex> o1 = (Set<Vertex>)one;
    Set<Vertex> o2 = (Set<Vertex>)two;
    if(o1.size() > o2.size()) {
      return -1;
    } else if (o1.size() < o2.size()) {
      return 1;
    }
    return 0;
  }

}
