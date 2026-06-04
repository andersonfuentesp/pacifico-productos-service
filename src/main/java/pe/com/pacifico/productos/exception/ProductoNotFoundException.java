package pe.com.pacifico.productos.exception;

public class ProductoNotFoundException extends RuntimeException {

    public ProductoNotFoundException(Long id) {
        super("No se encontro el producto con id " + id);
    }
}