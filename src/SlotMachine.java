package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SlotMachine extends JFrame implements ActionListener {

    private JLabel[] reels;
    private final JButton spinButton;
    private final JTextField betField;
    private final JLabel balanceLabel;
    private final JLabel winningsLabel;
    protected JProgressBar bonusPotMeter;
    protected JLabel bonusPotLabel;

    ImageResizer imageResizer = new ImageResizer();

    BonusRound bonusRound = new BonusRound(this);

    // Game data
    protected final int NUM_REELS = 5;
    public void setStopReels(ArrayList<ImageIcon> stopReels) {
        this.stopReels = stopReels;
    }
    private double balance, currentBet;
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    private ArrayList<ImageIcon> stopReels;

    protected final ImageIcon[] symbols = {
            new ImageIcon("src/symbols/cherry.png"), //0
            new ImageIcon("src/symbols/lemon.png"), //1
            new ImageIcon("src/symbols/orange.png"), //2
            new ImageIcon("src/symbols/plum.png"), //3
            new ImageIcon("src/symbols/bell.png"), //4
            new ImageIcon("src/symbols/bar.png"), //5
            new ImageIcon("src/symbols/seven.png"), //6
            new ImageIcon("src/symbols/code.png") //7
    };

    PayTable slotPayTable = new PayTable(symbols);

    // Bonus game data
    protected boolean bonusRoundActive;
    private boolean bonusRoundSpun;
    protected int bonusPotValue;

    public SlotMachine() {
        setTitle("Slot Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Rules button
        JButton rulesButton = new JButton("Rules");

        // Add action listener to rules button
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane optionPane = new JOptionPane(
                        "Rules:\n" +
                                "Spin the reels and match three symbols in a row to win!\n" +
                                "Cherries pay $1000!\n" +
                                "Lemons pay $600!\n" +
                                "Oranges pay $500!\n" +
                                "Plums pay $400!\n" +
                                "Bells pay $300!\n" +
                                "Bars pay $100!\n" +
                                "Sevens pay $2500!\n" +
                                "Scatter symbols pay $250 for each appearance!\n" +
                                "10% of each win is added to the bonus pot!\n" +
                                "Get the bonus pot to $1000 to activate the bonus round!\n" +
                                "In the bonus round, all wins are doubled!\n" +
                                "Good luck!",
                        JOptionPane.INFORMATION_MESSAGE
                );
                JDialog dialog = optionPane.createDialog("Rules");
                dialog.setModal(false);
                dialog.setVisible(true);
            }
        });

        // Set up main panel
        // GUI components
        JPanel mainPanel = new JPanel(new BorderLayout());
        JButton adminButton = new JButton("Admin Panel");
        mainPanel.add(adminButton, BorderLayout.EAST);

        // Add an ActionListener to the "Admin" button
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt for password
                String password = JOptionPane.showInputDialog("Enter admin password:");
                if ("password".equals(password)) {
                    // Create a new JFrame for the admin panel
                    JFrame adminFrame = new JFrame("Admin Panel");
                    adminFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    adminFrame.setSize(300, 300);
                    adminFrame.setLocationRelativeTo(null);

                    // Add the AdminPanel to the admin frame
                    AdminPanel adminPanel = new AdminPanel(SlotMachine.this);
                    adminFrame.add(adminPanel);

                    // Show the admin frame
                    adminFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel reelsPanel = new JPanel(new GridLayout(1, NUM_REELS));
        reels = new JLabel[NUM_REELS];
        for (int i = 0; i < NUM_REELS; i++) {
            reels[i] = new JLabel();
            reels[i].setHorizontalAlignment(JLabel.CENTER);
            reelsPanel.add(reels[i]);
        }
        mainPanel.add(reelsPanel, BorderLayout.CENTER);

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout());
        balanceLabel = new JLabel("Balance: $100.00");
        topPanel.add(balanceLabel);
        topPanel.add(rulesButton);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reels = new JLabel[NUM_REELS];
        for (int i = 0; i < NUM_REELS; i++) {
            reels[i] = new JLabel();
            reels[i].setFont(new Font("Arial", Font.BOLD, 48));
            reels[i].setPreferredSize(new Dimension(100, 100)); // Set a preferred size for each label
            centerPanel.add(reels[i]);
        }
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        betField = new JTextField(5);
        betField.setText("1.00");
        bottomPanel.add(new JLabel("Bet: "));
        bottomPanel.add(betField);
        spinButton = new JButton("Spin");
        spinButton.addActionListener(this);
        bottomPanel.add(spinButton);
        winningsLabel = new JLabel();
        bottomPanel.add(winningsLabel);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        //Bonus pot label
        bonusPotLabel = new JLabel("Bonus Pot: ");
        topPanel.add(bonusPotLabel);

        // Bonus pot meter
        bonusPotMeter = new JProgressBar(0, 1000);
        bonusPotMeter.setValue(0);
        bonusPotMeter.setStringPainted(true);
        topPanel.add(bonusPotMeter);

        // Initialize game data
        balance = 100.0;
        currentBet = 1.0;
        stopReels = new ArrayList<>();
        bonusRoundActive = false;
        bonusRoundSpun = false;

        add(mainPanel);
        setVisible(true);
    }

    public void updateReel(int reelIndex, ImageIcon symbol) {
        ImageIcon resizedSymbol = imageResizer.resizeImageIcon(symbol, reels[reelIndex].getWidth(), reels[reelIndex].getHeight());
        reels[reelIndex].setIcon(resizedSymbol);
        stopReels.set(reelIndex, symbol);
    }

    // Spin the reels and determine the outcome
    protected void spinReels() {
        stopReels.clear();
            // Generate random symbols
            Random rand = new Random();
            for (int i = 0; i < NUM_REELS; i++) {
                ImageIcon symbol = symbols[rand.nextInt(symbols.length)];
                ImageIcon resizedSymbol = imageResizer.resizeImageIcon(symbol, reels[i].getWidth(), reels[i].getHeight());
                reels[i].setIcon(resizedSymbol);
                stopReels.add(symbol);
            }
            if (bonusRoundActive && !bonusRoundSpun) {
                bonusRound.spinBonusReels();
                bonusRoundSpun = true;
            }
         else {
            // Use the symbols in stopReels
            for (int i = 0; i < NUM_REELS; i++) {
                ImageIcon symbol = stopReels.get(i);
                ImageIcon resizedSymbol = imageResizer.resizeImageIcon(symbol, reels[i].getWidth(), reels[i].getHeight());
                reels[i].setIcon(resizedSymbol);
            }
        }
        checkWinningCombination();
    }

    protected void checkWinningCombination() {
        boolean isWinning = false;

        // Set the multiplier based on whether the bonus round is active
        int multiplier = bonusRoundActive ? 2 : 1; // Set the multiplier to 2 if the bonus round is active

        // Check for winning combinations
        for (int i = 0; i < stopReels.size() - 2; i++) {
            if (stopReels.get(i).equals(stopReels.get(i + 1)) && stopReels.get(i).equals(stopReels.get(i + 2))) {
                isWinning = true;
                int payout = slotPayTable.payTable.get(stopReels.get(i)); // Get the payout for the winning combination
                double totalPayout = payout * currentBet * multiplier; // Calculate the total payout
                balance += totalPayout;
                winningsLabel.setText("You won $" + totalPayout + "!"); // Update the winning label to reflect the total payout

                // Check for bonus round
                bonusPotValue += (int) (totalPayout * 0.1); // 10% of total payout added to bonus pot
                bonusRound.updateBonusPot();
                bonusPotMeter.setValue(bonusPotValue);
                bonusPotLabel.setText("Bonus Pot: $" + bonusPotValue + "/1000!");
                break;
            }
        }

        // Check for scatter symbol
        long scatterCount = stopReels.stream().filter(symbol -> symbol.getDescription().equals(symbols[7].getDescription())).count();
        if (scatterCount > 0) {
            isWinning = true;
            double scatterPayout = 250 * scatterCount;
            balance += scatterPayout;
            bonusPotValue += (int) (scatterPayout * 0.1); // 10% of total payout added to bonus pot
            winningsLabel.setText("Scatter symbol appeared! You won $" + scatterPayout + "!");
        }

        if (!isWinning) {
            winningsLabel.setText("Try again!");
        }

        // Update balance
        updateBalanceLabel();

        // Check if bonus round should be activated
        if (bonusPotValue >= 1000 && !bonusRoundActive) {
            bonusRound.startBonusRound();
            bonusRoundActive = true;
            bonusRoundSpun = false;
        }
    }

    public void updateBalanceLabel() {
        balanceLabel.setText("Balance: $" + String.format("%.2f", getBalance()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == spinButton) {
            try {
                currentBet = Double.parseDouble(betField.getText());
                if (currentBet <= 0) {
                    winningsLabel.setText("Bet must be greater than 0!");
                    return;
                }
                if (balance >= currentBet) {
                    balance -= currentBet;
                    spinReels();
                } else {
                    winningsLabel.setText("Not enough balance!");
                }
            } catch (NumberFormatException ex) {
                winningsLabel.setText("Invalid bet amount!");
            }
        }
    }

    public static void main(String[] args) {
        new SlotMachine();
    }
}