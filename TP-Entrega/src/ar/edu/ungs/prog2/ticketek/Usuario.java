package ar.edu.ungs.prog2.ticketek;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Usuario {
	/**
	 * Usuario: representa un usuario del sistema Ticketek.
	 * 
	 * IREP:
	 * - El email es único y no nulo.
	 * - La lista de entradas puede contener varias (incluso de espectáculos y fechas distintas).
	 */
	private String email,nombre,apellido,contrasenia;
	private Map<Integer, IEntrada> entradas;

	
	public Usuario(String email, String nombre, String apellido, String contrasenia) {
		this.email = email; 
		this.nombre = nombre; 
		this.apellido = apellido; 
		this.contrasenia = contrasenia;
		this.entradas = new HashMap<>();  
	}
	
	public String getEmail() { 
		return email; 
	}
	
	public String getContrasenia() { 
		return contrasenia; 
	}
	
	public void comprarEntrada() {
		
	}
	 /**
     * Agrega una lista de entradas al usuario (usado en compras múltiples).
     */
	public void registrarEntradas(List<IEntrada> nuevas) {
		for (IEntrada e: nuevas) {
			entradas.put(e.getCodigoUnico(), e);
		}
	}
	
	public Map<Integer, IEntrada> getEntradas() {
		return entradas;
	}
	 /**
     * Agrega una sola entrada al usuario.
     */
	public void agregarEntrada(IEntrada entrada) {
		entradas.put(entrada.getCodigoUnico(), entrada);
	}
	//elimina entrada en O(1)
	public void eliminarEntrada(int codigoUnico) {
		entradas.remove(codigoUnico); //es O(1)
	}
}
