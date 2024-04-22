

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.toedter.calendar.JDateChooser;

public class BookingApp extends JFrame {
    private JPanel panel;
    private JTextField idField;
    private JTextField nameField;
    private JComboBox<String> vehicleTypeCombo;
    private JComboBox<String> sourceCombo;
    private JComboBox<String> destinationCombo;
    private JTextField seatsField;
    private JDateChooser entryDateChooser;
    private JSpinner entryTimeSpinner;
    private JSpinner exitTimeSpinner;
    private JComboBox<String> paymentModeCombo;
    private JButton bookButton;
    private JButton showEntriesButton;
    private JButton removeEntryButton;
    private JButton paymentButton;
    private JTextArea resultArea;

    private Entry[] entries = new Entry[10];
    private int entryCount = 0;

    private static class Entry {
        private int id;
        private String name;
        private String type;
        private String source;
        private String destination;
        private int seats;
        private LocalDateTime entryDateTime;
        private LocalDateTime exitDateTime;
        private String review;
        private int rating;
        private double amount; // Amount for the ticket
        private String paymentMode; // Credit or Debit

        public Entry(int id, String name, String type, String source, String destination, int seats, LocalDateTime entryDateTime, LocalDateTime exitDateTime, String review, int rating, double amount, String paymentMode) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.source = source;
            this.destination = destination;
            this.seats = seats;
            this.entryDateTime = entryDateTime;
            this.exitDateTime = exitDateTime;
            this.review = review;
            this.rating = rating;
            this.amount = amount;
            this.paymentMode = paymentMode;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public double getAmount() {
            return amount;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Type: " + type + ", Source: " + source + ", Destination: " + destination + ", Seats: " + seats + ", Entry Time: " + entryDateTime + ", Exit Time: " + exitDateTime + ", Review: " + review + ", Rating: " + rating + ", Amount: $" + amount + ", Payment: " + paymentMode;
        }
    }

    public BookingApp() {
        super("Transport Booking and Viewing System");
        createUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1040, 800);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(16, 2, 10, 10)); // Increased rows to accommodate payment button
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        idField = new JTextField(10);
        nameField = new JTextField(10);
        vehicleTypeCombo = new JComboBox<>(new String[]{"Train", "Bus", "Airplane", "Taxi"});
        sourceCombo = new JComboBox<>(new String[]{"Solapur", "Pune", "Mumbai", "Nasik", "Nagpur", "Ahmednagar"});
        destinationCombo = new JComboBox<>(new String[]{"Solapur", "Pune", "Mumbai", "Nasik", "Nagpur", "Ahemdnagar"});
        seatsField = new JTextField(10);
        entryDateChooser = new JDateChooser();
        entryDateChooser.setDateFormatString("yyyy-MM-dd");
        entryTimeSpinner = createTimeSpinner();
        exitTimeSpinner = createTimeSpinner();

        paymentModeCombo = new JComboBox<>(new String[]{"Credit", "Debit"});

        bookButton = new JButton("Book");
        showEntriesButton = new JButton("Show All Entries");
        removeEntryButton = new JButton("Remove Entry");
        paymentButton = new JButton("Payment");
        resultArea = new JTextArea(8, 20);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(createLabel("ID:"));
        panel.add(idField);
        panel.add(createLabel("Name:"));
        panel.add(nameField);
        panel.add(createLabel("Select Vehicle Type:"));
        panel.add(vehicleTypeCombo);
        panel.add(createLabel("Source:"));
        panel.add(sourceCombo);
        panel.add(createLabel("Destination:"));
        panel.add(destinationCombo);
        panel.add(createLabel("Number of Seats:"));
        panel.add(seatsField);
        panel.add(createLabel("Entry Date:"));
        panel.add(entryDateChooser);
        panel.add(createLabel("Departure Time:"));
        panel.add(entryTimeSpinner);
        panel.add(createLabel("Arrive Time:"));
        panel.add(exitTimeSpinner);
        panel.add(createLabel("Payment Mode:"));
        panel.add(paymentModeCombo);
        panel.add(bookButton);
        panel.add(showEntriesButton);
        panel.add(removeEntryButton);
        panel.add(paymentButton); // Added payment button

        add(panel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                book();
            }
        });

        showEntriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEntries();
            }
        });

        removeEntryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEntryById();
            }
        });

        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPaymentWindow();
            }
        });
    }

    private void openPaymentWindow() {
        PaymentWindow paymentWindow = new PaymentWindow();
        paymentWindow.setVisible(true);
    }

    private JSpinner createTimeSpinner() {
        SpinnerModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
        return spinner;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void book() {
        int id;
        try {
            id = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            resultArea.setText("Error: Invalid ID format. Please enter a valid ID.");
            return;
        }

        if (isIdRegistered(id)) {
            resultArea.setText("Error: ID " + id + " is already registered. Please use a new ID.");
            return;
        }

        String name = nameField.getText();
        String type = (String) vehicleTypeCombo.getSelectedItem();
        String source = (String) sourceCombo.getSelectedItem();
        String destination = (String) destinationCombo.getSelectedItem();
        int seats;
        try {
            seats = Integer.parseInt(seatsField.getText());
        } catch (NumberFormatException e) {
            resultArea.setText("Error: Invalid seats format. Please enter a valid number of seats.");
            return;
        }
        LocalDate entryDate = LocalDate.ofInstant(entryDateChooser.getDate().toInstant(), java.time.ZoneId.systemDefault());
        LocalTime entryTime = ((java.util.Date) entryTimeSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
        LocalTime exitTime = ((java.util.Date) exitTimeSpinner.getValue()).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
        LocalDateTime entryDateTime = LocalDateTime.of(entryDate, entryTime);
        LocalDateTime exitDateTime = LocalDateTime.of(entryDate, exitTime);

        double amount = calculateAmount(type, seats); // Calculate the total amount based on vehicle type and seats
        String paymentMode = (String) paymentModeCombo.getSelectedItem(); // Get the selected payment mode

        entries[entryCount] = new Entry(id, name, type, source, destination, seats, entryDateTime, exitDateTime, "", 0, amount, paymentMode);
        entryCount++;

        resultArea.setText("Booking Successful!\n" + "Name: " + name + ", Seats: " + seats + ", Total Amount: $" + amount + ", Payment Mode: " + paymentMode);

        // Show the review and rating window
        ReviewRatingWindow reviewRatingWindow = new ReviewRatingWindow(entries[entryCount - 1]);
        reviewRatingWindow.setVisible(true);
    }

    private double calculateAmount(String type, int seats) {
        double baseAmount;
        switch (type) {
            case "Train":
                baseAmount = 50.0; // Sample base amount for train ticket
                break;
            case "Bus":
                baseAmount = 30.0; // Sample base amount for bus ticket
                break;
            case "Airplane":
                baseAmount = 150.0; // Sample base amount for airplane ticket
                break;
            case "Taxi":
                baseAmount = 20.0; // Sample base amount for taxi ride
                break;
            default:
                baseAmount = 0.0;
        }
        return baseAmount * seats; // Multiply base amount by the number of seats
    }

    private void showEntries() {
        StringBuilder entriesText = new StringBuilder();
        for (int i = 0; i < entryCount; i++) {
            entriesText.append(entries[i]).append("\n");
        }
        resultArea.setText("All Entries:\n" + entriesText.toString());
    }

    private void removeEntryById() {
        int idToRemove;
        try {
            idToRemove = Integer.parseInt(idField.getText());
        } catch (NumberFormatException e) {
            resultArea.setText("Error: Invalid ID format. Please enter a valid ID to remove.");
            return;
        }
        boolean found = false;
        for (int i = 0; i < entryCount; i++) {
            if (entries[i].getId() == idToRemove) {
                found = true;
                for (int j = i; j < entryCount - 1; j++) {
                    entries[j] = entries[j + 1];
                }
                entries[entryCount - 1] = null;
                entryCount--;
                break;
            }
        }
        if (found) {
            resultArea.setText("Entry with ID '" + idToRemove + "' removed.");
            showEntries();
        } else {
            resultArea.setText("Entry with ID '" + idToRemove + "' not found.");
        }
    }

    private boolean isIdRegistered(int id) {
        for (int i = 0; i < entryCount; i++) {
            if (entries[i].getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new BookingApp().setVisible(true);
            }
        });
    }

    // Review and Rating Window
    private class ReviewRatingWindow extends JFrame {
        private JPanel panel;
        private JTextField reviewField;
        private JSlider ratingSlider;
        private JButton submitButton;

        private Entry entry;

        public ReviewRatingWindow(Entry entry) {
            super("Review and Rating");
            this.entry = entry;
            createUI();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(600, 300);
            setLocationRelativeTo(null);
        }

        private void createUI() {
            panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2, 10, 10));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            reviewField = new JTextField(10);
            ratingSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1); // Range from 1 to 5
            submitButton = new JButton("Submit");

            panel.add(createLabel("Review:"));
            panel.add(reviewField);
            panel.add(createLabel("Rating:"));
            panel.add(ratingSlider);
            panel.add(submitButton);

            add(panel, BorderLayout.CENTER);

            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    submitReviewRating();
                }
            });
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            return label;
        }

    private void submitReviewRating() {
    String review = reviewField.getText();
    int rating = getSelectedRating();

    entry.setReview(review);
    entry.setRating(rating);

    JOptionPane.showMessageDialog(null, "Thank you for your feedback!");
    dispose(); // Close the window after submitting
}

// Get the selected rating from the check buttons
private int getSelectedRating() {
    // Array to hold the JCheckBox components for ratings
    JCheckBox[] ratingCheckboxes = new JCheckBox[5];
    for (int i = 0; i < 5; i++) {
        ratingCheckboxes[i] = new JCheckBox(String.valueOf(i + 1)); // Rating values are 1 to 5
    }

    // Create a panel to hold the rating check boxes
    JPanel ratingPanel = new JPanel();
    ratingPanel.setLayout(new FlowLayout());

    // Add the rating check boxes to the panel
    for (JCheckBox checkbox : ratingCheckboxes) {
        ratingPanel.add(checkbox);
    }

    // Show an input dialog with the rating check boxes
    JOptionPane.showMessageDialog(null, ratingPanel, "Select Rating", JOptionPane.PLAIN_MESSAGE);

    // Get the selected rating from the checked box
    for (int i = 0; i < 5; i++) {
        if (ratingCheckboxes[i].isSelected()) {
            return i + 1; // Return the rating value (1 to 5)
        }
    }

  
    return 3; 
}

    }
    // Payment Window
    private class PaymentWindow extends JFrame {
        private JPanel panel;
        private JLabel balanceLabel;
        private JTextField pinField;
        private JButton deductButton;

        public PaymentWindow() {
            super("Payment");
            createUI();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(500, 350);
            setLocationRelativeTo(null);
        }

        private void createUI() {
            panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2, 10, 10));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));

            balanceLabel = new JLabel("Available Balance: $1000"); // Example balance, update as needed
            pinField = new JTextField(10);
            deductButton = new JButton("Deduct Payment");

            panel.add(balanceLabel);
            panel.add(new JLabel()); // Empty label for spacing
            panel.add(createLabel("Enter PIN:"));
            panel.add(pinField);
            panel.add(new JLabel()); // Empty label for spacing
            panel.add(deductButton);

            add(panel, BorderLayout.CENTER);

            deductButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deductPayment();
                }
            });
        }

        private JLabel createLabel(String text) {
            JLabel label = new JLabel(text);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            return label;
        }

        private void deductPayment() {
            String pin = pinField.getText(); // Get PIN from the field

            // Validate PIN and deduct payment
            if (pin.equals("1234")) { // Example PIN, replace with your logic
                double amount = entries[entryCount - 1].getAmount(); // Get the last entry's amount
                balanceLabel.setText("Available Balance: $" + (1000 - amount)); // Update balance label
                JOptionPane.showMessageDialog(null, "Payment deducted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Invalid PIN. Payment deduction failed.");
            }
        }
    }
}