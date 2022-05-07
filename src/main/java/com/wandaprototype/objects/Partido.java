package com.wandaprototype.objects;

import java.util.ArrayList;

public class Partido {
	
	public static ArrayList<Partido> partidos = new ArrayList<Partido>();
	
	public String id_partido;
	public String competicion;
	public String equipolocal;
	public String equipovisitante;
	public String jornada;
	public String fechapartido;
	public String horapartido;
	public String estadiopartido;
	
	public static ArrayList<Partido> getPartidos() {
		return partidos;
	}
	public static void setPartidos(ArrayList<Partido> partidos) {
		Partido.partidos = partidos;
	}
	public String getId_partido() {
		return id_partido;
	}
	public void setId_partido(String id_partido) {
		this.id_partido = id_partido;
	}
	public String getCompeticion() {
		return competicion;
	}
	public void setCompeticion(String competicion) {
		this.competicion = competicion;
	}
	public String getEquipolocal() {
		return equipolocal;
	}
	public void setEquipolocal(String equipolocal) {
		this.equipolocal = equipolocal;
	}
	public String getEquipovisitante() {
		return equipovisitante;
	}
	public void setEquipovisitante(String equipovisitante) {
		this.equipovisitante = equipovisitante;
	}
	public String getJornada() {
		return jornada;
	}
	public void setJornada(String jornada) {
		this.jornada = jornada;
	}
	public String getFechapartido() {
		return fechapartido;
	}
	public void setFechapartido(String fechapartido) {
		this.fechapartido = fechapartido;
	}
	public String getHorapartido() {
		return horapartido;
	}
	public void setHorapartido(String horapartido) {
		this.horapartido = horapartido;
	}
	public String getEstadiopartido() {
		return estadiopartido;
	}
	public void setEstadiopartido(String estadiopartido) {
		this.estadiopartido = estadiopartido;
	}
	
	@Override
	public String toString() {
		return "Partido [id_partido=" + id_partido + ", competicion=" + competicion + ", equipolocal=" + equipolocal
				+ ", equipovisitante=" + equipovisitante + ", jornada=" + jornada + ", fechapartido=" + fechapartido
				+ ", horapartido=" + horapartido + ", estadiopartido=" + estadiopartido + "]";
	}
	
	public Partido(String id_partido, String competicion, String equipolocal, String equipovisitante, String jornada,
			String fechapartido, String horapartido, String estadiopartido) {
		super();
		this.id_partido = id_partido;
		this.competicion = competicion;
		this.equipolocal = equipolocal;
		this.equipovisitante = equipovisitante;
		this.jornada = jornada;
		this.fechapartido = fechapartido;
		this.horapartido = horapartido;
		this.estadiopartido = estadiopartido;
	}
	
	public Partido() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
}
