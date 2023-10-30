import { HttpClientModule } from '@angular/common/http';
import { TestBed, inject } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let userService: UserService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers:[
        UserService
      ],
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    userService = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });

  it('should get user by id',
    inject(
      [HttpTestingController, UserService],
      (httpMock: HttpTestingController, userService: UserService) => {
        let id: string = '1';
        
        userService.getById(id).subscribe(
          
          (user: User) => {
            const mockReq = httpMock.expectOne(`${userService.pathService}/${id}`);

            expect(mockReq.request.method).toBe('GET');
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            httpMock.verify();
          }
        );
      }
    )
  );

  it('should delete user account',
    inject(
      [HttpTestingController, UserService],
      (httpMock: HttpTestingController, userService: UserService) => {
        let id: string = '1';
        
        userService.delete(id).subscribe(
          
          (any) => {
            const mockReq = httpMock.expectOne(`${userService.pathService}/${id}`);

            expect(mockReq.request.method).toBe('DELETE');
            expect(mockReq.cancelled).toBeFalsy();

            httpMock.verify();
          }
        );
      }
    )
  );
});
