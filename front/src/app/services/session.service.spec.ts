import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers:[
        SessionService
      ],
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should log the user', () => {
    let user: SessionInformation = {
      token: '12345',
      type: 'admin',
      id: 1,
      username: 'yoga@studio.com',
      firstName: 'yoga',
      lastName: 'studio',
      admin: true
    }
    sessionService.logIn(user);
    expect(sessionService.isLogged).toEqual(true);
  })

  it('should logout the user', () => {
    
    sessionService.logOut();
    expect(sessionService.sessionInformation).toEqual(undefined);
  })
});
