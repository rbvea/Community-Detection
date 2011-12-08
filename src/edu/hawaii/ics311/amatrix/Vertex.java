package edu.hawaii.ics311.amatrix;

import java.util.Map;
import java.util.HashMap;

/**
 * A Vertex implementation for an Adjacency Matrix.
 * 
 *@author Russell Vea for ICS 311 Fall 2011
 */
public class Vertex implements Comparable<Vertex>{
  public int id;
  public Object data;
  public Map<String, Object> annotations;
  
  /**
   * Complex Constructor
   * @param id the position of the vertex on the matrix
   * @param data the data to hold.
   */
  public Vertex (int id, Object data) {
    this.id = id;
    this.data = data;
    this.annotations = new HashMap<String, Object>();
  }

  /**
   * Simple Constructor
   * @param id the position of the vertex on the matrix
   */
  public Vertex (int id) {
    this.id = id;
    this.data = null;
    this.annotations = new HashMap<String, Object>();
  }
  
  public int getId(){
    return this.id;
  }
  
  public void setId(int Id) { 
    this.id = Id;
  }
  
  public Object getData () {
    return this.data;
  }
  
  public Object getAnnotation(String annotation){
    return this.annotations.get(annotation);
  }
  
  public void setAnnotation(String annotation, Object obj) {
    if (this.annotations.containsKey(annotation)) {
      this.annotations.remove(annotation);
    }
    this.annotations.put(annotation, obj);
  }
  
  public Object removeAnnotation(String annotation) {
    Object toReturn = this.annotations.get(annotation);
    this.annotations.remove(annotation);
    return toReturn;
  }

  public int compareTo(Vertex other) {
    if ((Double)this.annotations.get("weight") < (Double)this.annotations.get("weight")) {
      return -1;
    } else if ((Double)this.annotations.get("weight") > (Double)this.annotations.get("weight")) {
      return 1;
    } else {
      return 0;
    }
  }
  
  @Override public String toString() {
    return Integer.toString(id);
  }
}
