import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { JwtDto } from '@app/Models/jwt-dto.model';
import { User } from '@app/Models/user.model';
import { throwError } from 'rxjs';

describe('Test Login', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {

    authServiceSpy = jasmine.createSpyObj('AuthService', ['login', 'getUserbyEmail']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    });

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;

  });

  it('1. Login exitoso', () => {

    const jwtMock = {
      token: 'token_prueba'
    } as JwtDto;

    const userMock = {
      estado: 'ACTIVO'
    } as User;

    authServiceSpy.login.and.returnValue(of(jwtMock));
    authServiceSpy.getUserbyEmail.and.returnValue(of(userMock));

    component.loginUser = {
      email: 'usuario@test.com',
      password: '123456'
    };

    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith(component.loginUser);
    expect(authServiceSpy.getUserbyEmail).toHaveBeenCalledWith(component.loginUser.email);
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/welcome']);

  });

  it('2. Credenciales inválidas', () => {

  component.loginUser = {
    email: 'carlos@gmail.com',
    password: '123456'
  };

  const errorResponse = { status: 401 };

  authServiceSpy.login.and.returnValue(
    throwError(() => errorResponse)
  );

  component.onSubmit();

  expect(component.error).toBe('Credenciales incorrectas');

});

  it('3. Error en el servidor', () => {

    const serverError = { status: 500 };

    authServiceSpy.login.and.returnValue(
      throwError(() => serverError)
    );

    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalled();

    expect(component.error)
      .toBe('Ocurrió un problema técnico en el servidor. Intente nuevamente más tarde o contacte al soporte.');

    expect(routerSpy.navigate).not.toHaveBeenCalled();

  });

});