package views;

import model.Categoria;
import model.Produto;
import service.CategoriaService;
import service.ProdutoService;

import java.util.List;
import java.util.Scanner;

public class ProdutoView {
    private static ProdutoService produtoService;
    private static CategoriaService categoriaService;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (produtoService == null) produtoService = new ProdutoService();
        if (categoriaService == null) categoriaService = new CategoriaService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Produtos");
            System.out.println("[1] Listar produtos\n[2] Criar produto\n[3] Editar produto\n[4] Remover produto\n[0] Voltar");
            int opt = ViewsManager.readInt("-> ");
            switch (opt) {
                case 1: list(); break;
                case 2: create(); break;
                case 3: edit(); break;
                case 4: remove(); break;
                case 0: running = false; break;
                default: System.out.println("Opção inválida");
            }
            System.out.println("Pressione Enter para continuar...");
            SCANNER.nextLine();
        }
    }

    private static void list() {
        List<Produto> produtos = produtoService.read();
        System.out.println("=== Produtos ===");
        for (Produto p : produtos) {
            System.out.println(p.getId() + ": " + p);
        }
    }

    private static void create() {
        System.out.println("=== Criar Produto ===");
        System.out.print("Nome: "); String nome = SCANNER.nextLine();
        System.out.print("Descricao: "); String desc = SCANNER.nextLine();
        System.out.print("Preco: "); Double preco = Double.parseDouble(SCANNER.nextLine());
        System.out.print("Estoque: "); int estoque = Integer.parseInt(SCANNER.nextLine());

        System.out.println("Selecione uma categoria pelo nome:");
        List<Categoria> cats = categoriaService.read();
        for (Categoria c : cats) System.out.println("- " + c.getNome());
        System.out.print("Nome da categoria: "); String nomeCat = SCANNER.nextLine();
        Categoria categoria = categoriaService.buscarPorNome(nomeCat);
        if (categoria == null) { System.out.println("Categoria não encontrada. Operação cancelada."); return; }

        Produto p = new Produto(nome, desc, preco, estoque, categoria);
        produtoService.create(p);
        System.out.println("Produto criado com sucesso.");
    }

    private static void edit() {
        System.out.println("=== Editar Produto ===");
        Long id = Long.parseLong(String.valueOf(ViewsManager.readInt("ID do produto: ")));
        Produto p = produtoService.buscarPorId(id);
        if (p == null) { System.out.println("Produto não encontrado."); return; }

        System.out.print("Nome ("+p.getNome()+"): "); String nome = SCANNER.nextLine(); if (!nome.isBlank()) p.setNome(nome);
        System.out.print("Descricao ("+p.getDescricao()+"): "); String desc = SCANNER.nextLine(); if (!desc.isBlank()) p.setDescricao(desc);
        System.out.print("Preco ("+p.getPreco()+"): "); String precoStr = SCANNER.nextLine(); if (!precoStr.isBlank()) p.setPreco(Double.parseDouble(precoStr));
        System.out.print("Estoque ("+p.getEstoque()+"): "); String estStr = SCANNER.nextLine(); if (!estStr.isBlank()) p.setEstoque(Integer.parseInt(estStr));

        produtoService.update(p);
        System.out.println("Produto atualizado.");
    }

    private static void remove() {
        System.out.println("=== Remover Produto ===");
        Long id = Long.parseLong(String.valueOf(ViewsManager.readInt("ID do produto: ")));
        Produto p = produtoService.buscarPorId(id);
        if (p == null) { System.out.println("Produto não encontrado."); return; }
        produtoService.delete(id);
        System.out.println("Produto removido.");
    }
}
