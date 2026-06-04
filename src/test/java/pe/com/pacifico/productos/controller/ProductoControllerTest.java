package pe.com.pacifico.productos.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.pacifico.productos.dto.ProductoRequest;
import pe.com.pacifico.productos.dto.ProductoResponse;
import pe.com.pacifico.productos.exception.DuplicateSkuException;
import pe.com.pacifico.productos.exception.GlobalExceptionHandler;
import pe.com.pacifico.productos.exception.ProductoNotFoundException;
import pe.com.pacifico.productos.service.ProductoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(ProductoController.class)
@Import(GlobalExceptionHandler.class)
class ProductoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductoService service;

    private ProductoResponse response() {
        return new ProductoResponse(1L, "SKU-001", "Laptop", "Laptop de prueba",
                new BigDecimal("3500.00"), 10, true, LocalDateTime.now());
    }

    private ProductoRequest request() {
        return new ProductoRequest("SKU-001", "Laptop", "Laptop de prueba",
                new BigDecimal("3500.00"), 10);
    }

    @Test
    void listar_devuelve200() {
        when(service.listarTodos()).thenReturn(Flux.just(response()));

        webTestClient.get().uri("/api/v1/productos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ProductoResponse.class).hasSize(1);
    }

    @Test
    void obtener_existente_devuelve200() {
        when(service.obtenerPorId(1L)).thenReturn(Mono.just(response()));

        webTestClient.get().uri("/api/v1/productos/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.sku").isEqualTo("SKU-001");
    }

    @Test
    void obtener_inexistente_devuelve404() {
        when(service.obtenerPorId(99L))
                .thenReturn(Mono.error(new ProductoNotFoundException(99L)));

        webTestClient.get().uri("/api/v1/productos/99")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void crear_valido_devuelve201() {
        when(service.crear(any())).thenReturn(Mono.just(response()));

        webTestClient.post().uri("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    void crear_invalido_devuelve400() {
        ProductoRequest invalido = new ProductoRequest("", "", null, null, null);

        webTestClient.post().uri("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalido)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void crear_skuDuplicado_devuelve409() {
        when(service.crear(any()))
                .thenReturn(Mono.error(new DuplicateSkuException("SKU-001")));

        webTestClient.post().uri("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request())
                .exchange()
                .expectStatus().isEqualTo(409);
    }

    @Test
    void actualizar_devuelve200() {
        when(service.actualizar(eq(1L), any())).thenReturn(Mono.just(response()));

        webTestClient.put().uri("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void eliminar_devuelve204() {
        when(service.eliminar(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/api/v1/productos/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}