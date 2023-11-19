export class Candidate{
  public schoolId: string
  public firstName:string
  public lastName:string
  public grade:string


  constructor(schoolId: string, firstName: string, lastName: string, grade: string) {
    this.schoolId = schoolId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }
}
