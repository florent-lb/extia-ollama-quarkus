package dev.flb.infra.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.inject.Singleton;

import java.util.*;
import java.util.stream.Stream;

//@Singleton
public class CustomChatMemoryProvider /*implements ChatMemoryStore*/ {

    private final Map<Object, List<ChatMessage>> internalStore;

    public CustomChatMemoryProvider() {
        this.internalStore = new HashMap<>();
    }

  //  @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        return Optional.ofNullable(this.internalStore.get(memoryId)).orElse(Collections.emptyList());
    }

  //  @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        messages = messages == null ? new ArrayList<>() : messages;
        this.internalStore.merge(memoryId, messages, (chatMessages, chatMessages2) ->
                Stream.concat(chatMessages.stream(), chatMessages2.stream()).distinct().toList());
    }

  //  @Override
    public void deleteMessages(Object memoryId) {
        var messages = this.internalStore.get(memoryId);
        if(messages != null && messages.size() > 30)
        {
            this.internalStore.replace(memoryId,messages.stream().limit(30).toList());
        }
    }
}
