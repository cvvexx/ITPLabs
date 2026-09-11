package edu.course.lab04;

import java.util.Objects;

public final class Book {

    private final String isbn;
    private final String title;

    public Book(String isbn, String title) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("isbn cannot be null or blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title cannot be null or blank");
        }
        this.isbn = isbn;
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Book book = (Book) object;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }
}
