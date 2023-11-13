export class Candidate{
  schoolId: string
  firstName:string
  lastName:string
  grade:string


  constructor(schoolId: string, firstName: string, lastName: string, grade: string) {
    this.schoolId = schoolId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }
}
