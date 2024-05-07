package src;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PayTable {

    public final HashMap<ArrayList<ImageIcon>, Integer> payTable = new HashMap<>();
    private final ImageIcon[] symbols;

    // Initialize payTable with winning combinations and payouts
    public PayTable(ImageIcon[] symbols){
        this.symbols = symbols;
        payTable.put(createCombination(symbols[0], 5), 8000);
        ArrayList<ImageIcon> fiveSevens = new ArrayList<>();
        fiveSevens.add(symbols[6]);
        fiveSevens.add(symbols[6]);
        fiveSevens.add(symbols[6]);
        fiveSevens.add(symbols[6]);
        fiveSevens.add(symbols[6]);
        payTable.put(fiveSevens, 10000);

        ArrayList<ImageIcon> fiveCherries = new ArrayList<>();
        fiveCherries.add(symbols[0]);
        fiveCherries.add(symbols[0]);
        fiveCherries.add(symbols[0]);
        fiveCherries.add(symbols[0]);
        fiveCherries.add(symbols[0]);
        payTable.put(fiveCherries, 8000);

        ArrayList<ImageIcon> fourBarsOneSeven = new ArrayList<>();
        fourBarsOneSeven.add(symbols[5]);
        fourBarsOneSeven.add(symbols[5]);
        fourBarsOneSeven.add(symbols[5]);
        fourBarsOneSeven.add(symbols[5]);
        fourBarsOneSeven.add(symbols[6]);
        payTable.put(fourBarsOneSeven, 7500);

        ArrayList<ImageIcon> threeBellsTwoPlums = new ArrayList<>();
        threeBellsTwoPlums.add(symbols[4]);
        threeBellsTwoPlums.add(symbols[4]);
        threeBellsTwoPlums.add(symbols[4]);
        threeBellsTwoPlums.add(symbols[3]);
        threeBellsTwoPlums.add(symbols[3]);
        payTable.put(threeBellsTwoPlums, 5000);

        ArrayList<ImageIcon> threeSevens = new ArrayList<>();
        threeSevens.add(symbols[6]);
        threeSevens.add(symbols[6]);
        threeSevens.add(symbols[6]);
        payTable.put(threeSevens, 3000);

        ArrayList<ImageIcon> twoCherriesOneLemon = new ArrayList<>();
        twoCherriesOneLemon.add(symbols[0]);
        twoCherriesOneLemon.add(symbols[0]);
        twoCherriesOneLemon.add(symbols[1]);
        payTable.put(twoCherriesOneLemon, 100);

        ArrayList<ImageIcon> threeLemons = new ArrayList<>();
        threeLemons.add(symbols[1]);
        threeLemons.add(symbols[1]);
        threeLemons.add(symbols[1]);
        payTable.put(threeLemons, 200);

        ArrayList<ImageIcon> oneBarFourCherries = new ArrayList<>();
        oneBarFourCherries.add(symbols[5]);
        oneBarFourCherries.add(symbols[0]);
        oneBarFourCherries.add(symbols[0]);
        oneBarFourCherries.add(symbols[0]);
        oneBarFourCherries.add(symbols[0]);
        payTable.put(oneBarFourCherries, 300);
    }

    private ArrayList<ImageIcon> createCombination(ImageIcon symbol, int count) {
        ArrayList<ImageIcon> combination = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            combination.add(symbol);
        }
        return combination;
    }
}
