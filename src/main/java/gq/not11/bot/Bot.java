package gq.not11.bot;


    /*
     *     Copyright 2015-2018 Austin Keener & Michael Ritter & Florian Spieß
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *     http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.*;




import com.sedmelluq.*;
import net.dv8tion.jda.core.managers.AudioManager;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

    public class Bot extends ListenerAdapter
    {
        /**
         * This is the method where the program starts.
         */
        public static void main(String[] args)
        {
            //We construct a builder for a BOT account. If we wanted to use a CLIENT account
            // we would use AccountType.CLIENT
            try
            {
                JDA jda = new JDABuilder("NTUzOTg2MzAwNjkzMzE1NjE1.XTHgug.jp8vP3V_KQ4G8fAEZMw-ImK52LA")         // The token of the account that is logging in.
                        .addEventListener(new Bot())  // An instance of a class that will handle events.
                        .build();
                jda.awaitReady();

                //Setting Rich Presence





                //Initializing of Lavaplayer
                AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
                AudioSourceManagers.registerRemoteSources(playerManager);

                AudioPlayer player = playerManager.createPlayer();




            // Blocking guarantees that JDA will be completely loaded.
                System.out.println("Finished Building JDA!");
            }
            catch (LoginException e)
            {
                //If anything goes wrong in terms of authentication, this is the exception that will represent it
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                //Due to the fact that awaitReady is a blocking method, one which waits until JDA is fully loaded,
                // the waiting can be interrupted. This is the exception that would fire in that situation.
                //As a note: in this extremely simplified example this will never occur. In fact, this will never occur unless
                // you use awaitReady in a thread that has the possibility of being interrupted (async thread usage and interrupts)
                e.printStackTrace();
            }



        }



        /**
         * NOTE THE @Override!
         * This method is actually overriding a method in the ListenerAdapter class! We place an @Override annotation
         *  right before any method that is overriding another to guarantee to ourselves that it is actually overriding
         *  a method from a super class properly. You should do this every time you override a method!
         *
         * As stated above, this method is overriding a hook method in the
         * {@link ListenerAdapter ListenerAdapter} class. It has convience methods for all JDA events!
         * Consider looking through the events it offers if you plan to use the ListenerAdapter.
         *
         * In this example, when a message is received it is printed to the console.
         *
         * @param event
         *          An event containing information about a {@link Message Message} that was
         *          sent in a channel.
         */
        @Override
        public void onMessageReceived(MessageReceivedEvent event)
        {


            //Reading config











            if (event.getAuthor().isBot()) return;
            // We don't want to respond to other bot accounts, including ourself
            Message message = event.getMessage();

            //Slicing args




            String content = message.getContentRaw();







            // getContentRaw() is an atomic getter
            // getContentDisplay() is a lazy getter which modifies the content for e.g. console view (strip discord formatting)
            if (content.equals(".ping"))
            {
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Pong!").queue(); // Important to call .queue() on the RestAction returned by sendMessage(...)
            }
            else if (content.equals(".join"))
            {
                MessageChannel channel = event.getChannel();
                Member member = event.getMember();
                Guild guild = event.getGuild();
                VoiceChannel myChannel = member.getVoiceState().getChannel();

                AudioManager audioManager = guild.getAudioManager();


                try {

                    audioManager.openAudioConnection(myChannel);
                    channel.sendMessage(("Joined your voicechannel!")).queue();
                }
                catch(IllegalArgumentException e) {
                    channel.sendMessage(("Can't join your voice channel... Make sure you are connected and I have the proper permissions!")).queue();
                }
                return;

            }

            else if (content.equals(".play")) {
                Guild guild = event.getGuild();



                AudioManager audioManager = guild.getAudioManager();

            }

        }
    }

