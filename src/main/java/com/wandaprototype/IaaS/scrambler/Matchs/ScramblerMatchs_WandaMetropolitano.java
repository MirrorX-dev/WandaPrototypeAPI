package com.wandaprototype.IaaS.scrambler.Matchs;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Lists;
import com.jcraft.jsch.JSchException;
import com.wandaprototype.IaaS.information.db.DbManagerSSH;
import com.wandaprototype.dataformat.DameDatosFecha;
import com.wandaprototype.dataformat.DameFecha;

public class ScramblerMatchs_WandaMetropolitano {
	/**Variables inicializadas:*/
	private static int aux_contador_meses_serie = 0;
	private static int aux_contador_meses_serie_ant = 0;
	private static int global_year;
	private Document doc;
	private Elements links = null;

	/**Array de confirmación.*/
	public static ArrayList<Boolean> checkConfirmation=new ArrayList<Boolean>();    
	
	/**Lista dï¿½nde almacenaremos los partidos recopilados de la web de forma temporal..*/
	public static ArrayList<String> partidos = new ArrayList<>();
	public static ArrayList<String> partidos_con_fecha = new ArrayList<>();

	// Lista donde clasificaremos los datos brutos de la lista partidos de forma temporal..
	public static List<List<String>> lists = Lists.partition(partidos, 8);
	
	/** Constructor. Instanciar clase Scrambler.*/
	public ScramblerMatchs_WandaMetropolitano() {
		super();
	}
	
	@Override
	public String toString() {
		return "ScramblerMatchs_WandaMetropolitano [doc=" + doc + ", links=" + links + "]";
	}

	/**
	 * Función extractora de la información alojada en la web.
	 * Conecta a la web del recurso indicado. Sugiere un Timeout de 10 * 1000 para realizar
	 * las operaciones get del documento HTML.
	 */
	public void ExtractDataFromURL() {
		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/calendario-completo-primer-equipo/")
					.userAgent("Mozilla/5.0")
			        .timeout(10 * 1000)
					.get();
			links = doc.select("div.header-calendar > div.competition > img , div.header-calendar > div.info-calendario > span, ul.marcador > li.local > dl > dt, ul.marcador > li.visitante > dl > dt");
			
			int i;
			Element tempLink;
			for (i=0; i<links.size(); i++) {
				tempLink = links.get(i);
				
				/**
				 * :: Filtros Partidos datos ::
				 * Separa y filtra los datos de elementos que recopilemos mediante la extración web.
				 */
				if (tempLink.text().contentEquals("Resumen") || tempLink.text().contentEquals("AVÃ­SAME")
						|| tempLink.text().contentEquals("ENTRADAS") || tempLink.text().contentEquals("Previa")
						|| tempLink.text().contentEquals("Â¡En directo!") || tempLink.text().isEmpty()) {
				} else {
					if (tempLink.text().toString().contains("-")) {
						partidos.add(String.valueOf(tempLink.text().toString().substring(0 ,tempLink.text().toString().length()-2) ));
					} else {
						partidos.add(String.valueOf(tempLink.text()));
					}
				}

				/**
				 * :: Filtros La Liga ::
				 * Separa y filtra los datos de elementos que recopilemos mediante la extración web.
				 */
				if (tempLink.attributes().get("title").isEmpty()) {
				} else {
					partidos.add(tempLink.attributes().get("title").toString());
				}
			}
		} catch (Exception edfh_ex) {
			System.err.println("Error al extraer el contenido de la pÃ¡gina web. Revise las secciones y estructura web");
			edfh_ex.printStackTrace();
		} finally {
			doc.clearAttributes();
		}
	}

	/**
	 * Función extractora de la información alojada en la web.
	 * Conecta a la web del recurso indicado. Sugiere un Timeout de 10 * 1000 para realizar
	 * las operaciones get del documento HTML.
	 * 
	 * Permite obtener de el primer elemento publicado en los partidos, la
	 * fecha del evento existente. Proximamente es utilizada para varios fines
	 * como obtener un contador de transición de meses.
	 */
	public void ExtractFirstDatedMatchFromURL() {
		Document doc;
		Elements links = null;

		try {
			doc = Jsoup.connect("https://www.atleticodemadrid.com/calendario-completo-primer-equipo/").get();
			links = doc.select(
					"div.header-calendar > a[href], [href]"
			);
			
			//En el caso de no encontrar ningÃºn primer partido. Se asignarÃ¡ el aÃ±o actual de la fecha de recogida.
			for (Element e : links) {
				if (e.attr("href").contains("/postpartidos")) {
					partidos_con_fecha.add(e.attr("href"));
				} // Si no...	Establecemos que el aÃ±o es el actual. Ej: 202X.
			}
		} catch (Exception edfh_ex) {
			System.err.println("Error al extraer el contenido de la pÃ¡gina web. Revise las secciones y estructura web");
			edfh_ex.printStackTrace();
		}	
	}


	/**
	 * Iteraciones en la lista llamada al método
	 * Extrae y muestra la fecha del partido correspondiente.
	 * Formatea y clasifica la fecha en un formato valido.
	 */
	@SuppressWarnings("unused")
	public void ClasifDatosFirstDatedMatch() {
		global_year = 0;
		for (int j=0; j<partidos_con_fecha.size(); j++) {
			Object h = partidos_con_fecha.get(j);
			int fin = partidos_con_fecha.get(j).toString().length();
			int com = fin-16;
			String datos = h.toString().substring(com);
			//System.out.println("Partido: "+j+" : "+datos+" Ref: "+partidos_con_fecha.get(j));


			if (partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-12, partidos_con_fecha.get(j).toString().length()-8).contains("-")) {
				if ( partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6).contains("-") ) {
					//System.out.println("yeahAPlus1: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-14, partidos_con_fecha.get(j).toString().length()-10));
					global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-14, partidos_con_fecha.get(j).toString().length()-10));
					break;
				} else {
					//System.out.println("yeahA: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
					global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
					break;
				}
			} else if (partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6).contains("-"))  {
				//System.out.println("yeahB: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-12, partidos_con_fecha.get(j).toString().length()-8));
				global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-12, partidos_con_fecha.get(j).toString().length()-8));
				break;
			} else {
				//System.out.println("yeah_limpio: "+ partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
				global_year = Integer.valueOf(partidos_con_fecha.get(j).toString().substring(partidos_con_fecha.get(j).toString().length()-10, partidos_con_fecha.get(j).toString().length()-6));
				break;
			}

		}

	}

	/**
	 * Tranforma la información en un identificador. Permite que asignar un identificador para evitar
	 * la duplicidad de eventos en la base de datos o mediante su clasificación.
	 * Se realiza un particionado de los datos gracias a la libreria de Google Guava.
	 * Permite transformar y manipular la información de forma eficaz y sencilla.
	 */
	public void IdentifDatosParticionados() {
		 //Si excede del nÃºmero de elementos en el contador, puede ser debido a algÃºn
		 //elemento eliminado/introducido en la pÃ¡gina web.
		 
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

		lists = Lists.partition(partidos, 8); //No toma efecto aquÃ­, sino como variable declarada.
		new ScramblerMatchs_WandaMetropolitano().LecturaDatosParticionados();
	}

	/**
	 * Baaada en la clasificación de datos en un listado particionado permite
	 * gestionar y manipular la información para el uso de un formato valido.
	 * Fechas, resolución de años de primer evento.
	 * Se gestiona de forma sencilla un contador que permita diferenciar la
	 * transición de meses a años.
	 */
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
		int aux_mes = 0;
		String aux_dia = null;
		LocalDate current_date = LocalDate.now();
		int aux_anho = current_date.getYear();

		//Solo ejecuta la primera vez!
		if (global_year!=0 && (global_year<aux_anho || global_year==aux_anho) ) {
			aux_anho = global_year;
			//System.out.println(aux_anho+" y el globa: "+global_year);
		} else {
			//System.out.println("Er: "+aux_anho+" y el globa: "+global_year);

		}

		String aux_pos_break = null;
		int aux_contador_meses_inicial = 0;
		String aux_sep;

		/**
		 * :: CLASIFICA EL DATO EN REFERENCIA A LA FECHA ESPECIFICADA DEL CAMPO ::
		 * Recorre los elementos del listado con los datos particionados en filas.
		 * Si el listado contiene datos de tipo "meses", almacena la fila del mes en un variable
		 * axuiliar llamada aux_sep.
		 * Esta variable es formateada y separada en varios elementos para su manipulación.
		 */
		for (int c = 0; c < lists.size(); c++) {
			for (int ds = 0; ds <DameDatosFecha.meses.size(); ds++) {
				if (lists.get(c).toString().contains(DameDatosFecha.meses.get(ds).toString())) {
					aux_sep = lists.get(c).get(5).toString();
					String[] stringarray = aux_sep.split(" ");

					/**
					 * Recorre todos los elementos de la columna fecha para separar su contenido.
					 * Si los elementos contienen espacios, palabras que empiezen por "de" u "o". Caracteres que
					 * contengan "-".
					 */
					for (int i = 0; i < stringarray.length; i++) {
						if (stringarray[i].equals(" ") || stringarray[i].equals("de") || stringarray[i].equals("-")
								|| stringarray[i].equals("o")) {

						} else {
							
							// Condiciones de contador fechas por fecha de origen. :: MESES ::
							/**
							 * Contador de meses. Condición: i2 contador inicializado en 0. Si 0 fuese
							 * menor que el total del numero de meses igual a 12 suma al contador +1.
							 * 
							 * #Condición 1: Si la fecha obtenida separada por " ", "de", "-" y "o".
							 * 	es igual a un mes en el listado de meses (Es decir si es Junio y es igual a Junio).
							 * 	recoge el valor de la posición del mes correspondiente. De esta forma podemos
							 * 	identificar que "Junio" es 06...
							 * 
							 * #Condicion 2: Si el valor del mes no es igual a cero (Captura de errores).
							 * 
							 * #Condición 3: Si el valor del mes es menor a 10. Se le añade un 0 a la izquierda.
							 * 	Se utiliza para mostrar en vez de 6 a 06, si el mes obtenido es 10 no se añade ningun cero.
							 * 
							 * @break para la secuencia del bucle de contador de meses. El valor se almacena en la variable aux_mes.
							 * @author Jesús Blanco Antoraz
							 */
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
							/**
							 * Contador de dias. Condición: i2 contador inicializado en 0. Si 0 fuese
							 * menor que el total del numero de dias igual a 31 suma al contador +1.
							 * La función principal es de proveer a los días de la numeración del cero
							 * acompañado a la derecha si fuese preciso.
							 * 
							 * #Condición 1: Si el día obtenido equivale a su mismo valor 01 a 1. 
							 * 
							 * #Condicion 2: Si el valor del día no es igual a cero (Captura de errores).
							 * 
							 * #Condición 3: Si el valor del día es menor a 10. Se le añade un 0 a la izquierda.
							 * 	Se utiliza para mostrar en vez de 6 a 06, si el día obtenido es 10 no se añade ningun cero.
							 * 
							 * @break para la secuencia del bucle de contador de días. El valor se almacena en la variable aux_dia.
							 * @author Jesús Blanco Antoraz
							 */
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
							
							
							// Condiciones de contador fechas por fecha de origen. :: Anho ++ Contador. ::
							/**
							 * ¿Duplicado es necesario?
							 * Contador de meses. Condición: i2 contador inicializado en 0. Si 0 fuese
							 * menor que el total del numero de meses igual a 12 suma al contador +1.
							 * 
							 * #Condición 1: Si la variable aux_pos_break es nula. Punto de control.
							 * 
							 * #Condicion 2: Si los valores de la fecha son iguales a la posición del mes obtenido.
							 * 	Se asigna a la variable aux_pos_break = mes obtenido.
							 * 
							 * @break para la secuencia del bucle de contador de meses. El valor se almacena en la variable aux_pos_break.
							 * @author Jesús Blanco Antoraz
							 */
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
							/**
							 * Creamos un variable para almacenar el valor del mes auxiliar.
							 * #Condicion 1: Si la posición aux_pos_break no es un valor nulo procede
							 * 	asigna el valor del mes devuelto a la variable aux_mes_devuelto
							 * 	es decir devuelve el mes ¿Cuál? Si el mes es Junio == 06
							 * 	asigna a la variable de mes inicial el valor de mes devuelto.
							 * 	el aux_contador_meses_seria = aux_contador_meses_inicial.
							 * 	El valor por defecto es 0. => Caso de error.
							 */
							int aux_mes_devuelto;
							if (aux_pos_break != null) {
								//aux_mes_devuelto = new ScramblerMatchs_Atletico_Madrid().DevuelvemeElMesCorrespondiente(aux_pos_break); // 1Âº 7.
								aux_mes_devuelto = DameDatosFecha.suMes(aux_pos_break); // 1Âº 7.
								aux_contador_meses_inicial = aux_mes_devuelto;
								aux_contador_meses_serie = aux_contador_meses_inicial;
							}

							// :: Realiza el cambio de mes ::
							/**
							 * Anotaciones: Contadores inicializados en 0. aux_contador_meses_serie =
							 * aux_mes; Se basarÃ¡ en el primer mes obtenido de la lista.
							 *
							 * Bugs posibles: Si hay una jornada en el aÃ±o 2022 acabada por 12 y la
							 * siguiente en caso de ser Ãºnica es 12 del aÃ±o 2023.
							 *
							 * #Condición 1.
							 * 	No se producirÃ¡ la suma++ de aÃ±o. [Probabilidad: Extremadamente rara].
							 * 	Si el contador de meses anterior por defecto es 0. es Mayor que aux_mes.
							 * 	Siendo aux_mes = 6.
							 * 		asigna al año++ incrementa su valor es decir pasa de ser 2021 a 2022.
							 * 		el contador de meses anterior pasa a ser el valor actual 6.
							 * 		y el contador aux_contador_meses_serie pasa a ser el mes actual 6.
							 * si no..(2).
							 * 	el valor de contador de meses serie anterior será el mismo que el contador
							 * 	de meses serie actual.
							 * si no..(3).
							 * 	el valor aux_contador_meses_serie = el mes actual 6.
							 * 
							 * Explicación teorica:
							 * Siendo el mes actual Diciembre mes 12. Y siendo el mes siguiente Enero 01. Se asigna
							 * un contador del mes anterior 12 si el mes anterior era mayor quiere decir que el mes
							 * que ha pasado conlleva un incremento del año. Se almacena los contadores con el 
							 * mes que haya progresado.
							 * 	
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
							/**
							 * Asigna el valor del mes correspondiente clasificado y reestructurado en una
							 * fecha adecuada. La fecha anterior pasada como valor: 19 de enero.
							 * Pasa a ser traducida como:  19/01/año correspondiente Ej. 2022
							 * 
							 * #Condición 1: Si el valor del día es menor a 10. Se le añade un 0 a la izquierda.
							 * 	Se utiliza para mostrar en vez de 6 a 06, si el día obtenido es 10 no se añade ningun cero.
							 */
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

					/**
					 * Anula cualquier posibilidad de dato erroneo.
					 */
				} else {
				}

			}
			//System.out.println(lists.get(c));
		}
	}

	/**
	 * Realiza la lectura de los datos particionados en el listado
	 * Lectura individual de cada elemento almacendao en la lista.
	 */
	public void LecturaDatosParticionados() {
		// :: Lectura ::
		System.out.println("\n[LECTURA DE DATOS PARTICIONADOS]\n");
		for (List<String> list : lists) {
			System.out.println(list);
		}
		System.out.println("\n");
	}

	/**
	 * Realiza la lectura de los datos particionados en el listado
	 * Lectura individual de cada elemento almacendao en la lista.
	 * Lectura optimizada mas rápida.
	 */
	public void LecturaDatosParticionadosSimple() {
		// :: Lectura ::
		System.out.println("\n[LECTURA DE DATOS PARTICIONADOS]\n");
		for (int x=0; x<lists.size(); x++) {
			System.out.println(lists.get(x));
		}
		System.out.println("\n");
	}


	/**
	 * Filtra los partidos que contengan una fecha "Sin confirmar".
	 * Los eventos que contengan esta etiqueta no serán transformado
	 * como objeto Partido.
	 * 		[idpartido]
	 *		[compe] :: competiciï¿½n.
	 *		[jor] :: jornada.
	 *		[fechapa] :: fecha partido.
	 *		[hora] :: hora_partido.
	 *		[estad] :: estadio.
	 * #Condición 1:
	 * 	Si la fila del objeto selecionado en la columna correspondiente de fecha
	 * 	no contiene "Sin confirmar". La fila del listado con datos clasificados será
	 * 	transformada a un Objeto de tipo Partido.
	 * 
	 * #Condición 2:
	 * 	Realiza la comparación mediante un consulta al servidor. Comprueba que el dato transformado
	 * 	en el listado de tipo "indentificador" no exista en la base de datos.
	 * 	Procede a publicar el tipo de dato de información en la base de datos.
	 * 	La fecha es transformada al formato de tipo "fecha americana". 
	 * @throws JSchException
	 */
	public void agregarCamposConfirmardos() throws JSchException {
		for (int a=0; a<lists.size(); a++) {
			//	:: Filtra los partidos que contengan **Sin confirmar***, para evitar duplicaciÃ³n de eventos.
			if (!lists.get(a).get(6).toString().equals("Sin confirmar")) {
				/*
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
				*/
				try {
					if (new DbManagerSSH().getDataPerID("wandametropolitano_partidos",lists.get(a).get(0).toString()) == false) {
						if(checkConfirmation.size()<=0) {
							checkConfirmation.add(true);
						}
						
						new DbManagerSSH().postData(	
								"wandametropolitano_partidos",
								lists.get(a).get(0).toString(), 
								lists.get(a).get(1).toString(), 
								lists.get(a).get(2).toString(), 
								lists.get(a).get(3).toString(), 
								lists.get(a).get(4).toString(), 
								String.valueOf(new DameFecha().dameDateAmericana(lists.get(a).get(5).toString())), 
								lists.get(a).get(6).toString(), 
								lists.get(a).get(7).toString());
					}
				} catch (Exception e) {
					System.err.println("[Error] | La operación con DbManager no pudo completarse +[INFO | log]");
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	/**
	 * Optimización del programa limpia todas las listas auxiliares
	 * para liberar memoria en el programa.
	 */
	public void CleanLists() {
		partidos.clear();
		partidos_con_fecha.clear();
		lists.clear();
	}
	

	/**
	 * Secuenciación de ejecución de funciones en el programa.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new ScramblerMatchs_WandaMetropolitano().ExtractDataFromURL();
		new ScramblerMatchs_WandaMetropolitano().ExtractFirstDatedMatchFromURL();
		new ScramblerMatchs_WandaMetropolitano().IdentifDatosParticionados();
		new ScramblerMatchs_WandaMetropolitano().ClasifDatosFirstDatedMatch();
		new ScramblerMatchs_WandaMetropolitano().ClasifDatos();
		new ScramblerMatchs_WandaMetropolitano().LecturaDatosParticionadosSimple();
		new ScramblerMatchs_WandaMetropolitano().agregarCamposConfirmardos();
		new ScramblerMatchs_WandaMetropolitano().CleanLists();
		
	}
}