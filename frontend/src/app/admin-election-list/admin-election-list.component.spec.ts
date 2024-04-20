import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminElectionListComponent } from './admin-election-list.component';

describe('AdminElectionListComponent', () => {
  let component: AdminElectionListComponent;
  let fixture: ComponentFixture<AdminElectionListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminElectionListComponent]
    });
    fixture = TestBed.createComponent(AdminElectionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
