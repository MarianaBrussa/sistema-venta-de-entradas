package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Funcion {
	/**
	 * TAD Funcion: representa una función única de un espectáculo en una fecha y sede.
	 * 
	 * IREP:
	 * - La fecha es única para el espectáculo (garantizado por Espectaculo).
	 * - La sede no es nula y corresponde a una instancia de Sede válida.
	 * - El precio base es mayor que cero.
	 */
	private String nombre; // Nombre del espectáculo
	private LocalDate fecha;
	private double precioBase;
	private Sede sede;
	private int comun, baja, alta, vip = 0;

	public Funcion(String nombre, String fecha, Sede sede, double precioBase) {
		this.nombre = nombre;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		this.fecha = LocalDate.parse(fecha, formatter); // para poder saber si se paso la fecha pasada como cadena
		this.sede = sede;
		this.precioBase = precioBase;
	}
	/**
     * Vende una o varias entradas para estadio (campo).
     */
	public List<IEntrada> venderEntradas(int cantidad, Usuario comprador) {
		
		if (!(sede instanceof EstadioDeFutbol)) {
			throw new RuntimeException("No es un estadio de futbol");
		}
		
		EstadioDeFutbol estadio = (EstadioDeFutbol) sede;
		return estadio.generarEntradas(this, cantidad, comprador);
	}
	/**
     * Vende entradas para teatro.
     */
	public List<IEntrada> venderEntradas(String sector, int[] asientos, Usuario comprador) {
		
		if (!(sede instanceof Teatro)) {
			throw new RuntimeException("La función no es para teatro");
		}
		
		Teatro teatro = (Teatro) sede;
		
		if (!teatro.verificarDisponibilidad(sector, asientos)) {
			throw new RuntimeException("Asientos no disponibles");
		}
		
		List<IEntrada> entradas = new ArrayList<>();
		
		for (int asiento : asientos) {
			
			double precio = teatro.calcularPrecioBase(sector);
			IEntrada entrada = crearEntrada(comprador, precio, sector, asiento);
			entradas.add(entrada);
		}
		
		teatro.ocuparAsientos(sector, asientos);
		return entradas;
	}
	/**
     * Vende entradas para miniestadio.
     */
	public List<IEntrada> venderEntradasMiniEstadio(String sector, int[] asientos, Usuario comprador) {
		
		if (!(sede instanceof MiniEstadio)) {
			throw new RuntimeException("La función no es para MiniEstadio");
		}
		
		MiniEstadio mini = (MiniEstadio) sede;
		
		if (!mini.verificarDisponibilidad(sector, asientos)) {
			throw new RuntimeException("Asientos no disponibles");
		}
		
		List<IEntrada> entradas = new ArrayList<>();
		
		for (int asiento : asientos) {
			double precio = mini.calcularPrecioBase(sector);
			IEntrada entrada = crearEntrada(comprador, precio, sector, asiento);
			entradas.add(entrada);
		}
		
		mini.ocuparAsientos(sector, asientos);
		return entradas;
	}
	//para generar la entrada en vender de teatro y miniEstadio
	private Entrada crearEntrada(Usuario comprador, double precio, String sector, int asiento) {
		return new Entrada(comprador.getEmail(), nombre, sede.getNombre(), fecha.format(DateTimeFormatter.ofPattern("dd/MM/yy")), precio, sector, asiento);
	}
	
	public String getSede() {
		return sede.getNombre();
	}
	//devuelve el nombre del espectaculo
	public String getNombre() {
		return nombre;
	}
	
	public Sede getSedeObj() {
		return sede;
	}
	
	public double getPrecioBase() {
		return precioBase;
	}
	
	public String getFechaFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		return fecha.format(formatter);
	}
	
	public int getCapacidad() {
		return sede.getCapacidad();
	}
	//detecta el sector incrementa los asientos usados
	public void detectarSector(String sector) {
		if (sector.contains("Común")) {
			comun++;
		}
		else if (sector.contains("Baja")) {
			baja++;
		}
		else if (sector.contains("Alta")) {
			alta++;
		}
		else if (sector.contains("VIP")) {
			vip++;
		}
	}
	//lista los sectores de cada sede con capacidad y entradas vendidas 
	public List<String> listarSector() {
		List<String> lista = new ArrayList<String>();
		
		if (sede instanceof EstadioDeFutbol estadio) {
			int capacidad = estadio.getCapacidad();
			int entradasVendidas = estadio.getEntradasVendidas();
			String s = "CAMPO: " + String.valueOf(entradasVendidas) + "/" + String.valueOf(capacidad);
			lista.add(s);
			
			return lista;
		}
		Map<String, Integer> capacidades = sede.getCapacidades();
		
		for (Map.Entry<String, Integer> sector: capacidades.entrySet()) {
			String s = sector.getKey() + ": " + cantidadVendidaPorSector(sector.getKey()) + "/" + sector.getValue();
			lista.add(s);
		}	
		return lista;
	}
	//aux que usa listarSector()
	private int cantidadVendidaPorSector(String sector) {
		
		if (sector.contains("Común")) {
			return comun;
		}
		else if (sector.contains("Baja")) {
			return baja;
		}
		else if (sector.contains("Alta")) {
			return alta;
		}
		else if (sector.contains("VIP")) {
			return vip;
		}
		return 0;
	}
}
