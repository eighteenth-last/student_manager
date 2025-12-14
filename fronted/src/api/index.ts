import http from './http';

// Auth
export function loginApi(username: string, password: string) {
  return http.post('/auth/login', { username, password });
}
export function resetPasswordApi(username: string, newPassword: string) {
  return http.post('/auth/reset-password', { username, newPassword });
}

// Student
export function fetchStudents(page = 1, size = 20) {
  return http.get('/students', { params: { page, size } });
}
export function fetchStudent(id: number) {
  return http.get(`/students/${id}`);
}
export function fetchMeStudent() {
  return http.get('/students/me');
}
export function createStudent(payload: any) {
  return http.post('/students', payload);
}
export function updateStudent(id: number, payload: any) {
  return http.put(`/students/${id}`, payload);
}
export function deleteStudent(id: number) {
  return http.delete(`/students/${id}`);
}

// Teacher
export function fetchTeachers(page = 1, size = 20) {
  return http.get('/teachers', { params: { page, size } });
}
export function fetchTeacher(id: number) {
  return http.get(`/teachers/${id}`);
}
export function fetchMeTeacher() {
  return http.get('/teachers/me');
}
export function createTeacher(payload: any) {
  return http.post('/teachers', payload);
}
export function updateTeacher(id: number, payload: any) {
  return http.put(`/teachers/${id}`, payload);
}
export function deleteTeacher(id: number) {
  return http.delete(`/teachers/${id}`);
}

// Class
export function fetchClasses(page = 1, size = 20) {
  return http.get('/classes', { params: { page, size } });
}
export function createClass(payload: any) {
  return http.post('/classes', payload);
}
export function updateClass(id: number, payload: any) {
  return http.put(`/classes/${id}`, payload);
}
export function deleteClass(id: number) {
  return http.delete(`/classes/${id}`);
}

// Course
export function fetchCourses(page = 1, size = 20) {
  return http.get('/courses', { params: { page, size } });
}
export function fetchCourse(id: number) {
  return http.get(`/courses/${id}`);
}
export function fetchMyTeaching() {
  return http.get('/courses/my-teaching');
}
export function createCourse(payload: any) {
  return http.post('/courses', payload);
}
export function updateCourse(id: number, payload: any) {
  return http.put(`/courses/${id}`, payload);
}
export function deleteCourse(id: number) {
  return http.delete(`/courses/${id}`);
}

// CourseOffering
export function fetchCourseOfferings(page = 1, size = 20) {
  return http.get('/course-offerings', { params: { page, size } });
}
export function fetchCourseOfferingsBySemester(semester: string) {
  return http.get(`/course-offerings/semester/${semester}`);
}
export function fetchOpenCourseOfferings(semester = '2025-1') {
  return http.get('/course-offerings/open', { params: { semester } });
}
export function fetchMyTeachingOfferings() {
  return http.get('/course-offerings/my-teaching');
}
export function fetchCourseOffering(id: number) {
  return http.get(`/course-offerings/${id}`);
}
export function createCourseOffering(payload: any) {
  return http.post('/course-offerings', payload);
}
export function updateCourseOffering(id: number, payload: any) {
  return http.put(`/course-offerings/${id}`, payload);
}
export function deleteCourseOffering(id: number) {
  return http.delete(`/course-offerings/${id}`);
}

// Enrollment
export function enrollCourse(courseId: number) {
  return http.post('/enrollments', { courseId });
}
export function fetchMyEnrollments() {
  return http.get('/enrollments/my');
}
export function dropEnrollment(id: number) {
  return http.delete(`/enrollments/${id}`);
}
export function fetchEnrollmentsByCourse(courseId: number) {
  return http.get(`/enrollments/by-course/${courseId}`);
}

// Score
export function recordScore(payload: { studentId: number; courseId: number; score: number }) {
  return http.post('/scores', payload);
}
export function fetchMyScores() {
  return http.get('/scores/my');
}
export function fetchScoresByCourse(courseId: number) {
  return http.get(`/scores/by-course/${courseId}`);
}

// Department
export function fetchDepartments() {
  return http.get('/departments');
}
export function createDepartment(payload: any) {
  return http.post('/departments', payload);
}
export function updateDepartment(id: number, payload: any) {
  return http.put(`/departments/${id}`, payload);
}
export function deleteDepartment(id: number) {
  return http.delete(`/departments/${id}`);
}

// Major
export function fetchMajors(departmentId?: number) {
  return http.get('/majors', { params: departmentId ? { departmentId } : {} });
}
export function createMajor(payload: any) {
  return http.post('/majors', payload);
}
export function updateMajor(id: number, payload: any) {
  return http.put(`/majors/${id}`, payload);
}
export function deleteMajor(id: number) {
  return http.delete(`/majors/${id}`);
}
