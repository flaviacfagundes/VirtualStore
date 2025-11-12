package views;

import model.Cliente;
import service.ClienteService;

import java.util.List;
import java.util.Scanner;

public class ClienteView {
    private static ClienteService service;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (service == null) service = new ClienteService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Clientes");
            System.out.println("[1] Listar clientes\n[2] Criar cliente\n[3] Editar cliente\n[4] Remover cliente\n[0] Voltar");
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
        List<Cliente> list = service.read();
        System.out.println("=== Clientes ===");
        for (Cliente c : list) System.out.println(c.getId() + ": " + c.getNome() + " <" + c.getEmail() + ">");
    }

    private static void create() {
        System.out.println("=== Criar Cliente ===");
        System.out.print("Nome: "); String nome = SCANNER.nextLine();
        System.out.print("Email: "); String email = SCANNER.nextLine();
        System.out.print("CPF: "); String cpf = SCANNER.nextLine();
        System.out.print("Telefone: "); String tel = SCANNER.nextLine();
        Cliente c = new Cliente(nome, email, cpf, tel);
        service.create(c);
        System.out.println("Cliente criado com sucesso.");
    }

    private static void edit() {
        System.out.println("=== Editar Cliente ===");
        System.out.print("Email do cliente: "); String email = SCANNER.nextLine();
        Cliente c = service.buscarPorEmail(email);
        if (c == null) { System.out.println("Cliente não encontrado."); return; }
        System.out.print("Nome ("+c.getNome()+"): "); String nome = SCANNER.nextLine(); if (!nome.isBlank()) c.setNome(nome);
        System.out.print("Telefone ("+c.getTelefone()+"): "); String tel = SCANNER.nextLine(); if (!tel.isBlank()) c.setTelefone(tel);
        service.update(c);
        System.out.println("Cliente atualizado.");
    }

    private static void remove() {
        System.out.println("=== Remover Cliente ===");
        System.out.print("Email do cliente: "); String email = SCANNER.nextLine();
        service.delete(email);
        System.out.println("Operação concluída.");
    }
}
