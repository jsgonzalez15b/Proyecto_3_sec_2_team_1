package model.data_structures;

import java.util.ArrayList;

import model.vo.VOMovingViolations;

/*
 * Clase de vertice generico, funciona como una tupla con llave id, y valor infoVertex, los arcos que posee son almacenados en una pila
 */
public class Vertice <V extends Comparable<V>,K extends Comparable<K>,A extends Comparable<A>>
{
	//Atributos - Info
	
	private ArrayList<Arco> infoVertex;
	/**
	 * Id generico del vertice
	 */
	private K llaveId;
	
	/**
	 * InfoVertex c(orrespondiente a la latitud y longitud para el taller 8) 
	 */
	private V vVertex;

	//Atributos - Arcos
	
	/**
	 * Pila de arcos del vertice
	 */
	private IStack<Arco> arcosVertex;

		
	/**
	 * Atributo para aproximacion de infracciones a vertices
	 */
	private IStack<VOMovingViolations> infracciones; 
	
	/**
	 * Numero de arcos asociados al vertice
	 */
	private int nArcos; 
	
	
	//Constructor
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
	}
	
	
	//Metodos
	
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
	 * Modifica la informacion del vertice 
	 * @param infoVertex nueva informacion del vertice
	 */
	public void setInfoVertex(V pInfoVertex)
	{
		vVertex=pInfoVertex;
	}
	
	//arcos y operaciones sobre arcos
	/**
	 * retorna el arreglo de arcos del vertice (lista de adyacencia)
	 */
	public Stack<Arco> darArcos()
	{
		return (Stack<Arco>) arcosVertex;
	}
	
	/**
	 * agrega un arco a la pila de arcos
	 * @param pPeso Peso del nuevo Arco a crear
	 * @param pLlave Id del vertice asociado al vertive principal
	 */
	public void agregarArco(A pPeso,K pLlave)
	{	
		Arco elArco = new Arco<K,A>(pPeso,pLlave);
		arcosVertex.push(elArco);
		nArcos++;
	}
	
	/**
	 * dado un id de un vertice conectado retorna la informacion asociada al mismo
	 * @param pId Id del vertice asociado al arco, el pId DEBE existir, en el caso contrario el metodo retorna nulo
	 */
	public Arco darArco(K pId)
	{
		Stack<Arco> copiaArcos = (Stack<Arco>) arcosVertex; //copia de arcos para obtener la informacion sin borrarla
		Arco arcoEncontrado = null; //arco a retornar
		Arco arcoActual = null;
		while(!arcosVertex.isEmpty())
		{
			arcoActual = copiaArcos.pop();
			if(arcoActual.darAdyacente().compareTo(pId)==0)
			{
				arcoEncontrado = arcoActual;
				break;
			}
		}
		return (Arco) arcoEncontrado;
	}
	
	/**
	 * Modificar la informacion del arco con el vertice idVertexFin
	 */
	public void setInfoArc(K idVertexFin,
			A infoArc)
	{
		Arco arcoModificandose = darArco(idVertexFin);
		arcoModificandose.setInfoArc(infoArc);
	}
	
	public void setInfraccion(VOMovingViolations a)
	{
		infracciones.push(a);
	}
	public ArrayList<Arco> getArcos() {
		return infoVertex; 
	}
}
