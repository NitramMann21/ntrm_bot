package de.arnomann.martin.ntrmbot.commands.types;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Member;

public interface SlashCommand {
    void performCommand(SlashCommandEvent event, Member m, MessageChannel channel);
}
