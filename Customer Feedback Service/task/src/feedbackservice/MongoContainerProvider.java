package feedbackservice;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

@Component
class MongoContainerProvider {
    private final MongoDBContainer container;

    public MongoContainerProvider(@Value("${mongodb.image}") String mongodbImage) {
        try {
            container = new MongoDBContainer(mongodbImage); // image name
            container.withCreateContainerCmdModifier(cmd -> cmd.withName("feedback-service")); // container name
            container.addEnv("MONGO_INITDB_DATABASE", "feedback_db"); // init database
            container.setPortBindings(List.of("27017:27017")); // expose port 27017
            container.start();
        } catch (Exception e) {
            System.err.println("Failed to start MongoDB container: " + e.getMessage());
            throw e;
        }
    }

    @PreDestroy
    public void tearDown() {
        container.stop();
    }
}