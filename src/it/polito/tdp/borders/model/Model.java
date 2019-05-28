package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Map<Integer, Country> countryMap;
	private BordersDAO dao;
	private Graph<Country, DefaultEdge> graph;
	private List<Country> countries;
	
	public Model() {
		countryMap = new HashMap<Integer, Country>();
		dao = new BordersDAO();
		graph = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
	}
	
	public void createGraph(int anno){
		// Lista di vertici
		countryMap = dao.loadAllCountries();
		
		// Connessioni vertici(archi)
		List<Border> confini = dao.getCountryPairs(anno, countryMap);
		
		for(Border b : confini) {
			graph.addVertex(b.getC1());
			graph.addVertex(b.getC2());
			graph.addEdge(b.getC1(), b.getC2());
		}
		System.out.format("Inseriti %d vertici e %d archi\n", graph.vertexSet().size(), graph.edgeSet().size());
		
		// siccome devo stampare l'elenco degli stati li salvo ordinati in una lista
		countries = new ArrayList<>(graph.vertexSet());
		Collections.sort(countries);
		
	}

	public List<Country> getCountries() {
		return countries;
	}
	
	public Map<Country, Integer> countryCounts(){
		Map<Country, Integer> countryCount = new HashMap<Country, Integer>();
		if(graph == null)
			throw new RuntimeException("Grafo no esistente !!!");
		
		for(Country c : countries) {
			countryCount.put(c, graph.degreeOf(c));
		}
		return countryCount;
	}

	public int getNumeroComponentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> ci = new ConnectivityInspector<Country, DefaultEdge>(graph);
		return ci.connectedSets().size();
	}

	public List<Country> trovaVicini(Country country) {
		List<Country> vicini = this.trovaViciniJGraphT(country);
		return vicini;
	}

	private List<Country> trovaViciniJGraphT(Country country) {
		List<Country> visitati = new LinkedList<Country>();
		// POSSO USARE ANCHE UN BreadthFirstIterator
		GraphIterator<Country, DefaultEdge> dfi = new DepthFirstIterator<Country, DefaultEdge>(graph, country);
		while(dfi.hasNext())
			visitati.add(dfi.next());
		return visitati;
	}

}
