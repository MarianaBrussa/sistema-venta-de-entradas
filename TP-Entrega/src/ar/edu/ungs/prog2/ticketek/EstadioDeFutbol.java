package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;
import java.util.List;

public class EstadioDeFutbol extends Sede {
	/**
	 * EstadioDeFutbol: Sede sin asientos numerados, solo sector "campo".
	 * 
	 * IREP:
	 * - La capacidad total es fija y mayor que cero.
	 * - Las entradas se generan solo si hay capacidad disponible.
	 */
	private int capacidadTotal;
	private int entradasVendidas;
	private int codigoSecuencia;  // Para generar código único 
	
	public EstadioDeFutbol(String nombre, String direccion, int capacidad) {
		super(nombre, direccion, capacidad);
		this.capacidadTotal = capacidad;
		this.entradasVendidas = 0;
		this.codigoSecuencia = 1;
	}
	/**
     * Genera entradas para el sector "campo".
     */
	@Override
	public List<IEntrada> generarEntradas(Funcion funcion, int cantidad, Usuario comprador) {
		
		if (cantidad > capacidadDisponible()) {
			throw new RuntimeException("No hay suficientes entradas disponibles para la función");
		}
		
		List<IEntrada> entradas = new ArrayList<>();
		
		for (int i = 0; i < cantidad; i++) {
			entradasVendidas++;
			int codigoUnico = generarCodigoUnico();
			
			double precio = funcion.getPrecioBase();// Tomamos el precio base de la función
			IEntrada entrada = new Entrada(comprador.getEmail(),funcion.getNombre(), funcion.getSede(),funcion.getFechaFormateada(), precio );
			entradas.add(entrada);
		}
		return entradas;
	}
	 
	private int generarCodigoUnico() {
		return codigoSecuencia++;
	}
	
	private int capacidadDisponible() {
		return capacidadTotal - entradasVendidas;
	}
	
	public int getCapacidad() {
		return capacidadTotal;
	}
	
	public int getEntradasVendidas() {
		return entradasVendidas;
	}
	
	@Override
	public double calcularPrecioBase() {
		return super.getPrecioBase();
	}
	
	@Override
	public String tipoSede() {
		return "Estadio de Futbol";
	}
}
