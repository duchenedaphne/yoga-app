
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
import { LoginComponent } from './login.component';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { SessionInformation } from '../../../../interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let authService: AuthService;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
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
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should log user',
    inject(
      [HttpTestingController, AuthService],
      (httpMock: HttpTestingController, authService: AuthService) => {
        const loginRequest: LoginRequest = { 
          email: 'yoga@studio.com', 
          password: 'test!1234' 
        };

        authService.login(loginRequest).subscribe(

          (sessionInformation: SessionInformation) => {

            const mockReq = httpMock.expectOne(`${authService.pathService}/login`);

            expect(mockReq.request.method).toBe('POST');
            expect(mockReq.request.body).toBe(loginRequest);
            expect(mockReq.cancelled).toBeFalsy();
            expect(mockReq.request.responseType).toEqual('json');

            mockReq.flush(loginRequest);

            httpMock.verify();
          }
        );
      }
    )
  );
});
