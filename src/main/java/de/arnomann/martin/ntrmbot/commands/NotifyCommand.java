package de.arnomann.martin.ntrmbot.commands;

import de.arnomann.martin.ntrmbot.Bot;
import de.arnomann.martin.ntrmbot.commands.types.SlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class NotifyCommand implements SlashCommand {

    @Override
    public void performCommand(SlashCommandEvent event, Member m, MessageChannel channel) {
        if(event.getUser().isBot())
            return;

        if (event.getOption("notification").getAsString().equals("JTA")) {
            Role jtaUpdatesRole = Bot.guild.getRoleById(874252967858020373L);
            if (m.getRoles().contains(jtaUpdatesRole)) {
                Bot.guild.removeRoleFromMember(m, jtaUpdatesRole).queue();
                event.replyEmbeds(Bot.defaultEmbed().setTitle("Benachrichtigungen").setDescription(jtaUpdatesRole.getAsMention() +
                        "-Rolle entfernt.").build()).setEphemeral(true).queue();
            } else {
                Bot.guild.addRoleToMember(m, jtaUpdatesRole).queue();
                event.replyEmbeds(Bot.defaultEmbed().setTitle("Benachrichtigungen").setDescription(jtaUpdatesRole.getAsMention() +
                        "-Rolle hinzugefügt.").build()).setEphemeral(true).queue();
            }
        }
    }

}
