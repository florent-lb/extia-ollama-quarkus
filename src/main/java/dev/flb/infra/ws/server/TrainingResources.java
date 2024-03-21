package dev.flb.infra.ws.server;

import dev.flb.infra.ollama.ModelAdapter;
import dev.flb.service.TrainingService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("/training")
@RequiredArgsConstructor
public class TrainingResources {

    private final TrainingService trainingService;

    @POST
    @Path("rust")
    public void trainRust() {
        trainingService.trainRust();
    }

    @POST
    @Path("me")
    public void trainOnMe(String text) {
        trainingService.trainOnMe(text);
    }

    @POST
    @Path("employeeFile")
    @Consumes("application/csv")
    public void loadCsv(String csv) {
        trainingService.loadEmployeeCsv(csv);
    }
}
