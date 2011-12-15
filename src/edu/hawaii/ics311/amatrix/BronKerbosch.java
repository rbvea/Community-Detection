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
  private int k;
  
  public BronKerbosch(AdjacencyMatrix matrix, Integer[] vertexOrder, int k) {
    this.k = k;
    this.order = vertexOrder;
    this.matrix = matrix;
    this.ccMatrix = new AdjacencyMatrix(0);
  }

  public void find(Set<Vertex> originalSet) {
    Set<Vertex> rSet = new HashSet<Vertex>();
    Set<Vertex> xSet = new HashSet<Vertex>();
    Bron(rSet, originalSet , xSet);
  }

  private void Bron(Set<Vertex> R, Set<Vertex> P, Set<Vertex> X) {
    //System.out.format("Bron3: %s, %s, %s%n", R, P, X );
    Set<Vertex> nV;
   
    for(int i = 0; i < this.order.length; i++) {
      Set<Vertex>Rclone = new HashSet(R);
      Set<Vertex>Pclone = new HashSet(P);
      Set<Vertex>Xclone = new HashSet(X);
      Vertex v = this.matrix.getVertex(this.order[i]);
      nV= neighbors(v);
      Pclone.retainAll(nV);
      Xclone.retainAll(nV);
      Rclone.add(v);
      Bron2(Rclone, Pclone, Xclone);
      P.remove(v);
      X.add(v);
      //this.matrix.deleteVertex(v);
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
    //System.out.format("Bron2: %s, %s, %s%n", R, P, X);
    if (P.isEmpty() && X.isEmpty()) {
      if(R.size() >= this.k) {
        Vertex toAdd = new Vertex(this.ccMatrix.numVertices());
        Clique thisClique = new Clique();
        for(Vertex vert : R) {
          thisClique.addVertex(vert);
        }
        toAdd.setData(thisClique);
        this.ccMatrix.addVertex(toAdd);
      }
    } else {
      Set<Vertex> toChooseFrom = new HashSet<Vertex>(P);
      toChooseFrom.addAll(X);
      //choose random vertex form P U X
      Vertex pivot = (Vertex) toChooseFrom.toArray()[r.nextInt(toChooseFrom.size())];
      Set<Vertex> pivotVertices = neighbors(pivot);
      HashSet<Vertex> miniP = new HashSet<Vertex>(P);
      miniP.removeAll(pivotVertices);

      for (Iterator<Vertex> it = miniP.iterator(); it.hasNext();) {
        Set<Vertex> Rclone = new HashSet(R);
        Set<Vertex> Pclone = new HashSet(P);
        Set<Vertex> Xclone = new HashSet(X);
        Vertex v = it.next();
        Set<Vertex> vNeigh = neighbors(v);
        Rclone.add(v);
        Pclone.retainAll(vNeigh);
        Xclone.retainAll(vNeigh);
        Bron2(Rclone, Pclone, Xclone);
        Pclone.remove(v);
        Xclone.add(v);
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
