import { TestBed } from '@angular/core/testing';

import { DealServiceService } from './deal-service.service';

describe('DealServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DealServiceService = TestBed.get(DealServiceService);
    expect(service).toBeTruthy();
  });
});
