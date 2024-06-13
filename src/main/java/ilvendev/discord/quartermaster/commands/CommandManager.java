package ilvendev.discord.quartermaster.commands;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final HashMap<String, String> commands = new HashMap<String, String >() {{
        put("arrest", "Open an arrest modal");
        put("ranks", "Open a ranks modal");
        put("excuses", "Open an excuses modal");
        put("createschedule", "Create a new schedule embed");
    }};

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commands.forEach((name, description) -> {
            commandData.add(Commands.slash(name, description));
        });
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commands.forEach((name, description) -> {
            commandData.add(Commands.slash(name, description));
        });
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Modal modal;
        switch (command){
            case "arrest":
                event.replyModal(ActivityModals.createArrestModal()).queue();
                break;
            case "ranks":
                event.replyModal(ActivityModals.createRanksModal()).queue();
                break;
            case "excuses":
                event.replyModal(ActivityModals.createExcusesModal()).queue();
                break;
            case "createschedule":
                Schedule schedule = new Schedule();
                event.getChannel().sendMessageEmbeds(schedule.createScheduleEmbed()).queue();
                event.reply("Schedule created").setEphemeral(true).queue();
                break;
        }

    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String username = event.getMember().getNickname() != null ? event.getMember().getNickname() : event.getUser().getEffectiveName();
        switch (event.getModalId()){
            case "arrestModal":
                List<ModalMapping> arrestValues = event.getValues();

                event.getChannel().sendMessageEmbeds(ActivityModals.createArrestAnswerEmbed(arrestValues)).queue();
                event.reply("Twój areszt został przyjęty").setEphemeral(true).queue();

                break;
            case "ranksModal":
                List<ModalMapping> ranksValues = event.getValues();

                event.getChannel().sendMessageEmbeds(ActivityModals.createRanksAnswerEmbed(username, ranksValues)).queue();;
                event.reply("Twój stopień został zmieniony").setEphemeral(true).queue();
                break;
            case "excusesModal":
                List<ModalMapping> excusesValues = event.getValues();

                event.getChannel().sendMessageEmbeds(ActivityModals.createExcuseAnswerEmbed(username, excusesValues)).queue();
                event.reply("Twoje zwolnienie zostało wysłane").setEphemeral(true).queue();
                break;
        }
    }
}
