package ilvendev.discord.quartermaster.functionalities;

import ilvendev.discord.quartermaster.googlesheets.SearchHandler;
import ilvendev.discord.quartermaster.googlesheets.WritingHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.ArrayList;
import java.util.List;

public class FunctionalityModals {

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
        embed.addField("Aresztowany", modalValues.get(0).getAsString(), false);
        embed.addField("Aresztujący", modalValues.get(1).getAsString(), false);
        embed.addField("Powód", modalValues.get(2).getAsString(), false);
        embed.addField("Wymiar kary", modalValues.get(3).getAsString(), false);
        embed.addField("Przynależność", modalValues.get(4).getAsString(), false);
        embed.setColor(0xdb0404);
        return embed.build();
    }

    public static boolean writeArrestToRoster(List<ModalMapping> modalValues, int currentBlankRowNumber){
        String range = "Areszty!A"+currentBlankRowNumber+":E"+currentBlankRowNumber;
        List<Object> newValues = new ArrayList<>();
        for (ModalMapping modalValue : modalValues) {
            newValues.add(modalValue.getAsString());
        }
        try {
            WritingHandler.updateSheet(newValues, range);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static MessageEmbed createRanksAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Zmiana stopnia " + username);
        embed.addField("Obecny stopień", modalValues.get(0).getAsString(), false);
        embed.addField("Nowy stopień", modalValues.get(1).getAsString(), false);
        embed.addField("Nadający awans", modalValues.get(2).getAsString(), false);
        embed.setColor(0x00bf00);
        return embed.build();
    }

    public static boolean writeRankToRoster(String username, List<ModalMapping> modalValues){
        String nickColumn = SearchHandler.findColumnByName("Nick DC", "A1:L1");
        String rankColumn = SearchHandler.findColumnByName("Stopień", "A1:L1");
        String row = SearchHandler.findRowNumberByName(username,nickColumn);
        String cell = rankColumn+row+":"+rankColumn+row;
        try {
            WritingHandler.updateSheet(List.of(modalValues.get(1).getAsString()), cell);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static MessageEmbed createExcuseAnswerEmbed(String username, List<ModalMapping> modalValues){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Zwolnienie " + username);
        embed.addField("Termin zwolnienia", modalValues.get(0).getAsString(), false);
        embed.addField("Powód zwolnienia", modalValues.get(1).getAsString(), false);
        embed.setColor(0x00bf00);
        return embed.build();
    }

    public static boolean writeExcuseToRoster(String username, List<ModalMapping> modalValues){
        String nickColumn = SearchHandler.findColumnByName("Nick DC", "A1:L1");
        String excuseColumn = SearchHandler.findColumnByName("Zwolnienia", "A1:L1");
        String row = SearchHandler.findRowNumberByName(username,nickColumn);
        String cell = excuseColumn+row+":"+excuseColumn+row;
        try {
            WritingHandler.updateSheet(List.of(modalValues.get(0).getAsString()), cell);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
