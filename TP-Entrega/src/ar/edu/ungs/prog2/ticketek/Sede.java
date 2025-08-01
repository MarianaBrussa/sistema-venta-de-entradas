package ar.edu.ungs.prog2.ticketek;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Sede {
	/**
	 * TAD Sede: clase abstracta para sedes de espectáculos (Estadio, Teatro, MiniEstadio).
	 *
	 * IREP:
	 * - El nombre es único y no nulo.
	 * - La capacidad es mayor que 0.
	 * - Los métodos abstractos son implementados en las subclases.
	 */
	private String nombre, direccion;
	private int capacidad;
	private double precioBase;

	public Sede(String nombre, String direccion, int capacidad) {
		this.nombre = nombre;
		this.direccion = direccion; 
		this.capacidad = capacidad;
		this.precioBase = 1000;
	}
	
	
	public String getNombre() { 
		return nombre; 
	}
	
	public double getPrecioBase() {
		return precioBase;
	}
	
	public double calcularPrecioBase() {
		return precioBase;
	}
	
	public double calcularPrecioBase(String sector) {
		return precioBase;
	}
	
	public double calcularPrecioBase(double precioBase, String sector) {
		return precioBase;
	}
	/**
     * Genera entradas para una función (campo, para estadio).
     */
	public abstract List<IEntrada> generarEntradas(Funcion funcion, int cantidad, Usuario comprador) ;
	/**
     * Genera entradas para funciones en sedes numeradas (teatro o miniestadio).
     * Si no está implementado, lanza excepción, se sobreescribe en las subClases
     */
	public List<IEntrada> generarEntradas(Funcion funcion, String sector, int[] asientos, Usuario comprador) {
		throw new UnsupportedOperationException("No implementado");
	}
	
	public int getCapacidad() {
		return capacidad;
	}
	
	public Map<String, Integer> getCapacidades() {
		return new HashMap<String, Integer>();
	}
	/**
     * Devuelve el tipo de sede (Estadio, Teatro, MiniEstadio)
     */
	public abstract String tipoSede();
}
