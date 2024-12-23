import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Interface for course management
interface CourseManagement {
    void addCourse(String courseName, String description);
    void viewCourses();
    void updateCourse(String oldName, String newName, String newDescription);
    void deleteCourse(String courseName);
}

// Superclass User
class User {
    protected String id;
    protected String password;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public boolean verifyLogin(String inputId, String inputPassword) {
        return this.id.equals(inputId) && this.password.equals(inputPassword);
    }
}

// Subclass Student inheriting User and implementing CourseManagement
class Student extends User implements CourseManagement {
    private final List<Course> enrolledCourses = new ArrayList<>();
    private final Map<String, List<String>> feedbackMap = new HashMap<>();

    public Student(String id, String password) {
        super(id, password);
    }

    @Override
    public void addCourse(String courseName, String description) {
        enrolledCourses.add(new Course(courseName, description, LocalDate.now()));
        System.out.println("Successfully enrolled in " + courseName + ".");
    }

    @Override
    public void viewCourses() {
        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled yet.");
        } else {
            System.out.println("Enrolled Courses:");
            for (Course course : enrolledCourses) {
                System.out.println(course);
            }
        }
    }

    @Override
    public void updateCourse(String oldName, String newName, String newDescription) {
        for (Course course : enrolledCourses) {
            if (course.getCourseName().equalsIgnoreCase(oldName)) {
                course.setCourseName(newName);
                course.setDescription(newDescription);
                System.out.println("Course updated successfully.");
                return;
            }
        }
        System.out.println("Course not found.");
    }

    @Override
    public void deleteCourse(String courseName) {
        enrolledCourses.removeIf(course -> course.getCourseName().equalsIgnoreCase(courseName));
        System.out.println("Course deleted successfully.");
    }

    @SuppressWarnings("unused")
    public void giveFeedback(String courseName, String feedback) {
        feedbackMap.computeIfAbsent(courseName, Key -> new ArrayList<>()).add(feedback);
        System.out.println("Feedback submitted for " + courseName + ".");
    }

    public void viewFeedback(String courseName) {
        List<String> feedbacks = feedbackMap.get(courseName);
        if (feedbacks == null || feedbacks.isEmpty()) {
            System.out.println("No feedback available for this course.");
        } else {
            System.out.println("Feedback for " + courseName + ":");
            feedbacks.forEach(System.out::println);
        }
    }
}

// Class for Course
class Course {
    private String courseName;
    private String description;
    private final LocalDate enrollmentDate;

    public Course(String courseName, String description, LocalDate enrollmentDate) {
        this.courseName = courseName;
        this.description = description;
        this.enrollmentDate = enrollmentDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return courseName + " - " + description + " (Enrolled on: " + enrollmentDate.format(formatter) + ")";
    }
}

// Main class for program
public class StudentPortalApp {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Student student = new Student("101", "student123");

            // Login process
            boolean loggedIn = false;
            while (!loggedIn) {
                System.out.print("Enter ID: ");
                String id = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                if (student.verifyLogin(id, password)) {
                    loggedIn = true;
                    System.out.println("Login successful.");
                } else {
                    System.out.println("Invalid credentials. Please try again.");
                }
            }

            // Main menu
            int choice;
            do {
                System.out.println("\n************ Student Portal ************");
                System.out.println("1. View Courses");
                System.out.println("2. Enroll in Course");
                System.out.println("3. Update Course");
                System.out.println("4. Delete Course");
                System.out.println("5. Give Feedback");
                System.out.println("6. View Feedback");
                System.out.println("7. Logout");
                System.out.print("Enter your choice: ");

                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> student.viewCourses();
                    case 2 -> {
                        System.out.print("Enter course name: ");
                        String courseName = scanner.nextLine();
                        System.out.print("Enter course description: ");
                        String description = scanner.nextLine();
                        student.addCourse(courseName, description);
                    }
                    case 3 -> {
                        System.out.print("Enter current course name: ");
                        String oldName = scanner.nextLine();
                        System.out.print("Enter new course name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new course description: ");
                        String newDescription = scanner.nextLine();
                        student.updateCourse(oldName, newName, newDescription);
                    }
                    case 4 -> {
                        System.out.print("Enter course name to delete: ");
                        String courseName = scanner.nextLine();
                        student.deleteCourse(courseName);
                    }
                    case 5 -> {
                        System.out.print("Enter course name for feedback: ");
                        String courseName = scanner.nextLine();
                        System.out.print("Enter your feedback: ");
                        String feedback = scanner.nextLine();
                        student.giveFeedback(courseName, feedback);
                    }
                    case 6 -> {
                        System.out.print("Enter course name to view feedback: ");
                        String courseName = scanner.nextLine();
                        student.viewFeedback(courseName);
                    }
                    case 7 -> System.out.println("Logging out...");
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 7);
        }
    }
}
