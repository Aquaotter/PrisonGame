package prisongame.prisongame.commands.completers;

public class BuilderCompleter extends SubcommandCompleter {
    public BuilderCompleter() {
        super("builder", new String[] {
                "toggle"
        });
    }
}
