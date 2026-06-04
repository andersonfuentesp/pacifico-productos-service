package pe.com.pacifico.productos.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pe.com.pacifico.productos.domain.Producto;
import reactor.core.publisher.Mono;

public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {

    Mono<Boolean> existsBySku(String sku);

    Mono<Producto> findBySku(String sku);
}