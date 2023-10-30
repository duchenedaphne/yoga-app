
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { SessionService } from '../../../../services/session.service';

import { AuthService } from '../../../../features/auth/services/auth.service';
import { RegisterComponent } from './register.component';

import { RegisterRequest } from '../../interfaces/registerRequest.interface';

describe('LoginComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        SessionService,
        AuthService
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule
      ]
    })
      .compileComponents();
      
    authService = TestBed.inject(AuthService);
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should register user',
    inject(
      [HttpTestingController, AuthService],
      (httpMock: HttpTestingController, authService: AuthService) => {
        const registerRequest: RegisterRequest = { 
          email: 'test@studio.com', 
          firstName: 'test',
          lastName: 'register',
          password: 'password' 
        };
        
        authService.register(registerRequest).subscribe(
          
          () => {
            const mockReq = httpMock.expectOne(`${authService.pathService}/register`);

            expect(mockReq.request.method).toBe('POST');
            expect(mockReq.request.body).toBe(registerRequest);
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            mockReq.flush(registerRequest);

            httpMock.verify();
          }
        );
      }
    )
  );
});
