package model.data_structures;

public class ArregloDinamico<K extends Comparable<K>,V> implements IArregloDinamico<K,V>  
{
	/**
	 * Capacidad maxima del arreglo
	 */
        private int tamanoMax;
	/**
	 * Numero de elementos en el arreglo (de forma compacta desde la posicion 0)
	 */
        private int tamanoAct;
	
	/**
	 * ultimo elemento de la lista
	 */
        private Dupla ultimo;
        /**
         * Arreglo de elementos de tamaNo maximo
         */
        private Dupla elementos[ ];

        /**
         * Construir un arreglo con la capacidad maxima inicial.
         * @param max Capacidad maxima inicial
         */
		public ArregloDinamico( int max )
        {
               elementos = (Dupla[]) new Object[max];
               tamanoMax = max;
               tamanoAct = 0;
               ultimo = null;
        }
	
	/**
         * retorna el ultimo elemento del arreglo
         */
		public Dupla darUltimo()
        {
               return ultimo;
        }
        
		public void agregar( Dupla dato )
        {
               if ( tamanoAct == tamanoMax )
               {  // caso de arreglo lleno (aumentar tamaNo)
                    tamanoMax = 2 * tamanoMax;
                    Dupla [ ] copia = elementos;
                    elementos = (Dupla[])new Object[tamanoMax];
                    for ( int i = 0; i < tamanoAct; i++)
                    {
                     	 elementos[i] = copia[i];
                    } 
            	    System.out.println("Arreglo lleno: " + tamanoAct + " - Arreglo duplicado: " + tamanoMax);
               }	
               elementos[tamanoAct] = dato;
               ultimo = dato; //se actualiza el ultimo elemento
               tamanoAct++;
       }

		public int darTamano() 
		{
			return tamanoAct;
		}

		public Dupla darElemento(int i)
		{
			Dupla buscado=null;   
			for(int j=0; j<tamanoAct && buscado==null;j++) {
				if(j==i) {
					buscado=elementos[j];
				}
			}
			return buscado;
		}

		public Dupla buscar(K dato) {
			// TODO implementar
			// Recomendacion: Usar el criterio de comparacion natural (metodo compareTo()) definido en Strings.
			int inicio=0;
			int fin=elementos.length-1;
			Dupla buscado=null; 
			while (inicio<=fin && buscado==null) {
				int mitad=(inicio+fin)/2;
				if(dato.compareTo((K) elementos[mitad].getKey())==0){
					buscado=elementos[mitad];
				}else if(dato.compareTo((K) elementos[mitad].getKey())>0) {
					inicio=mitad+1;
				}else {
					fin=mitad-1;
				}
			}
			return buscado; 
		}

		public Dupla eliminar(Dupla dato) {
			// TODO implementar
			// Recomendacion: Usar el criterio de comparacion natural (metodo compareTo()) definido en Strings.
			Dupla aEliminar=null;
			for (int i=0; i<elementos.length; i++) {
				if(elementos[i].compareTo(dato)==0) {
					aEliminar=elementos[i];
					for(int j=i; j<elementos.length-1; j++) {
						elementos[j]=elementos[j+1];
					}
				}
			}
			tamanoAct--;
			ultimo= elementos[elementos.length-1]; //se actualiza el ultimo elemento
			return aEliminar;
		}




}

