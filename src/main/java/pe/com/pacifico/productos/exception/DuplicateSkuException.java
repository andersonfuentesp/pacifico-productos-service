package pe.com.pacifico.productos.exception;

public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Ya existe un producto con el sku " + sku);
    }
}