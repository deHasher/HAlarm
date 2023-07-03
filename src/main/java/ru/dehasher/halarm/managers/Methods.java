package ru.dehasher.halarm.managers;

import javazoom.jl.player.Player;
import ru.dehasher.halarm.HAlarm;
import java.io.BufferedInputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Methods {
    // Получить случайный int в заданном диапазоне.
    public static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * ((max + 1) - min)) + min);
    }

    // Спокойной ночи.
    public static void sleep(Long time) {
        try {
            Thread.sleep(time);
        } catch (Throwable ignored) {}
    }

    // Перезагрузить ПК.
    public static void reload() {
        try {
            Runtime.getRuntime().exec("shutdown -a");
            Runtime.getRuntime().exec("shutdown -r -t 0");
        } catch (Throwable ignored) {}
    }

    // Включить музыку.
    public static void play() {
        CompletableFuture.runAsync(() -> {
            try {
                BufferedInputStream buffer = new BufferedInputStream(Objects.requireNonNull(HAlarm.class.getResourceAsStream("/Helltaker.mp3")));
                Player player = new Player(buffer);
                player.play();
            } catch (Throwable ignored) {}
        });
    }
}