package com.library.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

    public class LibraryData implements Serializable {
        private List<Book> books = new ArrayList<>();
        private List<Member> members = new ArrayList<>();
        private List<Loan> loans = new ArrayList<>();

        private int nextBookId = 1;
        private int nextMemberId = 1;
        private int nextLoanId = 1;

        public List<Book> getBooks() {
            return books;
        }

        public List<Member> getMembers() {
            return members;
        }

        public List<Loan> getLoans() {
            return loans;
        }

        public int generateBookId() {
            return nextBookId++;
        }

        public int generateMemberId() {
            return nextMemberId++;
        }

        public int generateLoanId() {
            return nextLoanId++;
        }
    }


