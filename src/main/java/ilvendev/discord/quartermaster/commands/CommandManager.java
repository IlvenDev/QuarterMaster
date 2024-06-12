package ilvendev.discord.quartermaster.commands;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private Modal arrestModal(){

        TextInput arrestedPeople = TextInput.create("arrestedPeople", "Aresztowani", TextInputStyle.SHORT)
                .setPlaceholder("Kogo aresztowałeś")
                .build();

        TextInput arrestingOfficer = TextInput.create("arrestingOfficer", "Aresztujący", TextInputStyle.SHORT)
                .setPlaceholder("Kto aresztuje")
                .build();

        TextInput reasons = TextInput.create("reasons", "Powód aresztu", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Za co aresztowany")
                .build();

        TextInput punishment = TextInput.create("punishment", "Wymiar kary", TextInputStyle.SHORT)
                .setPlaceholder("Jaką karę otrzymał")
                .build();

        TextInput branch = TextInput.create("branch", "Przynależnośc oddziałowa", TextInputStyle.SHORT)
                .setPlaceholder("SZTAB | SAS | 43rd | 24th | MP")
                .build();

        Modal modal = Modal.create("arrestModal", "Arrest")
                .addComponents(
                        ActionRow.of(arrestedPeople),
                        ActionRow.of(arrestingOfficer),
                        ActionRow.of(reasons),
                        ActionRow.of(punishment),
                        ActionRow.of(branch))
                .build();

        return modal;
    }

    @Override
    public void onGuildReady(@NotNull  GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("tester", "return tester + userName"));
        commandData.add(Commands.slash("roles", "return role list"));
        commandData.add(Commands.slash("arrest", "open an arrest modal"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
      // Same as in onGuildReady
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("arrest")) {
            Modal modal = arrestModal();
            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("arrestModal")) {
            String arrestedPeople = String.valueOf(event.getValue("arrestedPeople"));
            String arrestingOfficer = String.valueOf(event.getValue("arrestingOfficer"));
            String reasons = String.valueOf(event.getValue("reasons"));
            String punishment = String.valueOf(event.getValue("punishment"));
            String branch = String.valueOf(event.getValue("branch"));

            System.out.println(arrestedPeople);
            System.out.println(arrestingOfficer);
            System.out.println(reasons);
            System.out.println(punishment);
            System.out.println(branch);

            event.reply("Your arrest has been accepted").setEphemeral(true).queue();
        }
    }
}
