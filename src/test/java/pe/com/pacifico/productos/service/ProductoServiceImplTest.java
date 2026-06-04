package pe.com.pacifico.productos.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.pacifico.productos.domain.Producto;
import pe.com.pacifico.productos.dto.ProductoRequest;
import pe.com.pacifico.productos.exception.DuplicateSkuException;
import pe.com.pacifico.productos.exception.ProductoNotFoundException;
import pe.com.pacifico.productos.mapper.ProductoMapper;
import pe.com.pacifico.productos.repository.ProductoRepository;
import pe.com.pacifico.productos.service.impl.ProductoServiceImpl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository repository;

    private ProductoMapper mapper;

    private ProductoServiceImpl service;

    private Producto producto;

    @BeforeEach
    void setUp() {
        mapper = new ProductoMapper();
        service = new ProductoServiceImpl(repository, mapper);

        producto = Producto.builder()
                .id(1L)
                .sku("SKU-001")
                .nombre("Laptop")
                .descripcion("Laptop de prueba")
                .precio(new BigDecimal("3500.00"))
                .stock(10)
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .version(0L)
                .build();
    }

    private ProductoRequest request() {
        return new ProductoRequest("SKU-001", "Laptop", "Laptop de prueba",
                new BigDecimal("3500.00"), 10);
    }

    @Test
    void listarTodos_devuelveProductos() {
        when(repository.findAll()).thenReturn(Flux.just(producto));

        StepVerifier.create(service.listarTodos())
                .expectNextMatches(r -> r.sku().equals("SKU-001"))
                .verifyComplete();
    }

    @Test
    void obtenerPorId_existente_devuelveProducto() {
        when(repository.findById(1L)).thenReturn(Mono.just(producto));

        StepVerifier.create(service.obtenerPorId(1L))
                .expectNextMatches(r -> r.id().equals(1L))
                .verifyComplete();
    }

    @Test
    void obtenerPorId_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.obtenerPorId(99L))
                .expectError(ProductoNotFoundException.class)
                .verify();
    }

    @Test
    void crear_skuNuevo_guarda() {
        when(repository.existsBySku(anyString())).thenReturn(Mono.just(false));
        when(repository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(service.crear(request()))
                .expectNextMatches(r -> r.sku().equals("SKU-001"))
                .verifyComplete();

        verify(repository).save(any(Producto.class));
    }

    @Test
    void crear_skuDuplicado_lanzaConflict() {
        when(repository.existsBySku(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(service.crear(request()))
                .expectError(DuplicateSkuException.class)
                .verify();

        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_existente_guarda() {
        when(repository.findById(1L)).thenReturn(Mono.just(producto));
        when(repository.save(any(Producto.class))).thenReturn(Mono.just(producto));

        StepVerifier.create(service.actualizar(1L, request()))
                .expectNextMatches(r -> r.id().equals(1L))
                .verifyComplete();
    }

    @Test
    void actualizar_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.actualizar(99L, request()))
                .expectError(ProductoNotFoundException.class)
                .verify();
    }

    @Test
    void eliminar_existente_completa() {
        when(repository.findById(1L)).thenReturn(Mono.just(producto));
        when(repository.delete(producto)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminar(1L))
                .verifyComplete();
    }

    @Test
    void eliminar_inexistente_lanzaNotFound() {
        when(repository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.eliminar(99L))
                .expectError(ProductoNotFoundException.class)
                .verify();
    }
}