export class Candidate{
  id: number | null
  schoolId: string
  firstName:string
  lastName:string
  grade:string

  constructor(id: number | null, schoolId: string, firstName: string, lastName: string, grade: string) {
    this.id = id;
    this.schoolId = schoolId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }
}
