package edu.hawaii.ics311.amatrix;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the BronKerbosch Algorithm with vertex ordering.
 * 
 * @author Russell Vea
 */
public class BronKerbosch {

  private AdjacencyMatrix matrix;
  private AdjacencyMatrix ccMatrix;

  public BronKerbosch(AdjacencyMatrix matrix) {
    this.matrix = matrix;
    this.ccMatrix = new AdjacencyMatrix(matrix.numVertices());
  }

  public void find(Set<Vertex> thisSet) {
    Set<Vertex> pSet = new HashSet<Vertex>();
    Set<Vertex> xSet = new HashSet<Vertex>();
    Bron(pSet, thisSet , xSet);
  }

  private void Bron(Set<Vertex> R, Set<Vertex> P, Set<Vertex> X) {
    Set<Vertex> nV;
    if(P.isEmpty() && X.isEmpty()) {
      Vertex toAdd = new Vertex(this.ccMatrix.numVertices() + 1);
      Clique thisClique = new Clique();
      toAdd.setAnnotation("Clique", thisClique);
      for(Vertex v : R) {
        thisClique.addVertex(v);
      }
      this.ccMatrix.addVertex(toAdd);
    }
    for(Vertex v : P) {
      nV = new HashSet<Vertex>();
      Iterator<Vertex> adj = matrix.adjacentVertices(v);
      while(adj.hasNext()) {
        nV.add(adj.next());
      }
      R.add(v);
      P.retainAll(nV);
      X.retainAll(nV);
      Bron(R, P, X);
      P.remove(v);
      X.add(v);
    }
  }
  
  public AdjacencyMatrix getCliqueCliqueMatrix() {
    return this.ccMatrix;
  }
}
