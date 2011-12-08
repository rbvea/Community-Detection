package edu.hawaii.ics311.amatrix;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of the Graph API using a Matrix
 * where Matrix[i][i] is the index of the vertices
 * and Matrix[i][j] = 1 if there exists an directional
 * edge e between i and j
 * 
 * @author Russell Vea for ICS 311 Fall 2011 Assignment 2
 */
public class AdjacencyMatrix implements Cloneable{
  private int[][] matrix;
  public Map<Integer , Vertex> vertices;
  private Map<Pair , Edge> edges;
  
  public AdjacencyMatrix (int size) {
    this.matrix = new int[size][size];
    this.vertices = new HashMap<Integer, Vertex>(size);
    this.edges = new HashMap<Pair, Edge>(size * size);
    for (int i = 0; i < size; i++) {
      matrix[i][i] = i;
    }
    
  }
  /**
   * @return the number of vertices in the graph
   */
  public int numVertices() {
    return this.vertices.size();
  }
  
  public int numEdges() {
    return this.edges.size();
  }
  
  /**
   * @return An iterator over all the vertices in the graph
   */
  public Iterator<Vertex> vertices() {
    return this.vertices.values().iterator();
  }
  
  /**
   * @return return an iterator over all edges in the graph
   */
  public Iterator<Edge> edges() {
    return this.edges.values().iterator();
  }
  
  /**
   * An alternative method to get a collection edges instead of an Iterator.
   * @return a collection of edges.
   */
  public Collection<Edge> getEdges() {
    return this.edges.values();
  }
  
  /**
   * @param v the index of the vertex in the matrix
   * @return the number of edges (directed and undirected) incident on v.
   */
  public int degree (int v){
    int total = 0;
    for (int i = 0; i < matrix.length; i++) { 
      if (matrix[v][i] == 1) {
        total++;
      } 
      if (matrix[i][v] == 1) {
        total++;
      }
    }
    return total;
  }
  
  /**
   * @param v
   * @return an iterator of the vertices adjacent to v.
   */
  public Iterator<Vertex> adjacentVertices (int v) { 
    ArrayList<Vertex> adj = new ArrayList<Vertex>();
    for (int i = 0; i < matrix.length; i++) {
      if (matrix[v][i] == 1 || matrix[i][v] == 1 && i != v) { 
        adj.add(vertices.get(i));
      }
    }
    return adj.iterator();
  }
  
  public Iterator<Vertex> adjacentVertices (Vertex v) {
    return adjacentVertices(v.getId());
  }
  
  /**
   * @param v
   * @return an iterator of the edge incident to v. 
   */
  public Iterator<Edge> incidentEdges (int v) {
     ArrayList<Edge> inc = new ArrayList<Edge>();
     int[] temp = {0, 0};
     for (int i = 0; i < matrix.length; i++) { 
       if (matrix[i][v] == 1) { 
         temp[0] = i;
         temp[1] = v;
         inc.add(edges.get(temp));
       } else if (matrix[v][i] == 1) {
         temp[0] = v;
         temp[1] = i;
         inc.add(edges.get(temp));
       }
     }
     return inc.iterator();
  }
  
  public Vertex[] endVertices (Edge e) { 
    Vertex[] ends = new Vertex[2];
    ends[0] = vertices.get(e.getStart());
    ends[1] = vertices.get(e.getEnd());
    return ends;
  }
  
  public Vertex opposite (Vertex v, Edge e) {
    int toReturn = v.getId() == e.getStart() ? e.getStart() : e.getEnd();
    return vertices.get(toReturn);
  }
  
  public boolean areAdjacent(Vertex v1, Vertex v2) { 
    int i = v1.getId();
    int j = v2.getId();
    System.out.println("Testing adjacency: " + i + " " + j);
    return matrix[i][j] == 1 || matrix[j][i] == 1;
  }
  
  /*BEGIN Accessing Directed Graphs*/
  
  public Iterator<Edge> directedEdges() {
    ArrayList<Edge> directedEdges = new ArrayList<Edge>();
    for(Edge e : edges.values()) { 
      if (e.isDirected())
        directedEdges.add(e);
    }
    return directedEdges.iterator();
  }
  
  public Iterator<Edge> undirectedEdges() {
    ArrayList<Edge> undirectedEdges = new ArrayList<Edge>();
    for(Edge e : edges.values()) { 
      if (!e.isDirected()) 
        undirectedEdges.add(e);
    }
    return undirectedEdges.iterator();
  }
  
  public int inDegree(int v) { 
    int inDegree = 0;
    for(int i = 0; i < matrix.length; i++)  
      if(matrix[i][v] == 1 && i != v)
        inDegree++;
    return inDegree;
  }
  
  public int inDegree(Vertex v) { 
    return inDegree(v.getId());
  }
  
  public int outDegree(int v) { 
    int outDegree = 0;
    for(int i = 0; i < matrix.length; i++)
      if(matrix[v][i] == 1)
        outDegree++;
    return outDegree;
  }
  
  public int outDegree(Vertex v) { 
    return outDegree(v.getId());
  }
  
  /**
   * 
   * @return an iterator over the vertices adjacent to v by incoming edges.
   */
  public Iterator<Vertex> inAdjacentVertices(Vertex v) { 
    ArrayList<Vertex> inAdj = new ArrayList<Vertex>();
    for(int i = 0; i < matrix.length; i++)
      if(matrix[i][v.getId()] == 1 && i != v.getId())
        inAdj.add(vertices.get(i));
    return inAdj.iterator();
  }
  
  /**
   * 
   * @return an iterator over the vertices adjacent to v by outgoing edges.
   */
  public Iterator<Vertex> outAdjacentVertices(Vertex v) { 
    ArrayList<Vertex> inAdj = new ArrayList<Vertex>();
    for(Edge e : this.edges.values())
      if(e.getStart() == v.getId())
        inAdj.add(vertices.get(e.getEnd()));
    return inAdj.iterator();
  }
  
  /**
   * Returns an iterator over the vertices adjacent to v by incoming edges. 
   * @param v 
   * @return the iterator over edges incident to v
   */
  public Iterator<Edge> inIncidentEdges(Vertex v) { 
    ArrayList<Edge> inEdge = new ArrayList<Edge>();
    for(int i = 0; i < matrix.length; i++)
      if(matrix[i][v.getId()] == 1 && i != v.getId()) {
        int[] temp = {i , v.getId()};
        inEdge.add(edges.get(temp));
      }
    return inEdge.iterator();
  }
  
  /**
   * TODO: Test whether iterating over matrix or list is more efficient
   * @param v 
   * @return an iterator over the vertices adjacent to v by incoming edges. 
   */
  public Iterator<Edge> outIncidentEdges(Vertex v) { 
    ArrayList<Edge> outEdge = new ArrayList<Edge>();
    for(Edge e : edges.values()) {
      if(e.getEnd() == v.getId())
        outEdge.add(e);
    }
    return outEdge.iterator();
  }
  
  /**
   * 
   * @param e Edge
   * @return the origin vertex of e, if e is directed.
   * @throws Exception 
   */
  public Vertex origin(Edge e) throws InvalidEdgeException {  
    if(!e.isDirected()) 
      throw new InvalidEdgeException("Invalid edge" + e);
     else 
      return vertices.get(e.getStart());
  }
  
  /**
   * 
   * @param e
   * @return
   * @throws InvalidEdgeException
   */
  public Vertex destination(Edge e) throws InvalidEdgeException { 
    if(!e.isDirected())
      throw new InvalidEdgeException("Invalid edge" + e);
   else 
     return vertices.get(e.getEnd());
  }
   
  /*BEGIN Mutators */
  
  /**
   * Add vertex by cloning matrix and extending dimensions
   * @param v vertex of matrix to be added
   */
  public void addVertex(Vertex v) {
    if (this.vertices.size() == this.matrix.length) {
      int[][] temp = this.matrix.clone();
      this.matrix = new int[matrix.length + 1][matrix.length + 1];
      System.arraycopy(temp, 0, matrix, 0, matrix.length);
      this.matrix[this.matrix.length][this.matrix.length] = this.matrix.length;
      v.setId(matrix.length);
    } 
    this.vertices.put(v.getId(), v);
  }
  
  /**
   * Delete Vertex by moving the rows up and the columns down
   * Also delete vertex from hashmap
   * @param v
   */
  public void deleteVertex(Vertex v) {
    int id = v.getId();
    for(int i = 0; i < numVertices(); i++) {
      this.matrix[id][i] = 0;
      this.matrix[i][id] = 0;
    }
  }
  
  /**
   * Add undirected edge to matrix and to hashmap
   * @param u
   * @param v
   * @param o
   */
  public void insertEdge(int u, int v, Object o) {

    Edge e = new Edge(u, v, 0, o);
    e.setDirected(false);
    this.matrix[u ][v] = 1;
    this.matrix[v][u] = 1;
    Pair toAdd = new Pair(u,v);
    this.edges.put(toAdd, e);
  }
  
  /**
   * Add directed edge to matrix and to hashmap
   * @param u
   * @param v
   * @param o
   */
  public void insertDirectedEdge(int u, int v, Object o) {
    Edge e = new Edge(u, v, 0, o);
    e.setDirected(true);
    this.matrix[u][v] = 1;
    Pair toAdd = new Pair(u, v);
    this.edges.put(toAdd, e);
  }
  
  
      
  /**
   *Sets the direction of an edge away from a vertex. Makes an undirected edge directed.
   * @param e edge.
   * @param newOrigin the new vertex.
   * @throws InvalidEdgeException when v is not an endpoint of e.
   */
  public void setDirectionFrom(Edge e, Vertex newOrigin) throws InvalidEdgeException {
    if (!e.isDirected()) {
      e.setDirected(true);
    }
    if(newOrigin.getId() < 0 || newOrigin.getId() > numVertices()) {
      throw new InvalidEdgeException("Invalid edge" + e);
    }
    this.matrix[e.getStart()][e.getEnd()] = 0;
    e.setStart(newOrigin.getId());
    this.matrix[newOrigin.getId()][e.getEnd()] = 1;
  }
  
  /**
   * Sets the direction of an edge towards a vertex.  Makes an undirected edge directed.
   * @param e
   * @param newDestination
   * @throws InvalidEdgeException when v is not an endpoint of e
   */
  public void setDirectionTo(Edge e, Vertex newDestination) throws InvalidEdgeException {
    if (!e.isDirected()) {
      e.setDirected(true);
    }
    if(newDestination.getId() < 0 || newDestination.getId() > numVertices()) {
      throw new InvalidEdgeException("Invalid edge" + e);
    }
    this.matrix[e.getStart()][e.getEnd()] = 0;
    e.setEnd(newDestination.getId());
    this.matrix[e.getEnd()][newDestination.getId()] = 1;
  }
  
  /**
   * Makes a directed edge undirected. Does nothing if the edge is undirected.
   * @param e edge to make undirected
   */
  public void makeUndirected(Edge e) {
    if (e.isDirected()) {
      this.matrix[e.getStart()][e.getEnd()] = 0;
      this.matrix[e.getEnd()][e.getStart()] = 0;
      e.setDirected(false);
    }
  }
  
  /**
   * Reverse the direction nof an edge
   * @param e
   * @throws InvalidEdgeException if the edge is undirected
   */
  public void reverseDirection(Edge e) {
    e.setDirected(true);
    final int start = e.getStart();
    final int end = e.getEnd();
    this.edges.remove(e);
    this.matrix[start][end] = 0;
    e.setStart(end);
    e.setEnd(start);
    this.edges.put(new Pair(end,start), e);
    this.matrix[end][start] = 1;
  }
  
  /* BEGIN user defined methods */
  
  protected int[][] getMatrix() {
    return this.matrix.clone();
  }
  
  public Vertex getVertex (int i) {
    return this.vertices.get(i);
  }
  
  public Edge getEdge (int source, int target) {
    Pair edge = new Pair(source, target);
    return this.edges.get(edge);
  }
  
  
  @Override
  public String toString() {
    String toReturn = "";
    for(int i = 0; i < this.matrix.length; i++) {
      for(int j = 0; j < this.matrix.length; j++) {
        toReturn += matrix[i][j] + " ";
      }
      toReturn += "\n";
    }
    return toReturn;
  }
  
  public AdjacencyMatrix clone() throws CloneNotSupportedException {
    AdjacencyMatrix toReturn = new AdjacencyMatrix(numVertices());
    toReturn.matrix = matrix.clone();
    toReturn.vertices = new HashMap<Integer, Vertex>(this.vertices);
    toReturn.edges = new HashMap<Pair, Edge>(this.edges);
    return toReturn;
  }
  
  public static void main (String[] args) throws IOException {
    AdjacencyMatrix matrix = new AdjacencyMatrix(10);
    matrix.addVertex(new Vertex(0));
    assertTrue(matrix.getVertex(0) != null);
    matrix.getVertex(0).setAnnotation("Test", "Foo");
    System.out.println(matrix.getVertex(0).getAnnotation("Test"));
  }
  
  public Object removeEdge(Edge e) {
    Object toReturn = e.getData();
    this.matrix[e.getStart()][e.getEnd()] = 0;
    this.matrix[e.getEnd()][e.getStart()] = 0;
    this.edges.remove(new Pair(e.getStart(), e.getEnd()));
    return toReturn;
  }
  
  public boolean contains(Vertex v) {
    if(v == null) {
      System.out.println("It was v");
    }
    try {
      return this.vertices.containsKey(v.getId());
    } catch(NullPointerException e) {
      return false;
    }
  }
}
