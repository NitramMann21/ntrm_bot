package de.arnomann.martin.ntrmbot;

import de.arnomann.martin.ntrmbot.commands.ApplyCommand;
import de.arnomann.martin.ntrmbot.commands.CreditsCommand;
import de.arnomann.martin.ntrmbot.commands.NotifyCommand;
import de.arnomann.martin.ntrmbot.commands.UploadPlanCommand;
import de.arnomann.martin.ntrmbot.commands.types.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SlashCommandManager extends ListenerAdapter {

    private Map<String, SlashCommand> commandMap;

    public SlashCommandManager() {
        // Put slash commands in map
        commandMap = new ConcurrentHashMap<>();

        commandMap.put("credits", new CreditsCommand());
        commandMap.put("apply", new ApplyCommand());
        commandMap.put("notify", new NotifyCommand());
        commandMap.put("uploadplan", new UploadPlanCommand());

        // Activate slash commands
        CommandListUpdateAction commands = Bot.guild.updateCommands();

        List<CommandData> commandDataList = new ArrayList<>();

        commandDataList.add(new CommandData("credits", "Shows the credits // Zeigt die Credits."));

        commandDataList.add(new CommandData("apply", "Apply for a role // Bewerbe dich für eine Rolle")
                .addOption(OptionType.ROLE, "role", "The role to apply for // Die Rolle zum Bewerben", true)
                .addOption(OptionType.STRING, "text", "Why should we take you? // Warum sollten wir dich nehmen?",
                        true));

        commandDataList.add(new CommandData("notify", "Toggles notifications // Schaltet Benachrichtigungen an/aus")
                .addOptions(new OptionData(OptionType.STRING, "notification", "The type of notification // Die Benachrichtigung",
                        true).addChoice("JTA", "JTA")));

        commandDataList.add(new CommandData("uploadplan", "Sets the next upload // Setzt den nächsten Upload")
                .setDefaultEnabled(false).addOption(OptionType.STRING, "project", "The project // Das Projekt", true)
                .addOption(OptionType.STRING, "content", "The content of the video // Der Inhalt des Videos", true)
                .addOption(OptionType.STRING, "time", "When the video will be released // Wann das Video aufgenommen wird",
                        true));

        commands.addCommands(commandDataList).complete();

        List<Command> commandList = Bot.guild.retrieveCommands().complete();

        Bot.guild.updateCommandPrivilegesById(getCommandByName("uploadplan", commandList).getIdLong(),
                CommandPrivilege.enable(Bot.guild.getRolesByName("Organisation", false).get(0))).queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Run slash command
        String name = event.getName();

        SlashCommand command;

        if((command = commandMap.get(name)) != null) {
            command.performCommand(event, event.getMember(), event.getChannel());
        }
    }

    private Command getCommandByName(String name, List<Command> commands) {
        for(Command cmd : commands) {
            if(cmd.getName().equalsIgnoreCase(name))
                return cmd;
        }
        return null;
    }

}
