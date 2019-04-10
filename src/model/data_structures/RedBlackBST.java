package model.data_structures;

import model.data_structures.NodoArbol.Color;

public class RedBlackBST <k extends Comparable<k>,T>{

//	public enum Color{
//		ROJO,
//		NEGRO
//	}
	private NodoArbol<k,T> raiz; 
	
	private int elementos;
	
	public RedBlackBST(){
		raiz=null; 
		elementos=0; 
	}
	
	public int size() {
		return elementos; 
	}
	
	public boolean isEmpty() {
		return raiz==null?true:false; 
	}
	
	public T get(k key) {
		T buscado=null; 
		if(!isEmpty()) {
			buscado=raiz.get(key);
		}
		return buscado; 
	}
	
	public int getHeight(k key) {
		if(isEmpty()||!contains(key)) {
			return -1; 
		}else {
			return raiz.getHeight(key);
		}
	}
	
	public int Height() {
		if(isEmpty()) {
			return 0; 
		}else {
			return raiz.Height();
		}
	}
	
	public boolean contains(k key) {
		if(!isEmpty()) {
			return raiz.contains(key); 
		}
		else {
			return false; 
		}
	}
	
	public k min() {
		if(isEmpty()) {
			return null; 
		}else {
			return raiz.min(); 
		}
	}
	
	public k max() {
		if(isEmpty()) {
			return null; 
		}else {
			return raiz.max(); 
		}
	}
	
	public void put(k key, T val) throws Exception { 
		if(isEmpty()) {
			NodoArbol<k,T> agregar= new NodoArbol<>(key, val,Color.NEGRO);
			raiz=agregar;
	
		}else {
			raiz.put(key, val);
		}
	}
}
