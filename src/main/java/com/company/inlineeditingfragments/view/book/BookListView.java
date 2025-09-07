package com.company.inlineeditingfragments.view.book;

import com.company.inlineeditingfragments.entity.Book;
import com.company.inlineeditingfragments.view.bookdetailfragment.BookDetailFragment;
import com.company.inlineeditingfragments.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Route(value = "books", layout = MainView.class)
@ViewController(id = "Book.list")
@ViewDescriptor(path = "book-list-view.xml")
@LookupComponent("booksDataGrid")
@DialogMode(width = "64em")
public class BookListView extends StandardListView<Book> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionLoader<Book> booksDl;

    @ViewComponent
    private CollectionContainer<Book> booksDc;

    @ViewComponent
    private DataGrid<Book> booksDataGrid;

    @Autowired
    protected Notifications notifications;

    @Autowired
    private Fragments fragments;

    @Subscribe("booksDataGrid.createAction")
    protected void onBookDataGridCreate(ActionPerformedEvent event) {
        Book newBook = dataContext.create(Book.class);
        booksDc.getMutableItems().add(newBook);
        booksDataGrid.scrollToEnd();
        booksDataGrid.select(newBook);
    }


    @Subscribe("booksDataGrid.editAction")
    protected void onBookDataGridEdit(ActionPerformedEvent event) {
        Book selectedBook = booksDataGrid.getSingleSelectedItem();

        if (selectedBook == null || booksDataGrid.getEditor().isOpen()) {
            notifications.create("Select an item to edit and close the editor before")
                    .withType(Notifications.Type.WARNING)
                    .withCloseable(false)
                    .show();
            return;
        }

        booksDataGrid.getEditor().editItem(selectedBook);
    }

    // ========== COMMIT toàn phiên khi bấm OK ==========
    @Subscribe("okBtn")
    protected void onOk(ClickEvent<JmixButton> event) {
        // nếu editor đang mở -> save hàng đang mở (validate ở đây)
        if (booksDataGrid.getEditor().isOpen()) {
            try {
                booksDataGrid.getEditor().save();
            } catch (Exception ex) {
                warn("Please fix validation errors before saving");
                return;
            }
        }
        dataContext.save();        // INSERT / UPDATE / DELETE theo diff
        booksDl.load();            // reload để thấy dữ liệu sạch từ DB
        notifications.create("Saved").withType(Notifications.Type.SUCCESS).show();
    }

    // ========== ROLLBACK toàn phiên khi bấm Cancel ==========
    @Subscribe("cancelBtn")
    protected void onCancel(ClickEvent<JmixButton> event) {
        if (booksDataGrid.getEditor().isOpen()) {
            booksDataGrid.getEditor().cancel();
        }
        dataContext.clear(); // quên hết thay đổi trong phiên
        booksDl.load();      // nạp lại từ DB (hủy mọi tạo/sửa/xoá chưa commit)
        notifications.create("Canceled").withType(Notifications.Type.DEFAULT).show();
    }

    private void warn(String msg) {
        notifications.create(msg).withType(Notifications.Type.WARNING).show();
    }

    @Supply(to = "booksDataGrid.detail", subject = "renderer")
    private Renderer<Book> booksDataGridDetailRenderer() {
        return new ComponentRenderer<>(book -> {
            BookDetailFragment fragment = fragments.create(this, BookDetailFragment.class);
            fragment.setItem(book);

            // ========== CODE RENDERER ENHANCEMENTS ==========

            // 2. Add hover effect with tooltip
            fragment.getContent().getElement().addEventListener("mouseenter", event -> {
                String tooltip = generateBookTooltip(book);
                fragment.getContent().getElement().setProperty("title", tooltip);
                fragment.getContent().getElement().getStyle().set("transform", "scale(1.02)");
                fragment.getContent().getElement().getStyle().set("transition", "transform 0.2s ease");
            });

            fragment.getContent().getElement().addEventListener("mouseleave", event -> {
                fragment.getContent().getElement().getStyle().set("transform", "scale(1)");
            });

            // 3. Add special styling based on business logic
            LocalDate today = LocalDate.now();
            if (book.getPublicDate() != null && book.getPublicDate().isAfter(today)) {
                // Upcoming book - add pulsing border
                fragment.getContent().getElement().getStyle()
                        .set("box-shadow", "0 0 10px rgba(102, 126, 234, 0.6)")
                        .set("animation", "pulse-border 3s infinite");
            }

            // 4. Add conditional badges
            if (isNewRelease(book)) {
                addNewReleaseBadge(fragment);
            }

            if (isBestseller(book)) {
                addBestsellerBadge(fragment);
            }

            // 5. Add double-click for quick edit
            fragment.getContent().getElement().addEventListener("dblclick", event -> {
                openQuickEditDialog(book);
            });

            return fragment;
        });
    }


    private String generateBookTooltip(Book book) {
        StringBuilder tooltip = new StringBuilder();
        tooltip.append("📖 ").append(book.getTitle() != null ? book.getTitle() : "Untitled").append("\n");

        if (book.getAuthor() != null) {
            tooltip.append("✍️ by ").append(book.getAuthor().getName()).append("\n");
        }

        if (book.getPublicDate() != null) {
            if (book.getPublicDate().isAfter(LocalDate.now())) {
                long days = ChronoUnit.DAYS.between(LocalDate.now(), book.getPublicDate());
                tooltip.append("🚀 Coming in ").append(days).append(" days\n");
            } else {
                tooltip.append("📅 Published: ").append(book.getPublicDate()).append("\n");
            }
        }

        tooltip.append("💰 Price: $").append(book.getPrice() != null ? book.getPrice() : "0.00").append("\n");
        tooltip.append("📦 Stock: ").append(book.getQuantity() != null ? book.getQuantity() : "0");

        return tooltip.toString();
    }

    private boolean isNewRelease(Book book) {
        if (book.getPublicDate() == null) return false;
        return book.getPublicDate().isAfter(LocalDate.now().minusMonths(3));
    }

    private boolean isBestseller(Book book) {
        // Example logic: books with quantity < 10 (selling fast) and price > $20
        return book.getQuantity() != null && book.getQuantity() < 10 &&
                book.getPrice() != null && book.getPrice() > 20;
    }

    private void addNewReleaseBadge(BookDetailFragment fragment) {
        fragment.getContent().getElement().executeJs(
                "const badge = document.createElement('div');" +
                        "badge.innerHTML = '🆕 NEW';" +
                        "badge.style.cssText = 'position:absolute; top:-5px; right:-5px; background:linear-gradient(45deg,#ff6b6b,#feca57); color:white; padding:2px 6px; border-radius:10px; font-size:9px; font-weight:bold; z-index:10; animation:bounce 2s infinite;';" +
                        "this.style.position = 'relative';" +
                        "this.appendChild(badge);"
        );
    }

    private void addBestsellerBadge(BookDetailFragment fragment) {
        fragment.getContent().getElement().executeJs(
                "const badge = document.createElement('div');" +
                        "badge.innerHTML = '🏆 HOT';" +
                        "badge.style.cssText = 'position:absolute; top:-5px; left:-5px; background:linear-gradient(45deg,#ff9a9e,#fecfef); color:white; padding:2px 6px; border-radius:10px; font-size:9px; font-weight:bold; z-index:10; animation:glow 2s infinite;';" +
                        "this.style.position = 'relative';" +
                        "this.appendChild(badge);"
        );
    }

    private void openQuickEditDialog(Book book) {
        notifications.create("Double-clicked on: " + book.getTitle() + " - Quick Edit feature!")
                .withType(Notifications.Type.WARNING)
                .show();

        // Here you could open a dialog, navigate to edit view, etc.
    }







}