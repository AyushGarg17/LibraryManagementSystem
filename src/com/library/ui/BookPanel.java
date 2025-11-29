package com.library.ui;

import com.library.backend.LibraryService;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookPanel extends JPanel {

    private LibraryService service;

    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtCategory;
    private JCheckBox chkReference;
    private JTable tblBooks;
    private JTextField txtSearch;

    public BookPanel(LibraryService service) {
        this.service = service;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 5, 5, 5));

        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtCategory = new JTextField();
        chkReference = new JCheckBox("Reference Only");

        JButton btnAdd = new JButton("Add Book");
        btnAdd.addActionListener(e -> addBook());

        formPanel.add(new JLabel("Title:"));
        formPanel.add(txtTitle);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(txtAuthor);
        formPanel.add(chkReference);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(txtCategory);
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        formPanel.add(btnAdd);

        add(formPanel, BorderLayout.NORTH);

        // Table
        tblBooks = new JTable();
        add(new JScrollPane(tblBooks), BorderLayout.CENTER);

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> searchBooks());
        JButton btnShowAll = new JButton("Show All");
        btnShowAll.addActionListener(e -> loadAllBooks());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        add(searchPanel, BorderLayout.SOUTH);

        loadAllBooks();
    }

    private void addBook() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String category = txtCategory.getText().trim();
        boolean reference = chkReference.isSelected();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and author are required");
            return;
        }

        service.addBook(title, author, category, reference);
        service.save();
        JOptionPane.showMessageDialog(this, "Book added");
        clearForm();
        loadAllBooks();
    }

    private void clearForm() {
        txtTitle.setText("");
        txtAuthor.setText("");
        txtCategory.setText("");
        chkReference.setSelected(false);
    }

    private void loadAllBooks() {
        populateTable(service.getAllBooks());
    }

    private void searchBooks() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadAllBooks();
        } else {
            populateTable(service.searchBooks(keyword));
        }
    }

    private void populateTable(List<Book> books) {
        String[] cols = {"ID", "Title", "Author", "Category", "Reference", "Issued", "Times Issued"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Book b : books) {
            model.addRow(new Object[]{
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.isReferenceOnly() ? "Yes" : "No",
                    b.isIssued() ? "Yes" : "No",
                    b.getTimesIssued()
            });
        }
        tblBooks.setModel(model);
    }
}
