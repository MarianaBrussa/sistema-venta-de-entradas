package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MiniEstadio extends Sede {
	/**
	 * MiniEstadio: Sede numerada con sectores y puestos de venta.
	 *
	 * IREP:
	 * - Sectores, capacidad y porcentajeAdicional tienen la misma longitud.
	 * - Los nombres de sectores no se repiten.
	 * - El mapa asientosOcupados lleva por sector el set de asientos ocupados.
	 */
	private int asientosPorFila,cantidadDePuestos;
	private double precioConsumicion;
	private String[] sectores;
	private int[] capacidad;
	private int[] porcentajeAdicional;
	//(OBSOLETO)private static final List<String> tipoSector = List.of("Platea VIP", "Platea Común", "Platea Baja", "Platea Alta");
	private Map<String, Set<Integer>> asientosOcupados = new HashMap<>();

	public MiniEstadio(String nombre, String direccion, int capacidadMaxima, int asientosPorFila, int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
		super(nombre, direccion, capacidadMaxima);
		this.asientosPorFila = asientosPorFila;
		this.cantidadDePuestos = cantidadDePuestos;
		this.precioConsumicion=precioConsumicion;
		this.sectores = sectores;
		this.capacidad = capacidad;
		this.porcentajeAdicional = porcentajeAdicional;	
	}
	//se usa en generar entradas
	@Override
	public double calcularPrecioBase(String sector) {
		
		for (int i = 0; i < sectores.length; i++) {
			if (sectores[i].equalsIgnoreCase(sector)) {
				double adicional = porcentajeAdicional[i] / 100.0;
				return getPrecioBase() * (1 + adicional) + precioConsumicion;
			}
		}
		
		throw new IllegalArgumentException("Sector no válido: " + sector);
	}
	/*
     * Calcula el precio final para un sector y un precio base de función.
     * @param sector el sector elegido
     * @param precioBase el precio base de la función
     * @return el precio final (base + adicional+consumision)
     */
	@Override
	public double calcularPrecioBase(double precioBase, String sector) {
		
		for (int i = 0; i < sectores.length; i++) {
			if (sectores[i].equalsIgnoreCase(sector)) {
				double adicional = porcentajeAdicional[i] / 100.0;
				return precioBase * (1 + adicional) + precioConsumicion;
			}
		}
		
		throw new IllegalArgumentException("Sector no válido: " + sector);
	}
	
	@Override
	public List<IEntrada> generarEntradas(Funcion funcion, int cantidad, Usuario comprador) {
		
		List<IEntrada> entradas = new ArrayList<>();
		
		for (int i = 0; i < sectores.length && entradas.size() < cantidad; i++) {
			String sector = sectores[i];
			int capacidadSector = capacidad[i];
			List<Integer> disponibles = new ArrayList<>();
			
			for (int asiento = 1; asiento <= capacidadSector && disponibles.size() < cantidad - entradas.size(); asiento++) {
				if (!asientoOcupado(sector, asiento)) {
					disponibles.add(asiento);
				}
			}
			
			// Marcar asientos como ocupados
			for (int asiento : disponibles) {
				ocuparAsientos(sector, new int[] { asiento });
				
				double precio = calcularPrecioBase(sector);
				IEntrada entrada = new Entrada(comprador.getEmail(), funcion.getNombre(), funcion.getSede(), funcion.getFechaFormateada(), precio, sector, asiento);
				entradas.add(entrada);
			}
		}
		
		if (entradas.size() < cantidad) {
			throw new RuntimeException("No hay suficientes asientos disponibles.");
		}
		
		return entradas;
	}
	
	public boolean asientoOcupado(String sector, int asiento) {
		return asientosOcupados.getOrDefault(sector, new HashSet<>()).contains(asiento);
	}
	
	public void ocuparAsientos(String sector, int[] asientos) {
		
		Set<Integer> ocupados = asientosOcupados.computeIfAbsent(sector, k -> new HashSet<>());
		
		for (int asiento : asientos) {
			ocupados.add(asiento);
		}
	}
	
	@Override
	public String tipoSede() {
		return "Mini estadio";
	}
	
	public boolean verificarDisponibilidad(String sector, int[] asientos) {
		
		Set<Integer> ocupados = asientosOcupados.getOrDefault(sector, new HashSet<>());
		
		for (int asiento : asientos) {
			if (ocupados.contains(asiento)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Map<String, Integer> getCapacidades() {
		
		Map<String, Integer> mapa = new TreeMap<String, Integer>(Comparator.reverseOrder());
		
		for (int i = 0; i < sectores.length; i++) {
			mapa.put(sectores[i], capacidad[i]);
		}
		
		return mapa;
	}
}
