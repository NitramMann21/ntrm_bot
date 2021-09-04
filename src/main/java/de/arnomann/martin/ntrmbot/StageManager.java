package de.arnomann.martin.ntrmbot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.stage.StageInstanceCreateEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.EnumSet;

public class StageManager extends ListenerAdapter {

    @Override
    public void onStageInstanceDelete(StageInstanceDeleteEvent event) {
        Guild g = event.getGuild();
        TextChannel stageChat = g.getTextChannelsByName("stage-chat", false).get(0);
        stageChat.putPermissionOverride(g.getPublicRole()).setPermissions(EnumSet.of(Permission.MESSAGE_READ),
                EnumSet.of(Permission.MESSAGE_WRITE, Permission.MESSAGE_EXT_EMOJI)).queue();
        stageChat.sendMessageEmbeds(Bot.defaultEmbed().setTitle("Die Stage ist offline!").setDescription("Erst während der nächsten Live-Session " +
                "könnt ihr hier wieder schreiben.").build()).queue();
    }

    @Override
    public void onStageInstanceCreate(StageInstanceCreateEvent event) {
        Guild g = event.getGuild();
        TextChannel stageChat = g.getTextChannelsByName("stage-chat", false).get(0);
        stageChat.putPermissionOverride(g.getPublicRole()).setPermissions(EnumSet.of(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE,
                Permission.MESSAGE_EXT_EMOJI), EnumSet.noneOf(Permission.class)).queue();
        stageChat.sendMessageEmbeds(Bot.defaultEmbed().setTitle("Die Stage ist live!").setDescription("Schreibrechte wurden aktiviert!").build())
                .queue();
    }

}
