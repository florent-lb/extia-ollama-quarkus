package dev.flb.infra.ollama;

import dev.flb.infra.redis.RedisRetrievalAugmentor;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

import java.util.UUID;

@RegisterAiService(
        retrievalAugmentor = RedisRetrievalAugmentor.class
)
public interface ModelWithRustDocAdapter {

    @SystemMessage("""
            You answer in French Only.
            You are a rust developer.
            Your responses must be short and neutral.
            You can generate code snippet if needed but it will enclosed by html tag <code> you'll will not use markdown or back quote in the answer.
            If you don't know the response do not try to create one and tell us you don't known.
            """)
    String askSomethingAboutRust(@MemoryId UUID memoryId, @UserMessage String question);

    @SystemMessage("""
            You answer in French Only.
            You are a manager.
            Your responses must be short and neutral.
            You can use the source EMPLOYEE as data source for employee
            If you don't know the response do not try to create one and tell us you don't known.
             """)
    String askSomethingAboutEmployee(@MemoryId UUID memoryId, @UserMessage String question);

    @SystemMessage("""
            You answer in French Only.
            You are a manager.
            Your responses must be short and neutral.
            If you don't know the response do not try to create one and tell us you don't known.
            The country must be equals to {{country}}, the case can be ignore
            Only Embedding with metadata header source and value EMPLOYEE can be use  
             """)
    @UserMessage("""
            Donne moi la liste des employee résidant en {{country}} uniquement
            Chaque employé est une entrée d'un tableau en format JSON.
            Ta réponse est sous forme de tableau JSON uniquement.
            Ce tableau doit contenir les champs de metadata suicant : {{headers}}
            Chaque Employee correspondant à la recherche est un objet en JSON.
            
            N'échappe pas les charactères spéciaux dans les metadata
            
            Chaque JSON doit etre conforme à la RFC 8259
            """)
    String listOfEmployeeInCountry(@MemoryId UUID memoryId, @V("country") String country, @V("headers") String headers);


}
