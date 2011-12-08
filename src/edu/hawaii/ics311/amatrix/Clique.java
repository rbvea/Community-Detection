package edu.hawaii.ics311.amatrix;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of Clique with size k.  
 * It is represented in a clique-clique matrix by a vertex with this Clique as one of its properties.
 * 
 * @author Russell Vea for Assignment 3 ICS 311 Fall 2011
 */
public class Clique {

  Vertex rep;
  private List<Vertex> members;
  private int size;
  
  /**
   * Instantiates a new Clique.
   * @param v the vertex representing the clique
   */
  public Clique(Vertex v) {
    this.members = new ArrayList<Vertex>();
    this.rep = v;
    this.size = 1;
  }
  
  /**
   * Instantiates a new Clique without a representative.
   */
  public Clique() {
    this.members = new ArrayList<Vertex>();
    this.rep = null;
    this.size = 0;
  }
  
  /**
   *  Adds a vertex to the clique.
   *  @param v the vertex to add. 
   */
  public void addVertex(Vertex v) {
    this.members.add(v);
    this.size++;
  }
  
  /**
   * @return size the size of the clique
   */
  public int getSize() {
    return this.size;
  }
  
  /**
   * Sets the Clique's representative.
   * @param rep the representative vertex.
   */
  public void setRepresentative(Vertex rep) {
    this.rep = rep;
  }
  
  /**
   * Returns the representative.
   * @return the representative of the Clique.
   */
  public Vertex getRepresentative() {
    return this.rep;
  }
  
  public List<Vertex> getMembers() {
    return this.members;
  }
}
