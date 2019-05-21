package controller;

import java.util.Iterator;

import model.data_structures.Grafo;
import model.data_structures.Iterador;
import model.data_structures.LinearProbingHashST;
import model.data_structures.Queue;
import model.data_structures.Stack;
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
	private LinearProbingHashST<Integer,Stack<Long>> caminos; 
	private int actual; 
	public Caminos(Grafo<verticeInfo,Long, Double> grafito, Vertice<verticeInfo,Long, Double> pfuente, Vertice<verticeInfo,Long, Double> pdestino) {
		grafo=grafito; 
		inicio=pfuente; 
		fin=pdestino;	
		numvertices=grafo.numVertices();
		caminos=new LinearProbingHashST<>(); 
		actual=0; 
		dfs(inicio,fin); 
	}
	
	public void dfs(Vertice<verticeInfo, Long, Double> fuente, Vertice<verticeInfo, Long, Double> pfin) {
		lista.enqueue(fuente.darLlave());
		marcados.put(fuente.darLlave(), true);
		size++; 
		if(fuente.darLlave().equals(pfin.darLlave())) {
			Iterador<Long> iter = lista.iterator(); 
			Stack<Long> caminoactual=new Stack<>(); 
			while(iter.hasNext()) {
				Long actual= iter.next();
				caminoactual.push(actual); 
			}
			caminos.put(actual, caminoactual);
			actual++; 
		}
		else {
			Iterator<Long> iteradyacentes = fuente.darAdyacentes().iterator();
			while(iteradyacentes.hasNext()) {
				Long adyacenteactual=iteradyacentes.next(); 
				if(marcados.get(adyacenteactual).equals(false)) {
					dfs(grafo.darTablaVertices().get(adyacenteactual), pfin); 
					marcados.put(adyacenteactual, false);
					size--; 
					lista.dequequeporId(size);
				}
			}
		}

	}
	public LinearProbingHashST<Integer,Stack<Long>> darCaminosTodos(){
		return caminos; 
	}
	
}
