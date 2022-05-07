package com.wandaprototype.standalone.information;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.wandaprototype.dataformat.DameDatosFecha;
import com.wandaprototype.objects.Partido;

public class ScramblerMatchs_Atletico_Madrid_AndroidVersion2 {
	// Variables inicializadas:
	private static int aux_contador_meses_serie = 0;
	private static int aux_contador_meses_serie_ant = 0;
	private static int global_year;
	private Document doc;
	private Elements links = null;

	// Lista dï¿½nde almacenaremos los partidos recopilados de la web de forma temporal..
	public static ArrayList<String> partidos = new ArrayList<>();
	public static ArrayList<String> partidos_con_fecha = new ArrayList<>();

	// Lista donde clasificaremos los datos brutos de la lista partidos de forma temporal..
	public static List<List<String>> lists = Lists.partition(partidos, 8);


	//	:: Agregar datos adicionales ::
	//	Falta agregar informaciÃ³n importante como:
	//	Equipo que juega y rival.
	//	:: Planear si relacionar con el partido o extraerlo todo.
	//	Un avez extraido asociar tambiÃ©n el marcador del partido (PrÃ³ximamente).
	public void ExtractDataFromURL() {
		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/calendario-completo-primer-equipo/")
					.userAgent("Mozilla/5.0")
			        .timeout(10 * 1000)
					.get();
			//File input = new File("C:/Users/MRX-Workstation/Downloads/web2019.html"); //Funciona con archivo local de 2020.
			//doc = Jsoup.parse(input, "UTF-8", "http://example.com/"); //Funciona con archivo local de 2020.

			//links = doc.select("div.header-calendar > div.competition > img , div.header-calendar > div.info-calendario > span");
			links = doc.select("div.header-calendar > div.competition > img , div.header-calendar > div.info-calendario > span, ul.marcador > li.local > dl > dt, ul.marcador > li.visitante > dl > dt");
			
			
			//Temp
			int i;
			Element tempLink;
			//for (Element e : links) {
			for (i=0; i<links.size(); i++) {

				//Pruebas:
				tempLink = links.get(i);
				
				// :: Filtros Partidos datos ::
				if (tempLink.text().contentEquals("Resumen") || tempLink.text().contentEquals("AVÃ­SAME")
						|| tempLink.text().contentEquals("ENTRADAS") || tempLink.text().contentEquals("Previa")
						|| tempLink.text().contentEquals("Â¡En directo!") || tempLink.text().isEmpty()) {
				} else {
					if (tempLink.text().toString().contains("-")) {
						partidos.add(String.valueOf(tempLink.text().toString().substring(0 ,tempLink.text().toString().length()-2) ));
					} else {
						partidos.add(String.valueOf(tempLink.text()));
					}

					//partidos.add(String.valueOf(e.text()));
				}

				// :: Filtros La Liga ::
				if (tempLink.attributes().get("title").isEmpty()) {
					// partidos.add("null");
				} else {
					partidos.add(tempLink.attributes().get("title").toString());
				}

			}
			
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la pÃ¡gina web. Revise las secciones y estructura web");
			ExtractDataFromURL();
		} finally {
			
		}

		
	}

	public void ExtractFirstDatedMatchFromURL() {
		Document doc;
		Elements links = null;

		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/calendario-completo-primer-equipo/").get();
			//File input = new File("C:/Users/MRX-Workstation/Downloads/web2019.html"); //Funciona con archivo local de 2020.
			//doc = Jsoup.parse(input, "UTF-8", "http://example.com/"); //Funciona con archivo local de 2020.

			links = doc.select(
					"div.header-calendar > a[href], [href]"
			);
			
			//En el caso de no encontrar ningÃºn primer partido. Se asignarÃ¡ el aÃ±o actual de la fecha de recogida.
			for (Element e : links) {
				if (e.attr("href").contains("/postpartidos")) {
					partidos_con_fecha.add(e.attr("href"));
				} else {
					//	Establecemos que el aÃ±o es el actual. Ej: 202X.
				}
			}
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la pÃ¡gina web. Revise las secciones y estructura web");
		}	
	}


	// unused debido a break. Pero los necesito para las iteraciÃ³n de la lista.
	@SuppressWarnings("unused")
	public void ClasifDatosFirstDatedMatch() {
		// 	Extrae y muestra la fecha del partido correspondiente.
		// 	Falta obtener y almacenar el aÃ±o del cual se asignarÃ¡ al aÃ±o de comienzo de la jornada.
		global_year = 0;
		for (int j=0; j<partidos_con_fecha.size(); j++) {
			Object h = partidos_con_fecha.get(j);
			int fin = partidos_con_fecha.get(j).toString().length();
			int com = fin-16;
			String datos = h.toString().substring(com);
			System.out.println("Partido: "+j+" : "+datos+" Ref: "+partidos_con_fecha.get(j));


			if (partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-12, partidos_con_fecha.get(j).toString().length()-8).contains("-")) {
				if ( partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6).contains("-") ) {
					System.out.println("yeahAPlus1: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-14, partidos_con_fecha.get(j).toString().length()-10));
					global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-14, partidos_con_fecha.get(j).toString().length()-10));
					break;
				} else {
					System.out.println("yeahA: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
					global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
					break;
				}
			} else if (partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6).contains("-"))  {
				System.out.println("yeahB: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-12, partidos_con_fecha.get(j).toString().length()-8));
				global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-12, partidos_con_fecha.get(j).toString().length()-8));
				break;
			} else {
				System.out.println("yeah_limpio: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
				global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
				break;
			}

		}

	}

	public void IdentifDatosParticionados() {
		/**
		 * Si excede del nÃºmero de elementos en el contador, puede ser debido a algÃºn
		 * elemento eliminado/introducido en la pÃ¡gina web.
		 */
		int contador = 0;
		String aux_compe, aux_jor, aux_local, aux_visit, aux_fechpa, aux_horapa, aux_estad, aux_id, aux_id_result;
		for (int c = 0; c < partidos.size(); c++) {
			//System.out.println(partidos.get(c));
			if (c == contador) {
				aux_compe = (String) partidos.get(c);
				aux_jor = (String) partidos.get(c + 1);
				aux_local = (String) partidos.get(c + 2);
				aux_visit = (String) partidos.get(c + 3);
				aux_fechpa = (String) partidos.get(c + 4);
				aux_horapa = (String) partidos.get(c + 5);
				aux_estad = (String) partidos.get(c + 6);

				aux_id = aux_compe + aux_jor + aux_local + aux_visit + aux_fechpa + aux_horapa + aux_estad;
				aux_id_result = UUID.nameUUIDFromBytes(aux_id.getBytes()).toString();

				partidos.add(c, aux_id_result);
				contador = contador + 8;
			}

		}

		//lists = Lists.partition(partidos, 6); // Orginal 5 Elementos sin :: id ::.
		lists = Lists.partition(partidos, 8); //No toma efecto aquÃ­, sino como variable declarada.
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().LecturaDatosParticionados();
	}

	public void ClasifDatos() {
		/**
		 * BUG:: 01
		 * Si intentemos recoger datos de aÃ±os pasados, es decir ej: 2019
		 * situandonos en el aÃ±o actual 2022. AjustarÃ¡ el mes a la fecha correspondiente.
		 * En este caso el evento con aÃ±o de 2019 pasarÃ¡ a ser de 2021.
		 *
		 * Este bug es indiferente ya que los partidos se centra en los prÃ³ximos que se
		 * establecen para el aÃ±o actual o el siguiente.
		 */

		//lists = Lists.partition(partidos, 8); // Orginal 5 Elementos sin :: id ::.

		// String aux_mes = null;
		int aux_mes = 0;
		String aux_dia = null;
		LocalDate current_date = LocalDate.now();
		int aux_anho = current_date.getYear();

		//Solo ejecuta la primera vez!
		if (global_year!=0 && (global_year<aux_anho || global_year==aux_anho) ) {
			aux_anho = global_year;
			System.out.println(aux_anho+" y el globa: "+global_year);
		} else {
			System.out.println("Er: "+aux_anho+" y el globa: "+global_year);

		}

		String aux_pos_break = null;
		int aux_contador_meses_inicial = 0;
		String aux_sep;

		//	:: CLASIFICA EL DATO EN REFERENCIA A LA FECHA ESPECIFICADA DEL CAMPO ::
		for (int c = 0; c < lists.size(); c++) {
			/*
			 * for (int ds = 0; ds < meses.size(); ds++) {
				if (lists.get(c).toString().contains(meses.get(ds).toString())) {
			 */
			for (int ds = 0; ds <DameDatosFecha.meses.size(); ds++) {
				if (lists.get(c).toString().contains(DameDatosFecha.meses.get(ds).toString())) {
					aux_sep = lists.get(c).get(5).toString();
					String[] stringarray = aux_sep.split(" ");

					for (int i = 0; i < stringarray.length; i++) {
						if (stringarray[i].equals(" ") || stringarray[i].equals("de") || stringarray[i].equals("-")
								|| stringarray[i].equals("o")) {

						} else {
							// Condiciones de contador fechas por fecha de origen. :: MESES ::
							for (int i2=0; i2<DameDatosFecha.meses.size(); i2++) {
								if (stringarray[i].toString().equalsIgnoreCase(DameDatosFecha.meses.get(i2))) {
									aux_mes = i2+1;
									if (Integer.valueOf(aux_mes) != 0) {
										if (Integer.valueOf(aux_mes) < 10) {
											aux_mes = Integer.valueOf("0"+String.valueOf(aux_mes));
										}
									}
									break;
								}	
							}
							
							// Condiciones de contador fechas por fecha de origen. :: DÃ�A ::
							for (int i2=0; i2<DameDatosFecha.dias.size(); i2++) {
								if (stringarray[i].toString().equalsIgnoreCase(DameDatosFecha.dias.get(i2))) {
									aux_dia = String.valueOf(i2+1);
									if (Integer.valueOf(aux_dia) != 0) {
										if (Integer.valueOf(aux_dia) < 10) {
											aux_dia = "0"+String.valueOf(aux_dia);
										}
									}
									break;
								}	
							}
							
							
							// Condiciones de contador fechas por fecha de origen. :: AÃ‘O ++ Contador. ::
							for (int x = 0; x < DameDatosFecha.meses.size(); x++) {
								// if (stringarray[i].toString().equalsIgnoreCase(meses.get(x))) {
								if (aux_pos_break == null) {
									if (stringarray[i].toString().equalsIgnoreCase(DameDatosFecha.meses.get(x))) {
										aux_pos_break = stringarray[i].toString();
										//System.out.println("Ãºnico break: " + aux_pos_break); // AquÃ­ declaras el primer
										// mes obtenido de la
										// lista.
										break;
									}
								}

							}

							// Obtenemos el primer valor vÃ¡lido [mes]. Asignamos como valor actual en el
							// contador.
							int aux_mes_devuelto;
							if (aux_pos_break != null) {
								//aux_mes_devuelto = new ScramblerMatchs_Atletico_Madrid().DevuelvemeElMesCorrespondiente(aux_pos_break); // 1Âº 7.
								aux_mes_devuelto = DameDatosFecha.suMes(aux_pos_break); // 1Âº 7.
								aux_contador_meses_inicial = aux_mes_devuelto;
								aux_contador_meses_serie = aux_contador_meses_inicial;
							}

							// :: Realiza el cambio de mes ::
							/*
							 * Anotaciones: Contadores inicializados en 0. aux_contador_meses_serie =
							 * aux_mes; Se basarÃ¡ en el primer mes obtenido de la lista.
							 *
							 * Bugs posibles: Si hay una jornada en el aÃ±o 2022 acabada por 12 y la
							 * siguiente en caso de ser Ãºnica es 12 del aÃ±o 2023.
							 *
							 * No se producirÃ¡ la suma++ de aÃ±o. [Probabilidad: Extremadamente rara].
							 */
							if (aux_contador_meses_serie_ant > aux_mes) {
								//System.out.println("SI pasa de aÃ±o: " + aux_contador_meses_serie);
								aux_anho++;
								aux_contador_meses_serie_ant = aux_mes;
								aux_contador_meses_serie = aux_mes;
							} else if (aux_mes == aux_contador_meses_serie) {
								aux_contador_meses_serie_ant = aux_contador_meses_serie;
								aux_contador_meses_serie = aux_mes;
							} else {
								aux_contador_meses_serie = aux_mes;
								//System.out.println("No pasa de aÃ±o: " + aux_contador_meses_serie);
							}

							// lists.get(c).set(3, lists.subList(0, 0)); //Subdividir apartado 3.
							if (aux_mes != 0) {
								if (Integer.valueOf(aux_mes) > 9) {
									lists.get(c).set(5, String.valueOf(aux_dia) + "/" + aux_mes + "/" + aux_anho);
								} else {
									// Poner el "0"
									lists.get(c).set(5, String.valueOf(aux_dia) + "/0" + aux_mes + "/" + aux_anho);
								}
							}

						}
					}
					//System.out.println("");

				} else {
				}

			}
			System.out.println(lists.get(c));
		}
	}

	public void LecturaDatosParticionados() {
		// :: Lectura ::
		System.out.println();
		for (List<String> list : lists) {
			System.out.println(list);
		}
		System.out.println("\n");
	}

	public void LecturaDatosParticionadosSimple() {
		// :: Lectura ::
		System.out.println("\n");
		for (int x=0; x<lists.size(); x++) {
			System.out.println(lists.get(x));
		}
		System.out.println("\n");
	}


	//Debemos aï¿½adir un mï¿½todo que aï¿½adirï¿½ el nuevo evento no confirmado:
	//Compararï¿½ en la bbdd el ID, ï¿½nico generado por evento.
	//Para que no aï¿½ada eventos repetidos que ya existen...
	//	:: Bugs anticipados ::
	//		[idpartido]
	//		Cambio de datos sensibles en la web.
	//		[compe] :: competiciï¿½n.
	//		[jor] :: jornada.
	//		[fechapa] :: fecha partido.
	//		[hora] :: hora_partido.
	//		[estad] :: estadio.
	public void agregarCamposConfirmardos() {
		for (int a=0; a<lists.size(); a++) {
			//	:: Filtra los partidos que contengan **Sin confirmar***, para evitar duplicaciÃ³n de eventos.
			if (!lists.get(a).get(6).toString().equals("Sin confirmar")) {
				Partido p = new Partido(
						lists.get(a).get(0).toString(),
						lists.get(a).get(1).toString(),
						lists.get(a).get(2).toString(),
						lists.get(a).get(3).toString(),
						lists.get(a).get(4).toString(),
						lists.get(a).get(5).toString(),
						lists.get(a).get(6).toString(),
						lists.get(a).get(7).toString());
				Partido.partidos.add(p);
			}
		}

		for (int i=0; i< Partido.partidos.size(); i++) {
			System.out.println(Partido.partidos.get(i));
		}
	}
	
	public void CleanLists() {
		partidos.clear();
		partidos_con_fecha.clear();
		lists.clear();
	}

	public static void main(String[] args) throws Exception {
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().ExtractDataFromURL();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().ExtractFirstDatedMatchFromURL();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().IdentifDatosParticionados();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().ClasifDatosFirstDatedMatch();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().ClasifDatos();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().LecturaDatosParticionadosSimple();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().agregarCamposConfirmardos();
		new ScramblerMatchs_Atletico_Madrid_AndroidVersion2().CleanLists();
		
	}
	
	
	

}
