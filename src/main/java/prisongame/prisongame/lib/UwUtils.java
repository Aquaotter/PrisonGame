package prisongame.prisongame.lib;

import java.util.Random;

public class UwUtils {

    public static String uwuify(String text) {
        return text
                .replaceAll("\\.", "~ ")
                .replaceAll(",", "~ ")
                .replaceAll("-", "~ ")
                .replaceAll("\\?", "~ ")
                .replaceAll("hurt", "hUWUrt")
                .replaceAll("kill", "hwuwrt")
                .replaceAll("you", "you<3")
                .replaceAll("r", "w")
                .replaceAll("l", "w")
                .replaceAll("uwu", "UWU")
                .replaceAll("owo", "OWO")
                .replaceAll(";-;", "(-_-)")
                .replaceAll("-_-", "(-_-)")
                .replaceAll(":o", "※(^o^)/※")
                .replaceAll(":0", "※(^o^)/※")
                .replaceAll(":\\)", "(｡◕‿‿◕｡)")
                .replaceAll(":>", "(｡◕‿‿◕｡)")
                .replaceAll(":\\(", "(︶︹︶)")
                .replaceAll(":<", "(︶︹︶)")
                .replaceAll(":3", "(・3・)")
                .replaceAll(":D", "(ﾉ◕ヮ◕)ﾉ*:・ﾟ✧")
                .replaceAll("\\._\\.", "(why can't i type this)")
                .replaceAll("fuck", "fwick")
                .replaceAll("shit", "*poops*")
                .replaceAll("wtf", "whawt the fwick")
                .replaceAll("wth", "whawt the hecc") + getRandomUwUSuffix();
    }

    static String getRandomUwUSuffix() {
        String[] uwuSuffixes = new String[]{
                    "~ uwu *nuzzles you*",
                    "~ owo whats this",
                    "~ *kisses you*",
                    "~ *blushes*",
                    "~ *hehe*",
                    "~ meow",
                    "~ owo",
                    "~ uwu",
                    " ;3",
                    "~ *boops your nose*",
                    "~ *snuggles with you*",
                    "~ *giggles*",
                    "~ *hugs you*",
        };

        return uwuSuffixes[new Random().nextInt(0,uwuSuffixes.length-1)];
    }
}
