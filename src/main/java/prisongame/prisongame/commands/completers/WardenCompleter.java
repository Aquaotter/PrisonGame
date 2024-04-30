package prisongame.prisongame.commands.completers;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class WardenCompleter extends SubcommandCompleter {
    public WardenCompleter() {
        super("warden", getConfig().getWarden().getHelp().keySet().toArray(new String[0]));
    }
}