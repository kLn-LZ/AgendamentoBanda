package br.edu.fateczl.agendamentobanda.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaCor {
    private final String blackHex = "000000";
    private final String whiteHex = "FFFFFF";

    public Cor getDefaultColor() {
        return basicColors().get(0);
    }

    public int colorPosition(Cor cor) {
        List<Cor> colors = basicColors();
        for (int i = 0; i < colors.size(); i++) {
            if (colors.get(i).equals(cor)) {
                return i;
            }
        }
        return 0;
    }

    public List<Cor> basicColors() {
        return new ArrayList<>(Arrays.asList(
                new Cor("Silver", "C0C0C0", blackHex),
                new Cor("Gray", "808080", whiteHex),
                new Cor("Maroon", "800000", whiteHex),
                new Cor("Red", "FF0000", whiteHex),
                new Cor("Fuchsia", "FF00FF", whiteHex),
                new Cor("Green", "008000", whiteHex),
                new Cor("Lime", "00FF00", blackHex),
                new Cor("Olive", "808000", whiteHex),
                new Cor("Yellow", "FFFF00", blackHex),
                new Cor("Navy", "000080", whiteHex),
                new Cor("Blue", "0000FF", whiteHex),
                new Cor("Teal", "008080", whiteHex),
                new Cor("Aqua", "00FFFF", blackHex)
        ));
    }

    @NonNull
    @Override
    public String toString() {
        return "BlackHex: " + blackHex + " - " + "WhiteHex: " + whiteHex;
    }
}
