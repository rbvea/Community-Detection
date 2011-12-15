package edu.hawaii.ics311.amatrix;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Implementation of the BronKerbosch Algorithm with vertex ordering.
 * 
 * @author Russell Vea
 */
public class BronKerbosch {

  private AdjacencyMatrix matrix;
  private AdjacencyMatrix ccMatrix;
  private Integer[] order;

  public BronKerbosch(AdjacencyMatrix matrix, Integer[] vertexOrder) {
    this.order = vertexOrder;
    this.matrix = matrix;
    //System.out.println(this.matrix);
    this.ccMatrix = new AdjacencyMatrix(0);
  }

  public void find(Set<Vertex> originalSet) {
    Set<Vertex> rSet = new HashSet<Vertex>();
    Set<Vertex> xSet = new HashSet<Vertex>();
    Bron(rSet, originalSet , xSet);
  }

  private void Bron(Set<Vertex> R, Set<Vertex> P, Set<Vertex> X) {
    Set<Vertex> nV;
    for(int i = 0; i < this.order.length; i++) {
      Vertex v = this.matrix.getVertex(this.order[i]);
      nV= neighbors(v);
      P.retainAll(nV);
      X.retainAll(nV);
      R.add(v);
      Bron2(new HashSet<Vertex>(R), new HashSet<Vertex>(P), new HashSet<Vertex>(X));
      P.remove(v);
      X.add(v);
      this.matrix.deleteVertex(v);
    }
  }

  /**
   * Bron algorithm with pivoting.
   * @param P
   * @param R
   * @param X
   */
  private void Bron2(Set<Vertex> R, Set<Vertex> P, Set<Vertex> X) {
    Random r = new Random();
    if (P.isEmpty() && X.isEmpty()) {
        Vertex toAdd = new Vertex(this.ccMatrix.numVertices());
        Clique thisClique = new Clique();
        for(Vertex vert : R) {
          thisClique.addVertex(vert);
        }
        toAdd.setData(thisClique);
        //System.out.print("Current Vertex " + this.currentVertex + " ");
       // System.out.println("adding clique: " + thisClique);
        this.ccMatrix.addVertex(toAdd);
    } else {
      Set<Vertex> toChooseFrom = new HashSet<Vertex>(P);
      toChooseFrom.addAll(X);
      //choose random vertex form P U X
      Vertex pivot = (Vertex) toChooseFrom.toArray()[r.nextInt(toChooseFrom.size())];
      //System.out.println(pivot);
      Set<Vertex> pivotVertices = neighbors(pivot);
      P.removeAll(pivotVertices);
      HashSet<Vertex> miniP = new HashSet<Vertex>(P);
      for (Iterator<Vertex> it = miniP.iterator(); it.hasNext();) {
        Vertex v = it.next();
        Set<Vertex> vNeigh = neighbors(v);
        R.add(v);
        P.retainAll(vNeigh);
        X.retainAll(vNeigh);
        Bron2(new HashSet<Vertex>(R), new HashSet<Vertex>(P), new HashSet<Vertex>(X));
        P.remove(v);
        X.add(v);
      }
    }
  }

  private Set<Vertex> neighbors(Vertex v) {
    Set<Vertex> toReturn = new HashSet<Vertex>();
    for(Iterator<Vertex> it = this.matrix.adjacentVertices(v); it.hasNext();) {
      toReturn.add(it.next());
    }
    return toReturn;
  }

  public AdjacencyMatrix getCliqueCliqueMatrix() {
    return this.ccMatrix;
  }
}
