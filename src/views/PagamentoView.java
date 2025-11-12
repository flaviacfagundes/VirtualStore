package views;

import model.Pagamento;
import service.PagamentoService;

import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;

public class PagamentoView {
    private static PagamentoService service;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (service == null) service = new PagamentoService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Pagamentos");
            System.out.println("[1] Listar pagamentos\n[2] Criar pagamento\n[3] Editar pagamento\n[4] Remover pagamento\n[0] Voltar");
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
        List<Pagamento> list = service.read();
        System.out.println("=== Pagamentos ===");
        for (Pagamento p : list) System.out.println(p.getId() + ": " + p.getStatus() + " - R$ " + p.getValor());
    }

    private static void create() {
        System.out.println("=== Criar Pagamento ===");
        System.out.print("Valor: "); BigDecimal valor = new BigDecimal(SCANNER.nextLine());
        System.out.print("Método: "); String metodo = SCANNER.nextLine();
        System.out.print("Status: "); String status = SCANNER.nextLine();
        // Nota: pedido vinculado não implementado na view (requer seleção de pedido)
        Pagamento p = new Pagamento();
        p.setValor(valor); p.setMetodoPagemento(metodo); p.setStatus(status);
        service.create(p);
        System.out.println("Pagamento criado (vinculação a pedido deve ser feita manualmente).");
    }

    private static void edit() {
        System.out.println("=== Editar Pagamento ===");
        int id = ViewsManager.readInt("ID do pagamento: ");
        Pagamento p = service.buscarPorId((long) id);
        if (p == null) { System.out.println("Pagamento não encontrado."); return; }
        System.out.print("Método ("+p.getMetodoPagemento()+"): "); String metodo = SCANNER.nextLine(); if (!metodo.isBlank()) p.setMetodoPagemento(metodo);
        System.out.print("Status ("+p.getStatus()+"): "); String status = SCANNER.nextLine(); if (!status.isBlank()) p.setStatus(status);
        service.update(p);
        System.out.println("Pagamento atualizado.");
    }

    private static void remove() {
        System.out.println("=== Remover Pagamento ===");
        int id = ViewsManager.readInt("ID do pagamento: ");
        service.delete((long) id);
        System.out.println("Operação concluída.");
    }
}
