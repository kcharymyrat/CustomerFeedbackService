package feedbackservice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class FeedbackController {
    private final FeedbackRepository repository;

    @Autowired
    public FeedbackController(FeedbackRepository repository) {
        this.repository = repository;
        this.repository.deleteAll();
    }

    @PostMapping(path = "/feedback")
    public ResponseEntity<Void> createFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = repository.save(feedback);
        URI location = URI.create(String.format("/feedback/%s", savedFeedback.getId()));

        return ResponseEntity.created(location).build();
    }

}
