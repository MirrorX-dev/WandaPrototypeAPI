package com.wandaprototype.dataformat;

import java.util.ArrayList;
import java.util.Arrays;

public interface DameDatosFecha {

	// Lista pre-definidida para constatar texto de los memes.
	/*
	static ArrayList<String> meses = new ArrayList<String>() {
		private static final long serialVersionUID = 3286704113498260770L;
		{
			add("enero");		//0
			add("febrero");		//1
			add("marzo");		//2
			add("abril");		//3
			add("mayo");		//4
			add("junio");		//5
			add("julio");		//6
			add("agosto");		//7
			add("septiembre");	//8
			add("octubre");		//9
			add("noviembre");	//10
			add("diciembre");	//11
		}
	};
	*/
	
	static ArrayList<String> meses = new ArrayList<String>(Arrays.asList("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","noviembre","diciembre"));
	
	static ArrayList<String> dias = new ArrayList<String>(
			Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
						  "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"));
	
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
