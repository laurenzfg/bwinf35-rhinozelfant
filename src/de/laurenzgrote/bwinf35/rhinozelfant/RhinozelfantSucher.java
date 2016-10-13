package de.laurenzgrote.bwinf35.rhinozelfant;

import java.util.HashSet;
import java.util.Stack;

class RhinozelfantSucher {

    // Konstanten
    private final int MINIMALBREITE, MINIMALHOEHE, MINIMALBEIN;

    private boolean[][] swBild;
    private HashSet<int[]> rhinozelfantenFelder = new HashSet<>();

    public RhinozelfantSucher(boolean[][] swBild) {
        // Defaults für die Konstanten
        this(swBild, 24, 8, 4);
    }

    public RhinozelfantSucher(boolean[][] swBild, int minimalbreite, int minimalhoehe, int minimalbein) {
        this.swBild = swBild;

        MINIMALBREITE = minimalbreite;
        MINIMALHOEHE = minimalhoehe;
        MINIMALBEIN = minimalbein;

        unterbechungsfreieStricheFilter();
    }

    // Filter 1: Wieviele Striche gibt es, die einen Körper formen (könnten)
    private void unterbechungsfreieStricheFilter() {
        for (int y = 0; y < swBild[0].length; y++) {
            int aktuellReiheLaenge = 0;
            for (int x = 0; x < swBild.length; x++) {
                if (swBild[x][y]) {
                    aktuellReiheLaenge++;
                } else {
                    // Unterbrechungsfreie Linie abgebrochen;
                    // Wenn über Treshhold dann weiterfiltern
                    if (aktuellReiheLaenge > MINIMALBREITE) rechteckFilter(x - aktuellReiheLaenge, x, y);
                    aktuellReiheLaenge = 0;
                }
            }
        }
    }

    // Filter 2: Gibt es ein einigermaßen großes Rechteck,
    // also wirklich einen Körper?
    private void rechteckFilter(int startX, int endeX, int startY) {
        // Kriterium 2: n Felder lässt sich der Strich nach unten bewegen
        int endeY = rechteckLimit(startX, endeX, startY);

        // Wen über Treshhold nächster Filter
        if (endeY - startY > MINIMALHOEHE) {
            anatomieFilter(startX, endeX, startY);
        }
    }

    // Hiflsfunktion zu 2, ermittelt wie weit der Strich nach unten runtergeht
    private int rechteckLimit (int startX, int endeX, int startY) {
        if (startY == swBild[0].length - 1) {
            return 0; // Das letzte Y kann nicht start eines Rechteckes sein...
        } else {
            int endeY = startY;
            while (endeY < swBild[0].length) {
                endeY++;
                for (int i = startX; i < endeX; i++) {
                    if (!swBild[i][endeY]) return --endeY;
                }
            }
            return endeY;
        }
    }

    // Filter 3: Ist die anatomie des Rhinozelfanten vorhanden (Beine)
    private void anatomieFilter(int startX, int endeX, int y) {
        // Was ist der längste von dem unteren Strichende wegführende Strich
        // im 1./3. Drittel --> Bein?
        int rechtesBeinLaenge = laengstesBein(startX, (int) Math.floor(startX * 1.3), y);
        int linkesBeinLaenge = laengstesBein((int) Math.floor((endeX - startX) * 0.6) + startX, endeX, y);

        // Wenn beide Striche über Minimum
        if (rechtesBeinLaenge > MINIMALBEIN && linkesBeinLaenge > MINIMALBEIN) {

            // Filter 4: Liegt zwischen den Hufen ein Freiraum.

            // Kürzeres Bein ermitten
            int j;
            if (rechtesBeinLaenge < linkesBeinLaenge) {
                j = rechtesBeinLaenge + y;
            } else {
                j = linkesBeinLaenge + y;
            }

            // Umgebungsfelder zwischen Beinen zählen
            int lueckengroesse = 0;
            for (int i = startX; i < endeX; i++) {
                if (!swBild[i][j]) lueckengroesse++;
                if (lueckengroesse > 0) break;
            }

            if (lueckengroesse != 0) {
                // A small step for a BwInf'ler, one giant leap for mankind: We found a rhinozelfant!

                // Jetzt muss die Gesamtstruktur des Rhinozelfanten ermittelt werden, damit diese weiß
                // gefärbt werden kann!
                expandElefant(endeX, y);
            }
        }
    }

    // Hilfsfunktion zur Suche nach Beinen
    private int laengstesBein(int startX, int endeX, int y) {
        int rekordtiefe = -1;

        // Suchradius kann nicht über der Gesamtbreite liegen
        if (endeX > swBild.length) endeX = swBild.length;
        // startX < 0 ist unmöglich, da nirgends mit negativen Zahlen gearbeitet wride

        // Jetzt gehen wir von Spalte zu Spalte
        for (int i = startX; i < endeX; i++) {

            // Und gucken um wie viele Felder wie uns im markierten Bereich
            // "herunterhangeln" können!
            boolean imBein = true;
            int j = y;
            while (imBein && j < swBild[0].length) {
                j++;
                if (!swBild[i][j]){
                    imBein = false;
                    // Wenn wir einen Rekord aufgestellt haben wird der gespeichert
                    if (j > rekordtiefe) rekordtiefe = j - y;
                }
            }
        }
        // Der Rekord wird ausgegeben, gibt es keine Beine wird -1 ausgegeben
        return rekordtiefe;
    }

    // Funktion um von P(x|y) aus alle erreichbaren Punkte findet
    // (nötig um den zu markierenden Bereich zu bestimmen)
    private HashSet<int[]> expandElefant(int x, int y) {
        // Statt mit Rekursion mit Stack
        // (siehe Doku)
        Stack<int[]> unbearteiteFelder = new Stack<>();

        int[] ursprungsfeld = new int[] {x, y};

        // Das Ursprungsfeld wird:
        //  (a) als zu markierende Fläche markiert
        //  (b) zu den Ausgangsfeldern hinzugefügt
        //  (c) als nicht gelichfarbig markiert. (Grund --> Doku)
        rhinozelfantenFelder.add(ursprungsfeld);
        unbearteiteFelder.push(ursprungsfeld);
        swBild[x][y] = false;

        // SOLANGE Ausgangsfelder vorhanden sind
        while (!unbearteiteFelder.empty()) {
            // NEHMEN wird diese vom Stapel
            ursprungsfeld = unbearteiteFelder.pop();
            x = ursprungsfeld[0];
            y = ursprungsfeld[1];

            // und fügen alle angrenzenden gleichfarbigen Felder den
            // zu markierenden und den zu bearbeitenden hinzu

            // Rechts?
            if (((x + 1) < swBild.length) && swBild[x + 1][y]) {
                rhinozelfantenFelder.add(new int[] {x + 1, y});
                unbearteiteFelder.add(new int[] {x + 1, y});
                swBild[x + 1][y] = false;
            }

            // Links?
            if (((x - 1) > 0) && swBild[x - 1][y]) {
                rhinozelfantenFelder.add(new int[] {x - 1, y});
                unbearteiteFelder.add(new int[] {x - 1, y});
                swBild[x - 1][y] = false;
            }

            // Was Unten?
            if (((y + 1) < swBild[0].length) && swBild[x][y + 1]) {
                rhinozelfantenFelder.add(new int[] {x, y + 1});
                unbearteiteFelder.add(new int[] {x, y + 1});
                swBild[x][y + 1] = false;
            }

            // Oben
            if (((y - 1) > 0) && swBild[x][y - 1] ) {
                rhinozelfantenFelder.add(new int[] {x, y - 1});
                unbearteiteFelder.add(new int[] {x, y - 1});
                swBild[x][y - 1] = false;
            }
        }

        // Zuguterletzt werden die zu markierenden Felder ausgegeben
        return rhinozelfantenFelder;
    }

    HashSet<int[]> getRhinozelfantenFelder() {
        return rhinozelfantenFelder;
    }
}