package com.company.inlineeditingfragments.view.author;

import com.company.inlineeditingfragments.entity.Author;
import com.company.inlineeditingfragments.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "authors/:id", layout = MainView.class)
@ViewController(id = "Author.detail")
@ViewDescriptor(path = "author-detail-view.xml")
@EditedEntityContainer("authorDc")
public class AuthorDetailView extends StandardDetailView<Author> {
}