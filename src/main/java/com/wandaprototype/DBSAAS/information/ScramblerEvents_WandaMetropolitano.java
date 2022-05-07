package com.wandaprototype.DBSAAS.information;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;

public class ScramblerEvents_WandaMetropolitano {
	public static List<Object> eventos = new ArrayList<>();
	// Lista donde clasificaremos los datos brutos de la lista partidos.
	List<List<Object>> lists = Lists.partition(eventos, 3);

	public void ExtractDataFromURL() {
		Document doc, doc2;
		Elements links = null, links2 = null;

		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/noticias-nuevo-estadio").get();
			// File input = new File("C:/Users/MRX-Workstation/Downloads/web2019.html");
			// //Funciona con archivo local de 2020.
			// doc = Jsoup.parse(input, "UTF-8", "http://example.com/"); //Funciona con
			// archivo local de 2020.

			// links = doc.select("div.header-calendar > div.competition > img ,
			// div.header-calendar > div.info-calendario > span");
			// links = doc.select("article.noticia > a[href] > span.picture[data-alt]");
			// links = doc.getElementsByClass("picture");
			// links = doc.getElementsByClass("picture");
			links = doc.select("article.noticia > time[datetime], article.noticia > a[href]");
			//links = doc.select("span.picture, article:matches((noticia)noticia) > time[datetime], article.noticia > a[href]");
			// links2 = doc.select("article.noticia > time[datetime], article.noticia >
			// a[href]");
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la página web. Revise las secciones y estructura web");
		}

		String aux_url_1 = "";
		for (Element e : links) {

			if (!e.cssSelector().contains("article.noticia.destacada")) {
				if (e.hasAttr("data-alt")) {
					eventos.add(e.attr("data-alt").toString());
				}
				
				if (!e.attributes().get("href").isEmpty()) {
					if (!aux_url_1.equals(e.attributes().get("href"))) {
						aux_url_1 = e.attributes().get("href").toString();
						//System.out.println("1: "+aux_url_1);
						eventos.add("https://www.atleticodemadrid.com"+e.attributes().get("href"));
						
						//
						try {
							doc2 = Jsoup.connect("https://www.atleticodemadrid.com"+e.attributes().get("href")).get();
							//links2 = doc2.select("span.preheading");
							links2 = doc2.select("article.noticia-interior > p");
							
							for (Element e2 : links2) {
								if (!e2.text().isEmpty()) {
									char[] chars = e2.text().toCharArray();
									StringBuilder sb = new StringBuilder();
									for (char c : chars) {
										if (Character.isDigit(c)) {
											sb.append(c);
										}
									}								
									
									//noticia-interior
									//Condición de fecha en título.
									//String regex = "\\d+\\s+\\w+\\s+\\w+";
									String regex = "\\d+\\s+\\w{2}\\s+\\w+";
									Matcher matcher = Pattern.compile(regex).matcher(e2.text().toString());
									if (matcher.find()) {
										if (!matcher.group().contains("vs")) {
											System.out.println("Patrón: "+matcher.group());
										}
									  
									} else {
										//System.out.println("2º: " + e2.text());
									}
									

								}
							}
							
						} catch (Exception ex2) {
							ex2.printStackTrace();
						}
						
					}
					
					
				}

				if (!e.text().isEmpty()) {
					eventos.add(e.text());
				}
			}
		}
	

	}

	public void ClasifDatos() {
		lists = Lists.partition(eventos, 3);
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
		new ScramblerEvents_WandaMetropolitano().ExtractDataFromURL();
		new ScramblerEvents_WandaMetropolitano().ClasifDatos();
		new ScramblerEvents_WandaMetropolitano().LecturaDatosParticionados();
	}
}
