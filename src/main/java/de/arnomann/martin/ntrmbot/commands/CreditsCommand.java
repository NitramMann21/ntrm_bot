package de.arnomann.martin.ntrmbot.commands;

import de.arnomann.martin.ntrmbot.Bot;
import de.arnomann.martin.ntrmbot.commands.types.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.List;

public class CreditsCommand implements SlashCommand {

    @Override
    public void performCommand(SlashCommandEvent event, Member m, MessageChannel channel) {
        Guild g = m.getGuild();
        event.replyEmbeds(Bot.defaultEmbed()
                .setTitle("Credits")
                .addField("Team", getMembersWithRole(g, g.getRolesByName("Team", false).get(0)), false)
                .addField("Supporter", getMembersWithRole(g, g.getRolesByName("Supporter", false).get(0)), false)
                .addField("ntrm_bot", "Nitram21", false)
                .build())
                .addActionRow(Button.link("https://www.youtube.com/channel/UC4Nn3hHsgSAymE1k9joY8Jw", "Nitram21 auf YouTube"))
                .setEphemeral(true)
                .queue();
    }

    private String getMembersWithRole(Guild g, Role r) {
        List<Member> members = g.getMembersWithRoles(r);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < members.size(); i++) {
            sb.append(members.get(i).getEffectiveName());
            if(i < members.size() - 1) sb.append(", ");
        }

        return sb.toString();
    }

}
