package gt.bchavajay.desafio.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
}
