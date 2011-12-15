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
 * edge Vertex between i and j
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
        if(contains(vertices.get(i)))
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
  /*
  public Vertex[] endVertices (Edge Vertex) { 
    Vertex[] ends = new Vertex[2];
    ends[0] = vertices.get(Vertex.getStart());
    ends[1] = vertices.get(Vertex.getEnd());
    return ends;
  }*/
  
  public Vertex opposite (Vertex v, Edge Vertex) {
    int toReturn = v.getId() == Vertex.getStart() ? Vertex.getStart() : Vertex.getEnd();
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
    for(Edge Vertex : edges.values()) { 
      if (Vertex.isDirected())
        directedEdges.add(Vertex);
    }
    return directedEdges.iterator();
  }
  
  public Iterator<Edge> undirectedEdges() {
    ArrayList<Edge> undirectedEdges = new ArrayList<Edge>();
    for(Edge Vertex : edges.values()) { 
      if (!Vertex.isDirected()) 
        undirectedEdges.add(Vertex);
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
    for(Edge Vertex : this.edges.values())
      if(Vertex.getStart() == v.getId())
        inAdj.add(vertices.get(Vertex.getEnd()));
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
   * @param v 
   * @return an iterator over the vertices adjacent to v by incoming edges. 
   */
  public Iterator<Edge> outIncidentEdges(Vertex v) { 
    ArrayList<Edge> outEdge = new ArrayList<Edge>();
    for(Edge Vertex : edges.values()) {
      if(Vertex.getEnd() == v.getId())
        outEdge.add(Vertex);
    }
    return outEdge.iterator();
  }
  
  /**
   * 
   * @param Vertex Edge
   * @return the origin Vertex of Vertex, if Vertex is directed.
   * @throws Exception 
   */
  public Vertex origin(Edge Vertex) throws InvalidEdgeException {  
    if(!Vertex.isDirected()) 
      throw new InvalidEdgeException("Invalid edge" + Vertex);
     else 
      return vertices.get(Vertex.getStart());
  }
  
  /**
   * 
   * @param Vertex
   * @return
   * @throws InvalidEdgeException
   */
  public Vertex destination(Edge Vertex) throws InvalidEdgeException { 
    if(!Vertex.isDirected())
      throw new InvalidEdgeException("Invalid edge" + Vertex);
   else 
     return vertices.get(Vertex.getEnd());
  }
   
  /*BEGIN Mutators */
  
  /**
   * Add Vertex by cloning matrix and extending dimensions
   * @param v Vertex of matrix to be added
   */
  public void addVertex(Vertex v) {
    if (this.vertices.size() >= this.matrix.length - 1) {
      int[][] temp = this.matrix.clone();
      this.matrix = new int[matrix.length + 7][matrix.length + 7];
      //System.arraycopy(temp, 0, matrix, 0, temp.length);

      for(int i = 0; i < temp.length; i++) {
        for(int j = 0; j < temp[i].length; j++) {
          this.matrix[i][j] = temp[i][j];
        }
      }
      v.setId(this.vertices.size());
      this.matrix[this.vertices.size() + 1][this.vertices.size() + 1] = this.vertices.size() + 1;
    }  else {
      if(this.matrix[v.getId()][v.getId()] == 0) {
        this.matrix[v.getId()][v.getId()] = v.getId();
      }
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
    this.vertices.remove(v.getId());
  }
  
  /**
   * Add undirected edge to matrix and to hashmap
   * @param u
   * @param v
   * @param o
   */
  public void insertEdge(int u, int v, Object o) {

    Edge Vertex = new Edge(u, v, 0, o);
    Vertex.setDirected(false);
    this.matrix[u ][v] = 1;
    this.matrix[v][u] = 1;
    Pair toAdd = new Pair(u,v);
    this.edges.put(toAdd, Vertex);
  }
  
  /**
   * Add directed edge to matrix and to hashmap
   * @param u
   * @param v
   * @param o
   */
  public void insertDirectedEdge(int u, int v, Object o) {
    Edge Vertex = new Edge(u, v, 0, o);
    Vertex.setDirected(true);
    this.matrix[u][v] = 1;
    Pair toAdd = new Pair(u, v);
    this.edges.put(toAdd, Vertex);
  }
  
  
      
  /**
   *Sets the direction of an edge away from a vertex. Makes an undirected edge directed.
   * @param Vertex edge.
   * @param newOrigin the new vertex.
   * @throws InvalidEdgeException when v is not an endpoint of Vertex.
   */
  public void setDirectionFrom(Edge Vertex, Vertex newOrigin) throws InvalidEdgeException {
    if (!Vertex.isDirected()) {
      Vertex.setDirected(true);
    }
    if(newOrigin.getId() < 0 || newOrigin.getId() > numVertices()) {
      throw new InvalidEdgeException("Invalid edge" + Vertex);
    }
    this.matrix[Vertex.getStart()][Vertex.getEnd()] = 0;
    Vertex.setStart(newOrigin.getId());
    this.matrix[newOrigin.getId()][Vertex.getEnd()] = 1;
  }
  
  /**
   * Sets the direction of an edge towards a vertex.  Makes an undirected edge directed.
   * @param Vertex
   * @param newDestination
   * @throws InvalidEdgeException when v is not an endpoint of Vertex
   */
  public void setDirectionTo(Edge Vertex, Vertex newDestination) throws InvalidEdgeException {
    if (!Vertex.isDirected()) {
      Vertex.setDirected(true);
    }
    if(newDestination.getId() < 0 || newDestination.getId() > numVertices()) {
      throw new InvalidEdgeException("Invalid edge" + Vertex);
    }
    this.matrix[Vertex.getStart()][Vertex.getEnd()] = 0;
    Vertex.setEnd(newDestination.getId());
    this.matrix[Vertex.getEnd()][newDestination.getId()] = 1;
  }
  
  /**
   * Makes a directed edge undirected. Does nothing if the edge is undirected.
   * @param Vertex edge to make undirected
   */
  public void makeUndirected(Edge Vertex) {
    if (Vertex.isDirected()) {
      this.matrix[Vertex.getStart()][Vertex.getEnd()] = 0;
      this.matrix[Vertex.getEnd()][Vertex.getStart()] = 0;
      Vertex.setDirected(false);
    }
  }
  
  /**
   * Reverse the direction nof an edge
   * @param Vertex
   * @throws InvalidEdgeException if the edge is undirected
   */
  public void reverseDirection(Edge Vertex) {
    Vertex.setDirected(true);
    final int start = Vertex.getStart();
    final int end = Vertex.getEnd();
    this.edges.remove(Vertex);
    this.matrix[start][end] = 0;
    Vertex.setStart(end);
    Vertex.setEnd(start);
    this.edges.put(new Pair(end,start), Vertex);
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
    AdjacencyMatrix matrix = new AdjacencyMatrix(0);
    for(int i = 0; i < 55; i++) {
      matrix.addVertex(new Vertex(i));
    }
    for(Iterator<Vertex> it = matrix.vertices(); it.hasNext();) {
      System.out.print(it.next() + " ");
    }
  }
  
  public Object removeEdge(Edge Vertex) {
    Object toReturn = Vertex.getData();
    this.matrix[Vertex.getStart()][Vertex.getEnd()] = 0;
    this.matrix[Vertex.getEnd()][Vertex.getStart()] = 0;
    this.edges.remove(new Pair(Vertex.getStart(), Vertex.getEnd()));
    return toReturn;
  }
  
  public boolean contains(Vertex v) {
    try {
      return this.vertices.containsKey(v.getId());
    } catch(NullPointerException Vertex) {
      return false;
    }
  }
}
