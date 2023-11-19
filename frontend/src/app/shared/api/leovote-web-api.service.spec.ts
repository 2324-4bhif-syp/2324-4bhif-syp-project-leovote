import { TestBed } from '@angular/core/testing';

import { LeovoteWebApiService } from './leovote-web-api.service';

describe('LeovoteWebApiService', () => {
  let service: LeovoteWebApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LeovoteWebApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
