package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Espectaculo {
	/**
	 * TAD Espectaculo: representa un espectáculo único con sus funciones asociadas.
	 * 
	 * IREP:
	 * - El nombre es único y no nulo.
	 * - Las claves del mapa funciones son fechas (String en formato dd/MM/yy), únicas para este espectáculo.
	 * - No existen dos funciones del mismo espectáculo para la misma fecha.
	 */
	private String nombre;
	private Map<String, Funcion> funciones;//clave:fecha
	private int codigoEspectaculo;
	
	public Espectaculo(String nombreEspectaculo) {
		this.funciones = new HashMap<>();
		this.nombre = nombreEspectaculo;
		this.codigoEspectaculo = codigoEspectaculo;
	}
	
	public void agregarFuncion(String nombreEspectaculo, String fecha, Sede sede, double precioBase) {
		
		if (funciones.containsKey(fecha)) {
			throw new RuntimeException("Ya hay una función en esa fecha"); 
		}
		funciones.put(fecha, new Funcion(nombreEspectaculo, fecha, sede, precioBase));
	}
	
	// Devuelve la función asociada a la fecha dada o null si no existe.
	public Funcion getFuncion(String fecha) {
		return funciones.get(fecha);
	}
	public String nombre() {
		return nombre;
	}
	//Devuelve las funciones con su sector, capacidad, fecha, sede.
	public String listarFunciones() {
		
		StringBuilder sb = new StringBuilder();
		for (Funcion f: funciones.values()) {
			String capacidades = String.join(" | ", f.listarSector());
			sb.append(" - (").append(f.getFechaFormateada()).append(") ").append(f.getSede()).append(" - ").append(capacidades).append("\n");
		}	
		return sb.toString();
	}
	
}
