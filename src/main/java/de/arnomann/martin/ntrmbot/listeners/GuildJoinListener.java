package de.arnomann.martin.ntrmbot.listeners;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        event.getGuild().loadMembers();
    }
}
