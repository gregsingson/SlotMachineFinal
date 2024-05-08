package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

// BonusRound.java
public class BonusRound {
    private boolean bonusRoundActive;
    private JFrame bonusFrame;
    private JLabel bonusPot;
    private final SlotMachine slotMachine;
    public BonusRound(SlotMachine slotMachine) {
        this.slotMachine = slotMachine;
    }
    public void setBonusPotValue(int bonusPotValue) {
        this.slotMachine.bonusPotValue = bonusPotValue;
    }

    // Constructor and other methods like startBonusRound, spinBonusReels, updateBonusPot
    // Start the bonus round with a separate frame
    public void startBonusRound() {
        bonusRoundActive = true;

        // Create bonus round frame
        bonusFrame = new JFrame("Bonus Round");
        bonusFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bonusFrame.setSize(400, 300);
        bonusFrame.setLocationRelativeTo(null);

        // Create bonus pot label
        bonusPot = new JLabel("Bonus Pot: $" + slotMachine.bonusPotValue);
        bonusPot.setHorizontalAlignment(JLabel.CENTER);
        bonusPot.setFont(new Font("Arial", Font.BOLD, 24));

        // Add bonus pot label to frame
        bonusFrame.add(bonusPot, BorderLayout.CENTER);

        // Add button to spin bonus reels
        JButton bonusSpinButton = new JButton("Spin Bonus Reels");
        bonusSpinButton.addActionListener(e -> spinBonusReels());
        bonusFrame.add(bonusSpinButton, BorderLayout.SOUTH);

        // update the bonus pot label and balance
        bonusPot.setText("Bonus Pot: $" + slotMachine.bonusPotValue);
        slotMachine.updateBalanceLabel();

        bonusFrame.setVisible(true);
    }

    // Spin the bonus reels and determine the bonus payout
    public void spinBonusReels() {
        if (!bonusRoundActive || slotMachine.bonusPotValue <= 0) {
            return;
        }

        // Generate random symbols for the bonus round
        Random rand = new Random();
        ArrayList<ImageIcon> spunSymbols = new ArrayList<>();
        for (int i = 0; i < slotMachine.NUM_REELS; i++) {
            ImageIcon symbol = slotMachine.symbols[rand.nextInt(slotMachine.symbols.length)];
            spunSymbols.add(symbol);
        }

        // Update the reels in the main game window
        for (int i = 0; i < slotMachine.NUM_REELS; i++) {
            slotMachine.updateReel(i, spunSymbols.get(i));
        }

        // Create a new BonusSlotGame instance and spin the reels
        BonusSlotGame bonusSlotGame = new BonusSlotGame(spunSymbols, slotMachine.slotPayTable, 2);
        int bonusPayout = bonusSlotGame.spinReels();

        // Check for scatter symbol
        long scatterCount = spunSymbols.stream().filter(symbol -> symbol.getDescription().equals(slotMachine.symbols[7].getDescription())).count();
        if (scatterCount > 0) {
            double scatterPayout = 250 * scatterCount * 2; // Multiply by the bonus round modifier
            bonusPayout += scatterPayout;
        }

        // Update the balance and bonus pot
        double balance = slotMachine.getBalance();
        balance += bonusPayout;
        slotMachine.setBalance(balance);
        slotMachine.updateBalanceLabel();
        bonusPot.setText("You won $" + bonusPayout + " from the bonus pot!");

        // Display a dialog box with the winnings and a prompt to close
        JOptionPane.showMessageDialog(null, "You won $" + bonusPayout + " from the bonus pot!", "Winnings", JOptionPane.INFORMATION_MESSAGE);

        // Update the bonus pot value based on the bonus payout
        updateBonusPot();
        bonusFrame.repaint();
        bonusFrame.revalidate();

        // Add a close button to end the bonus round

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> bonusFrame.dispose());
        bonusFrame.add(closeButton, BorderLayout.SOUTH);


        // Close bonus round window and end bonus round
        bonusFrame.dispose();
        endBonusRound();
    }
    public void endBonusRound() {
        // Reset bonus pot value
        slotMachine.bonusPotValue = 0;
        // Update bonus pot meter and label
        slotMachine.bonusPotMeter.setValue(slotMachine.bonusPotValue);
        slotMachine.bonusPotLabel.setText("Bonus Pot: $" + slotMachine.bonusPotValue + "/500!");
        // Reset bonus round active flag
        slotMachine.bonusRoundActive = false;
    }
    // Update the bonus pot label
    public void updateBonusPot() {
        if (bonusRoundActive) {
            bonusPot.setText("Bonus Pot: $" + slotMachine.bonusPotValue);
        }
    }
}
