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
		
		Nodo<T> nuevoultimo= new Nodo<T>(pElemento);
		if(primero==null){
			primero=nuevoultimo; 
			ultimo=nuevoultimo; 
		}else{
			ultimo.setSiguiente(nuevoultimo);
			ultimo=nuevoultimo; 
		}
		cantidad++;
	}

	/**
	 * mÃ©todo para dequeue el primer elemento. La lista tiene al menos un elemento 
	 *@return elemento retorna el elemento T del nodo eliminado.
	 */
	public T dequeue()
	{
		if(primero.darSiguiente()==null){
			T elemento=primero.darElemento(); 
			primero=null; 
			ultimo=null; 
			return elemento; 
		}else{
			T elemento=primero.darElemento(); 
			primero=primero.darSiguiente(); 
			return elemento; 
		}
	}

	public Iterador<T> iterator() {
		return new Iterador<T>(primero);
	}



}

