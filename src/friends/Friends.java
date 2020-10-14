package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2. ///////emphass
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		//visited check
		boolean[] vis = new boolean[g.members.length];
		
		//make queue
		Queue<Person> enqper = new Queue<Person>();
		
		//enqueue all members
		enqper.enqueue(g.members[g.map.get(p1)]);
		//list of strings
		ArrayList<String> friendslist = new ArrayList<String>();
		
		Queue<ArrayList<String>> stringqq = new Queue<ArrayList<String>>();
		//add to friendslist
		friendslist.add(g.members[g.map.get(p1)].name);
		
		stringqq.enqueue(friendslist);
		//while queue isnt empty
		while (!enqper.isEmpty()) {
			Person human = enqper.dequeue();
			//set to true
			vis[g.map.get(human.name)] = true;
			//dequeues to receive string
			ArrayList<String> arrlis = stringqq.dequeue();
			Friend ptr = g.members[g.map.get(human.name)].first;
			
			//checks all the neighbors
			while (ptr != null) {
				if (!vis[ptr.fnum]) {
					ArrayList<String> nuevo = new ArrayList<String>(arrlis);
					String tempsing = g.members[ptr.fnum].name;
					nuevo.add(tempsing);
					if (tempsing.equals(p2)) {
						return nuevo;
					}
					enqper.enqueue(g.members[ptr.fnum]);
					stringqq.enqueue(nuevo);
				}
				ptr = ptr.next;
			}
		}
		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		//visited check
		boolean[] vis = new boolean[g.members.length];
		
		ArrayList<ArrayList<String>> listacliques = new ArrayList<>();
		//traverse through 
		for (int i = 0; i < g.members.length; i++) {
			Person human = g.members[i];
			if (vis[i] || !human.student)
				continue;
			ArrayList<String> neywgang = new ArrayList<>();
			helpingboi(g, neywgang, vis, school, i);
			//adds list of cliques into the new squad, new friends new life
			if (neywgang != null && neywgang.size() > 0)
				listacliques.add(neywgang);
		}

		return listacliques;
	}

	//helper method for clique
	private static void helpingboi(Graph g, ArrayList<String> squad, boolean[] vis, String school, int ptr) {
		//creates object where current kids is pointed at
		Person pers = g.members[ptr];
		
		//checks to see if eligbile for the SQUADDDD
		if (!vis[ptr] && pers.student && pers.school.equals(school)) {
			squad.add(pers.name);}
		
		vis[g.map.get(pers.name)] = true;
		Friend kid = g.members[ptr].first;
		//if kid exists, then use recursion
		while (kid != null) {
			int count = kid.fnum;
			Person ptrkid = g.members[count];
			if (vis[count] == false && ptrkid.student && ptrkid.school.equals(school)) {
				helpingboi(g,squad, vis, school, count);
			}
			kid = kid.next;
		}
	}

	
	/*
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */

	public static ArrayList<String> connectors(Graph g) {
		
		String[] parts = new String[g.members.length];
		
		//traversal
		for (int i = 0; i < parts.length; i++) {
			parts[i] = g.members[i].name;
		}
		
		ArrayList<String> link = new ArrayList<String>();
		
		boolean[] vis = new boolean[g.members.length];
		int[] prevptr = new int[g.members.length];
		int[] dtracker = new int[g.members.length];

		
		int countr = 0;
		//traversal of visited array
		for (int i = 0; i < vis.length; i++) {
			boolean coolean = vis[i];
			if (coolean==false) {
				Person hooman = g.members[i];
				DFS(g, link, countr, hooman, vis, dtracker, prevptr);
			}
		}
		//goes thru to see connections
		for (int i = 0; i < link.size(); i++) {
			Person human = g.members[g.map.get(link.get(i))];
			//extracts arraylist size
			int lekher = amigosfinda(g, human).size();
			
			if (lekher == 1) {link.remove(i);}
		}
		return link;}

	private static ArrayList<Person> amigosfinda(Graph g, Person persona) {
		
		ArrayList<Person> amigos = new ArrayList<Person>();
		Friend amigo = persona.first;
		//goes thru until no mor friends and traversesarraylist of amigos
		while (amigo != null) {
			
			Person initial = g.members[amigo.fnum];
			amigos.add(initial);
			amigo = amigo.next;
		}
		return amigos;
	}

	private static void DFS(Graph graph, ArrayList<String> links,int countr, Person hum, boolean[] vis, int[] numeroD, int[] prev) {
		
		int ptr = graph.map.get(hum.name);
		numeroD[ptr] = countr;
		prev[ptr] = countr;
		//set to true and icnrease count
		vis[ptr] = true;
		countr++;
		
		ArrayList<Person> friends = amigosfinda(graph, hum);
		//traverses
		for (int i = 0; i < friends.size(); i++) {
			int frindex = graph.map.get(friends.get(i).name);
			
			if (vis[frindex]) {
				prev[ptr] = Math.min(prev[ptr], numeroD[frindex]);
			} else {
				DFS(graph, links, countr, friends.get(i), vis, numeroD, prev);
				
				if (numeroD[ptr] > prev[frindex]) {
					prev[ptr] = Math.min(prev[ptr], prev[frindex]);
				} else {
					String tempo = hum.name;
					
					if (numeroD[ptr] == 0) {
						
						if (i == friends.size() - 1) {
							if (!links.contains(tempo))
								links.add(tempo);}//closes the if for check of i size
					} else {
						if (!links.contains(tempo))
							links.add(tempo);
					}}}}

		return;}}


