package de.arnomann.martin.ntrmbot.listeners;

import de.arnomann.martin.ntrmbot.Bot;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if(event.getGuild().getIdLong() != 853325922145009664L)
            return;

        event.getGuild().loadMembers();

        // Get guild of Nitram21
        Bot.guild = Bot.jda.getGuildById(853325922145009664L);
        // Get Nitram21's user
        Bot.nitram = Bot.jda.retrieveUserById("424474905418137618").complete();
    }
}
