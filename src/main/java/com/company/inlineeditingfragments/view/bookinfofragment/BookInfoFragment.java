package com.company.inlineeditingfragments.view.bookinfofragment;

import com.company.inlineeditingfragments.entity.Book;
import com.vaadin.flow.component.formlayout.FormLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;

@FragmentDescriptor("bookInfo-fragment.xml")
@RendererItemContainer("booksDc")
public class BookInfoFragment extends FragmentRenderer<FormLayout, Book> {
}