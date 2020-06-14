package net.cloudybyte.bot.util;

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