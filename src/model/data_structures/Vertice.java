package model.data_structures;

import java.util.ArrayList;

import mapa.Mapa;
import model.vo.VOMovingViolations;

/*
 * Clase de vertice generico, funciona como una tupla con llave id, y valor infoVertex, los arcos que posee son almacenados en una pila
 */
public class Vertice <V extends Comparable<V>,K extends Comparable<K>,A extends Comparable<A>>
{
	//ATRIBUTOS - Info
	/**
	 * Id generico del vertice
	 */
	private K llaveId;	
	/**
	 * InfoVertex c(orrespondiente a la latitud y longitud para el taller 8) 
	 */
	private V vVertex;	
	/**
	 * Atributo para aproximacion de infracciones a vertices
	 */
	private IStack<Integer> infracciones; 	
	/**
	 * Numero de infracciones aproximadas a este vertice
	 */
	private int nInfracciones; 
	
	//Atributos - Arcos
	/**
	 * Pila de arcos del vertice
	 */
	private IStack<Arco<K,A>> arcosVertex;
	/**
	 * Numero de arcos asociados al vertice
	 */
	private int nArcos; 
	/**
	 * pila de llaves tipo long de vertices adyacentes 
	 */
	private IStack<Long> longAdyacentes; 
	
	//CONSTRUCTOR
	/**
	 * @param pLlave llave id del nuevo vertice
	 */
	public Vertice(K pLlave,V pValueVertex)
	{
		llaveId = pLlave;
		arcosVertex = new Stack<>();
		vVertex=pValueVertex;
		infracciones= new Stack<>();
		nArcos=0;
		longAdyacentes= new Stack<>(); 
		nInfracciones=0;
	}
	
	
	//METODOS
	//Atributos del vertice
	/**
	 * retorna la llave del vertice
	 */
	public K darLlave()
	{
		return llaveId;
	}
	/**
	 * retorna el valor asociado al vertice
	 */
	public V darValor()
	{
		return vVertex;
	}
	/**
	 * retorna el numero de arcos asociados al vertice
	 */
	public int darNArcos()
	{
		return nArcos;
	}
	/**
	 * retorna el numero de infracciones aproximadas al vertice
	 */
	public int darNInfracciones()
	{
		return nInfracciones;
	}
	/**
	 * Modifica la informacion del vertice 
	 * @param infoVertex nueva informacion del vertice
	 */
	public void setInfoVertex(V pInfoVertex)
	{
		vVertex=pInfoVertex;
	}

	//arcos y operaciones sobre arcos
	/**
	 * retorna la pila de arcos del vertice 
	 */
	public Stack<Arco<K,A>> darArcos()
	{
		return (Stack<Arco<K,A>>) arcosVertex;
	}
	/**
	 * agrega un arco a la pila de arcos
	 * @param pPeso Peso del nuevo Arco a crear
	 * @param pLlave Id del vertice asociado al vertive principal
	 */
	public void agregarArco(Arco<K,A> agregar)
	{	
		arcosVertex.push(agregar);
		nArcos++;
	}
	/**
	 * Agregar un identificador de infraccion a la pila de integers de infracciones
	 */
	public void setInfraccion(int a)
	{
		infracciones.push(a);
		nInfracciones++;
	}
	/**
	 * Retorna una pila de llaves genericas K de los vertices adyacentes al vertice
	 */
	public Stack<K> darAdyacentes()
	{
		Stack<K> retornar=new Stack<K>();
		Iterador<Arco<K,A>> iter= (Iterador<Arco<K, A>>) arcosVertex.iterator(); 
		Arco<K,A> actual= iter.next(); 
		while(iter.hasNext())
		{
			retornar.push(actual.darAdyacente()); 
			actual=iter.next(); 
		}
		return retornar; 
	}
	/**
	 * Retorna el arco asociado al vertice con llave recibida por parametro
	 * @param adyacente llave del vertice adyacente
	 */
	public Arco<K,A> darArcoPorAdyacente(K adyacente)
	{
		Iterador<Arco<K,A>> iter= (Iterador<Arco<K, A>>) arcosVertex.iterator(); 
		Arco<K,A> actual=iter.next(); 
		while(iter.hasNext())
		{
			if(actual.darAdyacente().equals(adyacente))
			{
				return actual; 
			}
			actual=iter.next() ; 
		}
		return null; 
	}
	public void agregarLongAdyacente(Long a)
	{
		longAdyacentes.push(a);
	}
	/**
	 * Retorna el arco asociado al vertice con llave recibida por parametro
	 * @return longAdyacentes lista de llaves de vertices adyacentes
	 */
	public IStack<Long> darLongAdyacente()
	{
		return longAdyacentes;
	}
}