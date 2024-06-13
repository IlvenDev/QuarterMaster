package ilvendev.discord.quartermaster.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Schedule {

    private LinkedHashMap<String,String> daysInformations = new LinkedHashMap<String, String>(){{
        put("Poniedziałek",
                "* Some desc\n" +
                "* Some desc\n" +
                "* Some desc");
        put("Wtorek",
                "* Some desc\n" +
                "* Some desc\n" +
                "* Some desc");
        put("Środa",
                "* Some desc\n" +
                "* Some desc\n" +
                "* Some desc");
        put("Czwartek",
                "* Some desc\n" +
                        "* Some desc\n" +
                        "* Some desc");
        put("Piątek",
                "* Some desc\n" +
                        "* Some desc\n" +
                        "* Some desc");
        put("Sobota",
                "* Some desc\n" +
                        "* Some desc\n" +
                        "* Some desc");
        put("Niedziela",
                "* Some desc\n" +
                        "* Some desc\n" +
                        "* Some desc");
    }};

    public LinkedHashMap<String, String> getDaysInformations() {
        return daysInformations;
    }

    public void setDaysInformations(LinkedHashMap<String, String> descriptions) {
        this.daysInformations = descriptions;
    }

    public MessageEmbed createScheduleEmbed(){
        EmbedBuilder embed = new EmbedBuilder();
        HashMap<String, String> daysInformations = getDaysInformations();
        daysInformations.forEach((day, description) -> {embed.addField(day, description, false);});
        embed.setColor(0xdb0404);
        return embed.build();
    }

}
