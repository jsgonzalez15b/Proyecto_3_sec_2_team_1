package test;
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
	private Grafo<String,VOMovingViolations,Double> graph1;

	/**
	 * Grafo de prueba 2
	 */
	private Grafo<String,VOMovingViolations,Double> graph2;

	/**
	 * Grafo de prueba 3
	 */
	private Grafo<String,VOMovingViolations,Double> graph3;

	//infracciones
	/**
	 * Infraccion 1
	 */
	private VOMovingViolations pInfraccion1;

	/**
	 * Infraccion 2
	 */
	private VOMovingViolations pInfraccion2;

	/**
	 * Infraccion 3
	 */
	private VOMovingViolations pInfraccion3;

	/**
	 * Infraccion 4
	 */
	private VOMovingViolations pInfraccion4;

	/**
	 * Infraccion 5 
	 */
	private VOMovingViolations pInfraccion5;

	/**
	 * Infraccion 6 
	 */
	private VOMovingViolations pInfraccion6;

	/**
	 * Infraccion 7 
	 */
	private VOMovingViolations pInfraccion7;

	/**
	 * Infraccion 8 
	 */
	private VOMovingViolations pInfraccion8;

	//arcos posibles
	/**
	 * Arco 1
	 */
	private Arco<Integer,Double> pArco1;

	/**
	 * Arco 2
	 */
	private Arco<Integer,Double> pArco2;

	/**
	 * Arco 3
	 */
	private Arco<Integer,Double> pArco3;

	/**
	 * Arco 4
	 */
	private Arco<Integer,Double> pArco4;

	/**
	 * Arco 5 
	 */
	private Arco<Integer,Double> pArco5;

	/**
	 * Arco 6 
	 */
	private Arco<Integer,Double> pArco6;

	/**
	 * Arco 7 
	 */
	private Arco<Integer,Double> pArco7;



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

		//grafos y pesos con respecto a vertice 1
		pArco1 = new Arco((double)12,pInfraccion2);
		pArco2 = new Arco((double)13,pInfraccion3);
		pArco3 = new Arco((double)7,pInfraccion4);
		pArco4 = new Arco((double)8,pInfraccion5);
		pArco5 = new Arco((double)5,pInfraccion6);
		pArco6 = new Arco((double)7,pInfraccion7);
		pArco7 = new Arco((double)2,pInfraccion8);

		//grafo1 para testeo de un solo vertice
		graph1.addVertex("355657020202,-74043452344", pInfraccion1);

		//se agregan multiples arcos
		graph1.addEdge("355657020202,-74043452344", "355657020223,-74043452364", pArco2.darPeso());
		graph1.addEdge("355657020202,-74043452344", "355657020224,-74043452387", pArco4.darPeso());
		graph1.addEdge("355657020202,-74043452344", "355657020235,-74043452389", pArco5.darPeso());
		graph1.addEdge("355657020202,-74043452344", "355657020236,-74043452395", pArco6.darPeso());
	}

	@Test
	public void testUnVertice() throws Exception 
	{
		setUp();
		//correcta adicion de arcos al vertice
		assertEquals( "El numero almacenado de arcos no es el esperado en el vertice", 4 ,graph1.findVertice("355657020202,-74043452344").darNArcos() );
		assertEquals( "El numero almacenado de arcos no es el esperado en el grafo", 4 ,graph1.E() );
		
		assertEquals( "El primero arco no es el esperado para el vertice", pArco2.darPeso() ,graph1.getInfoArc("355657020202,-74043452344", "355657020223,-74043452364") );
		assertEquals( "El segundo arco no es el esperado para el vertice", pArco4.darPeso() ,graph1.getInfoArc("355657020202,-74043452344", "355657020224,-74043452387") );
		assertEquals( "El tercer arco no es el esperado para el vertice", pArco5.darPeso() , graph1.getInfoArc("355657020202,-74043452344", "355657020235,-74043452389") );
		assertEquals( "El tercer arco no es el esperado para el vertice", pArco6.darPeso() , graph1.getInfoArc("355657020202,-74043452344", "355657020236,-74043452395"));
		
		//edicion de informacion de arco
		graph1.setInfoArc("355657020202,-74043452344", "355657020223,-74043452364", (double) 15);
		assertEquals( "El primero arco no fue editado correctamente", "15" , graph1.getInfoArc("355657020202,-74043452344", "355657020223,-74043452364") );
		
		//edicion de informacion de vertice
		graph1.setInfoVertex("355657020202,-74043452344", pInfraccion2);;
		assertEquals( "La informacion del vertice no fue correctamente modificada", pInfraccion2 , graph1.findVertice("355657020202,-74043452344") );
	}

	@Test
	public void testVariosVertices() throws Exception 
	{
		setUp();
		//grafo1 para testeo de un solo vertice
		graph2.addVertex("355657020002,-74043452004", pInfraccion3);
		graph2.addVertex("355657020224,-74043452387", pInfraccion4);
		graph2.addVertex("355657020235,-74043452389", pInfraccion5);
		graph2.addVertex("355657020236,-74043452395", pInfraccion6);


	}

}
