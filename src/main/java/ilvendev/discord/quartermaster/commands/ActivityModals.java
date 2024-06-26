package ilvendev.discord.quartermaster.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.List;

public class ActivityModals {

    public static Modal createArrestModal(){

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

        TextInput currentRank = TextInput.create("currentRank", "Obecny stopień", TextInputStyle.SHORT)
                .setPlaceholder("Twój obecny stopień")
                .build();

        TextInput newRank = TextInput.create("newRank", "Nowy stopień", TextInputStyle.SHORT)
                .setPlaceholder("Twój stopień po awansie")
                .build();

        TextInput giver = TextInput.create("giver", "Nadający", TextInputStyle.SHORT)
                .setPlaceholder("Kto nadał awans")
                .build();

        return Modal.create("ranksModal", "Ranks")
                .addComponents(
                        ActionRow.of(currentRank),
                        ActionRow.of(newRank),
                        ActionRow.of(giver))
                .build();
    }

    public static Modal createExcusesModal(){

        TextInput excuseDate = TextInput.create("excuseDate", "Termin zwolnienia", TextInputStyle.SHORT)
                .setPlaceholder("W jakim czasie będziesz na zwolnieniu np. 12.06-17.06")
                .build();

        TextInput reason = TextInput.create("reason", "Powód zwolnienia", TextInputStyle.SHORT)
                .setPlaceholder("Dlaczego jesteś na zwolneniu")
                .build();

        return Modal.create("excusesModal", "Excuses")
                .addComponents(
                        ActionRow.of(excuseDate),
                        ActionRow.of(reason))
                .build();
    }

    public static MessageEmbed createArrestAnswerEmbed(List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Przyjęto areszt");
        embed.setDescription(
                "Aresztowany: " + modalValues.get(0).getAsString() + "\n"
                + "Aresztujący: " + modalValues.get(1).getAsString() + "\n"
                + "Powód: " + modalValues.get(2).getAsString() + "\n"
                + "Wymiar kary: " + modalValues.get(3).getAsString() + "\n"
                + "Przynależność oddziałowa: " + modalValues.get(4).getAsString()
        );
        embed.setColor(0xdb0404);
        return embed.build();
    }

    public static MessageEmbed createRanksAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Zmiana stopnia " + username);
        embed.setDescription(
                "Obecny stopień: " + modalValues.get(0).getAsString() + "\n"
                + "Nowy stopień: " + modalValues.get(1).getAsString() + "\n"
                + "Nadający awans: " + modalValues.get(2).getAsString() + "\n"
        );
        embed.setColor(0x00bf00);
        return embed.build();
    }

    public static MessageEmbed createExcuseAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Zwolnienie " + username);
        embed.setDescription(
                "Termin zwolnienia: " + modalValues.get(0).getAsString() + "\n"
                + "Powód zwolnienia: " + modalValues.get(1).getAsString() + "\n"
        );
        embed.setColor(0x00bf00);
        return embed.build();
    }
}
