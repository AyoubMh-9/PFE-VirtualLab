import { TestBed } from '@angular/core/testing';

import { UploadedFileService } from '../services/uploaded-file.service';

describe('UploadedFileService', () => {
  let service: UploadedFileService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UploadedFileService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
