package mapa;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.swing.MapView;

import javafx.scene.layout.Border;
import model.data_structures.Arco;
import model.data_structures.Grafo;
import model.data_structures.IStack;
import model.vo.verticeInfo;
public class Mapa  extends MapView{

	private Map mapa; 
	private IStack<Arco<Integer, Double>> arcos; 
	public Mapa (IStack<Arco<Integer, Double>>mostrar){
		arcos=mostrar; 
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
					
					pintarGrafo(mostrar); 
				}
				
			}

		});
		ventana.add(BorderLayout.CENTER,this); 
		ventana.setSize(700,600);
		ventana.setVisible(true);
		
	}
	public void pintarGrafo(IStack<Arco<Integer, Double>> mostrar){
		
	}
}
