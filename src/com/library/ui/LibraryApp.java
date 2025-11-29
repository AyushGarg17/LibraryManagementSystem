package com.library.ui;

import com.library.backend.LibraryService;

import javax.swing.*;
import java.awt.*;

public class LibraryApp extends JFrame {

    private LibraryService service;

    public LibraryApp() {
        super("Library Management System");
        this.service = new LibraryService();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Dashboard", new DashboardPanel(service));
        tabs.addTab("Books", new BookPanel(service));
        // You can add:
        // tabs.addTab("Members", new MemberPanel(service));
        // tabs.addTab("Issue / Return", new IssueReturnPanel(service));

        add(tabs, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            app.setVisible(true);
        });
    }
}
