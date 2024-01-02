export class Candidate {
  //<editor-fold desc="Fields">
  schoolId: string;
  firstName: string;
  lastName: string;
  grade: string;
  id: number | null;
   //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    schoolId: string = "",
    firstName: string = "",
    lastName: string = "",
    grade: string = "",
    id: number | null = null,
  ) {
    this.id = id;
    this.schoolId = schoolId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }
}
