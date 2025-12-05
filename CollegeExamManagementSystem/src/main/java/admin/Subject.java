package admin;


public class Subject {
        public String name;
        public String code;
        public Subject(String name, String code) {
            this.name = name;
            this.code = code;
        }
        public String GetName() {
            return this.name;
        }
        public String GetCode() {
            return this.code;
        }
}