package dev.flb.infra.ollama;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
@RegisterAiService
public interface ModelAdapter {
   String askSomething(@UserMessage String question);


}
