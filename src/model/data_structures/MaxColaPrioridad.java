package model.data_structures;

import java.util.Iterator;

public class MaxColaPrioridad <T extends Comparable<T>> implements IMaxColaPrioridad<T>{

	private int tamano;

	public Nodo<T> primero; 

	public Nodo<T> ultimo; 	
	public MaxColaPrioridad() {
		tamano= 0;
		primero=null; 
		ultimo=null; 
	}

	public int darNumElementos() {
		return tamano; 
	}

	public void agregar(T elemento) {

		Nodo<T>agregar=new Nodo<T>(elemento);

		if(primero==null) {
			primero=agregar; 
			ultimo=agregar; 
		}
		else if (agregar.darElemento().compareTo(primero.darElemento())>0) {
			agregar.setSiguiente(primero);
			primero=agregar; 
		}
		else if(agregar.darElemento().compareTo(ultimo.darElemento())<0) {
			ultimo.setSiguiente(agregar);
			ultimo=agregar; 
		}
		else {
			boolean agregado=false; 
			for(Nodo<T> p=primero;!agregado&&p.darSiguiente()!=null;p=p.darSiguiente()) {
				if(agregar.darElemento().compareTo(p.darSiguiente().darElemento())>0) {
					agregar.setSiguiente(p.darSiguiente());
					p.setSiguiente(agregar);
					agregado=true;  
				}				
			}
		}
		tamano++;
	}

	public T delMax() {

		if(esVacia()) {
			return null; 
		}
		else {
			T retornar=primero.darElemento();
			primero=primero.darSiguiente();
			if(primero==null) {
				ultimo=null; 
			}
			tamano--; 
			return retornar; 
		}

	}

	public T max() {
		if(primero!=null) {
			return primero.darElemento();
		}
		else { 
			return null; 
		}
	}

	public boolean esVacia() {
		return primero==null?true:false; 
	}
	
	public Iterator<T> iterator() {
		return new Iterador<T>(primero);
	}
}