package model.data_structures;

import java.util.Iterator;

/** Clase que permite el almacenamiento de nodos genericos en colas
 */
public class Queue<T> implements IQueue<T>
{
	//Atributos

	/**
	 * Primer nodo para operaciÃ³n
	 */
	public Nodo<T> primero;
	
	/**
	 * Ãšltimo nodo para operaciÃ³n
	 */
	public Nodo<T> ultimo;
	
	/**
	 * tamaÃ±o de la cola
	 */
	public int cantidad;
	
	//MÃ©todo Constructor
	public Queue ()
	{
		//Inicializa el primer y Ãºltimo nodo como vacÃ­o, y la cantidad como 0.
		primero= null;
		ultimo = null;
		cantidad= 0;
	}

	//MÃ©todos del Queue

	/**
	 * Retorna true si la Cola esta vacia
	 * @return true si la Cola esta vacia, false de lo contrario
	 */
	public boolean isEmpty(){
		return primero==null;
	}
	
	/**
	 * Retorna el numero de elementos contenidos
	 * @return el numero de elemntos contenidos
	 */
	public int size(){
		return cantidad;
	}
	
	/**
	 * mÃ©todo para enqueue un nuevo elemento estando en el Ãºltimo nodo. 
	 *@param  nuevoUltimo tiene inicializacion primero= false, ultimo =true
	 */
	public void enqueue(T pElemento)
	{
		
		Nodo<T> nuevoUltimo= new Nodo<T>(pElemento);
		if(primero!=null && ultimo==null)
		{
			ultimo= nuevoUltimo;
			primero.setAnterior(nuevoUltimo);
			primero.setSiguiente(nuevoUltimo);
			ultimo.setSiguiente(primero); //el nodo se asocia a Ã©l mismo para poder operarse
			ultimo.setAnterior(primero);
		}
		else if(primero!=null && ultimo!=null)
		{	
			nuevoUltimo.setSiguiente(primero); //el siguiente nodo al nuevo Ãºltimo es el primero.
			nuevoUltimo.setAnterior(ultimo); //el nodo anterior al nuevo Ãºltimo es el Ãºltimo.
			ultimo.setSiguiente(nuevoUltimo); //se actualiza el siguiente del antiguo Ãºltimo.
			primero.setAnterior(nuevoUltimo); //se actualiza el nodo anterior del primer nodo.
			ultimo = nuevoUltimo; //se actualiza el Ãºltimo nodo. 
		}

		else
		{
			primero = nuevoUltimo;			
		}
		cantidad++;
	}

	/**
	 * mÃ©todo para dequeue el primer elemento. La lista tiene al menos un elemento 
	 *@return elemento retorna el elemento T del nodo eliminado.
	 */
	public T dequeue()
	{

		if(primero!=null) //caso cantidad>1
		{	
			if(primero!=ultimo)
			{
				T elElemento = primero.darElemento(); //se obtiene el elemento del primer nodo.
				ultimo.setSiguiente(primero.darSiguiente()); //se elimina la relaciÃ³n del Ãºltimo con el antiguo primero.
				primero.darSiguiente().setAnterior(ultimo); //se actualiza el nodo anterior del segundo nodo
				primero= primero.darSiguiente(); //se asigna el segundo nodo como nuevo primer nodo.
				return elElemento;
				
			}

			else
			{
				T elElemento = primero.darElemento(); //se obtiene el elemento del primer nodo.
				primero= null;
				ultimo= null;
				return elElemento;
			}
			
			
		}
		cantidad--;
		return null;
	}

	public Iterador<T> iterator() {
		return new Iterador<T>(primero);
	}



}

