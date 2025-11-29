package com.library.backend;

import com.library.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {

    private LibraryData data;
    private static final double FINE_PER_DAY = 2.0; // customize

    public LibraryService() {
        this.data = DataStore.load();
    }

    public void save() {
        DataStore.save(data);
    }

    // ---------- BOOKS ----------

    public Book addBook(String title, String author, String category, boolean referenceOnly) {
        int id = data.generateBookId();
        Book book = new Book(id, title, author, category, referenceOnly);
        data.getBooks().add(book);
        save();
        return book;
    }

    public List<Book> getAllBooks() {
        return data.getBooks();
    }

    public Optional<Book> findBookById(int id) {
        return data.getBooks().stream().filter(b -> b.getId() == id).findFirst();
    }

    public List<Book> searchBooks(String keyword) {
        String k = keyword.toLowerCase();
        return data.getBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(k)
                        || b.getAuthor().toLowerCase().contains(k)
                        || b.getCategory().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }

    // ---------- MEMBERS ----------

    public Member addStudent(String name, String email, String phone) {
        int id = data.generateMemberId();
        Member m = new Student(id, name, email, phone);
        data.getMembers().add(m);
        save();
        return m;
    }

    public Member addFaculty(String name, String email, String phone) {
        int id = data.generateMemberId();
        Member m = new Faculty(id, name, email, phone);
        data.getMembers().add(m);
        save();
        return m;
    }

    public List<Member> getAllMembers() {
        return data.getMembers();
    }

    public Optional<Member> findMemberById(int id) {
        return data.getMembers().stream().filter(m -> m.getId() == id).findFirst();
    }

    public long countActiveLoansForMember(int memberId) {
        return data.getLoans().stream()
                .filter(l -> l.getMemberId() == memberId && !l.isReturned())
                .count();
    }

    // ---------- LOANS / ISSUE / RETURN ----------

    public Loan issueBook(int bookId, int memberId, int loanDays) throws Exception {
        Book book = findBookById(bookId)
                .orElseThrow(() -> new Exception("Book not found"));
        if (book.isIssued()) {
            throw new Exception("Book already issued");
        }

        Member member = findMemberById(memberId)
                .orElseThrow(() -> new Exception("Member not found"));

        long activeLoans = countActiveLoansForMember(memberId);
        if (activeLoans >= member.getMaxBooksAllowed()) {
            throw new Exception("Member has reached maximum allowed issued books");
        }

        if (book.isReferenceOnly()) {
            throw new Exception("Reference only book cannot be issued");
        }

        int loanId = data.generateLoanId();
        LocalDate issueDate = LocalDate.now();
        LocalDate dueDate = issueDate.plusDays(loanDays);
        Loan loan = new Loan(loanId, bookId, memberId, issueDate, dueDate);
        data.getLoans().add(loan);

        book.setIssued(true);
        book.incrementTimesIssued();

        save();
        return loan;
    }

    public void returnBook(int loanId) throws Exception {
        Loan loan = data.getLoans().stream()
                .filter(l -> l.getId() == loanId)
                .findFirst()
                .orElseThrow(() -> new Exception("Loan not found"));

        if (loan.isReturned()) {
            throw new Exception("Book already returned");
        }

        loan.setReturnDate(LocalDate.now());
        double fine = calculateFine(loan);
        loan.setFineAmount(fine);

        // Mark book as available
        findBookById(loan.getBookId()).ifPresent(b -> b.setIssued(false));

        save();
    }

    public double calculateFine(Loan loan) {
        if (!loan.isReturned()) return 0.0;
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), loan.getReturnDate());
        if (daysLate <= 0) return 0.0;
        return daysLate * FINE_PER_DAY;
    }

    public List<Loan> getActiveLoans() {
        return data.getLoans().stream()
                .filter(l -> !l.isReturned())
                .collect(Collectors.toList());
    }

    public List<Loan> getOverdueLoans() {
        return data.getLoans().stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    public List<Loan> getUpcomingDueLoans(int days) {
        LocalDate now = LocalDate.now();
        LocalDate cutoff = now.plusDays(days);
        return data.getLoans().stream()
                .filter(l -> !l.isReturned()
                        && ( !l.getDueDate().isBefore(now) && !l.getDueDate().isAfter(cutoff)))
                .collect(Collectors.toList());
    }

    // ---------- STATS / INNOVATIVE FEATURES ----------

    public int getTotalBooks() {
        return data.getBooks().size();
    }

    public long getIssuedBooksCount() {
        return data.getBooks().stream().filter(Book::isIssued).count();
    }

    public int getTotalMembers() {
        return data.getMembers().size();
    }

    public long getDefaultersCount() {
        return getOverdueLoans().stream()
                .map(Loan::getMemberId)
                .distinct()
                .count();
    }

    public List<Book> getMostPopularBooks(int limit) {
        return data.getBooks().stream()
                .sorted(Comparator.comparingInt(Book::getTimesIssued).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<Member, Long> getTopReaders(int limit) {
        Map<Integer, Long> countMap = data.getLoans().stream()
                .collect(Collectors.groupingBy(Loan::getMemberId, Collectors.counting()));

        Map<Member, Long> result = new LinkedHashMap<>();

        data.getMembers().stream()
                .sorted((m1, m2) -> {
                    long c1 = countMap.getOrDefault(m1.getId(), 0L);
                    long c2 = countMap.getOrDefault(m2.getId(), 0L);
                    return Long.compare(c2, c1);
                })
                .limit(limit)
                .forEach(m -> result.put(m, countMap.getOrDefault(m.getId(), 0L)));

        return result;
    }

    public List<Book> recommendBooksForMember(int memberId, int limit) {
        // simple idea: recommend most popular books in categories this member borrowed before
        Set<String> categoriesUsed = data.getLoans().stream()
                .filter(l -> l.getMemberId() == memberId)
                .map(Loan::getBookId)
                .map(id -> findBookById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(Book::getCategory)
                .collect(Collectors.toSet());

        if (categoriesUsed.isEmpty()) {
            return getMostPopularBooks(limit);
        }

        return data.getBooks().stream()
                .filter(b -> categoriesUsed.contains(b.getCategory()))
                .sorted(Comparator.comparingInt(Book::getTimesIssued).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
