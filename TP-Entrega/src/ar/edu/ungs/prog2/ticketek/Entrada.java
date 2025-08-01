package ar.edu.ungs.prog2.ticketek;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Entrada implements IEntrada {
	/**
	 * Entrada: implementación de IEntrada para estadio, teatro o miniestadio.
	 * 
	 * IREP:
	 * - codigoUnico es único para cada entrada.
	 * - Si sector es null, es "campo" (estadio). Si no, contiene sector y ubicación (teatro/miniestadio).
	 */

	private static int siguienteCodigo = 1000;
	private int codigoUnico, ubicacion;
	private String emailUsuario, nombreEspectaculo, sector;
	private LocalDate fechaFuncion;
	private double precio;
	private String nombreSede;
	
	// Entrada para estadio
	public Entrada(String emailUsuario, String nombreEspectaculo, String nombreSede, String fechaFuncion, double precio) {
		this.codigoUnico = siguienteCodigo++;
		this.emailUsuario = emailUsuario;
		this.nombreEspectaculo = nombreEspectaculo;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		this.fechaFuncion = LocalDate.parse(fechaFuncion, formatter); 
		this.precio = precio;
		this.nombreSede = nombreSede;
		this.sector = null;
		this.ubicacion = -1;
	}
	
	// Entrada para teatro o miniestadio
	public Entrada(String emailUsuario, String nombreEspectaculo, String nombreSede, String fechaFuncion, double precio, String sector, int ubicacion) {
		this.codigoUnico = siguienteCodigo++;
		this.emailUsuario = emailUsuario;
		this.nombreEspectaculo = nombreEspectaculo;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		this.fechaFuncion = LocalDate.parse(fechaFuncion, formatter); 
		this.precio = precio;
		this.sector = sector;
		this.ubicacion = ubicacion; 
		this.nombreSede = nombreSede;
	}

	@Override
	public double precio() {
		return precio;
	}
	
	@Override
	public String ubicacion() {
		
		if (sector == null) {
			return "CAMPO";
		}
		
		else {
			int fila = ubicacion / 100;
			int asiento = ubicacion % 100;
			return sector + " f:" + fila + " a:" + asiento;
		}
	}
	
	
	public LocalDate getFecha() {
		return fechaFuncion;
	}
	
	//formateamos la fecha localDate para usar como String
	public String getFechaFormateada() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		return getFecha().format(formatter);
	}
	
	public String getEmailUsuario() {
		return emailUsuario;
	}

	public String getNombreEspectaculo() {
		return nombreEspectaculo;
	}

	public String getNombreSede() {
		return nombreSede;
	}

	public int getCodigoUnico() {
		return codigoUnico;
	}
	
	public double getPrecio() {
		return precio();
	}
	
	public String getSector() {
		return sector;
	}
	@Override
	public String toString() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		
		String fechaFormateada = fechaFuncion.format(formatter);
		
		if (fechaFuncion.isBefore(LocalDate.now())) {
			fechaFormateada += " P";
		}
		
		return codigoUnico + " - " + nombreEspectaculo + " - " + fechaFormateada + " - " + nombreSede + " - " + ubicacion();
	}
	
}
