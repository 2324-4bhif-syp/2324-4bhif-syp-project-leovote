import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrossVoteComponent } from './cross-vote.component';

describe('VoteComponent', () => {
  let component: CrossVoteComponent;
  let fixture: ComponentFixture<CrossVoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CrossVoteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrossVoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
