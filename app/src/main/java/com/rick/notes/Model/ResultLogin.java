package com.rick.notes.Model;

public class ResultLogin {
        private String success;
        private String message;
        private String username;

        public ResultLogin(String success, String message, String username) {
            this.success = success;
            this.message = message;
            this.username = username;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
}
