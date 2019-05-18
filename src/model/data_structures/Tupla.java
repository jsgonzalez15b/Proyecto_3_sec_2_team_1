package model.data_structures;

public class Tupla <K,V extends Comparable<V>>{

		private K k; 
		private V v; 
		
		public Tupla(K pk, V pv) {
			k=pk; 
			v=pv; 
		}
		public K darllave() {
			return k; 
		}
		public V darValor() {
			return v; 
		}
		public void setLlave(K a) {
			k=a; 
		}
		public void setValor(V b) {
			v=b; 
		}


}
