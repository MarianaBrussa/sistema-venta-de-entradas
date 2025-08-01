package ar.edu.ungs.prog2.ticketek;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Teatro extends Sede{
	/**
	 * Teatro: Sede con asientos numerados y sectores (sin puestos de venta).
	 *
	 * IREP:
	 * - Sectores, capacidad y porcentajeAdicional tienen la misma longitud.
	 * - Los nombres de sectores no se repiten.
	 * - El mapa asientosOcupados lleva por sector el set de asientos ocupados.
	 */
	private int asientosPorFila;
	private String[] sectores;
	private int[] capacidad;
	private int[] porcentajeAdicional;
	private Map<String, Set<Integer>> asientosOcupados = new HashMap<>();

	public Teatro(String nombre, String direccion, int capacidadMaxima, int asientosPorFila,
		String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
		super(nombre, direccion, capacidadMaxima);
		validarDatos(sectores, capacidad, porcentajeAdicional);
		this.asientosPorFila = asientosPorFila;
		this.sectores = sectores;
		this.capacidad = capacidad;
		this.porcentajeAdicional = porcentajeAdicional;
	}
	//valida los datos del teatro
	private void validarDatos(String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
		
		if (sectores == null || capacidad == null || porcentajeAdicional == null) {
			throw new IllegalArgumentException("Ningún parámetro puede ser null.");
		}
		
		if (sectores.length != capacidad.length || sectores.length != porcentajeAdicional.length) {
			throw new IllegalArgumentException("Las longitudes de sectores, capacidad y porcentajeAdicional deben coincidir.");
		}
	}
	// para validar que el sector usado esté dentro del arreglo sectores del teatro
	private void validarSector(String sector) {
		
		boolean encontrado = false;
		
		for (String s : sectores) {
			if (s.equalsIgnoreCase(sector)) {
				encontrado = true;
				break;
			}
		}
		
		if (!encontrado) {
			throw new IllegalArgumentException("Sector no válido para este teatro: " + sector);
		}
	}
	
	@Override
	public double calcularPrecioBase(String sector) {
		
		validarSector(sector);
		
		for (int i = 0; i < sectores.length; i++) {
			if (sectores[i].equalsIgnoreCase(sector)) {
				double adicional = porcentajeAdicional[i] / 100.0;
				return getPrecioBase() * (1 + adicional);
			}
		}
		
		throw new IllegalArgumentException("Sector no válido: " + sector);
	}
	
	@Override
	public double calcularPrecioBase(double precioBase, String sector) {
		
		validarSector(sector);
		
		for (int i = 0; i < sectores.length; i++) {
			if (sectores[i].equalsIgnoreCase(sector)) {
				double adicional = porcentajeAdicional[i] / 100.0;
				return precioBase * (1 + adicional);
			}
		}
		
		throw new IllegalArgumentException("Sector no válido: " + sector);
	}
	//metodo sobreescrito 
	public List<IEntrada> generarEntradas(Funcion funcion, String sector, int[] asientos, Usuario comprador) {
		
		validarSector(sector);
		
		if (!verificarDisponibilidad(sector, asientos)) {
			throw new RuntimeException("Algunos asientos no están disponibles");
		}
		
		ocuparAsientos(sector, asientos);
		
		List<IEntrada> entradas = new ArrayList<>();
		double precioBase = calcularPrecioBase(sector);
		
		for (int asiento : asientos) {
			IEntrada entrada = new Entrada(comprador.getEmail(),funcion.getNombre(), funcion.getSede(),funcion.getFechaFormateada(), precioBase, sector, asiento);
			entradas.add(entrada);
		}
		
		return entradas;
	}
	
	public boolean verificarDisponibilidad(String sector, int[] asientos) {
		
		validarSector(sector);
		
		for (int asiento : asientos) {
			if (asientoOcupado(sector, asiento)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean asientoOcupado(String sector, int asiento) {
		return asientosOcupados.getOrDefault(sector, new HashSet<>()).contains(asiento);
	}
	
	public void ocuparAsientos(String sector, int[] asientos) {
		
		validarSector(sector);
		Set<Integer> ocupados = asientosOcupados.computeIfAbsent(sector, k -> new HashSet<>());
		
		for (int asiento : asientos) {
			ocupados.add(asiento);
		}
	}
	
	@Override
	public String tipoSede() {
		return "Teatro";
	}
	
	@Override
	public Map<String, Integer> getCapacidades() {
		
		Map<String, Integer> mapa = new TreeMap<String, Integer>(Comparator.reverseOrder());
		
		for (int i = 0; i < sectores.length; i++) {
			mapa.put(sectores[i], capacidad[i]);
		}
		
		return mapa;
	}
	
	@Override
	public List<IEntrada> generarEntradas(Funcion funcion, int cantidad, Usuario comprador) {
		// Implementar según necesidad
			return null;
	}
}
