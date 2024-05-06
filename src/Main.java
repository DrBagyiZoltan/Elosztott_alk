import java.io.IOException;
import java.util.*;

public class Main {
    //enter hasznalata a promptnal
    static class EnterPress {

        static void enterPress() {
            try {
                System.out.println("Nyomjon entert a folytatashoz!");
                System.in.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {

        Csiga csiga1 = new Csiga("csigaKek", Csiga.CsigaPossibleColors.KEK);
        Csiga csiga2 = new Csiga("csigaZold", Csiga.CsigaPossibleColors.ZOLD);
        Csiga csiga3 = new Csiga("csigaPiros", Csiga.CsigaPossibleColors.PIROS);

        List<Csiga> versenyzok = new ArrayList<>();
        versenyzok.add(csiga1);
        versenyzok.add(csiga2);
        versenyzok.add(csiga3);

        Fogadas fogadas = new Fogadas();
        fogadas.fogadasInditas();

        Round round1 = new Round(1, versenyzok);
        Round round2 = new Round(2, versenyzok);
        Round round3 = new Round(3, versenyzok);
        Round round4 = new Round(4, versenyzok);
        Round round5 = new Round(5, versenyzok);

        Game game = new Game(Arrays.asList(round1, round2, round3, round4, round5));

        round1.csigaSpeedSzamitas();
        EnterPress.enterPress();
        round1.getSebessegek();
        round1.eredmenyHirdetes();
        game.roundEredmenyekSzamitasa(round1);

        round2.csigaSpeedSzamitas();
        EnterPress.enterPress();
        round2.getSebessegek();
        round2.eredmenyHirdetes();
        game.roundEredmenyekSzamitasa(round2);

        round3.csigaSpeedSzamitas();
        EnterPress.enterPress();
        round3.getSebessegek();
        round3.eredmenyHirdetes();
        game.roundEredmenyekSzamitasa(round3);

        round4.csigaSpeedSzamitas();
        EnterPress.enterPress();
        round4.getSebessegek();
        round4.eredmenyHirdetes();
        game.roundEredmenyekSzamitasa(round4);

        round5.csigaSpeedSzamitas();
        EnterPress.enterPress();
        round5.getSebessegek();
        round5.eredmenyHirdetes();
        game.roundEredmenyekSzamitasa(round5);

        game.vegeredmeny();
        game.fogadasEredmenye(fogadas);
    }
}

class Fogadas {

    private String csigaNameAmelyikreFogadtak;

    String getcsigaNameAmelyikreFogadtak() {
        return csigaNameAmelyikreFogadtak;
    }

    void fogadasInditas() {
        final Scanner input = new Scanner(System.in);

        System.out.println("Fogadas indul, kerem adjon meg egy szamot melyik csigara fogad:\n"
                + "1 - csiga1 azaz a kek csiga\n"
                + "2 - csiga2 azaz a zold csiga\n"
                + "3 - csiga3 azaz a piros csiga");

        while (!input.hasNextInt()) {
            System.out.println("Nem ervenyes csiga szam! Kerem adjon meg egy szamot 1 es 3 kozott!\n");
            fogadasInditas();
            input.next();
        }
        final int csigaSzama = input.nextInt();

        setCsigaName(csigaSzama);
        System.out.println("A " + csigaNameAmelyikreFogadtak
                + "-ra a fogadas megtortent!\nA verseny elkezdesehez nyomjon entert! Sok sikert!");
    }

    private void setCsigaName(final int csigaSzama) {
        switch (csigaSzama) {
            case 1:
                this.csigaNameAmelyikreFogadtak = "csigaKek";
                break;
            case 2:
                this.csigaNameAmelyikreFogadtak = "csigaZold";
                break;
            case 3:
                this.csigaNameAmelyikreFogadtak = "csigaPiros";
                break;
            default:
                System.out.println("Nem ervenyes csiga szam! Kerem adjon meg egy szamot 1 es 3 kozott!\n");
                fogadasInditas();
        }
    }
}

class Round {

    private final Random random = new Random();
    private final int numberOfRound;
    private boolean isCsigagyorsitoActive;
    private boolean holtverseny;
    private List<Csiga> roundNyertes = new ArrayList<>();
    private final List<Csiga> versenyzok;

    Round(final int numberOfRound, final List<Csiga> versenyzok) {
        this.numberOfRound = numberOfRound;
        this.versenyzok = versenyzok;
    }

    List<Csiga> getVersenyzok() {
        return versenyzok;
    }

    List<Csiga> getRoundNyertes() {
        return roundNyertes;
    }

    private void activateCsigagyorsito() {
        this.isCsigagyorsitoActive = true;
    }

    void csigaSpeedSzamitas() {

        versenyzok.forEach(Csiga::setStepLength);
        csigaGyorsitoSzamitas();

        if (isCsigagyorsitoActive) {
            final int randomCsigaNr = random.nextInt(3);
            versenyzok.get(randomCsigaNr).triggerGyorsito();
            System.out.println("Csigagyorsitot kapott "
                    + getNumberOfRound()
                    + ". sz. menetben az alabbi csiga:\n----> "
                    + versenyzok.get(randomCsigaNr).getCsigaName());
        }
    }

    int getNumberOfRound() {
        return numberOfRound;
    }

    void csigaGyorsitoSzamitas() {
        System.out.println("\nA " + numberOfRound + ". sz. menet elkezdodik!\n");
        if (random.nextDouble() <= 0.2) { //
            activateCsigagyorsito();
            System.out.println("Csigagyorsito lesz ebben a menetben?\n");
        } else System.out.println("Csigagyorsito nem lesz ebben a menetben!\n");
    }

    void getSebessegek() {
        System.out.println(getNumberOfRound() + ". sz. menet elindult.\nA csigak sebessege:\n");

        for (int i = 0; i < versenyzok.size(); i++) {
            System.out.println(
                    "A " + versenyzok.get(i).getCsigaName() + " nevu csiga sebessege:\n"
                            + versenyzok.get(i).getStepLength());
        }
    }

    //holtverseny eseten tobb gyoztes van
    private void holtversenyEllenorzes(int legnagyobbCsigaSebesseg) {
        if ((int) versenyzok.stream().filter(i -> i.getStepLength() == legnagyobbCsigaSebesseg).count() > 1) {
            holtverseny = true;
        }
    }

    void eredmenyHirdetes() {

        versenyzok.sort(Comparator.comparingInt(Csiga::getStepLength));
        final int legnagyobbCsigaSebesseg = versenyzok.getLast().getStepLength();
        holtversenyEllenorzes(legnagyobbCsigaSebesseg);
        System.out.println("----------------------------------------\n" + numberOfRound + ". sz. menet gyoztese(i):\n");

        if (holtverseny) {
            System.out.println("Holt versenyben:\n");
            roundNyertes = versenyzok.stream()
                    .filter(csiga -> csiga.getStepLength() == legnagyobbCsigaSebesseg).toList();
        } else {
            roundNyertes.add(versenyzok.getLast());
        }
        roundNyertes.forEach(csiga -> System.out.println(csiga.getCsigaName() + "\n"));
    }
}

class Game {

    private List<Csiga> vegsoNyertesek;
    private final List<Round> rounds;
    private final Map<Csiga, Integer> eredmenyek = new HashMap<>();

    void createEredmenyTabla() {
        for (int i = 0; i < rounds.getFirst().getVersenyzok().size(); i++) {
            eredmenyek.put(rounds.getFirst().getVersenyzok().get(i), 0);
        }
    }

    public Game(final List<Round> rounds) {
        this.rounds = rounds;
        createEredmenyTabla();
    }

    void roundEredmenyekSzamitasa(final Round round) {
        for (int i = 0; i < round.getRoundNyertes().size(); i++) {
            eredmenyek.compute(round.getRoundNyertes().get(i), (_, v) -> v + 1);
        }
        System.out.println(round.getNumberOfRound() + ". szamu menet utan az eredmeny a kovetkezo:\n");

        for (final Map.Entry<Csiga, Integer> eredmeny : eredmenyek.entrySet()) {
            final Csiga key = eredmeny.getKey();
            final Integer pontszam = eredmeny.getValue();
            System.out.println(key.getCsigaName() + " eredmenye: " + pontszam);
        }
    }

    void vegeredmeny() {
        final Integer nyertesCsigaEredmenye = Collections.max(eredmenyek.entrySet(), Map.Entry.comparingByValue()).getValue();
        vegsoNyertesek = eredmenyek.entrySet().stream()
                .filter(csiga -> csiga.getValue().equals(nyertesCsigaEredmenye))
                .map(Map.Entry::getKey)
                .toList();

        System.out.println("\n5 menet utan a nyertes(ek) " + nyertesCsigaEredmenye + " pontszammal:\n");
        vegsoNyertesek.forEach(nyertes -> System.out.println(nyertes.getCsigaName()));
    }

    void fogadasEredmenye(final Fogadas fogadas) {
        for (final Csiga csiga : vegsoNyertesek) {
            if (Objects.equals(csiga.getCsigaName(), fogadas.getcsigaNameAmelyikreFogadtak())) {
                System.out.println("\nA fogadast megnyerted, gratulalok!");
                return;
            } else {
                System.out.println("\nA fogadast elveszitetted!");
            }
        }
    }
}

class Csiga {

    private int stepLength;
    private final CsigaPossibleColors csigaColour;
    private final String csigaName;

    enum CsigaPossibleColors {
        KEK,
        PIROS,
        ZOLD;

    }

    String getCsigaName() {
        return csigaName;
    }

    int getStepLength() {
        return stepLength;
    }

    Csiga(final String csigaName, final CsigaPossibleColors csigaColour) {
        this.csigaName = csigaName;
        this.csigaColour = csigaColour;

    }

    void triggerGyorsito() {
        this.stepLength = stepLength * 2;
    }

    void setStepLength() {
        this.stepLength = new Random().nextInt(4);
    }
}