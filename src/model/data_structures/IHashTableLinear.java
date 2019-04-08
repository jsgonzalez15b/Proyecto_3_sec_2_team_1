package model.data_structures;

public interface IHashTableLinear<K extends Comparable<K>,V>
{
	/**
	 * Calcula el hash asociado a la llave
	 * @return.
	*/
	public int	hash(K pLlave);
	
	/**
	 * Actualiza la posicion de todos los elementos del hashTable segun su llave y el nuevo tamano
	*/
	public void	reHash();
	
	/**
	 * Agregar una dupla (K,V) a la tabla. Si la llave K existe reemplaza su valor V asociado
	 * V no puede ser null.
	*/
	public void	put(K pLlave, V pValor);

	/**
	 * Obtener el valor V asociada a la llave K. V no puede ser null.
	*/
	public V	get(K pLlave);

	/**
	 * Borrar la dupla asociada a la llave K. Se obtiene el valor V asociado a la llave K.
	 * Se obtiene null si la llave K no existe.
	*/
	public V	delete(K pLlave);

	/**
	 * Conjunto de llaves K presentes en la tabla.
	*/
	public Iterador<K>	keys();


}