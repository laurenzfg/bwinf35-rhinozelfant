package de.laurenzgrote.bwinf35.rhinozoelefant;

import java.util.HashSet;
import java.util.Stack;

public class RhinozoelefantSucher {

    // Konstanten
    final int MINIMALBREITE, MINIMALHOEHE, MINIMALBEIN;

    private boolean[][] swBild;
    HashSet<int[]> rhinozoelefantenFelder = new HashSet<>();

    public RhinozoelefantSucher(boolean[][] swBild) {
        this(swBild, 30, 10, 5);
    }

    public RhinozoelefantSucher(boolean[][] swBild, int minimalbreite, int minimalhoehe, int minimalbein) {
        this.swBild = swBild;

        MINIMALBREITE = minimalbreite;
        MINIMALHOEHE = minimalhoehe;
        MINIMALBEIN = minimalbein;

        unterbechungsfreieStricheFilter();
    }

    private void unterbechungsfreieStricheFilter() {
        for (int y = 0; y < swBild[0].length; y++) {
            int aktuellReiheLaenge = 0;
            for (int x = 0; x < swBild.length; x++) {
                if (swBild[x][y]) {
                    aktuellReiheLaenge++;
                } else {
                    if (aktuellReiheLaenge > MINIMALBREITE) rechteckFilter(x - aktuellReiheLaenge, x, y);
                    aktuellReiheLaenge = 0;
                }
            }
        }
    }



    private void rechteckFilter(int startX, int endeX, int startY) {
        // Kriterium 2: Ist es ein ausreichend grosses Rechteck?
        int endeY = rechteckLimit(startX, endeX, startY);

        if (endeY - startY > MINIMALHOEHE) {
            anatomieFilter(startX, endeX, startY);
        }
    }

    private int rechteckLimit (int startX, int endeX, int startY) {
        int endeY = startY;
        while (endeY < swBild[0].length) {
            endeY++;
            for (int i = startX; i < endeX; i++) {
                if (!swBild[i][endeY]) return --endeY;
            }
        }
        return endeY;
    }

    private int laengstesBein(int startX, int endeX, int y) {
        int rekordtiefe = -1;
        for (int i = startX; i < endeX; i++) {
            boolean imBein = true;
            int j = y;
            while (imBein) { //NPE!
                j++;
                if (!swBild[i][j]){
                    imBein = false;
                    if (j > rekordtiefe) rekordtiefe = j - y;
                }
            }
        }
        return rekordtiefe;
    }

    private void anatomieFilter(int startX, int endeX, int y) {
        // Kriterium 3: Passen Beine dran?
        // Versuchen wir mal irgendwo im 1. Drittel 5 Px Bein  dranzuklatschen
        int rechtesBeinLaenge = laengstesBein(startX, (int) Math.floor(startX * 1.3), y);
        int linkesBeinLaenge = laengstesBein((int) Math.floor((endeX - startX) * 0.6) + startX, endeX, y);
        if (rechtesBeinLaenge > MINIMALHOEHE && linkesBeinLaenge > MINIMALHOEHE) {
            // 4. Befindet isch zwischen den beiden Beinen eine große Lücke (min 50%)
            int j;
            if (rechtesBeinLaenge < linkesBeinLaenge) {
                j = rechtesBeinLaenge + y;
            } else {
                j = linkesBeinLaenge + y;
            }
            int lueckengroesse = 0;
            for (int i = startX; i < endeX; i++) {
                if (!swBild[i][j]) lueckengroesse++;
            }
            if (lueckengroesse > 1) {
                // A small step for zoologist, one giant leap for mankind: We found a rhinozoelephant
                expandElefant(endeX, y);
            }
        }
    }
    public HashSet<int[]> expandElefant(int x, int y) {
        Stack<int[]> unbearteiteFelder = new Stack<>();

        int[] ursprungsfeld = new int[] {x, y};

        rhinozoelefantenFelder.add(ursprungsfeld);
        unbearteiteFelder.push(ursprungsfeld);
        swBild[x][y] = false;

        while (!unbearteiteFelder.empty()) {
            ursprungsfeld = unbearteiteFelder.pop();
            x = ursprungsfeld[0];
            y = ursprungsfeld[1];

            // Rechts?
            if (((x + 1) < swBild.length) && swBild[x + 1][y]) {
                rhinozoelefantenFelder.add(new int[] {x + 1, y});
                unbearteiteFelder.add(new int[] {x + 1, y});
                swBild[x + 1][y] = false;
            }

            // Links?
            if (((x - 1) > 0) && swBild[x - 1][y]) {
                rhinozoelefantenFelder.add(new int[] {x - 1, y});
                unbearteiteFelder.add(new int[] {x - 1, y});
                swBild[x - 1][y] = false;
            }

            // Was Unten?
            if (((y + 1) < swBild[0].length) && swBild[x][y + 1]) {
                rhinozoelefantenFelder.add(new int[] {x, y + 1});
                unbearteiteFelder.add(new int[] {x, y + 1});
                swBild[x][y + 1] = false;
            }

            // Oben
            if (((y - 1) > 0) && swBild[x][y - 1] ) {
                rhinozoelefantenFelder.add(new int[] {x, y - 1});
                unbearteiteFelder.add(new int[] {x, y - 1});
                swBild[x][y - 1] = false;
            }
        }

        return rhinozoelefantenFelder;
    }

    public HashSet<int[]> getRhinozoelefantenFelder() {
        return rhinozoelefantenFelder;
    }
}
