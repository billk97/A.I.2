
public class Calculate {
    private double SpamCounter;
    private double MailCounter;

    public double getSpamCounter() {
        return SpamCounter;
    }

    public void setSpamCounter(double spamCounter) {
        SpamCounter = spamCounter;

    }

    public double getMailCounter() {
        return MailCounter;
    }

    public void setMailCounter(double mailCounter) {
        MailCounter = mailCounter;

    }

    public double log2(double n) {
        return (Math.log(n) / Math.log(2));
    }

    private double getSpamProp() {
        return SpamCounter / MailCounter;
    }

    private double getHamProp() {
        return 1 - getSpamProp();
    }

    public double TotalEntropy() {
        return (getSpamProp() * log2(1+getSpamProp()) + getHamProp() * log2(1+getHamProp()));
    }
}
