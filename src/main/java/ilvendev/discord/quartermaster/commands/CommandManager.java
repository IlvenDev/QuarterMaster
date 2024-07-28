package ilvendev.discord.quartermaster.commands;

import ilvendev.discord.quartermaster.functionalities.FunctionalityModals;
import ilvendev.discord.quartermaster.functionalities.ScheduleHandler;
import ilvendev.discord.quartermaster.googlesheets.SheetsSetup;
import ilvendev.discord.quartermaster.googlesheets.WritingHandler;
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
                        .addChoice("Monday", "monday")
                        .addChoice("Tuesday", "tuesday")
                        .addChoice("Wednesday", "wednesday")
                        .addChoice("Thursday", "thursday")
                        .addChoice("Friday", "friday")
                        .addChoice("Saturday", "saturday")
                        .addChoice("Sunday", "sunday"))
                .addOption(OptionType.STRING, "description", "New day description"));
        commandData.add((Commands.slash("setupusers", "Sets up user data")));
        commandData.add(Commands.slash("setup", "Sets up sheets to discord connection")
                .addOption(OptionType.STRING, "spreadsheet", "ID of your spreadsheet", true)
                .addOption(OptionType.STRING, "idcolumn", "Column in which discord user IDs are stored", true)
                .addOption(OptionType.STRING, "rankcolumn", "Column in which your ranks are stored", true)
                .addOption(OptionType.STRING, "excusecolumn", "Column in which your excuses are stored", true)
                .addOption(OptionType.STRING, "rostersheet", "Name of your roster sheet", true)
                .addOption(OptionType.STRING, "arrestsheet", "Name of your arrest sheet", true));


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
                HashMap<String, String> values = SheetsSetup.getSheetValues();
                for (int i = 0; i <= event.getOptions().size(); i++) {
                    values.put(event.getOptions().get(i).getName(), event.getOptions().get(i).getAsString());
                    System.out.println(event.getOptions().get(i).getName());
                    System.out.println(event.getOptions().get(i).getAsString());
                }
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
                if (WritingHandler.writeArrestToRoster(event.getValues(), arrestCounter)){
                    event.getChannel().sendMessageEmbeds(FunctionalityModals.createArrestAnswerEmbed(event.getValues())).queue();
                    event.getHook().sendMessage("Arrest accepted").queue();
                }
                arrestCounter++;
                break;
            case "ranksModal":
                event.deferReply().setEphemeral(true).queue();
                if (WritingHandler.writeRankToRoster(username, event.getValues())){
                    event.getChannel().sendMessageEmbeds(FunctionalityModals.createRanksAnswerEmbed(username, event.getValues())).queue();
                    event.getHook().sendMessage("Rank changed").queue();
                } else {
                    event.reply("An error occurred").queue();
                }
                break;
            case "excusesModal":
                event.deferReply().setEphemeral(true).queue();
                if (WritingHandler.writeExcuseToRoster(username,event.getValues())){
                    event.getChannel().sendMessageEmbeds(FunctionalityModals.createExcuseAnswerEmbed(username, event.getValues())).queue();
                    event.getHook().sendMessage("Excuse accepted").queue();
                } else {
                    event.reply("An error occurred").queue();
                }
                break;
        }
    }
}
