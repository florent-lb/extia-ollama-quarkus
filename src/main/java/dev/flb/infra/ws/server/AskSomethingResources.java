package dev.flb.infra.ws.server;

import dev.flb.domain.infra.CsvExtractor;
import dev.flb.infra.ollama.ModelAdapter;
import dev.flb.infra.ollama.ModelWithRustDocAdapter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@Path("/ask")
@RequiredArgsConstructor
@ApplicationScoped
public class AskSomethingResources {

    private final UUID rustSessionId = UUID.randomUUID();
    private final UUID managerSessionId = UUID.randomUUID();

    private final ModelAdapter modelPort;
    private final ModelWithRustDocAdapter modelWithRustPort;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String askSomething(String question) {
        return modelPort.askSomething(question);
    }

    @POST
    @Path("withStore")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String aboutWithStore(String question) {
        return modelWithRustPort.askSomethingAboutRust(rustSessionId,question);
    }

    @POST
    @Path("aboutEmployee")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String aboutEmploteeWithStore(String question) {
        return modelWithRustPort.askSomethingAboutEmployee(managerSessionId,question);
    }
    @POST
    @Path("employee/{country}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String employeeInCountry(@PathParam("country") String country) {
        return modelWithRustPort.listOfEmployeeInCountry(managerSessionId,country, Arrays.toString(CsvExtractor.HeadersFromType.EMPLOYEE.getHeaders()));
    }

}
