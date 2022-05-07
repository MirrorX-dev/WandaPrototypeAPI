package com.wandaprototype.DBSAAS.information;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.wandaprototype.DBSAAS.information.db.DbManager;
import com.wandaprototype.dataformat.DameFecha;

public class OldScrambler {
	// Variables inicializadas:
	private static int aux_contador_meses_serie = 0;
	private static int aux_contador_meses_serie_ant = 0;
	private static int global_year;
	

	// Lista dónde almacenaremos los partidos recopilados de la web.
	public static List<Object> partidos = new ArrayList<>();
	public static List<Object> partidos_con_fecha = new ArrayList<>();
	
	// Lista donde clasificaremos los datos brutos de la lista partidos.
	List<List<Object>> lists = Lists.partition(partidos, 6);

	// Lista pre-definidida para constatar texto de los memes.
	public static List<String> meses = new ArrayList<String>() {
		private static final long serialVersionUID = 3286704113498260770L;
		{
			add("enero");
			add("febrero");
			add("marzo");
			add("abril");
			add("mayo");
			add("junio");
			add("julio");
			add("agosto");
			add("septiembre");
			add("octubre");
			add("noviembre");
			add("diciembre");

			/*
			 * add("Enero"); add("Febrero"); add("Marzo"); add("Abril"); add("Mayo");
			 * add("Junio"); add("Julio"); add("Agosto"); add("Septiembre"); add("Octubre");
			 * add("Noviembre"); add("Diciembre");
			 */
		}
	};

	// Lista pre-definidida para constatar texto de los días de la semana.
	public static List<String> dia_semana = new ArrayList<String>() {
		private static final long serialVersionUID = -8458785409170869495L;
		{
			add("lunes");
			add("martes");
			add("miercoles");
			add("jueves");
			add("viernes");
			add("sabado");
			add("domingo");

			add("Lunes");
			add("Martes");
			add("Miercoles");
			add("Jueves");
			add("Viernes");
			add("Sabado");
			add("Domingo");
		}
	};

	// Lista pre-definidida para constatar texto de los días.
	public static List<String> dias = new ArrayList<String>() {
		private static final long serialVersionUID = 5337255028269659218L;
		{
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
			add("6");
			add("7");
			add("8");
			add("9");
			add("10");
			add("11");
			add("12");
			add("13");
			add("14");
			add("15");
			add("16");
			add("17");
			add("18");
			add("19");
			add("20");
			add("21");
			add("22");
			add("23");
			add("24");
			add("25");
			add("26");
			add("27");
			add("28");
			add("29");
			add("30");
			add("31");
		}
	};

	private int DevuelvemeElMesCorrespondiente(String valor) {
		int aux_mes_int = 0;
		if (valor.toString().equalsIgnoreCase(meses.get(0))) {
			aux_mes_int = 1;

		} else if (valor.toString().equalsIgnoreCase(meses.get(1))) {
			aux_mes_int = 2;

		} else if (valor.toString().equalsIgnoreCase(meses.get(2))) {
			aux_mes_int = 3;

		} else if (valor.toString().equalsIgnoreCase(meses.get(3))) {
			aux_mes_int = 4;

		} else if (valor.toString().equalsIgnoreCase(meses.get(4))) {
			aux_mes_int = 5;

		} else if (valor.toString().equalsIgnoreCase(meses.get(5))) {
			aux_mes_int = 6;

		} else if (valor.toString().equalsIgnoreCase(meses.get(6))) {
			aux_mes_int = 7;

		} else if (valor.toString().equalsIgnoreCase(meses.get(7))) {
			aux_mes_int = 8;

		} else if (valor.toString().equalsIgnoreCase(meses.get(8))) {
			aux_mes_int = 9;

		} else if (valor.toString().equalsIgnoreCase(meses.get(9))) {
			aux_mes_int = 10;

		} else if (valor.toString().equalsIgnoreCase(meses.get(10))) {
			aux_mes_int = 11;

		} else if (valor.toString().equalsIgnoreCase(meses.get(11))) {
			aux_mes_int = 12;

		} else {
		}

		return aux_mes_int;
	}

	
	//	:: Agregar datos adicionales ::
	//	Falta agregar información importante como:
	//	Equipo que juega y rival.
	//	:: Planear si relacionar con el partido o extraerlo todo.
	//	Un avez extraido asociar también el marcador del partido (Próximamente).
	public void ExtractDataFromURL() {
		Document doc;
		Elements links = null;

		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/calendario-completo-primer-equipo/").get();
			//File input = new File("C:/Users/MRX-Workstation/Downloads/web2019.html"); //Funciona con archivo local de 2020.
			//doc = Jsoup.parse(input, "UTF-8", "http://example.com/"); //Funciona con archivo local de 2020.
			
			links = doc.select("div.header-calendar > div.competition > img , div.header-calendar > div.info-calendario > span");
			//links = doc.select("div.header-calendar > div.competition > img , div.header-calendar > div.info-calendario > span, div.header-calendar > ul.marcador > li.local > dl ");
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la página web. Revise las secciones y estructura web");
		}

		for (Element e : links) {

			// :: Filtros Partidos datos ::
			if (e.text().contentEquals("Resumen") || e.text().contentEquals("AVíSAME")
					|| e.text().contentEquals("ENTRADAS") || e.text().contentEquals("Previa")
					|| e.text().contentEquals("¡En directo!") || e.text().isEmpty()) {
			} else {
				if (e.text().toString().contains("-")) {
					partidos.add(String.valueOf(e.text().toString().substring(0 ,e.text().toString().length()-2) ));
				} else {
					partidos.add(String.valueOf(e.text()));
				}
					
				//partidos.add(String.valueOf(e.text()));
			}

			// :: Filtros La Liga ::
			if (e.attributes().get("title").isEmpty()) {
				// partidos.add("null");
			} else {
				partidos.add(e.attributes().get("title").toString());
			}

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
		} catch (Exception edfh_ex) {
			edfh_ex.printStackTrace();
			System.err.println("Error al extraer el contenido de la página web. Revise las secciones y estructura web");
		}

		//	En el caso de no encontrar ningún primer partido. Se asignará el año actual de la fecha de recogida.
		for (Element e : links) {
			if (e.attr("href").contains("/postpartidos")) {
				partidos_con_fecha.add(e.attr("href"));
			} else {
				//	Establecemos que el año es el actual. Ej: 202X.
			}
		}
	}

	
	// unused debido a break. Pero los necesito para las iteración de la lista.
	@SuppressWarnings("unused")
	public void ClasifDatosFirstDatedMatch() {
		// 	Extrae y muestra la fecha del partido correspondiente.
		// 	Falta obtener y almacenar el año del cual se asignará al año de comienzo de la jornada.
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
		 * Si excede del número de elementos en el contador, puede ser debido a algún
		 * elemento eliminado/introducido en la página web.
		 */
		int contador = 0;
		String aux_compe, aux_jor, aux_fechpa, aux_horapa, aux_estad, aux_id, aux_id_result;
		for (int c = 0; c < partidos.size(); c++) {
			//System.out.println(partidos.get(c));
			if (c == contador) {
				aux_compe = (String) partidos.get(c);
				aux_jor = (String) partidos.get(c + 1);				
				aux_fechpa = (String) partidos.get(c + 2);
				aux_horapa = (String) partidos.get(c + 3);
				aux_estad = (String) partidos.get(c + 4);

				aux_id = aux_compe + aux_jor + aux_fechpa + aux_horapa + aux_estad;
				aux_id_result = UUID.nameUUIDFromBytes(aux_id.getBytes()).toString();

				partidos.add(c, aux_id_result);
				contador = contador + 6;
			}

		}

		//lists = Lists.partition(partidos, 6); // Orginal 5 Elementos sin :: id ::.	
		lists = Lists.partition(partidos, 6);
		new OldScrambler().LecturaDatosParticionados();
	}

	public void ClasifDatos() {
		/**
		 * BUG:: 01
		 * Si intentemos recoger datos de años pasados, es decir ej: 2019 
		 * situandonos en el año actual 2022. Ajustará el mes a la fecha correspondiente.
		 * En este caso el evento con año de 2019 pasará a ser de 2021.
		 * 
		 * Este bug es indiferente ya que los partidos se centra en los próximos que se
		 * establecen para el año actual o el siguiente.
		 */
		
		lists = Lists.partition(partidos, 6); // Orginal 5 Elementos sin :: id ::.

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
			for (int ds = 0; ds < meses.size(); ds++) {
				if (lists.get(c).toString().contains(meses.get(ds).toString())) {
					aux_sep = lists.get(c).get(3).toString();
					Object[] stringarray = aux_sep.split(" ");

					for (int i = 0; i < stringarray.length; i++) {
						if (stringarray[i].equals(" ") || stringarray[i].equals("de") || stringarray[i].equals("-")
								|| stringarray[i].equals("o")) {

						} else {
							//System.out.println("Resul: " + stringarray[i].toString());

							// Condiciones de contador fechas por fecha de origen. :: MESES ::
							if (stringarray[i].toString().equalsIgnoreCase(meses.get(0))) {
								aux_mes = 1;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(1))) {
								aux_mes = 2;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(2))) {
								aux_mes = 3;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(3))) {
								aux_mes = 4;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(4))) {
								aux_mes = 5;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(5))) {
								aux_mes = 6;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(6))) {
								aux_mes = 7;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(7))) {
								aux_mes = 8;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(8))) {
								aux_mes = 9;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(9))) {
								aux_mes = 10;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(10))) {
								aux_mes = 11;

							} else if (stringarray[i].toString().equalsIgnoreCase(meses.get(11))) {
								aux_mes = 12;

							} else {
							}

							// Condiciones de contador fechas por fecha de origen. :: DÍA ::
							if (stringarray[i].toString().equalsIgnoreCase(dias.get(0))) {
								aux_dia = "01";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(1))) {
								aux_dia = "02";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(2))) {
								aux_dia = "03";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(3))) {
								aux_dia = "04";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(4))) {
								aux_dia = "05";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(5))) {
								aux_dia = "06";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(6))) {
								aux_dia = "07";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(7))) {
								aux_dia = "08";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(8))) {
								aux_dia = "09";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(9))) {
								aux_dia = "10";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(10))) {
								aux_dia = "11";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(11))) {
								aux_dia = "12";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(12))) {
								aux_dia = "13";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(13))) {
								aux_dia = "14";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(14))) {
								aux_dia = "15";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(15))) {
								aux_dia = "16";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(16))) {
								aux_dia = "17";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(17))) {
								aux_dia = "18";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(18))) {
								aux_dia = "19";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(19))) {
								aux_dia = "20";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(20))) {
								aux_dia = "21";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(21))) {
								aux_dia = "22";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(22))) {
								aux_dia = "23";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(23))) {
								aux_dia = "24";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(24))) {
								aux_dia = "25";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(25))) {
								aux_dia = "26";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(26))) {
								aux_dia = "27";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(27))) {
								aux_dia = "28";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(28))) {
								aux_dia = "29";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(29))) {
								aux_dia = "30";
							} else if (stringarray[i].toString().equalsIgnoreCase(dias.get(30))) {
								aux_dia = "31";
							} else {
							}

							
							// Condiciones de contador fechas por fecha de origen. :: AÑO ++ Contador. ::							
							for (int x = 0; x < meses.size(); x++) {
								// if (stringarray[i].toString().equalsIgnoreCase(meses.get(x))) {
								if (aux_pos_break == null) {
									if (stringarray[i].toString().equalsIgnoreCase(meses.get(x))) {
										aux_pos_break = stringarray[i].toString();
										//System.out.println("único break: " + aux_pos_break); // Aquí declaras el primer
																								// mes obtenido de la
																								// lista.
										break;
									}
								}

							}

							// Obtenemos el primer valor válido [mes]. Asignamos como valor actual en el
							// contador.
							int aux_mes_devuelto;
							if (aux_pos_break != null) {
								aux_mes_devuelto = new OldScrambler()
										.DevuelvemeElMesCorrespondiente(aux_pos_break); // 1º 7.
								aux_contador_meses_inicial = aux_mes_devuelto;
								aux_contador_meses_serie = aux_contador_meses_inicial;
							}

							// :: Realiza el cambio de mes ::
							/*
							 * Anotaciones: Contadores inicializados en 0. aux_contador_meses_serie =
							 * aux_mes; Se basará en el primer mes obtenido de la lista.
							 * 
							 * Bugs posibles: Si hay una jornada en el año 2022 acabada por 12 y la
							 * siguiente en caso de ser única es 12 del año 2023.
							 * 
							 * No se producirá la suma++ de año. [Probabilidad: Extremadamente rara].
							 */
							if (aux_contador_meses_serie_ant > aux_mes) {
								//System.out.println("SI pasa de año: " + aux_contador_meses_serie);
								aux_anho++;
								aux_contador_meses_serie_ant = aux_mes;
								aux_contador_meses_serie = aux_mes;
							} else if (aux_mes == aux_contador_meses_serie) {
								aux_contador_meses_serie_ant = aux_contador_meses_serie;
								aux_contador_meses_serie = aux_mes;
							} else {
								aux_contador_meses_serie = aux_mes;
								//System.out.println("No pasa de año: " + aux_contador_meses_serie);
							}

							// lists.get(c).set(3, lists.subList(0, 0)); //Subdividir apartado 3.
							if (aux_mes != 0) {
								if (Integer.valueOf(aux_mes) > 9) {
									lists.get(c).set(3, String.valueOf(aux_dia) + "/" + aux_mes + "/" + aux_anho);
								} else {
									// Poner el "0"
									lists.get(c).set(3, String.valueOf(aux_dia) + "/0" + aux_mes + "/" + aux_anho);
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
		for (List<Object> list : lists) {
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
	

	
	//Debemos añadir un método que añadirá el nuevo evento no confirmado:
	//Comparará en la bbdd el ID, único generado por evento.
	//Para que no añada eventos repetidos que ya existen...
	//	:: Bugs anticipados ::
	//		[idpartido]
	//		Cambio de datos sensibles en la web.
	//		[compe] :: competición.
	//		[jor] :: jornada.
	//		[fechapa] :: fecha partido.
	//		[hora] :: hora_partido.
	//		[estad] :: estadio.
	public void agregarCamposConfirmardos() {
		// :: Lectura ::
		
		System.out.println("\n");
		for (int a=0; a<lists.size(); a++) {
			//	:: Filtra los partidos que contengan **Sin confirmar***, para evitar duplicación de eventos.
			if (lists.get(a).get(4).toString().equals("Sin confirmar")) {
				System.out.println(lists.get(a).get(4));	
			} else {
				//	:: Agregar Error en caso de partido duplicado :: Got an exception!
				//	Duplicate entry 'b8c33af3-5ba2-3301-9a68-eb508c728b21' for key 'partido.PRIMARY'...
				if (new DbManager().getDataPerID(lists.get(a).get(0).toString()) == false) {
					new DbManager().postData(	lists.get(a).get(0).toString(), 
							lists.get(a).get(1).toString(), 
							lists.get(a).get(2).toString(), 
							String.valueOf(new DameFecha().dameDateAmericana(lists.get(a).get(3).toString())), 
							lists.get(a).get(4).toString(), 
							lists.get(a).get(5).toString(),
							//Parche debido a uso de más datos.
							lists.get(a).get(5).toString(),
							lists.get(a).get(5).toString());
				}				
			}
			
		}
		System.out.println("\n");
	}
	
	
	
	public static void main(String[] args) throws Exception {
		new OldScrambler().ExtractDataFromURL();
		new OldScrambler().ExtractFirstDatedMatchFromURL();
		new OldScrambler().IdentifDatosParticionados();
		new OldScrambler().ClasifDatosFirstDatedMatch();
		new OldScrambler().ClasifDatos();
		new OldScrambler().LecturaDatosParticionadosSimple();
		new OldScrambler().agregarCamposConfirmardos();
		
		

	}
	
	
	

}
