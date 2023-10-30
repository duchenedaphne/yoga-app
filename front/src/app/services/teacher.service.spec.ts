import { HttpClientModule } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let teacherService: TeacherService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers:[
        TeacherService
      ],
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    teacherService = TestBed.inject(TeacherService);
  });

  it('should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  it('should show the teachers list',
    inject(
      [HttpTestingController, TeacherService],
      (httpMock: HttpTestingController, teacherService: TeacherService) => {
        
        teacherService.all().subscribe(
          
          (teachers: Teacher[]) => {
            const mockReq = httpMock.expectOne(`${teacherService.pathService}`);

            expect(mockReq.request.method).toBe('GET');
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            httpMock.verify();
          }
        );
      }
    )
  );

  it('should show the teacher detail',
    inject(
      [HttpTestingController, TeacherService],
      (httpMock: HttpTestingController, teacherService: TeacherService) => {
        let id: string = '1';
        
        teacherService.detail(id).subscribe(
          
          (teacher: Teacher) => {
            const mockReq = httpMock.expectOne(`${teacherService.pathService}/${id}`);

            expect(mockReq.request.method).toBe('GET');
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            httpMock.verify();
          }
        );
      }
    )
  );
});
