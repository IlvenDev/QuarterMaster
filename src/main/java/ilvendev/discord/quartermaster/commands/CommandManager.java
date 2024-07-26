package ilvendev.discord.quartermaster.commands;

import ilvendev.discord.quartermaster.functionalities.FunctionalityModals;
import ilvendev.discord.quartermaster.functionalities.Schedule;
import ilvendev.discord.quartermaster.googlesheets.SheetsSetup;
import ilvendev.discord.quartermaster.userManagement.UserManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandManager extends ListenerAdapter {
    private static int arrestCounter = 2;
    List<CommandData> commandData = new ArrayList<>();

    private final HashMap<String, String> commands = new HashMap<String, String >() {{
        put("arrest", "Open an arrest modal");
        put("ranks", "Open a ranks modal");
        put("excuses", "Open an excuses modal");
        put("createschedule", "Create a new schedule embed");
        put("setupusers", "Sets up rowNumber/User map");
    }};

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        commands.forEach((name, description) -> {
            commandData.add(Commands.slash(name, description));
        });
        commandData.add(Commands.slash("setup", "Sets up sheets to discord connection")
                .addOption(OptionType.STRING, "spreadsheet", "ID of your spreadsheet", true));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        commands.forEach((name, description) -> {
            commandData.add(Commands.slash(name, description));
        });
        commandData.add(Commands.slash("setup", "Sets up sheets to discord connection")
                .addOption(OptionType.STRING, "spreadsheet", "ID of your spreadsheet", true));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        switch (command){
            case "arrest":
                event.replyModal(FunctionalityModals.createArrestModal()).queue();
                break;
            case "ranks":
                event.replyModal(FunctionalityModals.createRanksModal()).queue();
                break;
            case "excuses":
                event.replyModal(FunctionalityModals.createExcusesModal()).queue();
                break;
            case "createschedule":
                Schedule schedule = new Schedule();
                event.getChannel().sendMessageEmbeds(schedule.createScheduleEmbed()).queue();
                event.reply("Schedule created").setEphemeral(true).queue();
                break;
            case "setup":
                SheetsSetup.setSpreadsheetId(event.getOption("spreadsheet").getAsString());
                event.reply("Connection set up").setEphemeral(true).queue();
                break;
            case "setupusers":
                event.deferReply().setEphemeral(true).queue();
                UserManager.setupUsers(event.getGuild().getMembers());
                event.getHook().sendMessage("Users set up").queue();
                break;
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String username = event.getUser().getName();
        switch (event.getModalId()){
            case "arrestModal":
                event.deferReply().setEphemeral(true).queue();
                if (FunctionalityModals.writeArrestToRoster(event.getValues(), arrestCounter)){
                    event.getChannel().sendMessageEmbeds(FunctionalityModals.createArrestAnswerEmbed(event.getValues())).queue();
                    event.getHook().sendMessage("Twój areszt został przyjęty").queue();
                }
                arrestCounter++;
                break;
            case "ranksModal":
                event.deferReply().setEphemeral(true).queue();
                if (FunctionalityModals.writeRankToRoster(username, event.getValues())){
                    event.getChannel().sendMessageEmbeds(FunctionalityModals.createRanksAnswerEmbed(username, event.getValues())).queue();
                    event.getHook().sendMessage("Twój stopień został zmieniony").queue();
                } else {
                    event.reply("Wystąpił błąd").queue();
                }
                break;
            case "excusesModal":
                event.deferReply().setEphemeral(true).queue();
                if (FunctionalityModals.writeExcuseToRoster(username ,event.getValues())){
                    event.getChannel().sendMessageEmbeds(FunctionalityModals.createExcuseAnswerEmbed(username, event.getValues())).queue();
                    event.getHook().sendMessage("Twoje zwolnienie zostało przyjęte").queue();
                } else {
                    event.reply("Wystąpił błąd").queue();
                }
                break;
        }
    }
}
