package model.data_structures;

import model.vo.VOMovingViolations;

/*
 * Clase de vertice generico, funciona como una tupla con llave id, y valor infoVertex
 */
public class Vertice <V extends Comparable<V>,K extends Comparable<K>,A>
{

	//Atributos
	/**
	 * Id generico del vertice
	 */
	private K llaveId;

	/**
	 * Arreglo de informacion del vertice
	 */
	private Arco[] infoVertex;

	/**
	 * Tamano en uso
	 */
	private int cargaInfoVertex;

	private IStack<VOMovingViolations> infracciones; 
	/**
	 * InfoVertex c(orrespondiente a la latitud y longitud para el taller 8) 
	 */
	private V vVertex;
	//Constructor
	/**
	 * @param pLlave llave id del nuevo vertice
	 */
	public Vertice(K pLlave,V pValueVertex)
	{
		//el metodo agregar arco tiene un factor de carga maximo del 0.75
		llaveId = pLlave;
		infoVertex = new Arco[100];
		vVertex=pValueVertex;
		cargaInfoVertex = 0;
		infracciones= new Stack<>();
	}
	//Metodos
	
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
	 * Modifica la informacion del vertice 
	 * @param infoVertex nueva informacion del vertice
	 */
	public void setInfoVertex(V pInfoVertex)
	{
		vVertex=pInfoVertex;
	}
	
	/**
	 * retorna el arreglo de arcos del vertice (lista de adyacencia)
	 */
	public A[] darArcos()
	{
		return (A[]) infoVertex;
	}
	
	/**
	 * agrega un arco a la lista de adyacencia
	 * @param pPeso Peso del nuevo Arco a crear
	 * @param pLlave Id del vertice asociado al vertive principal
	 */
	public void agregarArco(A pPeso,K pLlave)
	{
		double factorActual= cargaInfoVertex/infoVertex.length;
		int hashCalculado = pLlave.hashCode()& 0x7fffffff %infoVertex.length; 
		
		if(factorActual<0.75)
		{
			if(infoVertex[hashCalculado]!=null)//no es requerido un else ya que los id son unicos
			{
				infoVertex[hashCalculado]=new Arco(pPeso,pLlave);
				cargaInfoVertex++;
			}
		}
		else
		{
			reHash();
			agregarArco( pPeso, pLlave);
		}
	}
	
	/**
	 * Actualiza la posicion de todos los elementos del hashTable segun su llave y el nuevo tamano
	 */
	public void	reHash()
	{
		//CAMBIAR
		Arco[] copiaHash= infoVertex; //se crea un copia con los arcos actuales
		infoVertex = new Arco[infoVertex.length*2];//se aumenta la tabla del HashTableLinear

		for ( int i = 0; i < copiaHash.length; i++)//se obtienen los nuevos indices 
		{
			if(copiaHash[i]!=null)
			{
				agregarArco((A)copiaHash[i].darPeso(),(K)copiaHash[i].darAdyacente());
			}
		} 
	}
	
	/**
	 * dado un id de un vertice conectado retorna la informacion asociada al mismo
	 * @param pId Id del vertice asociado al arco, el pId DEBE existir, en el caso contrario el metodo retorna nulo
	 */
	public A darArco(K pId)
	{
		int hashCalculado = pId.hashCode()& 0x7fffffff %infoVertex.length;
		A pArco = (A) infoVertex[hashCalculado];
		
		return pArco;
	}
	
	/**
	 * Modificar la informacion del arco con el vertice idVertexFin
	 */
	public void setInfoArc(K idVertexFin,
			A infoArc)
	{
		int hashCalculado = idVertexFin.hashCode()& 0x7fffffff %infoVertex.length;
		infoVertex[hashCalculado].setInfoArc(infoArc);;
		
	}
	public void setInfraccion(VOMovingViolations a) {
		infracciones.push(a);
	}
}
