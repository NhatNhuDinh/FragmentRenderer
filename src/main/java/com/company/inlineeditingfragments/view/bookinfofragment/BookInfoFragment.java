package com.company.inlineeditingfragments.view.bookinfofragment;

import com.company.inlineeditingfragments.entity.Author;
import com.company.inlineeditingfragments.entity.Book;
import com.vaadin.flow.component.formlayout.FormLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;

@FragmentDescriptor("bookInfo-fragment.xml")
@RendererItemContainer("booksDc")
public class BookInfoFragment extends FragmentRenderer<FormLayout, Book> {

    @ViewComponent
    private CollectionLoader<Author> authorsDl;

    @Subscribe
    public void onReady(ReadyEvent event) {
        authorsDl.load();
    }
}