package gt.bchavajay.desafio.principal;

import gt.bchavajay.desafio.model.Datos;
import gt.bchavajay.desafio.model.DatosLibros;
import gt.bchavajay.desafio.service.ConsumoAPI;
import gt.bchavajay.desafio.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        //Top 10 libros mas descargados
        System.out.println("Top 10 libros mas descargados");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Busque de libros por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json,Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l-> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro encontrado");
            System.out.println(libroBuscado.get());
        }else{
            System.out.println("Libro no encontrado");
        }

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d -> d.numeroDeDescargas()>0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("Cantidad media de descargas: "+est.getAverage());
        System.out.println("Cantidad maxima de descargas: "+est.getMax());
        System.out.println("Cantidad minima de descargas: "+est.getMin());
        System.out.println("Cantidad de registos para calcular las estadisticas: "+est.getCount());

    }
}
