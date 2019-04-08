package model.data_structures;

public interface IArregloDinamico<K extends Comparable<K>,V> {

	/**
	 * Retornar el numero de elementos en el arreglo
	 * @return tamano del arreglo
	 */
	int darTamano( );
	
	/**
         * retorna la ultima dupla del arreglo
	 * @return ultimo dupla del arreglo
         */
	Dupla darUltimo();
	
	
	/**
	 * Retornar la dupla en la posicion i
	 * @param i posicion de consulta
	 * @return dupla de consulta. null si no hay dupla en posicion.
	 */
	Dupla darElemento( int i );

	/**
	 * Agregar un dato de forma compacta (en la primera casilla disponible) 
	 * Caso Especial: Si el arreglo esta lleno debe aumentarse su capacidad, agregar el nuevo dato y deben quedar multiples casillas disponibles para futuros nuevos datos.
	 * @param dato nuevo elemento
	 */
	public void agregar( Dupla dato );
		
	/**
	 * Buscar un dato en el arreglo.
	 * @param dato Objeto de busqueda en el arreglo
	 * @return elemento encontrado en el arreglo (si existe). null si no se encontro el dato.
	 */
	Dupla buscar(K dato);
	
	/**
	 * Eliminar una dupla del arreglo.
	 * Los datos restantes deben quedar "compactos" desde la posicion 0.
	 * @param dato Objeto de eliminacion en el arreglo
	 * @return dato eliminado
	 */
	Dupla eliminar( Dupla dato );

}