package model.data_structures;

/** Clase que permite el almacenamiento de objetos genéricos en colas y filas
 *  Implementa los métodos necesarios para ambas estructuras de datos ()
 */
public class Nodo<T> 
{
	//Atributos

	/**
	 * objeto genérico almacenado por el nodo. 
	 */
	private T elemento;

	/**
	 * Relación de cola/pila en lista simplemente encadenada
	 */
	private Nodo<T> siguiente;
	
	/**
	 * Relación de cola/pila en lista doblemente encadenada
	 */
	private Nodo<T> anterior;
	
	//Método Constructor
	public Nodo (T pElemento)
	{
		//Para construir un nodo es necesario conocer el elemento de entrada, si es primero o ultimo.
		elemento= pElemento;
		siguiente = null; //El nodo siguiente no es contruido pero debe ser inmediatamente asignado
		anterior = null; //El nodo siguiente no es contruido pero debe ser inmediatamente asignado
	}

	//Métodos del nodo

	/**
	 * método encargado de reasignar el nodo siguiente. 
	 */
	public void setSiguiente(Nodo<T> nuevoSiguiente)
	{
		siguiente =nuevoSiguiente;
	}
	
	/**
	 * método encargado de reasignar el nodo siguiente. 
	 */
	public void setAnterior(Nodo<T> nuevoAnterior)
	{
		anterior =nuevoAnterior;
	}
	
	/**
	 * método encargado de retornar el elemento almacenado en el nodo. 
	 */
	public T darElemento()
	{
		return elemento;
	}

	/**
	 * método que retorna el siguiente nodo. 
	 */
	public Nodo<T> darSiguiente()
	{
		return siguiente;
	}
	
	/**
	 * método que retorna el nodo anterior. 
	 */
	public Nodo<T> darAnterior()
	{
		return anterior;
	}
	
}

	
	
