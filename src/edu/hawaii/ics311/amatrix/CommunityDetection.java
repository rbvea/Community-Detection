package edu.hawaii.ics311.amatrix;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import edu.hawaii.ics311.amatrix.utils.GetMatrix;

/**
 * Implementation of the algorithm specified by Palla et al.
 * Algorithm:
 *  Find largest possible clique from degree sequence
 *  Extract cliques by:
 *    - Choosing one node belonging to clique of largest size
 *    - Extract node and all neighbors, putting them into a clique
 *    - Once all cliques of size k are depleted, decrease k and repeat until no nodes.
 *  Construct Clique-clique matrix:
 *    - Each row and column represents clique
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
  AdjacencyMatrix matrix;
  AdjacencyMatrix ccmatrix;
  Integer[] degreeVertices;
  List<Clique> cliques;

  public CommunityDetection (AdjacencyMatrix matrix) {
    this.matrix = matrix;
    this.cliques = new ArrayList<Clique>();
  }

  public void find() throws IOException{
    this.degrees = new VerticeDegrees();
    this.degreeVertices = degrees.getDegreeSequence(matrix);
    for(int i = 0; i < degreeVertices.length; i++) {
      Vertex rep = this.matrix.getVertex(degreeVertices[i]);
      if (rep != null) {
        Set<Vertex> thisSet = new HashSet<Vertex>();
        Iterator<Vertex> adjacent = this.matrix.adjacentVertices(rep);
        matrix.deleteVertex(rep);
        while (adjacent.hasNext()) {
          Vertex v = adjacent.next();
          if(matrix.contains(v)) {
             thisSet.add(v);
          }
        }
      }
    }
   for (int i = 0; i < this.cliques.size(); i++) {
     
   }
  }
  
  public static void main(String[] args) throws IOException{
    GetMatrix get = new GetMatrix();
    AdjacencyMatrix test = get.parseFile("doc/karate.net");
    System.out.println(test);
    CommunityDetection com = new CommunityDetection(test);
    com.find();
  }
}
