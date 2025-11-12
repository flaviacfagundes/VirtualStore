package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "metodo_pagamento", nullable = false, length = 50)
    private String metodoPagemento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagemento;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @OneToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id", nullable = false)
    private Pedido pedido;

    public Pagamento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getMetodoPagemento() { return metodoPagemento; }
    public void setMetodoPagemento(String metodoPagemento) { this.metodoPagemento = metodoPagemento; }

    public LocalDateTime getDataPagemento() { return dataPagemento; }
    public void setDataPagemento(LocalDateTime dataPagemento) { this.dataPagemento = dataPagemento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

}
