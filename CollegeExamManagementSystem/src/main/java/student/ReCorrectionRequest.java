package student;

import util.FileHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReCorrectionRequest {

    private static final String REQUESTS_FILE = "re_correction_requests.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }

    private String requestId;
    private String studentId;
    private String examId;
    private String gradeId;
    private String reason;
    private RequestStatus status;
    private String timestamp;
    private String adminComment;

    public ReCorrectionRequest(String requestId, String studentId, String examId, String gradeId,
                                String reason, RequestStatus status, String timestamp, String adminComment) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.examId = examId;
        this.gradeId = gradeId;
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
        this.adminComment = adminComment;
    }

    // Getters and setters
    public String getRequestId() { return requestId; }
    public String getStudentId() { return studentId; }
    public String getExamId() { return examId; }
    public String getGradeId() { return gradeId; }
    public String getReason() { return reason; }
    public RequestStatus getStatus() { return status; }
    public String getTimestamp() { return timestamp; }
    public String getAdminComment() { return adminComment; }
    
    public void setStatus(RequestStatus status) { this.status = status; }
    public void setAdminComment(String adminComment) { this.adminComment = adminComment; }

    /**
     * Submit a new re-correction request
     */
    public static String submitRequest(String studentId, String examId, String gradeId, String reason) {
        List<ReCorrectionRequest> requests = getAllRequests();
        
        // Generate new request ID
        int maxId = 0;
        for (ReCorrectionRequest req : requests) {
            try {
                int id = Integer.parseInt(req.getRequestId());
                if (id > maxId) maxId = id;
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        String newId = String.valueOf(maxId + 1);
        
        // Create new request
        String timestamp = LocalDateTime.now().format(FORMATTER);
        ReCorrectionRequest newRequest = new ReCorrectionRequest(
            newId, studentId, examId, gradeId, reason, RequestStatus.PENDING, timestamp, ""
        );
        
        requests.add(newRequest);
        saveAllRequests(requests);
        
        return newId;
    }

    /**
     * Get all re-correction requests
     */
    public static List<ReCorrectionRequest> getAllRequests() {
        List<ReCorrectionRequest> requests = new ArrayList<>();
        List<String> lines = FileHandler.readAllLines(REQUESTS_FILE);
        
        for (String line : lines) {
            String[] parts = line.split("\\|", 8);
            if (parts.length >= 7) {
                try {
                    String requestId = parts[0];
                    String studentId = parts[1];
                    String examId = parts[2];
                    String gradeId = parts[3];
                    String reason = parts[4];
                    RequestStatus status = RequestStatus.valueOf(parts[5]);
                    String timestamp = parts[6];
                    String adminComment = parts.length == 8 ? parts[7] : "";
                    
                    requests.add(new ReCorrectionRequest(
                        requestId, studentId, examId, gradeId, reason, status, timestamp, adminComment
                    ));
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parsing request line: " + line);
                }
            }
        }
        
        return requests;
    }

    /**
     * Get requests for a specific student
     */
    public static List<ReCorrectionRequest> getRequestsByStudent(String studentId) {
        return getAllRequests().stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    /**
     * Get pending requests
     */
    public static List<ReCorrectionRequest> getPendingRequests() {
        return getAllRequests().stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    /**
     * Update a request status
     */
    public static void updateRequest(String requestId, RequestStatus newStatus, String adminComment) {
        List<ReCorrectionRequest> requests = getAllRequests();
        
        for (ReCorrectionRequest req : requests) {
            if (req.getRequestId().equals(requestId)) {
                req.setStatus(newStatus);
                req.setAdminComment(adminComment);
                break;
            }
        }
        
        saveAllRequests(requests);
    }

    /**
     * Save all requests to file
     */
    private static void saveAllRequests(List<ReCorrectionRequest> requests) {
        List<String> lines = requests.stream()
                .map(req -> req.toFileFormat())
                .collect(Collectors.toList());
        FileHandler.writeAllLines(REQUESTS_FILE, lines);
    }

    /**
     * Format request for file storage
     */
    private String toFileFormat() {
        return String.join("|",
                requestId,
                studentId,
                examId,
                gradeId,
                reason.replace("|", ""),
                status.name(),
                timestamp,
                adminComment.replace("|", "")
        );
    }
}
