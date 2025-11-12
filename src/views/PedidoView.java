package views;

import model.Pedido;
import model.Cliente;
import service.PedidoService;
import service.ClienteService;

import java.util.List;
import java.util.Scanner;
import java.math.BigDecimal;

public class PedidoView {
    private static PedidoService service;
    private static ClienteService clienteService;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (service == null) service = new PedidoService();
        if (clienteService == null) clienteService = new ClienteService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Pedidos");
            System.out.println("[1] Listar pedidos\n[2] Criar pedido\n[3] Editar pedido\n[4] Remover pedido\n[0] Voltar");
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
        List<Pedido> list = service.read();
        System.out.println("=== Pedidos ===");
        for (Pedido p : list) {
            String clienteNome = p.getCliente() != null ? p.getCliente().getNome() : "(sem cliente)";
            System.out.println(p.getId() + ": Cliente=" + clienteNome + " | Status=" + p.getStatusPedido() + " | Total=R$ " + p.getValorTotal());
        }
    }

    private static void create() {
        System.out.println("=== Criar Pedido ===");
        System.out.print("Email do cliente: "); String email = SCANNER.nextLine();
        Cliente c = clienteService.buscarPorEmail(email);
        if (c == null) { System.out.println("Cliente não encontrado. Operação cancelada."); return; }
        System.out.print("Status do pedido: "); String status = SCANNER.nextLine();
        Pedido p = new Pedido();
        p.setCliente(c);
        p.setStatusPedido(status);
        p.setValorTotal(BigDecimal.ZERO);
        service.create(p);
        System.out.println("Pedido criado com sucesso.");
    }

    private static void edit() {
        System.out.println("=== Editar Pedido ===");
        int id = ViewsManager.readInt("ID do pedido: ");
        Pedido p = service.buscarPorId((long) id);
        if (p == null) { System.out.println("Pedido não encontrado."); return; }
        System.out.print("Status ("+p.getStatusPedido()+"): "); String status = SCANNER.nextLine(); if (!status.isBlank()) p.setStatusPedido(status);
        service.update(p);
        System.out.println("Pedido atualizado.");
    }

    private static void remove() {
        System.out.println("=== Remover Pedido ===");
        int id = ViewsManager.readInt("ID do pedido: ");
        service.delete((long) id);
        System.out.println("Operação concluída.");
    }
}
