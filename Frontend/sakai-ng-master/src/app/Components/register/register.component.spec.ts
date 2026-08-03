import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { throwError } from 'rxjs';

describe('Test Registrar Usuario', () => {

  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {

    authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [FormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('1: Registro de Usuario exitoso', () => {

    // Datos válidos del usuario
    component.user = {
      nombre: 'Juan',
      apellidos: 'Perez',
      username: 'juan123',
      email: 'juan@test.com',
      password: 'Password1!',
      direccion: 'Calle 123',
      estado: 'ACTIVO',
      fechaNacimiento: '2000-01-01',
      tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
      identificacion: '123456789',
      genero: 'Masculino',
      ocupacion: 'Estudiante',
      telefono: '3001234567'
    };

    spyOn(window, 'alert');

    // Simula respuesta exitosa del backend
    authServiceSpy.register.and.returnValue(of({}));

    // Ejecutar
    component.onSubmit();

    // Verificaciones
    expect(authServiceSpy.register).toHaveBeenCalledWith(component.user, 'investigador');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
    expect(window.alert).toHaveBeenCalledWith('Registro exitoso, inicia sesión');

  });

  it('2. Datos inválidos', () => {

  component.user = {
    nombre: 'Juan',
    apellidos: 'Perez',
    username: 'juan123',
    email: 'correo_invalido', // correo incorrecto
    password: 'Password1!',
    direccion: 'Calle 123',
    estado: 'ACTIVO',
    fechaNacimiento: '2000-01-01',
    tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
    identificacion: '123456789',
    genero: 'Masculino',
    ocupacion: 'Estudiante',
    telefono: '3001234567'
  };

  component.onSubmit();

  // No debe llamar al servicio
  expect(authServiceSpy.register).not.toHaveBeenCalled();

  // Debe mostrar error
  expect(component.error).toBe('Formato de correo electrónico inválido');

});

it('3. Email duplicado', () => {

  component.user = {
    nombre: 'Juan',
    apellidos: 'Perez',
    username: 'juan123',
    email: 'juan@test.com',
    password: 'Password1!',
    direccion: 'Calle 123',
    estado: 'ACTIVO',
    fechaNacimiento: '2000-01-01',
    tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
    identificacion: '123456789',
    genero: 'Masculino',
    ocupacion: 'Estudiante',
    telefono: '3001234567'
  };

  // Simular error del backend (correo duplicado)
  authServiceSpy.register.and.returnValue(
    throwError(() => ({ status: 409 }))
  );

  component.onSubmit();

  // Verificar que se llamó al servicio
  expect(authServiceSpy.register).toHaveBeenCalled();

  // Verificar mensaje de error
  expect(component.error).toBe('El correo electrónico ya está en uso');

});

it('4. Campos requeridos no completos', () => {

  component.user = {
    nombre: '', // campo obligatorio vacío
    apellidos: 'Perez',
    username: 'juan123',
    email: 'juan@test.com',
    password: 'Password1!',
    direccion: 'Calle 123',
    estado: 'ACTIVO',
    fechaNacimiento: '2000-01-01',
    tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
    identificacion: '123456789',
    genero: 'Masculino',
    ocupacion: 'Estudiante',
    telefono: '3001234567'
  };

  component.onSubmit();

  // No debe llamar al servicio de registro
  expect(authServiceSpy.register).not.toHaveBeenCalled();

  // Debe mostrar mensaje de error
  expect(component.error).toBe('El nombre es obligatorio');

});

  it('5. Error en el servidor', () => {

    component.user = {
      nombre: 'Carlos',
      apellidos: 'Gomez',
      username: 'carlos123',
      email: 'carlos@gmail.com',
      password: 'Password1!',
      direccion: 'Calle 123 #45',
      estado: 'ACTIVO',
      fechaNacimiento: '2000-05-10',
      tipoIdentificacion: 'TARJETA_DE_IDENTIDAD',
      identificacion: '123456789',
      genero: 'Masculino',
      ocupacion: 'Estudiante',
      telefono: '3001234567'
    };

    const serverError = { status: 500 };

    authServiceSpy.register.and.returnValue(
      throwError(() => serverError)
    );

    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalled();

    expect(component.error)
    .toBe('Ocurrió un problema técnico en el servidor. Intenta nuevamente más tarde o contacta al soporte.');

    expect(routerSpy.navigate).not.toHaveBeenCalled();

  });

});