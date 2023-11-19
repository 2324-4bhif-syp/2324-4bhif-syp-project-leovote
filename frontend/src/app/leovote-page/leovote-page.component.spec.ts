import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeovotePageComponent } from './leovote-page.component';

describe('LeovotePageComponent', () => {
  let component: LeovotePageComponent;
  let fixture: ComponentFixture<LeovotePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeovotePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LeovotePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
