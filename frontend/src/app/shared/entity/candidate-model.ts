export class Candidate {
  //<editor-fold desc="Fields">
  id: number | null
  schoolId: string
  firstName: string
  lastName: string
  grade: string
  candidates: Candidate[] = []
  //</editor-fold>

  //<editor-fold desc="Constructors">
  constructor(
    id: number | null = null,
    schoolId: string = "",
    firstName: string = "",
    lastName: string = "",
    grade: string = ""
  ) {
    this.id = id;
    this.schoolId = schoolId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }
}
