package views;

import model.Fornecedor;
import service.FornecedorService;

import java.util.List;
import java.util.Scanner;

public class FornecedorView {
    private static FornecedorService service;
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void manage() {
        if (service == null) service = new FornecedorService();

        boolean running = true;
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Gerenciar Fornecedores");
            System.out.println("[1] Listar fornecedores\n[2] Criar fornecedor\n[3] Editar fornecedor\n[4] Remover fornecedor\n[0] Voltar");
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
        List<Fornecedor> list = service.read();
        System.out.println("=== Fornecedores ===");
        for (Fornecedor f : list) System.out.println(f.getId() + ": " + f.getNome());
    }

    private static void create() {
        System.out.println("=== Criar Fornecedor ===");
        System.out.print("Nome: "); String nome = SCANNER.nextLine();
        System.out.print("CNPJ: "); String cnpj = SCANNER.nextLine();
        System.out.print("Email: "); String email = SCANNER.nextLine();
        System.out.print("Telefone: "); String tel = SCANNER.nextLine();
        Fornecedor f = new Fornecedor();
        f.setNome(nome); f.setCNPJ(cnpj); f.setEmail(email); f.setTelefone(tel);
        service.create(f);
        System.out.println("Fornecedor criado com sucesso.");
    }

    private static void edit() {
        System.out.println("=== Editar Fornecedor ===");
        int id = ViewsManager.readInt("ID do fornecedor: ");
        Fornecedor f = service.buscarPorId((long) id);
        if (f == null) { System.out.println("Fornecedor não encontrado."); return; }
        System.out.print("Nome ("+f.getNome()+"): "); String nome = SCANNER.nextLine(); if (!nome.isBlank()) f.setNome(nome);
        System.out.print("Email ("+f.getEmail()+"): "); String email = SCANNER.nextLine(); if (!email.isBlank()) f.setEmail(email);
        System.out.print("Telefone ("+f.getTelefone()+"): "); String tel = SCANNER.nextLine(); if (!tel.isBlank()) f.setTelefone(tel);
        service.update(f);
        System.out.println("Fornecedor atualizado.");
    }

    private static void remove() {
        System.out.println("=== Remover Fornecedor ===");
        int id = ViewsManager.readInt("ID do fornecedor: ");
        service.delete((long) id);
        System.out.println("Operação concluída.");
    }
}
