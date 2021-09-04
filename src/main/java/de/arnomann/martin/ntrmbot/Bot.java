package de.arnomann.martin.ntrmbot;

import de.arnomann.martin.ntrmbot.listeners.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Random;

public class Bot {

    public static JDA jda;

    private static final Activity[] activities = {
            Activity.playing("nichts..."),
            Activity.watching("Nitram21"),
            Activity.listening("der Konsole"),
            Activity.watching("sich Discord-Channels an.") };

    private static SlashCommandManager slashCmdMan;

    public static Guild guild;
    public static User nitram;

    public static final String VERSION = "1.5.2";

    public static void main(String[] args) {
        try {
            new Bot();
        } catch (LoginException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Bot() throws LoginException, IOException, InterruptedException {
        Configuration.read();
        YamlUtil.load();

        JDABuilder builder = JDABuilder.createDefault(Configuration.getToken());
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(activities[0]);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.addEventListeners(new ReadyListener(), new StageManager(), new MessageListener(), new GuildJoinListener(), new ButtonClickListener(),
                new ListenerAdapter() {
                    @Override
                    public void onGuildReady(@NotNull GuildReadyEvent event) {
                        if(event.getGuild().getName().equals("Nitram21"))
                            event.getGuild().loadMembers();
                    }
                }
        );

        jda = builder.build();

        jda.awaitReady();

        jda.addEventListener(slashCmdMan = new SlashCommandManager());

        ConsoleListener.start();
        activitySwitcher();
    }

    public static void shutdown() throws InterruptedException {
        if (jda != null) {
                for (int i = 5; i > 0; i--) {
                    if (i != 1)
                        System.out.println("Bot stops in " + i + " seconds.");
                    else
                        System.out.println("Bot stops in 1 second.");

                    Thread.sleep(1000);
                }
            System.out.println("Stopping...");
            jda.getPresence().setStatus(OnlineStatus.OFFLINE);
            jda.shutdown();
        }

        System.exit(0);
    }

    private void activitySwitcher() {
        new Thread(() -> {
            long time = System.currentTimeMillis();

            while (true) {
                if (System.currentTimeMillis() >= time + 10000) {
                    Random r = new Random();
                    int i = r.nextInt(activities.length);

                    jda.getPresence().setActivity(activities[i]);

                    time = System.currentTimeMillis();
                }
            }
        }).start();
    }

    public static EmbedBuilder defaultEmbed() {
        return new EmbedBuilder().setTimestamp(OffsetDateTime.now()).setColor(new Color(
                0x00ffff)).setImage("https://nitrammann21.github.io/ntrmbotseperator.png").setFooter(VERSION);
    }

}
