package ar.edu.ungs.prog2.ticketek;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Ticketek implements ITicketek {
	private Map<String , Usuario> usuarios;//clave: email 
	private Map<String,Espectaculo> espectaculos;//clave: nombre espectaculo
	private Map<String,Sede> sedes;//clave: nombre sede
	private Map<String, Map<Integer, IEntrada>> entradasVendidas;//clave: email/clave: codigo entrada
	private float totalRecaudado;

	
	public Ticketek() {
		this.usuarios= new HashMap<>();
		this.espectaculos= new HashMap<>();
		this.sedes= new HashMap<>();
		this.entradasVendidas = new HashMap<>();
	}
	
	@Override
	public void registrarUsuario(String email, String nombre, String apellido, String contrasenia) {
		
		if (usuarioInvalido(email, nombre, apellido, contrasenia)) {
			throw new IllegalArgumentException("Datos de usuario no válidos");
		}
		
		if (usuarios.containsKey(email)) {
			throw new RuntimeException("El Usuario ya está registrado");
		}
		
		usuarios.put(email, new Usuario(email, nombre, apellido, contrasenia));
	}
	
	private boolean usuarioInvalido(String email, String nombre, String apellido, String contrasenia) {
		return ((email == null) || (email.isEmpty()) || (nombre == null) || (nombre.isEmpty()) || (apellido == null) || (apellido.isEmpty()) || (contrasenia == null) || (contrasenia.isEmpty()));
	}

	
	@Override

	public void registrarSede(String nombre, String direccion, int capacidadMaxima) {
		
		if (sedes.containsKey(nombre)) {
			throw new RuntimeException("Sede ya registrada");
		}
		
		sedes.put(nombre, crearEstadioSegunTipo(nombre, direccion, capacidadMaxima));
	}
	
	private EstadioDeFutbol crearEstadioSegunTipo(String nombre, String direccion, int capacidadMaxima) {
		boolean esCampo = nombre.equalsIgnoreCase("La bombonera");
		return new EstadioDeFutbol(nombre, direccion, capacidadMaxima);
	}

	// Sobrecarga para registrar teatro

	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila, String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
			
			if (teatroInvalido(nombre, direccion, capacidadMaxima, asientosPorFila, sectores, capacidad, porcentajeAdicional)) {
				throw new IllegalArgumentException("Datos de sede no válidos");
			}
			
			if (sedes.containsKey(nombre)) {
				throw new IllegalArgumentException("La sede ya está registrada");
			}
			
			// Crear Teatro
			Sede teatro = new Teatro(nombre, direccion, capacidadMaxima,asientosPorFila, sectores, capacidad, porcentajeAdicional);
			sedes.put(nombre, teatro);
	}
	
	private boolean teatroInvalido(String nombre, String direccion, int capacidadMaxima, int asientosPorFila, String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
		return ((nombre == null) || (nombre.isEmpty()) || (direccion == null) || (direccion.isEmpty()) || (capacidadMaxima <= 0) || (asientosPorFila <= 0) || (sectores == null) || (capacidad == null) || (porcentajeAdicional == null) || (sectores.length != capacidad.length) || (sectores.length != porcentajeAdicional.length));
	}

	//Sobrecarga registra miniEstadio
	
	@Override
	public void registrarSede(String nombre, String direccion, int capacidadMaxima, int asientosPorFila, int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
		
		if (miniEstadioInvalido(nombre, direccion, capacidadMaxima, asientosPorFila, cantidadPuestos, precioConsumicion, sectores, capacidad, porcentajeAdicional)) {
			throw new IllegalArgumentException("Datos de sede no válidos");
		}
		
		if (sedes.containsKey(nombre)) {
			throw new IllegalArgumentException("La sede ya está registrada");
		}
		
		// Crear MiniEstadio 
		Sede miniEstadio = new MiniEstadio(nombre, direccion, capacidadMaxima, asientosPorFila, cantidadPuestos, precioConsumicion,sectores, capacidad, porcentajeAdicional);
		sedes.put(nombre, miniEstadio);
	}
	
	private boolean miniEstadioInvalido(String nombre, String direccion, int capacidadMaxima, int asientosPorFila, int cantidadPuestos, double precioConsumicion, String[] sectores, int[] capacidad, int[] porcentajeAdicional) {
		return ((nombre == null) || (nombre.isEmpty()) || (direccion == null) || (direccion.isEmpty()) || (capacidadMaxima <= 0) || (asientosPorFila <= 0) || (cantidadPuestos < 0) || (precioConsumicion < 0) || (sectores == null) || (capacidad == null) || (porcentajeAdicional == null) || (sectores.length != capacidad.length) || (sectores.length!= porcentajeAdicional.length));
	}
	
	@Override
	public void registrarEspectaculo(String nombre) {
		
		if (espectaculoInvalido(nombre)) {
			throw new IllegalArgumentException("Nombre de espectáculo inválido");
		}
		
		if (espectaculos.containsKey(nombre)) {
			throw new IllegalArgumentException("El espectáculo ya está registrado");
		}
		
		Espectaculo nuevo = new Espectaculo(nombre);
		espectaculos.put(nombre, nuevo);
	}
	
	private boolean espectaculoInvalido(String nombre) {
		return ((nombre == null) || (nombre.isEmpty()));
	}
	
	@Override
	public void agregarFuncion(String nombreEspectaculo, String fecha, String nombreSede, double precioBase) {
		
		//Valida que el espectáculo exista
		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		
		if (espectaculo == null) {
			throw new IllegalArgumentException("El espectáculo no está registrado.");
		}
		
		//  Valida que la sede exista
		Sede sede = sedes.get(nombreSede);
		
		if (sede == null) {
			throw new IllegalArgumentException("La sede no está registrada.");
		}
		
		// Valida formato de fecha y pasa a LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		
		try {
			LocalDate.parse(fecha, formatter);
		}
		catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Fecha inválida. Debe tener formato dd/MM/yy.");
		}
		
		espectaculo.agregarFuncion(nombreEspectaculo, fecha, sede, precioBase);
	}

	@Override
	public List<IEntrada> venderEntrada(String nombreEspectaculo, String fecha, String email, String contrasenia, int cantidadEntradas) {
		
		// Valida existencia de la función
		Funcion funcion = buscarFuncion(nombreEspectaculo, fecha);
		
		if (funcion == null) {
			throw new RuntimeException("No se encontró la función para el espectáculo '" + nombreEspectaculo + "' en la fecha '" + fecha + "'");
		}
		
		// Valida autenticación del usuario
		Usuario usuario = autenticar(email, contrasenia);
		
		if (usuario == null) {
			throw new RuntimeException("Usuario no registrado o contraseña incorrecta");
		}
		
		// Valida que la sede sea EstadioDeFutbol
		Sede sede = funcion.getSedeObj();
		
		if (!(sede instanceof EstadioDeFutbol)) {
			throw new RuntimeException("La sede no es Estadio de Fútbol, no se puede vender entradas generales con este método");
		}
		
		List<IEntrada> entradas = funcion.venderEntradas(cantidadEntradas, usuario);
		
		// Guardar en el usuario las entradas de estadio de futbol
		for (IEntrada entrada : entradas) {
			usuario.agregarEntrada(entrada);
		}
		
		// Guardar en el mapa de entradas vendidas
		Map<Integer, IEntrada> entradasUsuario = entradasVendidas.get(email);
		
		if (entradasUsuario == null) {
			entradasUsuario = new HashMap<>();
			entradasVendidas.put(email, entradasUsuario);
		}
		
		for (IEntrada entrada : entradas) {
			entradasUsuario.put(entrada.getCodigoUnico(), entrada);
			totalRecaudado+=entrada.precio();
		}
		
		return entradas;
	}
	
	@Override
	public List<IEntrada> venderEntrada(String espectaculo, String fecha, String email, String contrasenia, String sector, int[] asientos) {
		
		//Valida existencia de la funcion
		Funcion funcion = buscarFuncion(espectaculo, fecha);
		
		if (funcion == null) {
			throw new RuntimeException("No se encontró la función para el espectáculo '" + espectaculo + "' en la fecha '" + fecha + "'");
		}
		
		// Valida autenticación del usuario
		Usuario usuario = autenticar(email, contrasenia);
		
		if (usuario == null) {
			throw new RuntimeException("Usuario o contraseña incorrectos");
		}
		
		// Valida que la sede sea
		Sede sede = funcion.getSedeObj();
		
		List<IEntrada> entradas;
		
		if (sede instanceof Teatro) {
			entradas = funcion.venderEntradas(sector, asientos, usuario);
		} 
		
		else if (sede instanceof MiniEstadio) {
			entradas = funcion.venderEntradasMiniEstadio(sector, asientos, usuario);
		} 
		
		else {
			throw new RuntimeException("No se puede vender entradas para esta sede");
		}
		
		// Guardar en el usuario
		for (IEntrada entrada : entradas) {
			usuario.agregarEntrada(entrada);
			funcion.detectarSector(sector);
		}
		
		// Guardar en entradasVendidas
		Map<Integer, IEntrada> entradasUsuario = entradasVendidas.get(email);
		
		if (entradasUsuario == null) {
			entradasUsuario = new HashMap<>();
			entradasVendidas.put(email, entradasUsuario);
		}
		
		for (IEntrada entrada : entradas) {
			entradasUsuario.put(entrada.getCodigoUnico(), entrada);
			totalRecaudado+=entrada.precio();
		}
		
		
		return entradas;
	}
	
	private Funcion buscarFuncion(String nombreEspectaculo, String fechaStr) {
		Espectaculo espectaculo = espectaculos.get(nombreEspectaculo);
		
		if (espectaculo == null) {
			return null;
		}
		
		return espectaculo.getFuncion(fechaStr);
	}
	
	private Usuario autenticar(String email, String contrasenia) {//O(1)
		
		Usuario u = usuarios.get(email);
		
		if (u == null) {
			throw new RuntimeException("Usuario no registrado");
		}	
		if (!u.getContrasenia().equals(contrasenia)) {
			throw new RuntimeException("Contraseña incorrecta");
		}
		
		return u;
	}
	
	@Override
	public String listarFunciones(String nombreEspectaculo) {
		
		Espectaculo esp = espectaculos.get(nombreEspectaculo);
		
		if (esp == null) {
			return "";
		}
		
		return esp.listarFunciones();
	}
	
	@Override
	public List<IEntrada> listarEntradasFuturas(String emailUsuario, String contrasenia) {
		
		Usuario usuario = autenticar(emailUsuario, contrasenia);
		
		if (usuario == null) {
			throw new RuntimeException("Usuario o contraseña incorrectos");
		}
		
		List<IEntrada> entradasFuturasUsuario = new ArrayList<>();
		
		for (Map<Integer, IEntrada> mapaEntradasUsuario: entradasVendidas.values()) {
			for (IEntrada entrada: mapaEntradasUsuario.values()) {
				
				Entrada e = (Entrada) entrada; 
				
				if ((e.getFecha().isAfter(LocalDate.now())) && (emailUsuario == e.getEmailUsuario())) {
					entradasFuturasUsuario.add(e);
				}
			}
			
		}
		
		return entradasFuturasUsuario;
	}
	
	public List<IEntrada> listarTodasLasEntradasDelUsuario(String emailUsuario, String contrasenia) {
		
		autenticar(emailUsuario, contrasenia);
		
		Map<Integer, IEntrada> entradasUsuario = entradasVendidas.get(emailUsuario);
		
		if (entradasUsuario == null) {
			return new ArrayList<>();
		}
		
		return new ArrayList<>(entradasUsuario.values());
	}
	//anular entrada en O(1)
	@Override
	public boolean anularEntrada(IEntrada entrada, String contrasenia) {
	    Usuario usuario = autenticar(entrada.getEmailUsuario(), contrasenia);//O(1)
	    if (usuario == null) {
	        throw new RuntimeException("Contraseña incorrecta");
	    }

	    Map<Integer, IEntrada> entradasUsuario = entradasVendidas.get(entrada.getEmailUsuario());//O(1)

	    if (entradasUsuario != null && entradasUsuario.containsKey(entrada.getCodigoUnico())) {//O(1)
	        entradasUsuario.remove(entrada.getCodigoUnico());//O(1)
	        usuario.eliminarEntrada(entrada.getCodigoUnico()); // eliminación en O(1)
	        return true;
	    } else {
	        throw new RuntimeException("Entrada inexistente");
	    }
	}

	
	@Override
	public List<IEntrada> listarEntradasEspectaculo(String nombreEspectaculo) {
		List<IEntrada> resultado = new ArrayList<>();
		
		for (Map<Integer, IEntrada> mapaEntradasUsuario : entradasVendidas.values()) {
			for (IEntrada entrada : mapaEntradasUsuario.values()) {
				if (entrada instanceof Entrada) {
					Entrada entradaConcreta = (Entrada) entrada;
					if (entradaConcreta.getNombreEspectaculo().equals(nombreEspectaculo)) {
						resultado.add(entrada);
					}
				}
			}
		}
		return resultado;
	}
	
	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha, String sector, int asiento) {
		return null;
	}

	@Override
	public IEntrada cambiarEntrada(IEntrada entrada, String contrasenia, String fecha) {
		
		Usuario usuario = autenticar(entrada.getEmailUsuario(), contrasenia);
		
		if (usuario == null) {
			throw new RuntimeException("Contraseña incorrecta");
		}
		
		if (entradasVendidas.get(entrada.getEmailUsuario()).containsValue(entrada)) {
			
			Funcion funcion = buscarFuncion((String) entrada.getNombreEspectaculo(), fecha);
			
			if (funcion == null) {
				throw new RuntimeException("Funcion inexistente");
			}
			
			String nombreSede = funcion.getSede();
			
			IEntrada entradaNueva = new Entrada(entrada.getEmailUsuario(), (String) entrada.getNombreEspectaculo(), nombreSede, fecha, entrada.precio());
			
			entradasVendidas.get(entrada.getEmailUsuario()).remove(entrada.getCodigoUnico());
			entradasVendidas.get(entrada.getEmailUsuario()).put(entradaNueva.getCodigoUnico(), entradaNueva);
			
			return entradaNueva;
		}
		
		
		return null;
	}


	// Método costoEntrada sin sector (usamos "campo")
	public double costoEntrada(String nombreEspectaculo, String fecha) {
		
		Funcion funcion = buscarFuncion(nombreEspectaculo, fecha);
		
		if (funcion == null) {
			throw new RuntimeException("Funcion inexistente");
		}
		
		return funcion.getPrecioBase();
	}

	// Método costoEntrada con sector
	public double costoEntrada(String nombreEspectaculo, String fecha, String sector) {
		
		Funcion funcion = buscarFuncion(nombreEspectaculo, fecha);
		
		if (funcion == null) {
			throw new RuntimeException("Funcion inexistente");
		}
		
		double precioBase = espectaculos.get(nombreEspectaculo).getFuncion(fecha).getPrecioBase();
		Sede sede = funcion.getSedeObj();
		
		return sede.calcularPrecioBase(precioBase, sector);
	}
	
	@Override
	public double totalRecaudado(String nombreEspectaculo) {
		
		double total = 0.0;
		
		for (Map<Integer, IEntrada> mapaEntradas : entradasVendidas.values()) {
			for (IEntrada entrada : mapaEntradas.values()) {
				
				if (entrada.getNombreEspectaculo().equals(nombreEspectaculo)) {
					
					if (entrada instanceof Entrada e) {
						
						Funcion funcion = buscarFuncion(nombreEspectaculo, e.getFechaFormateada());
						
						if (funcion == null) {
							throw new RuntimeException("Funcion inexistente");
						}
						
						double precioBase = espectaculos.get(nombreEspectaculo).getFuncion(e.getFechaFormateada()).getPrecioBase();
						Sede sede = funcion.getSedeObj();
						
						double precioFinal = sede.calcularPrecioBase(precioBase, e.getSector());
						
						total += precioFinal;
					}
				}
			}
		}
		
		return total;
	}
	
	@Override
	public double totalRecaudadoPorSede(String nombreEspectaculo, String nombreSede) {
		double total = 0.0;
		
		for (Map<Integer, IEntrada> mapaEntradas : entradasVendidas.values()) {
			for (IEntrada entrada : mapaEntradas.values()) {
				
				if (entrada.getNombreEspectaculo().equals(nombreEspectaculo)) {
					
					if (entrada instanceof Entrada e) {
						
						Funcion funcion = buscarFuncion(nombreEspectaculo, e.getFechaFormateada());
						
						if (funcion == null) {
							throw new RuntimeException("Funcion inexistente");
						}
						
						double precioBase = espectaculos.get(nombreEspectaculo).getFuncion(e.getFechaFormateada()).getPrecioBase();
						Sede sede = funcion.getSedeObj();
						
						double precioFinal = sede.calcularPrecioBase(precioBase, e.getSector());
						
						if (e.getNombreSede() == nombreSede) {
							total += precioFinal;
						}
					}
				}
			}
		}
		
		return total;
		
		
	}

	
	
	//Uso de Tecnologias Java (iterator/forEach/StringBuillder)
	@Override
    public String toString() {
	   
        StringBuilder sb = new StringBuilder();
        sb.append("****************Ticketek****************** \n\n".toUpperCase());
        sb.append("Sedes: \n");
        sb.append(sedes.keySet()).append("\n\n");
        sb.append("Usuarios:\n ").append(usuarios.keySet()).append("\n\n");
        sb.append("Espectaculos: \n").append(espectaculos.keySet()).append("\n\n");
        sb.append("Entradas vendidas: \n");
        
        Iterator<Map<Integer, IEntrada>> it = entradasVendidas.values().iterator();

        while (it.hasNext()) {
            Map<Integer, IEntrada> subMapa = it.next();
            for (IEntrada entrada : subMapa.values()) {
                sb.append(entrada).append("\n");
      
            }
        }
        sb.append("\n");
        sb.append("Recaudacion total: "+totalRecaudado).append("\n"); 
    return sb.toString();
    }

}
