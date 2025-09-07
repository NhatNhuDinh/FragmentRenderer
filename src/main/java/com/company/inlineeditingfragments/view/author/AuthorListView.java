package com.company.inlineeditingfragments.view.author;

import com.company.inlineeditingfragments.entity.Author;
import com.company.inlineeditingfragments.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "authors", layout = MainView.class)
@ViewController(id = "Author.list")
@ViewDescriptor(path = "author-list-view.xml")
@LookupComponent("authorsDataGrid")
@DialogMode(width = "64em")
public class AuthorListView extends StandardListView<Author> {
}