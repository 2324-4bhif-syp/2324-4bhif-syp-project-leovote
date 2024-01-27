import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCandidateElectionComponent } from './create-candidate-election.component';

describe('CreateCandidateElectionComponent', () => {
  let component: CreateCandidateElectionComponent;
  let fixture: ComponentFixture<CreateCandidateElectionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateCandidateElectionComponent]
    });
    fixture = TestBed.createComponent(CreateCandidateElectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
