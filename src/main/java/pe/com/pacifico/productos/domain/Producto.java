package pe.com.pacifico.productos.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    private Long id;

    private String sku;

    private String nombre;

    private String descripcion;

    private BigDecimal precio;

    private Integer stock;

    private Boolean activo;

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Version
    private Long version;
}