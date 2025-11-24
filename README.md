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
CollegeExamManagementSystem/
│
├─ src/
│   ├─ main/
│   │   ├─ java/
│   │   │   ├─ admin/            
│   │   │   │    ├─ Admin.java
│   │   │   │    ├─ UserManagement.java
│   │   │   │    ├─ SubjectManagement.java
│   │   │   │    └─ GradeApproval.java
│   │   │   │
│   │   │   ├─ lecturer/          
│   │   │   │    ├─ Lecturer.java
│   │   │   │    ├─ ExamManagement.java
│   │   │   │    └─ Reporting.java
│   │   │   │
│   │   │   ├─ student/          
│   │   │   │    ├─ Student.java
│   │   │   │    ├─ ExamAccess.java
│   │   │   │    └─ Feedback.java
│   │   │   │
│   │   │   ├─ user/             
│   │   │   │    ├─ User.java
│   │   │   │    ├─ Authentication.java
│   │   │   │    └─ UpdateProfile.java
│   │   │   │
│   │   │   ├─ util/              
│   │   │   │    ├─ FileHandler.java   
│   │   │   │    └─Validator.java     
│   │   │   │          
│   │   │   │
│   │   │   └─ Main.java           
│   │   │
│   │   └─ resources/
│   │        ├─ fxml/             
│   │        │    ├─ Login.fxml
│   │        │    ├─ AdminDashboard.fxml
│   │        │    ├─ LecturerDashboard.fxml
│   │        │    └─ StudentDashboard.fxml
│   │        │
│   │        └─ styles/           
│   │             └─ style.css
│   │
│   └─ test/                     
│
├─ data/                          
│    ├─ users.txt
│    ├─ students.txt
│    ├─ lecturers.txt
│    ├─ subjects.txt
│    ├─ exams.txt
│    ├─ grades.txt
│    └─ feedback.txt
│
├─ docs/ 
│
├─ .gitignore
├─ README.md
└─ build.gradle / pom.xml  
```
