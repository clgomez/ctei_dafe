import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChangePasswordComponent } from './change-password.component'; 
import { EmailPasswordService } from '@app/Services/email.service'; 
import { Router, ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ChangePasswordDTO } from '../models/change-password-dto';

describe('Test Change password (Cambiar contraseña)', () => {

  let component: ChangePasswordComponent;
  let fixture: ComponentFixture<ChangePasswordComponent>;
  let emailServiceSpy: jasmine.SpyObj<EmailPasswordService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {

    emailServiceSpy = jasmine.createSpyObj('EmailPasswordService', ['changePassword']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [ChangePasswordComponent],
      imports: [FormsModule],
      providers: [
        { provide: EmailPasswordService, useValue: emailServiceSpy },
        { provide: Router, useValue: routerSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({ tokenPassword: 'token123' }))
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('1. Cambio de contraseña exitoso', () => {

    const responseMock = {
      mensaje: 'Contraseña restablecida correctamente'
    };

    emailServiceSpy.changePassword.and.returnValue(of(responseMock));

    component.password = '123456';
    component.confirmPassword = '123456';
    component.tokenPassword = 'token123';

    component.onChangePassword();

    expect(emailServiceSpy.changePassword)
      .toHaveBeenCalledWith(new ChangePasswordDTO('123456', '123456', 'token123'));

    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);

  });

    it('2. Token inválido', () => {

    const errorMock = {
        error: {
        mensaje: 'El token es inválido o ha expirado'
        }
    };

    emailServiceSpy.changePassword.and.returnValue(
        throwError(() => errorMock)
    );

    component.password = '123456';
    component.confirmPassword = '123456';
    component.tokenPassword = 'token_invalido';

    component.onChangePassword();

    expect(emailServiceSpy.changePassword)
        .toHaveBeenCalledWith(new ChangePasswordDTO('123456','123456','token_invalido'));

    expect(routerSpy.navigate)
        .toHaveBeenCalledWith(['/email/send-email']);

    });

    it('3. Error en el servidor', () => {

      const serverError = {
        status: 500
      };

      emailServiceSpy.changePassword.and.returnValue(
        throwError(() => serverError)
      );

      component.password = '123456';
      component.confirmPassword = '123456';
      component.tokenPassword = 'token123';

      component.onChangePassword();

      expect(emailServiceSpy.changePassword)
        .toHaveBeenCalledWith(new ChangePasswordDTO('123456','123456','token123'));

      expect(component.message)
        .toBe('Ocurrió un problema técnico en el servidor. Intente nuevamente más tarde o contacte al soporte.');

      expect(routerSpy.navigate).not.toHaveBeenCalled();

    });

});