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
    }};

    @Override
    public void onGuildReady(@NotNull  GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commands.forEach((name, description) -> {
            commandData.add(Commands.slash(name, description));
        });
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
      // Same as in onGuildReady
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        Modal modal;
        switch (command){
            case "arrest":
                event.replyModal(CommandModalManager.createArrestModal()).queue();
                break;
            case "ranks":
                event.replyModal(CommandModalManager.createRanksModal()).queue();
                break;
            case "excuses":
                event.replyModal(CommandModalManager.createExcusesModal()).queue();
                break;
        }

    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String modalName = event.getModalId();
        String username = event.getUser().getName();
        switch (modalName){
            case "arrestModal":
                List<ModalMapping> modalValues = event.getValues();

                event.reply("Twój areszt został przyjęty").setEphemeral(true).queue();
                event.getChannel().sendMessageEmbeds(CommandModalManager.createArrestAnswerEmbed(username, modalValues)).queue();

                break;
            case "ranksModal":
                event.reply("Your rank has been changed").setEphemeral(true).queue();
                break;
            case "excusesModal":
                event.reply("Your excuse has been sent").setEphemeral(true).queue();

                break;
        }
    }
}
