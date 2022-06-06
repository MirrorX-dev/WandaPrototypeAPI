package com.wandaprototype.dataformat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase de tipo interfaz, definimos listado de meses, dás para manejar estos datos
 * de forma optimizada y ser utilizados por cualquier Scrambler.
 * @author MirrorX
 */
public interface DameDatosFecha {

	static ArrayList<String> meses = new ArrayList<String>(Arrays.asList("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"));	
	static ArrayList<String> dias = new ArrayList<String>(
			Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
						  "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"));
	
	/**
	 * Permite convertir y hacer uso de los formatos de día númericos convertidos
	 * de 0 a 01. Si fuese necesario.
	 * @param dia
	 * @return 01 ó 1
	 */
	public static Integer formatoDia(int dia) {
		int aux_dia = 0;
		if (dia != 0) {
			if (dia > 9) {
				aux_dia = dia;
			} else {
				// Poner el "0"
				aux_dia = Integer.valueOf(0+String.valueOf(dia));
			}
		}
		return aux_dia;
	}
	
	/**
	 * Permite convertir y hacer uso de los formatos de mes númericos convertidos
	 * de 0 a 01. Si fuese necesario. 
	 * @param input
	 * @return 01 o 10
	 */
	public static int suMes(String input) {
		int aux_mes = 0; //Default Value. 0 ==> Not Found.
		for (int i=0; i<meses.size(); i++) {
			if (input.toString().equalsIgnoreCase(meses.get(i))) {
				aux_mes = i+1;	//Enero
				break;
			}
		}	
		return aux_mes;
	}
	
	/**
	 * Permite convertir y hacer uso de los formatos de día númericos convertidos
	 * de 0 a 01. Si fuese necesario.
	 * @param input
	 * @return 01 o 10.
	 */
	public static int suDia(String input) {
		int aux_dia = 0; //Default Value. 0 ==> Not Found.
		for (int i=0; i<dias.size(); i++) {
			if (input.toString().equalsIgnoreCase(dias.get(i))) {
				aux_dia = i+1;	//Día 0 => 01
				break;
			}
		}	
		return aux_dia;
	}
}
