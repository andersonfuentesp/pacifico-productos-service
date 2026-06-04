package pe.com.pacifico.productos.mapper;

import org.springframework.stereotype.Component;
import pe.com.pacifico.productos.domain.Producto;
import pe.com.pacifico.productos.dto.ProductoRequest;
import pe.com.pacifico.productos.dto.ProductoResponse;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        return Producto.builder()
                .sku(request.sku().trim().toUpperCase())
                .nombre(request.nombre().trim())
                .descripcion(Optional.ofNullable(request.descripcion())
                        .map(String::trim)
                        .orElse(null))
                .precio(request.precio())
                .stock(request.stock())
                .activo(Boolean.TRUE)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getSku(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getActivo(),
                producto.getFechaCreacion()
        );
    }
}