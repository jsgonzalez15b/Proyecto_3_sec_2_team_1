package model.data_structures;

/*
 * Clase que representa el arco, almacena el id V generico del vertice adyacente y su peso A generico
 */
public class Arco<K extends Comparable<K>,A extends Comparable<A>>
{

	//Atributos
	/**
	 * Id generico del segundo vertice
	 */
	private K llaveId;

	/**
	 * Peso del arco
	 */
	private A pesoArco;
	
	//Constructor
	/**
	 * @param pPeso peso del arco
	 * @param pLlave llave id del vertice adyacente
	 */
	public Arco (A pPeso, K pLlave)
	{
		//se inicializan los atributos del arco
		llaveId=pLlave;
		pesoArco=pPeso;
	}
	//Metodos
	/**
	 * Retorna el Id del vertice adyacente
	 */
	public K darAdyacente()
	{
		return llaveId;
	}
	
	/**
	 * Retorna el Peso del arco de tipo A
	 */
	public A darPeso()
	{
		return pesoArco;
	}
	
	/**
	 * Modificar la informacion del arco con el vertice idVertexFin
	 */
	public void setInfoArc(A infoArc)
	{
		pesoArco=infoArc;
	}
}
