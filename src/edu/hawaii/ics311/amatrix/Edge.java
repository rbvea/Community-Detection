package edu.hawaii.ics311.amatrix;
import java.util.Map;
import java.util.HashMap;

/**
 * Edge implmentation for an Adjacency Matrix.
 * 
 * @author Russell Vea for ICS 311 Fall 2011
 */
public class Edge {

  private int start;
  private int end;
  public Map<String, Object> annotations;
  private boolean isDirected;
  
  /**
   * Constructor specifying only start and end, assumes edge is directed.
   * @param start the id of the source vertex.
   * @param end the id of the target verex.
   */
  public Edge (int start, int end) {
    this.start = start;
    this.end = end;
    this.isDirected = true;
    this.annotations = new HashMap<String, Object>();
  }
  
  /**
   * Constructor specifying source, target, weight and data.
   * The latter two are added as annotations.
   * @param start
   * @param end
   * @param weight
   * @param data
   */
  public Edge (int start, int end, int weight, Object data) {
    this.start = start;
    this.end = end;
    this.isDirected = true;
    this.annotations = new HashMap<String, Object>();
    this.annotations.put("weight", weight);
    this.annotations.put("data", data);
  }
  
  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public Object getData() {
    return getAnnotation("data");
  }

  public void setData(Object data) {
    setAnnotation("data", data);
  }
  
  public boolean isDirected() { 
    return this.isDirected();
  }
  
  public void setDirected(boolean isDirected) { 
    this.isDirected = isDirected;
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
  
  
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
    result = prime * result + end;
    result = prime * result + start;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Edge other = (Edge) obj;
    if (annotations == null) {
      if (other.annotations != null)
        return false;
    }
    else if (!annotations.equals(other.annotations))
      return false;
    if (end != other.end)
      return false;
    if (start != other.start)
      return false;
    return true;
  }

  @Override public String toString() {
    return "<" + Integer.toString(start) + " , " + Integer.toString(end) + ">";
  }
}
