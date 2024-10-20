import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MultivalueVoteComponent } from './multivalue-vote.component';

describe('MultivalueVoteComponent', () => {
  let component: MultivalueVoteComponent;
  let fixture: ComponentFixture<MultivalueVoteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MultivalueVoteComponent]
    });
    fixture = TestBed.createComponent(MultivalueVoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
