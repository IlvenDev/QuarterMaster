package ilvendev.discord.quartermaster.commands;

import ilvendev.discord.quartermaster.functionalities.FunctionalityModals;
import ilvendev.discord.quartermaster.functionalities.ScheduleHandler;
import ilvendev.discord.quartermaster.googlesheets.SheetsSetup;
import ilvendev.discord.quartermaster.userManagement.UserManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.RestAction;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandManager extends ListenerAdapter {
    private static int arrestCounter = 2;

    private void setupCommands(Guild event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add((Commands.slash("arrest", "Open an arrest modal")));
        commandData.add((Commands.slash("ranks", "Open a ranks modal")));
        commandData.add((Commands.slash("excuses", "Open a excuses modal")));
        commandData.add((Commands.slash("createschedule", "Create a new schedule")));
        commandData.add((Commands.slash("changeschedule", "Change a day in the schedule"))
                .addOptions(new OptionData(OptionType.STRING, "day", "Day which description to change")
                        .addChoice("Poniedziałek", "poniedziałek")
                        .addChoice("Wtorek", "wtorek")
                        .addChoice("Środa", "środa")
                        .addChoice("Czwartek", "czwartek")
                        .addChoice("Piątek", "piątek"))
                        .addOption(OptionType.STRING, "description", "New day description"));
        commandData.add((Commands.slash("setupusers", "Sets up user data")));
        commandData.add(Commands.slash("setup", "Sets up sheets to discord connection")
                .addOption(OptionType.STRING, "spreadsheet", "ID of your spreadsheet", true));

        event.updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        setupCommands(event.getGuild());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        setupCommands(event.getGuild());
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
                MessageEmbed embed = ScheduleHandler.createScheduleEmbed();
                event.getChannel().sendMessageEmbeds(embed).queue();
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
            case "changeschedule":
                MessageChannelUnion channel = event.getChannel();
                RestAction<Message> action = channel.retrieveMessageById(channel.getLatestMessageId());
                Message message = action.complete();
                ScheduleHandler.updateDay(message, event.getOption("day").getAsString(), event.getOption("description").getAsString());
                event.reply("Schedule changed").setEphemeral(true).queue();
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
