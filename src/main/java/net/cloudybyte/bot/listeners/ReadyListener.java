package net.cloudybyte.bot.listeners;


import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.cloudybyte.bot.util.Colors;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;



public class ReadyListener implements EventListener
    {
    private String GREEN = Colors.GREEN;
    private String RESET = Colors.RESET;
    SentryClient sentry = SentryClientFactory.sentryClient();


        public static void main(String[] args)
                throws LoginException
        {
            JDA jda = new JDABuilder(args[0])
                    .addEventListeners(new ReadyListener()).build();
        }

        @Override
        public void onEvent(GenericEvent event)
        {
            if(event instanceof ReadyEvent)
                System.out.println(GREEN + "API is ready!" + RESET);
                sentry.sendMessage("Booted!");

        }
    }

