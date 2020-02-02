package br.com.exemplo.demofileapi.util;

public class Utils {

    static void printMemoryUsage() {
        Runtime r = Runtime.getRuntime();
        System.out.println("Memory Used: " + (r.totalMemory() - r.freeMemory()));
    }

    static void printAndGetTime(String mensagem) {
        long timeInMillis = System.currentTimeMillis();

    }
}
