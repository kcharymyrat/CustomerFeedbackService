package feedbackservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping(path="/feedback", produces = "application/json")
    public ResponseEntity<FeedbackPaginatedResponse> getFeedbackList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) String vendor
    ) {

        page = Math.max(page, 1);
        if (perPage < 5 || perPage > 20) {
            perPage = 10;
        }

        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, perPage, sort);

        Feedback probe = new Feedback();
        if (rating != null) probe.setRating(rating);
        if (customer != null) probe.setCustomer(customer);
        if (product != null) probe.setProduct(product);
        if (vendor != null) probe.setVendor(vendor);

        Example<Feedback> example = Example.of(probe);

        Page<Feedback> feedbackPage = repository.findAll(example, pageable);
        FeedbackPaginatedResponse response = new FeedbackPaginatedResponse(
                feedbackPage.getTotalElements(),
                feedbackPage.isFirst(),
                feedbackPage.isLast(),
                feedbackPage.getContent()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping(path ="/feedback/{id}", produces = "application/json")
    public ResponseEntity<Feedback> getFeedback(@PathVariable String id) {
        var feedback = repository.findById(id);
        if (feedback.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.of(feedback);
    }

}
