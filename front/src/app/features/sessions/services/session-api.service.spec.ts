
import { HttpClientModule } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let sessionApiService: SessionApiService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers:[
        SessionApiService
      ],
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    sessionApiService = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(sessionApiService).toBeTruthy();
  });

  it('should show sessions list',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {
        
        sessionApiService.all().subscribe(
          
          (sessions: Session[]) => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}`);

            expect(mockReq.request.method).toBe('GET');
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            httpMock.verify();
          }
        );
      }
    )
  );

  it('should show session detail',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {
        let id: string = '1';
        
        sessionApiService.detail(id).subscribe(
          
          (session: Session) => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}/${id}`);

            expect(mockReq.request.method).toBe('GET');
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            httpMock.verify();
          }
        );
      }
    )
  );

  it('should delete session',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {

        let id: string = '1';
        
        sessionApiService.delete(id).subscribe(
          
          (any) => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}/${id}`);

            expect(mockReq.request.method).toBe('DELETE');
            expect(mockReq.cancelled).toBeFalsy();
            
            httpMock.verify();
          }
        );
      }
    )
  );

  it('should create session',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {

        const sessionTest: Session = {
          name: 'session 1',
          description: 'nouvelle session test',
          date: new Date(),
          teacher_id: 1,
          users: []
        };
        
        sessionApiService.create(sessionTest).subscribe(
          
          (session: Session) => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}`);

            expect(mockReq.request.method).toBe('POST');
            expect(mockReq.request.body).toBe(sessionTest);
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            mockReq.flush(sessionTest);

            httpMock.verify();
          }
        );
      }
    )
  );

  it('should update session',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {

        const sessionTest: Session = {
          name: 'session test',
          description: 'nouvelle session test',
          date: new Date(),
          teacher_id: 1,
          users: []
        };
        const id: string = '1';
        
        sessionApiService.update(id, sessionTest).subscribe(
          
          (session: Session) => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}/${id}`);

            expect(mockReq.request.method).toBe('PUT');
            expect(mockReq.request.body).toBe(sessionTest);
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            mockReq.flush(sessionTest);

            httpMock.verify();
          }
        );
      }
    )
  );

  it('should participate to a session',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {

        let id: string = '1';
        let userId: string = '1';
        
        sessionApiService.participate(id, userId).subscribe(
          
          () => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}/${id}/participate/${userId}`);

            expect(mockReq.request.method).toBe('POST');
            expect(mockReq.cancelled).toBeFalsy();
            
            httpMock.verify();
          }
        );
      }
    )
  );

  it('should unparticipate to a session',
    inject(
      [HttpTestingController, SessionApiService],
      (httpMock: HttpTestingController, sessionApiService: SessionApiService) => {

        let id: string = '1';
        let userId: string = '1';
        
        sessionApiService.unParticipate(id, userId).subscribe(
          
          () => {
            const mockReq = httpMock.expectOne(`${sessionApiService.pathService}/${id}/participate/${userId}`);

            expect(mockReq.request.method).toBe('DELETE');
            expect(mockReq.cancelled).toBeFalsy();
            
            httpMock.verify();
          }
        );
      }
    )
  );
});
