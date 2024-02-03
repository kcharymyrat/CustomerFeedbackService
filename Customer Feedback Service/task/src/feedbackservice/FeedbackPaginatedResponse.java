package feedbackservice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FeedbackPaginatedResponse {
    @JsonProperty("total_documents")
    private long totalDocuments;

    @JsonProperty("is_first_page")
    private boolean isFirstPage;

    @JsonProperty("is_last_page")
    private boolean isLastPage;

    @JsonProperty("documents")
    private List<Feedback> documents;

    public FeedbackPaginatedResponse(long totalDocuments, boolean isFirstPage, boolean isLastPage, List<Feedback> documents) {
        this.totalDocuments = totalDocuments;
        this.isFirstPage = isFirstPage;
        this.isLastPage = isLastPage;
        this.documents = documents;
    }
}