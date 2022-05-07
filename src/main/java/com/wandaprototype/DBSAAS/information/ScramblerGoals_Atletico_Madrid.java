package com.wandaprototype.DBSAAS.information;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

public class ScramblerGoals_Atletico_Madrid {

	public static List<Object> matchsGoals = new ArrayList<>();
	// Lista donde clasificaremos los datos brutos de la lista partidos.
	List<List<Object>> lists = Lists.partition(matchsGoals, 2);
	
	public void ExtractDataFromURL() {
		Document doc;
		Elements links = null;

		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/calendario-completo-primer-equipo/").get();
			//File input = new File("C:/Users/MRX-Workstation/Downloads/web2019.html"); //Funciona con archivo local de 2020.
			//doc = Jsoup.parse(input, "UTF-8", "http://example.com/"); //Funciona con archivo local de 2020.
			
			//links = doc.select("div.header-calendar > div.competition > img , div.header-calendar > div.info-calendario > span");
			links = doc.select("ul.marcador > li.local > dl > dd.marcador[title], ul.marcador > li.visitante > dl > dd.marcador");
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la página web. Revise las secciones y estructura web");
		}

		for (Element e : links) {
			matchsGoals.add(e.text());
		}
	}
	
	public void ClasifDatos() {
		lists = Lists.partition(matchsGoals, 2); 
	}
	
	public void LecturaDatosParticionados() {
		// :: Lectura ::
		System.out.println();
		for (List<Object> list : lists) {
			System.out.println(list);
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args) {
		new ScramblerGoals_Atletico_Madrid().ExtractDataFromURL();
		new ScramblerGoals_Atletico_Madrid().ClasifDatos();
		new ScramblerGoals_Atletico_Madrid().LecturaDatosParticionados();
	}
}
