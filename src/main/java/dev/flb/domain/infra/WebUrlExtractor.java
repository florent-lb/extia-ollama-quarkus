package dev.flb.domain.infra;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.document.transformer.HtmlTextExtractor;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkiverse.langchain4j.redis.RedisEmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

@ApplicationScoped
public class WebUrlExtractor {

    @Inject
    RedisEmbeddingStore redisEmbeddingStore;
    @Inject
    EmbeddingModel embeddingModel;

    public void ingest(String path) {
        URI uri;
        try {
            uri = new URI(path);

            HtmlTextExtractor extractor = new HtmlTextExtractor(".page"
                    , Map.of(
                    "title", "title",
                    "code_snippet","code"
            )
                    , false
            );


            var printedRustDoc = new String(((InputStream) URL.of(uri, null).getContent()).readAllBytes());

            Document document = Document.document(printedRustDoc);

            var splitter = DocumentSplitters.recursive(200, 50);

            EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                    .embeddingStore(redisEmbeddingStore)
                    .embeddingModel(embeddingModel)
                    .documentTransformer(extractor)
                    .documentSplitter(splitter)
                    .build();

            ingestor.ingest(document);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
