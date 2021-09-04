package de.arnomann.martin.ntrmbot.listeners;

import de.arnomann.martin.ntrmbot.Bot;
import de.arnomann.martin.ntrmbot.YamlUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ButtonClickListener extends ListenerAdapter {

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        YamlUtil.YamlData.Application application = YamlUtil.getApplicationByMessage(event.getMessage());
        if(application != null) {
            if(event.getMessage().getIdLong() == application.getMessageId()) {
                MessageEmbed embed = event.getMessage().getEmbeds().get(0);
                EmbedBuilder builder = new EmbedBuilder(event.getMessage().getEmbeds().get(0));

                if (event.getButton().getId().equalsIgnoreCase("yes")) {
                    Bot.guild.getMember(Bot.jda.getUserById(application.getUserId())).getGuild().addRoleToMember(application.getUserId(),
                            Bot.guild.getRoleById(application.getRoleId())).queue();

                    builder.setTitle(embed.getTitle() + " (Angenommen)");

                    event.reply("Anfrage angenommen. Dem Benutzer wurde die Rolle gegeben.").queue();
                } else if (event.getButton().getId().equalsIgnoreCase("no")) {
                    builder.setTitle(embed.getTitle() + " (Abgelehnt)");

                    event.reply("Anfrage abgelehnt.").queue();
                }
                YamlUtil.getApplications().remove(application);
                event.getMessage().editMessageEmbeds(builder.build()).queue();
                YamlUtil.saveYAML();
            }
        }
    }
}
