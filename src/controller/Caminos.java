package controller;

import model.data_structures.Grafo;
import model.data_structures.LinearProbingHashST;
import model.data_structures.Queue;
import model.data_structures.Vertice;
import model.vo.verticeInfo;

public class Caminos {

	private int numvertices; 
	private LinearProbingHashST<Long,Boolean> marcados; 
	private Queue<Long> lista; 
	private  int size; 
	private Grafo<verticeInfo,Long,Double> grafo; 
	private Vertice<verticeInfo,Long, Double> inicio; 
	private Vertice<verticeInfo,Long, Double> fin; 
	public Caminos(Grafo<verticeInfo,Long, Double> grafito, Vertice<verticeInfo,Long, Double> pfuente, Vertice<verticeInfo,Long, Double> pdestino) {
		grafo=grafito; 
		inicio=pfuente; 
		fin=pdestino;
		
	}
}
