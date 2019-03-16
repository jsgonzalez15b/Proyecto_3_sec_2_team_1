package model.data_structures;

import java.util.Iterator;

public class Stack<T> implements IStack<T>{

	private Nodo <T> primero; 
	private int tamano;



	public Stack(){
		primero=null;
		tamano=0;  
	}
	/**
	 * Retorna true si la Pila esta vacia
	 * @return true si la Pila esta vacia, false de lo contrario
	 */
	public boolean isEmpty(){
		return primero==null?true:false;
	}


	/**
	 * Retorna el numero de elementos contenidos
	 * @return el numero de elemntos contenidos
	 */
	public int size(){
		return tamano;
	}


	/**
	 * Quita y retorna el elemento agregado más recientemente
	 * @return el elemento agregado más recientemente
	 */
	public T pop() {
		T retornar=null;
		if(primero==null){
		System.out.println("La pila esta vacìa, no se puede eliminar nigùn elemento");

		}else if(primero.darSiguiente()==null){
			retornar=primero.darElemento();
			primero=null;
		}else{
			retornar=primero.darElemento();
			primero=primero.darSiguiente();

		}
		tamano--;
		return retornar; 
	}



	/**
	 * Inserta un nuevo elemento en la Pila
	 * @param t el nuevo elemento que se va ha agregar
	 */
	public void push(T t){
		Nodo<T> aAgregar= new Nodo<T> (t);
		if(isEmpty()){
			primero=aAgregar; 
		}else {
			aAgregar.setSiguiente(primero);
			primero=aAgregar;
		}
		tamano++; 
	}


	public Iterator<T> iterator() {
		return new Iterador<T>(primero);
	}
	
	public Nodo<T> darPrimero(){
		return primero;
	}
}
