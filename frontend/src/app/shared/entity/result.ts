export class Result {
  firstname: string;
  lastname: string;
  grade: string;
  percentage: number;

  constructor(firstname: string, lastname: string, grade: string, percentage: number) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.grade = grade;
    this.percentage = percentage;
  }
}
