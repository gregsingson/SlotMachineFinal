package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

// BonusRound.java
public class BonusRound {
    private static final int INITIAL_BONUS_POT_VALUE = 0;
    private boolean bonusRoundActive;
    private JFrame bonusFrame;
    private JLabel bonusPot;
    private JButton bonusSpinButton;
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
        if (bonusFrame == null) {
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
    }

    // Spin the bonus reels and determine the bonus payout
    public void spinBonusReels() {
        if (!bonusRoundActive || slotMachine.bonusPotValue <= 0) {
            return;
        }
            // Spin the reels in the main game window
            slotMachine.spinReels();
            ArrayList<ImageIcon> spunSymbols = slotMachine.getStopReels();
            for (int i = 0; i < spunSymbols.size(); i ++) {
                slotMachine.updateReel(i, spunSymbols.get(i));
            }

            // Create a new BonusSlotGame instance and spin the reels
            BonusSlotGame bonusSlotGame = new BonusSlotGame(spunSymbols, slotMachine.slotPayTable, 2);
            int bonusPayout = bonusSlotGame.spinReels();

            // Update the balance and bonus pot
            double balance = slotMachine.getBalance();
            balance += bonusPayout;
            slotMachine.setBalance(balance);
            slotMachine.updateBalanceLabel();
            bonusPot.setText("You won $" + bonusPayout + " from the bonus pot!");

        // Display a dialog box with the winnings and a prompt to close
        JOptionPane.showMessageDialog(null, "You won $" + bonusPayout + " from the bonus pot!", "Winnings", JOptionPane.INFORMATION_MESSAGE);

            // Update the bonus pot value based on the bonus payout
            slotMachine.bonusPotValue = 0;
            bonusRoundActive = false;

            updateBonusPot();
            bonusFrame.repaint();
            bonusFrame.revalidate();

            if (bonusSpinButton != null) {
                bonusFrame.remove(bonusSpinButton);
            }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> bonusFrame.dispose());
        bonusFrame.add(closeButton, BorderLayout.SOUTH);
        }

    // Update the bonus pot label
    public void updateBonusPot() {
        if (bonusRoundActive) {
            bonusPot.setText("Bonus Pot: $" + slotMachine.bonusPotValue);
        }
    }
}
