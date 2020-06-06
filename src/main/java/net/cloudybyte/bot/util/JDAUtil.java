package net.cloudybyte.bot.util;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.requests.RestFuture;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.concurrent.ExecutionException;


public class JDAUtil {

    /**
     * Waits for an JDA entity without blocking the Thread
     *
     * @param action The RestAction
     * @param <T>    The return type
     * @return T The RestActions output
     *//*
    public static <T> T waitForEntity(RestAction<T> action) {
        RestFuture<T> future = (RestFuture<T>) action.submit();
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("[Message] Could not wait for entity:\n\n" +  e);
            return future.getNow(null);
        }
    }*/
}