package model.data_structures;

import java.util.NoSuchElementException;

import javax.swing.text.html.HTMLDocument.Iterator;

public class Iterador <K> implements java.util.Iterator<K>{

	Nodo<K> siguiente;
	public Iterador(Nodo<K> pPrimero) {
		siguiente=pPrimero;
	}
	public boolean hasNext() {
		return siguiente!=null?true:false;
	}

	
	public K next() {
		if(siguiente==null) {
			throw new NoSuchElementException("No hay siguiente");
		}else {
			K elemento=siguiente.darElemento();
			siguiente=siguiente.darSiguiente();
			return elemento; 
		}
	}


}
