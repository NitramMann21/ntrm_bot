package de.arnomann.martin.ntrmbot.commands;

import de.arnomann.martin.ntrmbot.Bot;
import de.arnomann.martin.ntrmbot.YamlUtil;
import de.arnomann.martin.ntrmbot.commands.types.SlashCommand;
import de.arnomann.martin.ntrmbot.YamlUtil.YamlData.Application;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

public class ApplyCommand implements SlashCommand {

    @Override
    public void performCommand(SlashCommandEvent event, Member m, MessageChannel channel) {
        if (m.getRoles().contains(event.getOption("role").getAsRole())) {
            event.replyEmbeds(Bot.defaultEmbed().setTitle("Fehler").setDescription("Diese Rolle besitzt du bereits!").build())
                    .setEphemeral(true).queue();
            return;
        }

        PrivateChannel privChannel = Bot.nitram.openPrivateChannel().complete();
        Message message = privChannel.sendMessageEmbeds(new EmbedBuilder().setTitle("Bewerbung von " + m.getEffectiveName() + "#" + m.getUser()
                .getDiscriminator() + " als " + event.getOption("role").getAsRole().getName()).setDescription(event.getOption("text")
                .getAsString()).build()).setActionRow(Button.success("yes", "Annehmen"), Button.danger("no", "Ablehnen")
        ).complete();

        privChannel.close().queue();
        event.replyEmbeds(Bot.defaultEmbed().setTitle("Anfrage gesendet!").setDescription("Nun kannst du warten, ob deine Anfrage angenommen wird.\n" +
                        "Sollte dies geschehen, wirst du allerdings nicht gepingt. Halte also nach deinen Rollen Ausschau!")
                .build()).setEphemeral(true).queue();

        YamlUtil.getApplications().add(new Application(m.getIdLong(), event.getOption("role").getAsRole().getIdLong(), message.getIdLong()));
        YamlUtil.saveYAML();
    }

}
