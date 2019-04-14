package model.data_structures;

public class HashTableChaining <K extends Comparable<K>,V> implements IHashTableChaining<K,V>
{
	//Atributos
	/**
	 * Arreglo que almacena la informacion del Hash implementado para variar tamaño (NO ARREGLO DINAMICO)
	 */
	private Dupla[] hashTable;

	/**
	 * Numero de indices en uso
	 */
	private int nCarga;

	/**
	 * Factor de carga maximo
	 */
	private double factorCarga;

	//Constructor
	public HashTableChaining()
	{
		hashTable= new Dupla[10];
		factorCarga=0.5;
	}

	//Métodos
	/**
	 * Calcula el hash asociado a la llave
	 * @return.
	 */
	public int	hash(K pLlave)
	{
		return pLlave.hashCode()& 0x7fffffff %nCarga; 
	}

	/**
	 * Actualiza la posicion de todos los elementos del hashTable segun su llave y el nuevo tamano
	 */
	public void	reHash()
	{
		//CAMBIAR
		Dupla[] copiaHash= hashTable; //se crea un copia con las duplas actuales
		hashTable = new Dupla[hashTable.length*2];//se aumenta la tabla del HashTableLinear

		for ( int i = 0; i < copiaHash.length; i++)//se obtienen los nuevos indices 
		{
			if(copiaHash[i]!=null)
			{
				put((K)copiaHash[i].getKey(),(V)copiaHash[i].getValue());
			}
		} 
	}

	/**
	 * Agregar una dupla (K,V) a la tabla. Si la llave K existe reemplaza su valor V asociado
	 * V no puede ser null.
	 */
	public void	put(K pLlave, V pValor)
	{
		int pIndice= hash(pLlave);//calculo indice de la dupla segun tamano actual

		//booleanos para indicar el estado de insercion de la dupla para linear colision
		boolean insertado = false;
		boolean colision = false;

		while(!insertado)//se recorre la tabla hasta insertar el elemento
		{
			if(hashTable[pIndice]==null)//cuando se encuente una dupla vacia esta es inicializada con la nueva dupla
			{
				hashTable[pIndice]= new Dupla(pLlave,pValor);
				insertado=true; //se actualiza estado de insercion
			}
			else
			{
				colision = true; //ya que la dupla esta ocupada ocurrio una colision
				hashTable[pIndice].add(pLlave,pValor);//se agrega a la dupla en esta posicion
				insertado=true; //se actualiza estado de insercion
			}
		}

		if(!colision) //si no hubo colision se actualiza el numero de carga en uso
		{
			nCarga++;
			if(nCarga/hashTable.length>=factorCarga)
			{
				reHash();
			}
		}
	}
	/**
	 * Obtener el valor V asociada a la llave K. V no puede ser null.
	 */
	public V get(K pLlave)
	{
		int indice = hash(pLlave);
		V elValueBuscado = null;
		if(hashTable[indice]!=null) //necesariamente esta condicion debe cumplirse
		{
			for(int i=0;i<hashTable[indice].chain.darTamano();i++)//busqueda en arreglo de duplas
			{
				if(hashTable[indice].chain.darElemento(i)!=null)
				{
					if(hashTable[indice].chain.darElemento(i).getKey().compareTo(pLlave)==0)
					{
						elValueBuscado = (V) hashTable[indice].chain.darElemento(i).getValue();
					}
				}
			}
			elValueBuscado = (V) hashTable[indice].getValue();
		}
		return elValueBuscado;
	}
	/**
	 * Borrar la dupla asociada a la llave K. Se obtiene el valor V asociado a la llave K.
	 * Se obtiene null si la llave K no existe.
	 */
	public V	delete(K pLlave)
	{
		int indice = hash(pLlave);
		V elValueBuscado = null;
		if(hashTable[indice]!=null) //necesariamente esta condicion debe cumplirse
		{
			for(int i=0;i<hashTable[indice].chain.darTamano();i++)//busqueda en arreglo de duplas
			{
				if(hashTable[indice].chain.darElemento(i)!=null)
				{
					if(hashTable[indice].chain.darElemento(i).getKey().compareTo(pLlave)==0)
					{
						elValueBuscado = (V) hashTable[indice].chain.darElemento(i).getValue();
						hashTable[indice].chain.eliminar(hashTable[indice].chain.darElemento(i)); //elimina la duplaBuscada
					}
				}
			}
		}
		return elValueBuscado;
	}
	/**
	 * Conjunto de llaves K presentes en la tabla.
	 */
	public Iterador<K>	keys()
	{
		boolean encontrada= false;
		boolean primeraEncontrada=false;
		int i = 0;
		Nodo<K> firstKey = null;
		Nodo<K> keyActual = null;
		Iterador<K> llavero = null;

		while(!encontrada && i<hashTable.length)//busqueda de primera y segunda dupla en HashTableLinear
		{
			if(hashTable[i]==null)
			{
				i++;
			}
			else if(!primeraEncontrada)
			{
				firstKey = new Nodo( hashTable[i].getKey()); //creacion del primer nodo key
				primeraEncontrada=true;
			}
			else
			{
				keyActual = new Nodo( hashTable[i].getKey()); //creacion del segundo nodo key
				firstKey.setSiguiente(keyActual);
				encontrada=true;
			}
		}
		if(encontrada) 
		{
			for(int j=0;j<hashTable.length;j++)//creacion de una lista de nodos simplemente encadenada
			{
				if(hashTable[i]!=null)
				{
					keyActual.setSiguiente(new Nodo(hashTable[i].getKey()));
					keyActual=keyActual.darSiguiente();
				}
			}
		}

		llavero = new Iterador<>(firstKey);
		return llavero;
	}

}
