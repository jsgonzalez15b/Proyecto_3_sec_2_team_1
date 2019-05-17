package mapa;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;

import javafx.scene.layout.Border;
import model.data_structures.Arco;
import model.data_structures.Grafo;
import model.data_structures.Stack;
import model.data_structures.Vertice;
import model.vo.verticeInfo;
import view.MovingViolationsManagerView;
public class Mapa  extends MapView{

	private Map mapa; 
	private Grafo<verticeInfo, Long, Double> grafo;
	private MovingViolationsManagerView view; 
	public Mapa (Grafo<verticeInfo, Long, Double> mostrar, int num,Stack<Vertice<verticeInfo, Long, Double>> vertices){
		grafo=mostrar; 
		view=new MovingViolationsManagerView();
		JFrame ventana= new JFrame("Mapa"); 
		setOnMapReadyHandler(new MapReadyHandler() {
			
			@Override
			public void onMapReady(MapStatus arg0) {
				if(arg0==MapStatus.MAP_STATUS_OK){
					mapa=getMap(); 
					MapOptions opciones= new MapOptions(); 
					MapTypeControlOptions control= new MapTypeControlOptions(); 
					opciones.setMapTypeControlOptions(control);
					mapa.setOptions(opciones);
					mapa.setZoom(10.0);
					mapa.setCenter(new LatLng(38.8991, -77.0259));
					if(num==1) {
					pintarGrafo(mostrar);
					}
					else {
						pintarGrafoporVertices(vertices); 
					}
				}
				
			}

		});
		ventana.add(BorderLayout.CENTER,this); 
		ventana.setSize(700,600);
		ventana.setVisible(true);
		
	}
	public void pintarGrafo(Grafo<verticeInfo,Long, Double> mostrar){
		Stack<Arco<Long, Double>> arcos=(Stack<Arco<Long,Double>>) mostrar.darArcosGrafo();
		int i=0; 
		while (i<arcos.size()){
			Arco<Long, Double> arco=arcos.pop(); 
			verticeInfo primero=grafo.getInfoVertex(arco.darInicio()); 
			verticeInfo segundo=grafo.getInfoVertex(arco.darAdyacente()); 
			
			LatLng[] datos= new LatLng[2]; 
			datos[0]=new LatLng(primero.darLatitud(), primero.darlongitud()); 
			datos [1]=new LatLng(segundo.darLatitud(), segundo.darlongitud()); 
			
			Circle vertice1=new Circle(mapa); 
			vertice1.setCenter(datos[0]);
			vertice1.setRadius(0.4);
			CircleOptions opciones= new CircleOptions(); 
			opciones.setFillColor("#FF0000");
			vertice1.setOptions(opciones);
			vertice1.setVisible(true);
			
			Circle vertice2=new Circle(mapa); 
			vertice2.setCenter(datos[1]);
			vertice1.setRadius(0.4);
			CircleOptions opciones2= new CircleOptions(); 
			opciones2.setFillColor("#FF0000");
			vertice2.setOptions(opciones2);
			vertice2.setVisible(true);
			
			Polygon w=new Polygon(mapa); 
			w.setPath(datos);
			w.setVisible(true);
			i++	; 	
		}
	}
	private void pintarGrafoporVertices(Stack<Vertice<verticeInfo, Long, Double>> vertices) { 
		while(!vertices.isEmpty()) {
			Vertice<verticeInfo, Long, Double> vertice=vertices.pop(); 
			verticeInfo info=vertice.darValor(); 
			LatLng[] datos=new LatLng[1]; 
			datos[0]=new LatLng(info.darLatitud(), info.darlongitud()); 
			Circle circulo=new Circle(mapa); 
			circulo.setCenter(datos[0]);
			circulo.setRadius(0.4);
			CircleOptions opciones= new CircleOptions(); 
			opciones.setFillColor("#FF0000");
			circulo.setOptions(opciones);
			circulo.setVisible(true);
		}
		
	}
}
