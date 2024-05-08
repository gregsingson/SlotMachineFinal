package src;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class BonusSlotGame {
    private final ArrayList<ImageIcon> symbols;
    private final Random rand = new Random();
    private final PayTable payTable;
    private int payout;

    public BonusSlotGame(ArrayList<ImageIcon> symbols, PayTable payTable, int multiplier) {
        this.symbols = symbols;
        this.payTable = payTable;
    }

    public int spinReels() {
        ImageIcon reel1 = symbols.get(rand.nextInt(symbols.size()));
        ImageIcon reel2 = symbols.get(rand.nextInt(symbols.size()));
        ImageIcon reel3 = symbols.get(rand.nextInt(symbols.size()));
        ImageIcon reel4 = symbols.get(rand.nextInt(symbols.size()));
        ImageIcon reel5 = symbols.get(rand.nextInt(symbols.size()));


        // Create an ArrayList of the spun symbols
        ArrayList<ImageIcon> spunSymbols = new ArrayList<>();
        spunSymbols.add(reel1);
        spunSymbols.add(reel2);
        spunSymbols.add(reel3);
        spunSymbols.add(reel4);
        spunSymbols.add(reel5);

        // Determine the payout based on the images that appear on the reels
        for (ImageIcon winningCombo : payTable.payTable.keySet()) {
            if (spunSymbols.equals(winningCombo)) {
                int multiplier = 2;
                payout = payTable.payTable.get(winningCombo) * multiplier;
                break;
            }
        }
        return payout;
    }
}