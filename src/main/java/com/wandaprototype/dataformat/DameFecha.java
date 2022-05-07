package com.wandaprototype.dataformat;

import java.util.StringTokenizer;
import java.sql.Date;
import java.sql.Timestamp;

public class DameFecha {

	@SuppressWarnings("unused")
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

	public Date dameDateAlli(String fechaAqui) {
		StringTokenizer st = new StringTokenizer(fechaAqui, "/");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();

		String alli = tres + "-" + dos + "-" + uno;
		Date fechaAlli = Date.valueOf(alli);

		return fechaAlli;
	}

	public String dameDateAqui(Date fechaAlli) {
		String cadena = fechaAlli.toString();
		StringTokenizer st = new StringTokenizer(cadena, "-");
		String uno = st.nextToken();
		String dos = st.nextToken();
		String tres = st.nextToken();

		String aqui = tres + "/" + dos + "/" + uno;

		return aqui;
	}
	
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
