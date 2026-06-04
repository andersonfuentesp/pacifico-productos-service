package pe.com.pacifico.productos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pe.com.pacifico.productos.dto.ProductoRequest;
import pe.com.pacifico.productos.dto.ProductoResponse;
import pe.com.pacifico.productos.service.ProductoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/productos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Operaciones sobre el catalogo de productos")
public class ProductoController {

    private final ProductoService service;

    @GetMapping
    @Operation(summary = "Lista todos los productos")
    public Flux<ProductoResponse> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un producto por su id")
    public Mono<ProductoResponse> obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crea un nuevo producto")
    public Mono<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return service.crear(request);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualiza un producto existente")
    public Mono<ProductoResponse> actualizar(@PathVariable Long id,
                                             @Valid @RequestBody ProductoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Elimina un producto")
    public Mono<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id);
    }
}