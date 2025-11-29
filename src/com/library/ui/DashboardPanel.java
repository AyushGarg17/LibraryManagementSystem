package com.library.ui;

import com.library.backend.LibraryService;
import com.library.model.Loan;
import com.library.model.Member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private LibraryService service;

    private JLabel lblTotalBooks;
    private JLabel lblIssuedBooks;
    private JLabel lblTotalMembers;
    private JLabel lblDefaulters;

    private JTable tblOverdue;
    private JTable tblUpcoming;

    public DashboardPanel(LibraryService service) {
        this.service = service;
        setLayout(new BorderLayout());

        // Top stats
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        lblTotalBooks = new JLabel();
        lblIssuedBooks = new JLabel();
        lblTotalMembers = new JLabel();
        lblDefaulters = new JLabel();

        statsPanel.add(wrapInCard("Total Books", lblTotalBooks));
        statsPanel.add(wrapInCard("Issued Books", lblIssuedBooks));
        statsPanel.add(wrapInCard("Total Members", lblTotalMembers));
        statsPanel.add(wrapInCard("Defaulters", lblDefaulters));

        add(statsPanel, BorderLayout.NORTH);

        // Tables
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2));

        tblOverdue = new JTable();
        tblUpcoming = new JTable();

        tablesPanel.add(new JScrollPane(tblOverdue));
        tablesPanel.add(new JScrollPane(tblUpcoming));

        add(tablesPanel, BorderLayout.CENTER);

        refreshData();
    }

    private JPanel wrapInCard(String title, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 16f));
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private void refreshData() {
        lblTotalBooks.setText(String.valueOf(service.getTotalBooks()));
        lblIssuedBooks.setText(String.valueOf(service.getIssuedBooksCount()));
        lblTotalMembers.setText(String.valueOf(service.getTotalMembers()));
        lblDefaulters.setText(String.valueOf(service.getDefaultersCount()));

        // Overdue table (defaulters)
        String[] overdueCols = {"Loan ID", "Book ID", "Member", "Due Date"};
        DefaultTableModel overdueModel = new DefaultTableModel(overdueCols, 0);
        for (Loan loan : service.getOverdueLoans()) {
            Member m = service.findMemberById(loan.getMemberId()).orElse(null);
            String memberName = (m != null) ? m.getName() : "Unknown";
            overdueModel.addRow(new Object[]{
                    loan.getId(),
                    loan.getBookId(),
                    memberName,
                    loan.getDueDate()
            });
        }
        tblOverdue.setModel(overdueModel);
        tblOverdue.setBorder(BorderFactory.createTitledBorder("Overdue Loans"));

        // Upcoming due table
        String[] upcomingCols = {"Loan ID", "Book ID", "Member", "Due Date"};
        DefaultTableModel upcomingModel = new DefaultTableModel(upcomingCols, 0);
        for (Loan loan : service.getUpcomingDueLoans(3)) {
            Member m = service.findMemberById(loan.getMemberId()).orElse(null);
            String memberName = (m != null) ? m.getName() : "Unknown";
            upcomingModel.addRow(new Object[]{
                    loan.getId(),
                    loan.getBookId(),
                    memberName,
                    loan.getDueDate()
            });
        }
        tblUpcoming.setModel(upcomingModel);
        tblUpcoming.setBorder(BorderFactory.createTitledBorder("Due in Next 3 Days"));
    }
}
