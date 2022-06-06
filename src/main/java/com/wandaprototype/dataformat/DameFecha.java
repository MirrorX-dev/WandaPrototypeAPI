package com.wandaprototype.dataformat;

import java.util.StringTokenizer;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Coleción de formateo de fechas.
 * Funciones que permiten convertir formatos
 * de distintas fechas. 
 */
public class DameFecha {

	@SuppressWarnings("unused")
	/**
	 * Adjunta valor de fecha y realiza un StringTokenizer
	 * Ejemplo: 2020 03 10 a 10 03 2020
	 * @param fechaAlli
	 * @return
	 */
	public String dameDateUtilAqui(java.util.Date fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, " ");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();
		String cuatro = st.nextToken();
		String cinco = st.nextToken();
		String seis = st.nextToken();

		String aqui = tres + " " + dos + " " + cuatro + " " + seis;

		return aqui;
	}

	/**
	 * Adjunta valor de fecha y realiza un StringTokenizer
	 * Ejemplo: 2020/03/10 00:00 a 10/03/2020 00:00h
	 * @param fechaAlli
	 * @return 00/00/0000 00:00h
	 */
	public String dameTimestampAqui(Timestamp fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, " ");
		String uno = st.nextToken();
		String dos = st.nextToken();
		st = new StringTokenizer(uno, "-");
		String tres = st.nextToken();
		String cuatro = st.nextToken();
		String cinco = st.nextToken();

		String aqui = cinco + "/" + cuatro + "/" + tres + " " + dos;

		return aqui;
	}

	
	/**
	 * Adjunta valor de fecha y realiza un StringTokenizer
	 * Ejemplo: 10-08-2022 00:00 a 2022-08-10 00:00 Formato Americano.
	 * @param fechaAlli
	 * @return 0000/00/00 00:00h
	 */
	public Timestamp dameTimestampAlli(String fechaTAqui) {
		StringTokenizer st = new StringTokenizer(fechaTAqui, " ");
		
		String uno = st.nextToken();
		st = new StringTokenizer(uno, "/");
		String dos = st.nextToken();
		st = new StringTokenizer(dos, "/");
		String tres = st.nextToken();
		st = new StringTokenizer("", "");

		String alli = tres + "-" + dos + "-" + uno+" 00";
		Timestamp fechaAlli = Timestamp.valueOf(alli);

		return fechaAlli;
	}

	/**
	 * Adjunta valor de fecha y realiza un StringTokenizer
	 * Ejemplo: 10-08-2022 a 2022-08-10 Formato Americano.
	 * @param fechaAlli
	 * @return 0000-00-00
	 */
	public Date dameDateAlli(String fechaAqui) {
		StringTokenizer st = new StringTokenizer(fechaAqui, "/");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();

		String alli = tres + "-" + dos + "-" + uno;
		Date fechaAlli = Date.valueOf(alli);

		return fechaAlli;
	}

	/**
	 * Adjunta valor de fecha y realiza un StringTokenizer
	 * Ejemplo: 2022-08-10 a 10-08-2022 Formato Americano.
	 * @param fechaAlli
	 * @return 00-00-0000
	 */
	public String dameDateAqui(Date fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, "-");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();

		String aqui = tres + "/" + dos + "/" + uno;

		return aqui;
	}
	
	/**
	 * Adjunta valor de fecha y realiza un StringTokenizer
	 * Ejemplo: 10-08-2022 a 2022-08-10 Formato Americano.
	 * @param fechaAlli
	 * @return 0000-00-00
	 */
	public String dameDateAmericana(String fecha) {
		String cadena = fecha.toString();
		StringTokenizer st = new StringTokenizer(cadena, "/");
		String dia = st.nextToken();
		String mes = st.nextToken();
		String anho = st.nextToken();

		String aqui = anho + "-" + mes + "-" + dia;
		return aqui;
		
	}

}
