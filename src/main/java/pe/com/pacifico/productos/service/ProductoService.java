package pe.com.pacifico.productos.service;

import pe.com.pacifico.productos.dto.ProductoRequest;
import pe.com.pacifico.productos.dto.ProductoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<ProductoResponse> listarTodos();

    Mono<ProductoResponse> obtenerPorId(Long id);

    Mono<ProductoResponse> crear(ProductoRequest request);

    Mono<ProductoResponse> actualizar(Long id, ProductoRequest request);

    Mono<Void> eliminar(Long id);
}