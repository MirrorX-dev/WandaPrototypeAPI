package com.wandaprototype.IaaS.scrambler.Events;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.wandaprototype.dataformat.DameDatosFecha;

/**
 * Recopila los eventos que transcurren en el estadio Wanda Metropolitano
 * Esta clase todavia no ha sido finalizada debido a que su resultado no
 * es el adecuado. Funciona, pero no devuelve datos esenciales como
 * fechas y horarios.
 * 
 * Debido a la complejidad que supone manejar y clasificar esta información
 * esta clase no está en funcionamiento. No garantiza un correcto funcionamiento.
 * TODO: Terminar de clasificar informacíón/fuentes.
 * @author MirrorX
 */
public class ScramblerEvents_WandaMetropolitano {
	private static ArrayList<String> eventos = new ArrayList<>();
	// Lista donde clasificaremos los datos brutos de la lista partidos.
	static List<List<String>> lists = Lists.partition(eventos, 3);
	
	int global_year = LocalDate.now().getYear();
	int global_month = LocalDate.now().getMonthValue();
	
	/**
	 * Función extractora de la información alojada en la web.
	 * Conecta a la web del recurso indicado. Sugiere un Timeout de 10 * 1000 para realizar
	 * las operaciones get del documento HTML.
	 */
	public void ExtractDataFromURL(String page) {
		Document doc, doc2;
		Elements links = null, links2 = null;

		try {
			doc = Jsoup.connect(page).userAgent("Mozilla/5.0").timeout(10 * 1000).get();
			links = doc.select("article.noticia > a[href]");
			
			String aux_url_1 = "";
			int i, i2;
			Element tempLink, tempLink2;
			for (i = 0; i < links.size(); i++) {
				// for (Element e : links) {
				tempLink = links.get(i);

				if (!tempLink.cssSelector().contains("article.noticia.destacada")) {
					
					//Añade al array título del evento por la imagen del evento.
					if (tempLink.hasAttr("data-alt")) {
						eventos.add(tempLink.attr("data-alt").toString());
					}

					/**
					 * Obtiene la URL del evento.
					 * Si el dato especifico contiene href y no está vacio lo recoge.
					 * ->Esta condición se limita a valor nulo.
					 * 
					 * si la variable aux no coincide. Recoge la variable anterior para almacenar la url del evento.
					 */
					if (!tempLink.attributes().get("href").isEmpty()) {
						if (!aux_url_1.equals(tempLink.attributes().get("href"))) {
							aux_url_1 = tempLink.attributes().get("href").toString();
							// System.out.println("1: "+aux_url_1);
							eventos.add("https://www.atleticodemadrid.com" + tempLink.attributes().get("href"));

							/**
							 * Se accede al evento para recoger sus fechas y en el caso posible la hora
							 * del evento que transcurrirá.
							 */
							try {
								doc2 = Jsoup.connect("https://www.atleticodemadrid.com" + tempLink.attributes().get("href"))
										.userAgent("Mozilla/5.0").timeout(10 * 1000).get();
								// links2 = doc2.select("span.preheading");
								links2 = doc2.select("span.preheading");

								// for (Element e2 : links2) {
								for (i2 = 0; i2 < links2.size(); i2++) {
									tempLink2 = links2.get(i2);
									if (!tempLink2.text().isEmpty()) {

										// noticia-interior
										// Condición de fecha en título.
										// String regex = "\\d+\\s+\\w+\\s+\\w+";
										String regex = "\\d+\\s+\\w{2}\\s+\\w+";
										Matcher matcher = Pattern.compile(regex).matcher(tempLink2.text().toString());
										if (matcher.find()) {
											if (!matcher.group().contains("vs")) {
												System.out.println("Patrón: " + matcher.group());
												//eventos.add(matcher.group());
												
												String fecha_final, dia = null, mes = null;
												String fechaSinFormato = matcher.group().toString();

												// \d+
												// Separador Día del mes:

												regex = "\\d+";
												matcher = Pattern.compile(regex).matcher(fechaSinFormato);
												if (matcher.find()) {
													System.out.println("Patrón_aux: " + matcher.group());
													dia = matcher.group().toString();

													if (Integer.valueOf(dia) != 0) {
														if (Integer.valueOf(dia) > 9) {
															dia = String.valueOf(Integer.valueOf(dia));
														} else {
															// Poner el "0"
															dia = "0" + String.valueOf(Integer.valueOf(dia));
														}
													}
												}

												// \w+\w{3,} Solo muestra el mes deseado.
												// Separador Mes: => 4 DE JUNIO => JUNIO.

												regex = "\\w+\\w{3,}";
												matcher = Pattern.compile(regex).matcher(fechaSinFormato);
												if (matcher.find()) {
													System.out.println("Patrón_aux: " + matcher.group());
													mes = matcher.group().toString();
													int conversion = DameDatosFecha.suMes(mes);

													if (conversion != 0) {
														if (conversion > 9) {
															mes = String.valueOf(conversion);
														} else {
															// Poner el "0"
															mes = "0" + String.valueOf(conversion);
														}
													}

												}
											
												
												//Para acceder a la hora del evento: Debemos intentar realizar la consulta por Buscador de Google.
												// Buscando similutdes de formato 00:00h ó 00:00 h ó 00:00 ó 00pm sino... Se establece: Concierto: Tarde. Evento: Durante el día.
												

												// fecha_final = String.valueOf(dia+"/"+mes);
												fecha_final = String.valueOf(dia + "/" + mes+ "/");
												eventos.add(fecha_final);
												
												
											}
										} else {
											eventos.add("X");
										}

									}
								}

							} catch (Exception ex2) {
								ex2.printStackTrace();
								System.err.println("Error al extraer el contenido de la página de destino");
							}

						}

					}

					if (!tempLink.text().isEmpty()) {
						eventos.add(tempLink.text());
					}
				}
			}
			
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la página web. Revise las secciones y estructura web");
		}
		

	}

	/**
	 * Accede al enlace de la noticia recopilando información de los elementos
	 * permitiendo pasar patrones de texto para extraer una fecha limpia.
	 */
	public void ExtractLostDate() {
		Document doc3;
		Elements links3 = null;
		
		try {
			for (int i = 0; i < lists.size(); i++) {
				// eventos.get(0);
				doc3 = Jsoup.connect(lists.get(i).get(0).toString()).get();
				links3 = doc3.select("article.noticia-interior > p");

				for (Element e3 : links3) {
					if (!lists.get(i).get(1).toString().contains("X")) {
						links3.next();
					} else {
						if (!e3.text().isEmpty()) {
							String regex = "\\d+\\s+\\w{2}\\s+\\w+";
							Matcher matcher = Pattern.compile(regex).matcher(e3.text().toString());
							if (matcher.find()) {
								if (!matcher.group().contains("vs")) {
									String fecha_final, dia = null, mes = null;
									String fechaSinFormato = matcher.group().toString();

									regex = "\\d+";
									matcher = Pattern.compile(regex).matcher(fechaSinFormato);
									if (matcher.find()) {
										System.out.println("Patrón_aux: " + matcher.group());
										dia = matcher.group().toString();

										if (Integer.valueOf(dia) != 0) {
											if (Integer.valueOf(dia) > 9) {
												dia = String.valueOf(Integer.valueOf(dia));
											} else {
												// Poner el "0"
												dia = "0" + String.valueOf(Integer.valueOf(dia));
											}
										}
									}

									// \w+\w{3,} Solo muestra el mes deseado.
									// Separador Mes: => 4 DE JUNIO => JUNIO.

									regex = "\\w+\\w{3,}";
									matcher = Pattern.compile(regex).matcher(fechaSinFormato);
									if (matcher.find()) {
										System.out.println("Patrón_aux: " + matcher.group());
										mes = matcher.group().toString();
										int conversion = DameDatosFecha.suMes(mes);

										if (conversion != 0) {
											if (conversion > 9) {
												mes = String.valueOf(conversion);
											} else {
												// Poner el "0"
												mes = "0" + String.valueOf(conversion);
											}
										}

									}
									
									
									//Para acceder a la hora del evento: Debemos intentar realizar la consulta por Buscador de Google.
									// Buscando similutdes de formato 00:00h ó 00:00 h ó 00:00 ó 00pm sino... Se establece: Concierto: Tarde. Evento: Durante el día.
									

									// fecha_final = String.valueOf(dia+"/"+mes);
									fecha_final = String.valueOf(dia + "/" + mes+ "/");

									// System.out.println("Patrón_aux: " + matcher.group());
									// lists.get(i).set(1, matcher.group().toUpperCase());
									lists.get(i).set(1, fecha_final);

								}
							}
						}
					}

				}
			}

		} catch (Exception ex3) {
			ex3.printStackTrace();
		}

	}

	/**
	 * Particiona los datos para su separación y organización.
	 */
	public void ClasifDatos() {
		//lists = Lists.partition(eventos, 3);
	}
	
	/**
	 * Permite obtener la fecha del evento a partir
	 * de un contador de meses/año. Accede a la información
	 * del recurso para conseguir el valor estimado.
	 * Anho del evento actual.
	 * Contador de meses. Si estamos en enero. que saque el año y mes de hoy.
	 * Si el evento anterior era en diciembre año --1.
	 * Condicionates eventos pasados y futuros.
	 */
	public void FormatoAnhoFecha() {		
		//¿Cuanto meses le falta hasta el próximo año?
		//Condiciona fecha mayor o superior
		for (int i=0; i<lists.size(); i++) {
			System.out.println(lists.get(i).get(1));
		}
		
		//Aceder al año del evento revisando publicación de la noticia:
		// Clasificaremos eventos antiguos y nuevos sin importar el orden.
		Document doc3;
		Elements links3;
		StringTokenizer st;
		try {
			doc3 = Jsoup.connect("https://www.atleticodemadrid.com/noticias-nuevo-estadio").userAgent("Mozilla/5.0")
					.timeout(10 * 1000).get();
			//links3 = doc3.select("article.noticia > time[datetime]");
			links3 = doc3.select("article.noticia > time[datetime]");
			System.out.println("\n"); //Separador borrar al limpiar.
			
			Element tempLink;
			//for (Element e : links) {
			for (int i=0; i<links3.size(); i++) {

				//Pruebas:
				tempLink = links3.get(i);
				if (!tempLink.cssSelector().contains("article.noticia.destacada")) {
					System.out.println("*> "+tempLink.attr("datetime"));	
					
					// -1 | Es debido a que los elementos comienzan desde el 1. Pero el ArrayList desde el índice 0.
					st = new StringTokenizer(lists.get(i-1).get(1).toString(), "/");
                    st.nextToken();
                    String mes = st.nextToken();
                    System.out.println("*"+mes);
                    
                    //String tokenizer mes de la fecha del artículo:
                    st = new StringTokenizer(tempLink.attr("datetime"), "-");
                    st.nextToken();
                    String month_articulo = st.nextToken();
                    System.out.println("//"+month_articulo);
                    
                    //Condicional mes/anho.
                    if (Integer.valueOf(mes)<Integer.valueOf(month_articulo)) {
                    	//año anterior -1
                    	lists.get(i-1).set(1, String.valueOf(lists.get(i-1).get(1).toString()+Integer.valueOf(global_year-1)));
                    } else if (Integer.valueOf(mes)>=Integer.valueOf(month_articulo)) {
                    	//mismo año.
                    	lists.get(i-1).set(1, String.valueOf(lists.get(i-1).get(1).toString()+Integer.valueOf(global_year)));
                    }

				}
			}		
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Permita la lectura de los datos particionados.
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
		new ScramblerEvents_WandaMetropolitano().ExtractDataFromURL("https://www.atleticodemadrid.com/noticias-nuevo-estadio");
		//new ScramblerEvents_WandaMetropolitano().ExtractDataFromURL("https://www.atleticodemadrid.com/noticias-nuevo-estadio?page=2");//Pag 2.
		new ScramblerEvents_WandaMetropolitano().ClasifDatos();
		new ScramblerEvents_WandaMetropolitano().ExtractLostDate();
		new ScramblerEvents_WandaMetropolitano().LecturaDatosParticionados();
		new ScramblerEvents_WandaMetropolitano().FormatoAnhoFecha();
		new ScramblerEvents_WandaMetropolitano().LecturaDatosParticionados();
	}
}
