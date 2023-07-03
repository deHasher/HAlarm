package ru.dehasher.halarm.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class TaskManager {
    private final HashSet<String> data = new HashSet<>();
    private final HashSet<String> tmp  = new HashSet<>();

    @SuppressWarnings("InfiniteLoopStatement")
    public TaskManager() {
        CompletableFuture.runAsync(() -> {
           while (true) {
               updateTasks();
               if (getTasks().stream().anyMatch(task -> task.toLowerCase().contains("taskmgr.exe"))) {
                   Methods.reload();
               }
               Methods.sleep(200L);
           }
        });
    }

    // Получить список запущенных задач.
    private HashSet<String> getTasks() {
        return data;
    }

    // Получить список запущенных задач.
    private void updateTasks() {
        try {
            tmp.clear();
            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                tmp.add(line);
            }
            process.destroyForcibly();
            data.clear();
            data.addAll(tmp);
            reader.close();
        } catch (Throwable ignored) {}
    }
}