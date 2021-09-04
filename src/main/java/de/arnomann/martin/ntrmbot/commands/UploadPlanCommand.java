package de.arnomann.martin.ntrmbot.commands;

import de.arnomann.martin.ntrmbot.Bot;
import de.arnomann.martin.ntrmbot.YamlUtil;
import de.arnomann.martin.ntrmbot.commands.types.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static de.arnomann.martin.ntrmbot.YamlUtil.saveYAML;

public class UploadPlanCommand implements SlashCommand {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    public UploadPlanCommand() {
        scheduler.scheduleWithFixedDelay(() -> {
            if(YamlUtil.getUploadPlan().deleteOldUploads()) {
                updateEmbed();
                saveYAML();
            }
        }, 1L, 1L, TimeUnit.MINUTES);
    }

    @Override
    public void performCommand(SlashCommandEvent event, Member m, MessageChannel channel) {
        YamlUtil.YamlData.UploadPlan plan = YamlUtil.getUploadPlan();
        plan.addUpload(new YamlUtil.YamlData.UploadPlan.Upload(event.getOption("project").getAsString(), event.getOption("content")
                .getAsString(), LocalDateTime.parse(event.getOption("time").getAsString(), DateTimeFormatter.ofPattern(
                        "dd.MM.yyyy HH:mm"))));
        saveYAML();

        updateEmbed();

        event.replyEmbeds(Bot.defaultEmbed().setTitle("Uploadplan").setDescription("Uploadplan geupdated!").build()).setEphemeral(true).queue();
    }

    public static void updateEmbed() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        YamlUtil.YamlData.UploadPlan plan = YamlUtil.getUploadPlan();

        TextChannel uploadPlanChannel = Bot.guild.getTextChannelsByName("upload-plan", false).get(0);
        EmbedBuilder builder = Bot.defaultEmbed().setTitle("Uploadplan");

        for(YamlUtil.YamlData.UploadPlan.Upload upload : plan.getUploads())
            builder.addField(upload.getProject(), "> *" + upload.getContent() + "*\n" + format.format(YamlUtil.YamlData.UploadPlan
                    .longToDateTime(upload.getTime())), false);

        MessageEmbed embed = builder.build();

        MessageHistory history = uploadPlanChannel.getHistoryFromBeginning(1).complete();
        if(history.isEmpty())
            uploadPlanChannel.sendMessageEmbeds(embed).queue();
        else {
            history.getRetrievedHistory().get(0).editMessageEmbeds(embed).queue();
        }
    }

}
