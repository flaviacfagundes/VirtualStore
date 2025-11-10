package utils;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Logger {

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[94m";
    private static final String GREEN = "\u001B[92m";
    private static final String YELLOW = "\u001B[93m";
    private static final String RED = "\u001B[91m";

    private static final String CODE_INFO = "NNN";
    private static final String CODE_SUCCESS = "101";
    private static final String CODE_WARNING = "202";
    private static final String CODE_ERROR = "404";

    private boolean saveToFile;
    private String fileName;
    private String minLevel;
    private final String[] levelsOrder = {"INFO", "SUCCESS", "WARNING", "ERROR"};
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger(boolean saveToFile, String fileName, String minLevel) {
        this.saveToFile = saveToFile;
        this.fileName = fileName;
        this.minLevel = minLevel.toUpperCase();

        if (saveToFile) {
            try {
                Path path = Paths.get(fileName);
                Files.createDirectories(path.getParent());
                if (!Files.exists(path)) {
                    Files.write(path, ("||===== Logger iniciado em " + LocalDateTime.now() + " =====||\n").getBytes());
                }
            } catch (IOException e) {
                System.err.println(RED + "[ERRO] Falha ao criar arquivo de log: " + e.getMessage() + RESET);
            }
        }
    }

    public Logger(boolean saveToFile) {
        this(saveToFile, "LOGS/log.txt", "INFO");
    }

    private boolean shouldLog(String level) {
        int current = getLevelIndex(level);
        int min = getLevelIndex(minLevel);
        return current >= min;
    }

    private int getLevelIndex(String level) {
        for (int i = 0; i < levelsOrder.length; i++) {
            if (levelsOrder[i].equalsIgnoreCase(level)) return i;
        }
        return 0;
    }

    private void writeToFile(String message) {
        if (!saveToFile) return;

        try {
            Path path = Paths.get(fileName);

            // Lê o conteúdo existente
            List<String> linhasAntigas = Files.exists(path)
                    ? Files.readAllLines(path)
                    : List.of("||===== Logger iniciado em " + LocalDateTime.now() + " =====||");

            // Cria o novo conteúdo com o log mais recente no topo
            StringBuilder novoConteudo = new StringBuilder();
            novoConteudo.append(message).append("\n");
            for (String linha : linhasAntigas) {
                novoConteudo.append(linha).append("\n");
            }

            // Sobrescreve o arquivo
            Files.write(path, novoConteudo.toString().getBytes());

        } catch (IOException e) {
            System.err.println(RED + "[ERRO] Falha ao escrever no log: " + e.getMessage() + RESET);
        }
    }

    private void log(String level, String message, String color, String code) {
        if (!shouldLog(level)) return;

        String timestamp = LocalDateTime.now().format(formatter);
        String formatted = String.format("[%s] [%s] %s: %s", timestamp, code, level, message);

        System.out.println(color + formatted + RESET);
        writeToFile(formatted);
    }

    public void logInfo(String message) {
        log("INFO", message, BLUE, CODE_INFO);
    }

    public void logSuccess(String message) {
        log("SUCCESS", message, GREEN, CODE_SUCCESS);
    }

    public void logWarning(String message) {
        log("WARNING", message, YELLOW, CODE_WARNING);
    }

    public void logError(String message) {
        log("ERROR", message, RED, CODE_ERROR);
    }

    public void clearLogs() {
        if (!saveToFile) {
            System.out.println(RED + "[!] O salvamento em arquivo está desativado." + RESET);
            return;
        }
        try {
            Files.write(Paths.get(fileName),
                    ("||===== Log limpo em " + LocalDateTime.now() + " =====||\n").getBytes());
            System.out.println(GREEN + "[✓] Arquivo de log limpo com sucesso." + RESET);
        } catch (IOException e) {
            System.out.println(RED + "[ERRO] Falha ao limpar o log: " + e.getMessage() + RESET);
        }
    }
}
