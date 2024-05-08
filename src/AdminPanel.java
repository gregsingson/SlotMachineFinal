package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AdminPanel extends JPanel {
    private JComboBox<ImageIcon>[] symbolSelectors;
    private JFrame spinReelsFrame;

    public AdminPanel(SlotMachine slotMachine) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Button to select reels manually
        JButton manualReels = new JButton("Select Reels Manually");
        manualReels.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new JFrame for the spinReels test
                spinReelsFrame = new JFrame("Select Reels Manually");
                spinReelsFrame.setSize(300, 300);
                spinReelsFrame.setLocationRelativeTo(null);

                // Create a JPanel to hold the symbol selectors
                JPanel symbolSelectorsPanel = new JPanel();
                symbolSelectors = new JComboBox[slotMachine.NUM_REELS];
                for (int i = 0; i < slotMachine.NUM_REELS; i++) {
                    symbolSelectors[i] = new JComboBox<>(slotMachine.symbols);
                    symbolSelectors[i].setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if (value instanceof ImageIcon icon) {
                                ImageIcon resizedIcon = slotMachine.imageResizer.resizeImageIcon(icon, 30, 30); // Resize the icon to fit comfortably within the dropdown box
                                setIcon(resizedIcon);
                            }
                            return this;
                        }
                    });
                    symbolSelectorsPanel.add(symbolSelectors[i]);
                }
                spinReelsFrame.add(symbolSelectorsPanel, BorderLayout.CENTER);

                // Create a confirm button
                JButton confirmButton = new JButton("Confirm");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<ImageIcon> selectedSymbols = new ArrayList<>();
                        for (int i = 0; i < slotMachine.NUM_REELS; i++) {
                            ImageIcon selectedSymbol = (ImageIcon) symbolSelectors[i].getSelectedItem();
                            selectedSymbols.add(selectedSymbol);
                        }
                        slotMachine.setStopReels(selectedSymbols);
                        for (int i = 0; i < slotMachine.NUM_REELS; i++) {
                            slotMachine.updateReel(i, selectedSymbols.get(i));
                        }
                    }
                });
                spinReelsFrame.add(confirmButton, BorderLayout.SOUTH);

                // Show the spinReels test frame
                spinReelsFrame.setVisible(true);
            }
        });
        add(manualReels);

            // Button to test checkWinningCombination function
            JButton checkWinningCombinationButton = new JButton("Test checkWinningCombination");
            checkWinningCombinationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    slotMachine.checkWinningCombination();
                }
            });
            add(checkWinningCombinationButton);

            // Button to activate bonus round
            JButton activateBonusRoundButton = new JButton("Activate Bonus Round");
            activateBonusRoundButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    slotMachine.bonusRound.startBonusRound();
                    slotMachine.bonusRound.updateBonusPot();
                    slotMachine.bonusPotMeter.setValue(slotMachine.bonusPotValue);
                }
            });
        add(activateBonusRoundButton);


            // Field and button to set balance manually
            JTextField balanceField = new JTextField(5);
            add(new JLabel("Set Balance: "));
            add(balanceField);
            JButton setBalanceButton = new JButton("Set Balance");
            setBalanceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        double balance = Double.parseDouble(balanceField.getText());
                        slotMachine.setBalance(balance);
                        slotMachine.updateBalanceLabel();
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid balance amount!");
                    }
                }
            });
            add(setBalanceButton);

            // Field and button to set bonusPotValue manually
            JTextField bonusPotValueField = new JTextField(5);
            add(new JLabel("Set Bonus Pot Value: "));
            add(bonusPotValueField);
            JButton setBonusPotValueButton = new JButton("Set Bonus Pot Value");
            setBonusPotValueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int bonusPotValue = Integer.parseInt(bonusPotValueField.getText());
                        slotMachine.bonusRound.setBonusPotValue(bonusPotValue);
                        slotMachine.bonusRound.updateBonusPot();
                        slotMachine.bonusPotLabel.setText("Bonus Pot: $" + bonusPotValue + "/1000!");
                        slotMachine.bonusPotMeter.setValue(bonusPotValue);
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid bonus pot value!");
                    }
                }
            });
            add(setBonusPotValueButton);
        }
    }
