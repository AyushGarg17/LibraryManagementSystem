package model;

public class IssueRecord {
    private String memberId;
    private String bookId;
    private String issueDate;
    private String returnDate;

    public IssueRecord(String memberId, String bookId, String issueDate) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.returnDate = "Not Returned";
    }

    public String getMemberId() { return memberId; }
    public String getBookId() { return bookId; }
    public String getIssueDate() { return issueDate; }
    public String getReturnDate() { return returnDate; }

    public void setReturnDate(String date) {
        this.returnDate = date;
    }
}
