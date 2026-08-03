import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SendEmailComponent } from './send-email.component';
import { EmailPasswordService } from '@app/Services/email.service';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { EmailValuesDTO } from '../models/email-values-dto';

describe('Test Send Email (Recuperar Contraseña) ', () => {

  let component: SendEmailComponent;
  let fixture: ComponentFixture<SendEmailComponent>;
  let emailServiceSpy: jasmine.SpyObj<EmailPasswordService>;

  beforeEach(async () => {

    emailServiceSpy = jasmine.createSpyObj('EmailPasswordService', ['sendEmail']);

    await TestBed.configureTestingModule({
      declarations: [SendEmailComponent],
      imports: [FormsModule],  
      providers: [
        { provide: EmailPasswordService, useValue: emailServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SendEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('1. Recuperación de contraseña exitosa', () => {

    const responseMock = {
      mensaje: 'Correo enviado correctamente'
    };

    emailServiceSpy.sendEmail.and.returnValue(of(responseMock));

    component.mailTo = 'usuario@test.com';

    component.onSendEmail();

    expect(emailServiceSpy.sendEmail)
      .toHaveBeenCalledWith(new EmailValuesDTO('usuario@test.com'));

    expect(component.message).toBe('Correo enviado correctamente');

    expect(component.isError).toBeFalse();

  });

  it('2. Cuenta desactivada', () => {

    const errorMock = {
      error: {
        mensaje: 'La cuenta está desactivada o bloqueada. Contacte con soporte técnico.'
      }
    };

    emailServiceSpy.sendEmail.and.returnValue(
      throwError(() => errorMock)
    );

    component.mailTo = 'usuario@test.com';

    component.onSendEmail();

    expect(emailServiceSpy.sendEmail)
      .toHaveBeenCalledWith(new EmailValuesDTO('usuario@test.com'));

    expect(component.message)
      .toBe('La cuenta está desactivada o bloqueada. Contacte con soporte técnico.');

    expect(component.isError).toBeTrue();

  });


  it('3. Usuario no registrado', () => {

    const errorMock = {
      error: {
        mensaje: 'El nombre de usuario no está registrado'
      }
    };

    emailServiceSpy.sendEmail.and.returnValue(
      throwError(() => errorMock)
    );

    component.mailTo = 'noexiste@test.com';

    component.onSendEmail();

    expect(emailServiceSpy.sendEmail)
      .toHaveBeenCalledWith(new EmailValuesDTO('noexiste@test.com'));

    expect(component.message)
      .toBe('El nombre de usuario no está registrado');

    expect(component.isError).toBeTrue();

  });

  it('4. Error en el servidor', () => {

    const serverError = { status: 500 };

    emailServiceSpy.sendEmail.and.returnValue(
      throwError(() => serverError)
    );

    component.onSendEmail();

    expect(emailServiceSpy.sendEmail).toHaveBeenCalled();

    expect(component.message)
      .toBe('Ocurrió un problema técnico en el servidor. Intente nuevamente más tarde o contacte al soporte.');

    expect(component.isError).toBeTrue();

  });

});