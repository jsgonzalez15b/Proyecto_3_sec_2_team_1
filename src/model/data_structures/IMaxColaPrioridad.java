package model.data_structures;

public interface IMaxColaPrioridad<T extends Comparable<T>> {

	/**
	 * Retornar el numero de elementos en el arreglo
	 * @return
	 */
	int darNumElementos( );

	/**
	 * Agregar un dato de forma compacta (en la primera casilla disponible) 
	 * Caso Especial: Si el arreglo esta lleno debe aumentarse su capacidad, agregar el nuevo dato y deben quedar multiples casillas disponibles para futuros nuevos datos.
	 * @param dato nuevo elemento
	 */
	public void agregar( T dato );
	
	/**
	 *Elimina el elemento máximo de la cola de prioridad
	 * @return dato eliminado
	 */
	T delMax();
	
	/**
	 * Determina si la cola esta vacia
	 * @return true si la cola esta vacia, false de lo contrario
	 */
	boolean esVacia();
	
	/**
	 * Retorna el elemento máximo de la cola sin eliminarlo de la cola
	 * @return Elemnto máximo de la cola
	 */
	T max();
}