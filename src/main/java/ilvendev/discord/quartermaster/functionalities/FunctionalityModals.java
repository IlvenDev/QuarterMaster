package ilvendev.discord.quartermaster.functionalities;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.List;

public class FunctionalityModals {

    public static Modal createArrestModal(){
        return Modal.create("arrestModal", "Arrest")
                .addComponents(
                        ActionRow.of(TextInput.create("arrestedPeople", "Arrested", TextInputStyle.SHORT)
                                .setPlaceholder("Who is arrested")
                                .build()),
                        ActionRow.of(TextInput.create("arrestingOfficer", "Arresting officer", TextInputStyle.SHORT)
                                .setPlaceholder("Who is arresting")
                                .build()),
                        ActionRow.of(TextInput.create("reasons", "Reasons", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("Reason for arrest")
                                .build()),
                        ActionRow.of(TextInput.create("punishment", "Punishment", TextInputStyle.SHORT)
                                .setPlaceholder("What is the punishment")
                                .build()),
                        ActionRow.of(TextInput.create("branch", "Branch", TextInputStyle.SHORT)
                                .setPlaceholder("Branch that the arrested is in")
                                .build())
                )
                .build();
    }

    public static Modal createRanksModal(){
        return Modal.create("ranksModal", "Ranks")
                .addComponents(
                        ActionRow.of(TextInput.create("currentRank", "Current rank", TextInputStyle.SHORT)
                                .setPlaceholder("Your current rank")
                                .build()),
                        ActionRow.of(TextInput.create("newRank", "New rank", TextInputStyle.SHORT)
                                .setPlaceholder("Your new rank")
                                .build()),
                        ActionRow.of(TextInput.create("giver", "Giver", TextInputStyle.SHORT)
                                .setPlaceholder("Who gave the promotion/degradation")
                                .build())
                )
                .build();
    }

    public static Modal createExcusesModal(){
        return Modal.create("excusesModal", "Excuses")
                .addComponents(
                        ActionRow.of(TextInput.create("excuseDate", "Excuse time", TextInputStyle.SHORT)
                                .setPlaceholder("When will you be excused")
                                .build()),
                        ActionRow.of(TextInput.create("reason", "Reason", TextInputStyle.SHORT)
                                .setPlaceholder("Reason for excuse")
                                .build())
                )
                .build();
    }

    public static MessageEmbed createArrestAnswerEmbed(List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("New arrest")
            .addField("Arrested", modalValues.get(0).getAsString(), false)
            .addField("Arresting officer", modalValues.get(1).getAsString(), false)
            .addField("Reason", modalValues.get(2).getAsString(), false)
            .addField("Punishment", modalValues.get(3).getAsString(), false)
            .addField("Branch", modalValues.get(4).getAsString(), false)
            .setColor(0xdb0404);

        return embed.build();
    }

    public static MessageEmbed createRanksAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Rank change" + username)
            .addField("Current rank", modalValues.get(0).getAsString(), false)
            .addField("New rank", modalValues.get(1).getAsString(), false)
            .addField("Giver", modalValues.get(2).getAsString(), false)
            .setColor(0x00bf00);

        return embed.build();
    }

    public static MessageEmbed createExcuseAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Excuse " + username)
            .addField("Excuse date", modalValues.get(0).getAsString(), false)
            .addField("Reason", modalValues.get(1).getAsString(), false)
            .setColor(0x00bf00);

        return embed.build();
    }
}
