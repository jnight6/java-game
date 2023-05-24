public class Command {

    private String verb;
    private String noun;

    public Command(String verb, String noun) {
        this.verb = verb;
        this.noun = noun;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }
}
