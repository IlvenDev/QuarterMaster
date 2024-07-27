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

        TextInput arrestedPeople = TextInput.create("arrestedPeople", "Arrested", TextInputStyle.SHORT)
                .setPlaceholder("Who is arrested")
                .build();

        TextInput arrestingOfficer = TextInput.create("arrestingOfficer", "Arresting officer", TextInputStyle.SHORT)
                .setPlaceholder("Who is arresting")
                .build();

        TextInput reasons = TextInput.create("reasons", "Reasons", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Reason for arrest")
                .build();

        TextInput punishment = TextInput.create("punishment", "Punishment", TextInputStyle.SHORT)
                .setPlaceholder("What is the punishment")
                .build();

        TextInput branch = TextInput.create("branch", "Branch", TextInputStyle.SHORT)
                .setPlaceholder("Branch that the arrested is in")
                .build();

        return Modal.create("arrestModal", "Arrest")
                .addComponents(
                        ActionRow.of(arrestedPeople),
                        ActionRow.of(arrestingOfficer),
                        ActionRow.of(reasons),
                        ActionRow.of(punishment),
                        ActionRow.of(branch))
                .build();
    }

    public static Modal createRanksModal(){

        TextInput currentRank = TextInput.create("currentRank", "Current rank", TextInputStyle.SHORT)
                .setPlaceholder("Your current rank")
                .build();

        TextInput newRank = TextInput.create("newRank", "New rank", TextInputStyle.SHORT)
                .setPlaceholder("Your new rank")
                .build();

        TextInput giver = TextInput.create("giver", "Giver", TextInputStyle.SHORT)
                .setPlaceholder("Who gave the promotion/degradation")
                .build();

        return Modal.create("ranksModal", "Ranks")
                .addComponents(
                        ActionRow.of(currentRank),
                        ActionRow.of(newRank),
                        ActionRow.of(giver))
                .build();
    }

    public static Modal createExcusesModal(){

        TextInput excuseDate = TextInput.create("excuseDate", "Excuse time", TextInputStyle.SHORT)
                .setPlaceholder("When will you be excused")
                .build();

        TextInput reason = TextInput.create("reason", "Reason", TextInputStyle.SHORT)
                .setPlaceholder("Reason for excuse")
                .build();

        return Modal.create("excusesModal", "Excuses")
                .addComponents(
                        ActionRow.of(excuseDate),
                        ActionRow.of(reason))
                .build();
    }

    public static MessageEmbed createArrestAnswerEmbed(List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("New arrest");
        embed.addField("Arrested", modalValues.get(0).getAsString(), false);
        embed.addField("Arresting officer", modalValues.get(1).getAsString(), false);
        embed.addField("Reason", modalValues.get(2).getAsString(), false);
        embed.addField("Punishment", modalValues.get(3).getAsString(), false);
        embed.addField("Branch", modalValues.get(4).getAsString(), false);
        embed.setColor(0xdb0404);
        return embed.build();
    }

    public static MessageEmbed createRanksAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Rank change" + username);
        embed.addField("Current rank", modalValues.get(0).getAsString(), false);
        embed.addField("New rank", modalValues.get(1).getAsString(), false);
        embed.addField("Giver", modalValues.get(2).getAsString(), false);
        embed.setColor(0x00bf00);
        return embed.build();
    }

    public static MessageEmbed createExcuseAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Excuse " + username);
        embed.addField("Excuse date", modalValues.get(0).getAsString(), false);
        embed.addField("Reason", modalValues.get(1).getAsString(), false);
        embed.setColor(0x00bf00);
        return embed.build();
    }
}
