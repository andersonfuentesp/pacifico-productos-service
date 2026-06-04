package pe.com.pacifico.productos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponse(
        Long id,
        String sku,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        Boolean activo,
        LocalDateTime fechaCreacion
) {
}