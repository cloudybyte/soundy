package gq.not11.bot.listeners;


import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;


import  gq.not11.bot.util.Colors;


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
                    .addEventListener(new ReadyListener()).build();
        }

        @Override
        public void onEvent(Event event)
        {
            if(event instanceof ReadyEvent)
                System.out.println(GREEN + "API is ready!" + RESET);
                sentry.sendMessage("Booted!");

        }
    }

