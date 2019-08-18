package gq.not11.bot.listeners;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import javax.security.auth.login.LoginException;


public class ReadyListener implements EventListener
    {
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
                System.out.println("API is ready!");
        }
    }

