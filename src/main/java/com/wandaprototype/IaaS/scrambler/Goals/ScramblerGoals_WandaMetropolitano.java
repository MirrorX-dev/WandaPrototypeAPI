package com.wandaprototype.IaaS.scrambler.Goals;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

/**
 * Obtiene todos los resultados de los partidos en directo.
 * Actualmente no ha sido implementado debido a finalización
 * del proyecto. Pero su uso y funcionamiento está garantizado.
 * @author MirrorX
 *
 */
public class ScramblerGoals_WandaMetropolitano {

	public static List<String> matchsGoals = new ArrayList<>();
	// Lista donde clasificaremos los datos brutos de la lista partidos.
	List<List<String>> lists = Lists.partition(matchsGoals, 2);
	
	/**
	 * Función extractora de la información alojada en la web.
	 * Conecta a la web del recurso indicado. Sugiere un Timeout de 10 * 1000 para realizar
	 * las operaciones get del documento HTML.
	 */
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
	
	/**
	 * Permite particionar los datos en subjuntos de listados
	 * para poder acceder a la información organizada.
	 */
	public void ClasifDatos() {
		lists = Lists.partition(matchsGoals, 2); 
	}
	
	/**
	 * Realiza una lectura de los datos pasados
	 * a lista particionados.
	 */
	public void LecturaDatosParticionados() {
		// :: Lectura ::
		System.out.println();
		for (List<String> list : lists) {
			System.out.println(list);
		}
		System.out.println("\n");
	}
	
	public static void main(String[] args) {
		new ScramblerGoals_WandaMetropolitano().ExtractDataFromURL();
		new ScramblerGoals_WandaMetropolitano().ClasifDatos();
		new ScramblerGoals_WandaMetropolitano().LecturaDatosParticionados();
	}
}
