package advisor.view;

import advisor.Pagination;

import java.util.List;

public class CategoriesPrinter implements Printer {
    @Override
    public void printContent(List content) {
        System.out.println("---CATEGORIES---");
        ((List<String>) content).forEach(System.out::println);
        System.out.println("---PAGE " + Pagination.getCurrentPage() + " OF " + Pagination.getLastPage() + "---");
    }
}
