package src;

import javax.swing.*;
import java.util.HashMap;

public class PayTable {

    public final HashMap<ImageIcon, Integer> payTable = new HashMap<>();

    // Initialize payTable with winning combinations and payouts
    public PayTable(ImageIcon[] symbols){
        payTable.put(symbols[0], 1000); // Cherry
        payTable.put(symbols[1], 600); // Lemon
        payTable.put(symbols[2], 500); // Orange
        payTable.put(symbols[3], 400); // Plum
        payTable.put(symbols[4], 300); // Bell
        payTable.put(symbols[5], 100); // Bar
        payTable.put(symbols[6], 2500); // Seven
    }
}
