import service.PersistenceManager;
import utils.Logger;
import views.ViewsManager;

public class Main {
    private static boolean running = true;
    private static final Logger LOGGER = new Logger(false);

    public static void main(String[] args) {
        LOGGER.logInfo("Aplicação iniciada...");
        
        // Inicializa o banco de dados e cria as tabelas automaticamente
        LOGGER.logInfo("Inicializando banco de dados e criando tabelas...");
        try {
            PersistenceManager.initialize();
            LOGGER.logSuccess("Banco de dados inicializado com sucesso! Tabelas criadas/verificadas.");
        } catch (Exception e) {
            LOGGER.logError("Erro ao inicializar banco de dados: " + e.getMessage());
            e.printStackTrace();
            LOGGER.logWarning("Aplicação continuará, mas podem ocorrer erros ao acessar o banco de dados.");
        }
        
        while (running) {
            ViewsManager.clear();
            ViewsManager.title("Bem-vindo à Loja Virtual");
            int choice = ViewsManager.mainMenu();
            switch (choice) {
                case 1: ViewsManager.manageAvaliacao(); break;
                case 2: ViewsManager.manageCategoria(); break;
                case 3: ViewsManager.manageCliente(); break;
                case 4: ViewsManager.manageFornecedor(); break;
                case 5: ViewsManager.managePagamento(); break;
                case 6: ViewsManager.managePedido(); break;
                case 7: ViewsManager.manageProduto(); break;
                case 0:
                    LOGGER.logInfo("Aplicação finalizada pelo usuário.");
                    ViewsManager.println("Saindo...");
                    running = false; break;
                default:
                    LOGGER.logWarning("Opção inválida selecionada: " + choice);
                    ViewsManager.println("Opção inválida. Tente novamente.");
            }
        }
    }
}