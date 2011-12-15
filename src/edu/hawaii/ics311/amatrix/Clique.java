package edu.hawaii.ics311.amatrix;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of Clique with size k.  
 * It is represented in a clique-clique matrix by a vertex with this Clique as one of its properties.
 * 
 * @author Russell Vea for Assignment 3 ICS 311 Fall 2011
 */
public class Clique {

	Vertex rep;
	private Set<Vertex> members;
	
	/**
	 * Instantiates a new Clique without a representative.
	 */
	public Clique() {
		this.members = new HashSet<Vertex>();
	}

	/**
	 *  Adds a vertex to the clique.
	 *  @param v the vertex to add. 
	 */
	public void addVertex(Vertex v) {
		this.members.add(v);
	}

	public Set<Vertex> getMembers() {
		return this.members;
	}
	
	public String toString() {
	  String toReturn = "";
	  for(Iterator<Vertex> it = this.members.iterator(); it.hasNext();)
	    toReturn += it.next().toString() + " ";
	  return toReturn;
	}
}
