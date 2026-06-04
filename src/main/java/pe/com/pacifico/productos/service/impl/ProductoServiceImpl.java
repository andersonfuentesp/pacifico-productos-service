package pe.com.pacifico.productos.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.pacifico.productos.domain.Producto;
import pe.com.pacifico.productos.dto.ProductoRequest;
import pe.com.pacifico.productos.dto.ProductoResponse;
import pe.com.pacifico.productos.exception.DuplicateSkuException;
import pe.com.pacifico.productos.exception.ProductoNotFoundException;
import pe.com.pacifico.productos.mapper.ProductoMapper;
import pe.com.pacifico.productos.repository.ProductoRepository;
import pe.com.pacifico.productos.service.ProductoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    @Override
    public Flux<ProductoResponse> listarTodos() {
        return repository.findAll()
                .map(mapper::toResponse);
    }

    @Override
    public Mono<ProductoResponse> obtenerPorId(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(id)))
                .map(mapper::toResponse);
    }

    @Override
    public Mono<ProductoResponse> crear(ProductoRequest request) {
        String sku = request.sku().trim().toUpperCase();
        return repository.existsBySku(sku)
                .flatMap(existe -> {
                    if (Boolean.TRUE.equals(existe)) {
                        return Mono.error(new DuplicateSkuException(sku));
                    }
                    return repository.save(mapper.toEntity(request));
                })
                .doOnSuccess(p -> log.info("Producto creado id={} sku={}", p.getId(), p.getSku()))
                .map(mapper::toResponse);
    }

    @Override
    public Mono<ProductoResponse> actualizar(Long id, ProductoRequest request) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(id)))
                .flatMap(existente -> {
                    existente.setNombre(request.nombre().trim());
                    existente.setDescripcion(request.descripcion());
                    existente.setPrecio(request.precio());
                    existente.setStock(request.stock());
                    return repository.save(existente);
                })
                .doOnSuccess(p -> log.info("Producto actualizado id={}", p.getId()))
                .map(mapper::toResponse);
    }

    @Override
    public Mono<Void> eliminar(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductoNotFoundException(id)))
                .flatMap(repository::delete);
    }
}