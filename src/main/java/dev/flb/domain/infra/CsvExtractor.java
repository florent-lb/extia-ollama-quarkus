package dev.flb.domain.infra;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequestScoped
public class CsvExtractor {

    @Inject
    RedisEmbeddingStore redisEmbeddingStore;
    @Inject
    EmbeddingModel embeddingModel;

    public void ingest(String csv, HeadersFromType type) {

        CSVFormat csvFormat = getFormat(type);
        try (Reader reader = new StringReader(csv)) {
            AtomicInteger i = new AtomicInteger(1);
            List<Document> documents = csvFormat.parse(reader).stream()
                    .map(record -> {
                        Map<String, String> metadata = new HashMap<>();
                        metadata.put("source", type.name());
                        metadata.put("row", String.valueOf(i.getAndIncrement()));

                        StringBuilder content = new StringBuilder();
                        Arrays.stream(type.headers)
                                .forEach(header -> {
                                    metadata.put(header, record.get(header));
                                    content.append(record.get(header)).append(csvFormat.getDelimiterString());
                                });
                        content.deleteCharAt(content.length() - 1);//remove uselessend delimiter
                        content.append(csvFormat.getRecordSeparator());
                        return new Document(content.toString(), Metadata.from(metadata));
                    })
                    .toList();

            var splitter = DocumentSplitters.recursive(200, 50);

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .embeddingStore(redisEmbeddingStore)
                    .embeddingModel(embeddingModel)
                    .documentSplitter(splitter)
                    .build();

            ingestor.ingest(documents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CSVFormat getFormat(HeadersFromType type) {
        return CSVFormat.EXCEL.builder()
                .setHeader(type.headers)
                .build();
    }

    @RequiredArgsConstructor
    @Getter
    public enum HeadersFromType {
        EMPLOYEE(new String[]{"name", "address", "country", "age","employee_id"});

        private final String[] headers;
    }
}
