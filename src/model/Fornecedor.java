package model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "fornecedor")
public class Fornecedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", nullable = false, length = 100)
	private String nome;

	@Column(name = "cnpj", unique = true, length = 18)
	private String CNPJ;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "telefone", length = 20)
	private String telefone;

	@OneToMany(mappedBy = "fornecedor", fetch = FetchType.LAZY)
	private List<Produto> produtos;

	public Fornecedor() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCNPJ() { return CNPJ; }
    public void setCNPJ(String CNPJ) { this.CNPJ = CNPJ; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }

}
