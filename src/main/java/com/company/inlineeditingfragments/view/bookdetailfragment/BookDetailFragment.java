package com.company.inlineeditingfragments.view.bookdetailfragment;

import com.company.inlineeditingfragments.entity.Book;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;

@FragmentDescriptor("bookDetail-fragment.xml")
@RendererItemContainer("booksDc")
public class BookDetailFragment extends FragmentRenderer<FormLayout, Book> {
}