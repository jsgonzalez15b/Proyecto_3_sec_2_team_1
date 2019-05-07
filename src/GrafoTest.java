import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import model.data_structures.Arco;
import model.data_structures.Grafo;
import model.vo.*;
public class GrafoTest 
{
	// -----------------------------------------------------------------
  	 // Atributos
  	 // -----------------------------------------------------------------

	//grafos
   	/**
   	 * Grafo de prueba 1
   	 */
   	private Grafo<String,VOMovingViolations,Integer> graph1;

   	/**
   	 * Grafo de prueba 2
   	 */
   	private Grafo<String,VOMovingViolations,Integer> graph2;

	/**
   	 * Grafo de prueba 3
   	 */
   	private Grafo<String,VOMovingViolations,Integer> graph3;
   
   	//infracciones
   	/**
    	* Elemento 1
    	*/
   	private VOMovingViolations pInfraccion1;

   	/**
    	* Elemento 2
    	*/
   	private VOMovingViolations pInfraccion2;

   	/**
    	* Elemento 3
    	*/
   	private VOMovingViolations pInfraccion3;
   
   	/**
    	* Elemento 4
    	*/
   	private VOMovingViolations pInfraccion4;
	
	/**
    	* Elemento 5 
    	*/
   	private VOMovingViolations pInfraccion5;
	
	/**
    	* Elemento 6 
    	*/
   	private VOMovingViolations pInfraccion6;
	
	/**
    	* Elemento 7 
    	*/
   	private VOMovingViolations pInfraccion7;
	
	/**
    	* Elemento 8 
    	*/
   	private VOMovingViolations pInfraccion8;

   	//arcos
   	/**
	* Elemento 1
	*/
	private Arco<Integer,Double> pArco1;

	/**
	* Elemento 2
	*/
	private Arco<Integer,Double> pArco2;

	/**
	* Elemento 3
	*/
	private Arco<Integer,Double> pArco3;

	/**
	* Elemento 4
	*/
	private Arco<Integer,Double> pArco4;

/**
	* Elemento 5 
	*/
	private Arco<Integer,Double> pArco5;

/**
	* Elemento 6 
	*/
	private Arco<Integer,Double> pArco6;

/**
	* Elemento 7 
	*/
	private Arco<Integer,Double> pArco7;

/**
	* Elemento 8 
	*/
	private Arco<Integer,Double> pArco8;
	
   // -----------------------------------------------------------------
   // Metodos
   // -----------------------------------------------------------------

  
	@Before
	public void setUp() throws Exception
	{
		System.out.println("Codigo de configuracion de muestra de datos a probar");
		//Inicializacion de infracciones
		System.out.println("Codigo de iniciacion");
		pInfraccion1 = new VOMovingViolations(1, "Bogota", "2018-02-13", 0, "123000", "Licence", "0001", 1453.2,456789,0,0,123321,248359,225860);
		pInfraccion2 = new VOMovingViolations(2, "Bogota", "2018-02-15", 0, "123001", "Drunk", "0002", 1450.2,753159,100,0,321123,357157,971359);
		pInfraccion3 = new VOMovingViolations(3, "Bogota", "2018-02-17", 0, "123002", "Asshole", "9999", 1451.2,951753,500,0,147741,157953,456325);
		pInfraccion4 = new VOMovingViolations(4, "Bogota", "2018-02-23", 0, "123003", "Speed", "0007", 1451.2,123987,0,0,258852,423000,123123);
		pInfraccion5 = new VOMovingViolations(5, "Bogota", "2018-02-23", 0, "123004", "RedLight", "0010", 1455.2,741963,50,0,369963,121212,134679);
		pInfraccion6 = new VOMovingViolations(6, "Bogota", "2018-02-23", 0, "123005", "Cellphone", "0100", 1432.2,369147,50,0,159951,323232,392817);
		pInfraccion7 = new VOMovingViolations(7, "Bogota", "2018-02-25", 0, "123006", "RedLight", "0010", 1423.2,842684,50,0,357753,232323,283956);
		pInfraccion8 = new VOMovingViolations(8, "Bogota", "2018-02-26", 0, "123007", "Glasses", "0083", 1550.2,862486,0,0,456789,268422,452585);
		
		
		//grafo1 para testeo de un solo vertice
		graph1.addVertex("355657020202,-74043452344", pInfraccion1);
		
		//se agregan multiples arcos
	}
	
	@Test
	public void testUnVertice() 
	{
		
	}
	
	@Test
	public void testVarioVertices() 
	{
		
	}
	
}
