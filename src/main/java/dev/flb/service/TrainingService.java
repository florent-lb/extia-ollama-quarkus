package dev.flb.service;

import dev.flb.domain.infra.CsvExtractor;
import dev.flb.domain.infra.TextExtractor;
import dev.flb.domain.infra.WebUrlExtractor;
import jakarta.enterprise.context.RequestScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.List;

import static dev.flb.domain.infra.CsvExtractor.HeadersFromType.EMPLOYEE;

@RequestScoped
@RequiredArgsConstructor
@Log
public class TrainingService {

    private final WebUrlExtractor webUrlExtractor;
    private final TextExtractor textExtractor;
    private final CsvExtractor csvExtractor;

    public void trainRust() {

        var paths = List.of(
                "ch00-00-introduction.html",
                "ch01-01-installation.html",
                "ch01-02-hello-world.html",
                "ch01-03-hello-cargo.html",
                "ch02-00-guessing-game-tutorial.html",
                "ch03-01-variables-and-mutability.html",
                "ch03-02-data-types.html",
                "ch03-03-how-functions-work.html",
                "ch03-04-comments.html",
                "ch03-05-control-flow.html"

        );
        var rustUrl = "https://doc.rust-lang.org/stable/book/%s";

        paths
                .forEach(s -> {
                    log.info("Ingesting %s".formatted(s));
                    webUrlExtractor.ingest(rustUrl.formatted(s));
                });


    }

    public void trainOnMe(String text) {
        textExtractor.ingest(text);
    }

    public void loadEmployeeCsv(String csv) {
        csvExtractor.ingest(csv, EMPLOYEE);
    }
}
