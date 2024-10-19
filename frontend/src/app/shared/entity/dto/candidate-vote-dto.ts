export class CandidateVoteDto {
    candidateId: number;
    points: number;

    constructor(candidateId: number, points: number) {
        this.candidateId = candidateId;
        this.points = points;
    }
}
