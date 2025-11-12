package views;

import java.util.Scanner;

public class ViewsManager {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void println(String message)  { System.out.println(message); }
    public static void print(String message)    { System.out.println(message); }
    public static void clear()                  { for (int i = 0; i < 50; ++i) println(""); }
    public static void title(String title)      { println("||>-==============-<|[ " + title + " ]|>-==============-<||"); }

    public static int mainMenu() {
        println("""
            Escolha uma opção:
                    [1] Gerenciar Avaliacão
                    [2] Gerenciar Categoria
                    [3] Gerenciar Cliente
                    [4] Gerenciar Fornecedor
                    [5] Gerenciar Pagamento
                    [6] Gerenciar Pedido
                    [7] Gerenciar Produto
                    [0] Sair
        """
        );
        print("-> ");
        return SCANNER.nextInt();
    }

    public static int readInt(String prompt) {
        while (true) {
            print(prompt);
            try { return Integer.parseInt(SCANNER.next()); }
            catch (NumberFormatException e) { println("Entrada inválida. Informe um número inteiro."); }
        }
    }

    public static void manageProduto() { ProdutoView.manage(); }

    public static void manageCategoria() { CategoriaView.manage(); }

    public static void manageCliente() { ClienteView.manage(); }

    public static void manageFornecedor() { FornecedorView.manage(); }

    public static void managePagamento() { PagamentoView.manage(); }

    public static void managePedido() { PedidoView.manage(); }

    public static void manageAvaliacao() { AvaliacaoView.manage(); }

}
