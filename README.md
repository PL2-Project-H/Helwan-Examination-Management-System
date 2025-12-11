# Helwan-Examination-Management-System
 # اللَّهُــمَّ-صَلِّ-وَسَـــلِّمْ-وَبَارِكْ-على-نَبِيِّنَـــا-مُحمَّد
# Adham
### Authentication Module
- Implement login & logout functionality
- Handle credential verification
- Connect authentication with all user roles
# Abdo - Hamza 
### User Management (Admin)
- Add, update, delete, and search for users
- Manage student and lecturer accounts
- Subject Management (Admin)
- Create and update subjects
- Assign subjects to students and lecturers
- Grades Approval (Admin)
- Approve grades submitted by lecturers
- Publish final grades to students
# Ziead - Saber
### Exam Management (Lecturer)
- Create and edit exam
- Add questions and set correct answers
- Grading System (Lecturer)
- Implement automatic grading logic
- Calculate final scores from student answers
- Results (Lecturer)
- Generate results for all students
- Provide reports and summaries
# Amr Akram - Abdo Gamal 
### Exam Access (Student)
- Allow students to access only their assigned exams
- Enforce one-time exam entry
- Result Viewing (Student)
- Display published result
- Re-Correction Requests (Student)
- Let students request grade reevaluation
- Feedback System (Student)
- Submit feedback about exams and exam experience
---
## Naming Conventions
- **Functions**: Use PascalCase (`GetUsersExample()`).  
---
##File Structure
---
```bash
CollegeExamManagementSystem
├── build.gradle
├── data
│   ├── exams.txt
│   ├── feedback.txt
│   ├── grades.txt
│   ├── lecturers.txt
│   ├── students.txt
│   ├── subjects.txt
│   └── users.txt
├── docs
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── admin
│   │   │   │   ├── Admin.java
│   │   │   │   ├── GradeApproval.java
│   │   │   │   ├── Subject.java
│   │   │   │   ├── SubjectManagement.java
│   │   │   │   └── UserManagement.java
│   │   │   ├── lecturer
│   │   │   │   ├── ExamManagement.java
│   │   │   │   ├── Lecturer.java
│   │   │   │   └── Reporting.java
│   │   │   ├── Main.java
│   │   │   ├── student
│   │   │   │   ├── ExamAccess.java
│   │   │   │   ├── Feedback.java
│   │   │   │   └── Student.java
│   │   │   ├── user
│   │   │   │   ├── Authentication.java
│   │   │   │   ├── UpdateProfile.java
│   │   │   │   └── User.java
│   │   │   └── util
│   │   │       ├── FileHandler.java
│   │   │       └── Validator.java
│   │   └── resources
│   │       ├── fxml
│   │       │   ├── AdminDashboard.fxml
│   │       │   ├── LecturerDashboard.fxml
│   │       │   ├── Login.fxml
│   │       │   └── StudentDashboard.fxml
│   │       └── styles
│   │           └── style.css
│   └── test
└── target
    ├── classes
    │   ├── admin
    │   │   ├── Admin.class
    │   │   ├── Subject.class
    │   │   ├── SubjectManagement.class
    │   │   └── UserManagement.class
    │   ├── fxml
    │   │   ├── AdminDashboard.fxml
    │   │   ├── LecturerDashboard.fxml
    │   │   ├── Login.fxml
    │   │   └── StudentDashboard.fxml
    │   ├── Main.class
    │   ├── student
    │   │   └── Student.class
    │   ├── styles
    │   │   └── style.css
    │   ├── user
    │   │   ├── Authentication$1.class
    │   │   ├── Authentication.class
    │   │   ├── User$Role.class
    │   │   └── User.class
    │   └── util
    │       ├── FileHandler.class
    │       └── Validator.class
    ├── generated-sources
    │   └── annotations
    └── maven-status
        └── maven-compiler-plugin
            └── compile
                └── default-compile
                    ├── createdFiles.lst
                    └── inputFiles.lst  
```
