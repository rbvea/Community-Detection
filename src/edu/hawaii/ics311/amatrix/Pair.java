package edu.hawaii.ics311.amatrix;

/**
 * An object storing two integers corresponding to vertices of an edge.
 * For use as keys when storing Edges (int arrays are unique.)
 * 
 * @author Russell Vea for ICS 311 Fall 2011.
 */
public class Pair {
  
  private final Integer i;
  private final Integer j;
  
  public Pair(int i, int j) {
    this.i = i;
    this.j = j;
  }
  
  public int getFirst() {
    return this.i;
  }
  
  public int getSecond() {
    return this.j;
  }
  
  @Override public int hashCode() {
    return this.i.hashCode() ^ this.j.hashCode();
  }
  
  @Override public boolean equals(Object o) {
    if(o == null) return false;
    if(!(o instanceof Pair)) return false;
    Pair other = (Pair)o;
    if(other.getFirst() == this.i) {
      if(other.getSecond() == this.j) {
        return true;
      }
    }
    return false;
  }
}
