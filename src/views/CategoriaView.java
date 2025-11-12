package views;

import model.Categoria;
import service.CategoriaService;

import java.util.List;
import java.util.Scanner;

public class CategoriaView {
    private static CategoriaService service;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (service == null) service = new CategoriaService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Categorias");
            ViewsManager.println("[1] Listar categorias\n[2] Criar categoria\n[3] Editar categoria\n[4] Remover categoria\n[0] Voltar");
            int opt = ViewsManager.readInt("-> ");
            switch (opt) {
                case 1: list(); break;
                case 2: create(); break;
                case 3: edit(); break;
                case 4: remove(); break;
                case 0: running = false; break;
                default: ViewsManager.println("Opção inválida");
            }
            ViewsManager.println("Pressione Enter para continuar...");
            SCANNER.nextLine();
        }
    }

    private static void list() {
        List<Categoria> list = service.read();
        ViewsManager.println("=== Categorias ===");
        for (Categoria c : list) ViewsManager.println(c.getId() + ": " + c);
    }

    private static void create() {
        ViewsManager.println("=== Criar Categoria ===");
        ViewsManager.print("Nome: "); String nome = SCANNER.nextLine();
        ViewsManager.print("Descricao: "); String desc = SCANNER.nextLine();
        Categoria c = new Categoria(nome, desc);
        service.create(c);
        ViewsManager.println("Categoria criada com sucesso.");
    }

    private static void edit() {
        ViewsManager.println("=== Editar Categoria ===");
        int id = ViewsManager.readInt("ID da categoria: ");
        List<Categoria> list = service.read();
        Categoria found = null;
        for (Categoria c : list) if (c.getId() == id) { found = c; break; }
        if (found == null) { ViewsManager.println("Categoria não encontrada."); return; }
        ViewsManager.print("Nome ("+found.getNome()+"): "); String nome = SCANNER.nextLine(); if (!nome.isBlank()) found.setNome(nome);
        ViewsManager.print("Descricao ("+found.getDescricao()+"): "); String desc = SCANNER.nextLine(); if (!desc.isBlank()) found.setDescricao(desc);
        service.update(found);
        ViewsManager.println("Categoria atualizada.");
    }

    private static void remove() {
        ViewsManager.println("=== Remover Categoria ===");
        ViewsManager.print("Nome da categoria: "); String nome = SCANNER.nextLine();
        service.delete(nome);
        ViewsManager.println("Operação concluída.");
    }
}
