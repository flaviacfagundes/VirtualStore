package views;

import model.Avaliacao;
import model.Cliente;
import model.Produto;
import service.AvaliacaoService;
import service.ClienteService;
import service.ProdutoService;

import java.util.List;
import java.util.Scanner;

public class AvaliacaoView {
    private static AvaliacaoService service;
    private static ClienteService clienteService;
    private static ProdutoService produtoService;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (service == null) service = new AvaliacaoService();
        if (clienteService == null) clienteService = new ClienteService();
        if (produtoService == null) produtoService = new ProdutoService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Avaliações");
            System.out.println("[1] Listar avaliações\n[2] Criar avaliação\n[3] Editar avaliação\n[4] Remover avaliação\n[0] Voltar");
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
        List<Avaliacao> list = service.read();
        System.out.println("=== Avaliações ===");
        for (Avaliacao a : list) System.out.println(a.getId() + ": " + a);
    }

    private static void create() {
        System.out.println("=== Criar Avaliação ===");
        System.out.print("Email do cliente: "); String email = SCANNER.nextLine();
        Cliente c = clienteService.buscarPorEmail(email);
        if (c == null) { System.out.println("Cliente não encontrado. Operação cancelada."); return; }
        System.out.print("ID do produto: "); long pid = ViewsManager.readInt("-> ");
        Produto p = produtoService.buscarPorId(pid);
        if (p == null) { System.out.println("Produto não encontrado. Operação cancelada."); return; }
        System.out.print("Nota (1-5): "); int nota = ViewsManager.readInt("-> ");
        System.out.print("Comentário: "); String comentario = SCANNER.nextLine();
        Avaliacao a = new Avaliacao(nota, comentario, c, p);
        service.create(a);
        System.out.println("Avaliação criada com sucesso.");
    }

    private static void edit() {
        System.out.println("=== Editar Avaliação ===");
        int id = ViewsManager.readInt("ID da avaliação: ");
        Avaliacao a = service.buscarPorId((long) id);
        if (a == null) { System.out.println("Avaliação não encontrada."); return; }
        System.out.print("Nota ("+a.getNota()+"): "); String nota = SCANNER.nextLine(); if (!nota.isBlank()) a.setNota(Integer.parseInt(nota));
        System.out.print("Comentário ("+a.getComentario()+"): "); String com = SCANNER.nextLine(); if (!com.isBlank()) a.setComentario(com);
        service.update(a);
        System.out.println("Avaliação atualizada.");
    }

    private static void remove() {
        System.out.println("=== Remover Avaliação ===");
        int id = ViewsManager.readInt("ID da avaliação: ");
        service.delete((long) id);
        System.out.println("Operação concluída.");
    }
}
