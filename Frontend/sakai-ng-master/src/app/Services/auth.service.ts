import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { JwtDto } from '@app/Models/jwt-dto.model'; 
import { TokenService } from './token.service'; 
import { LoginUser } from '@app/Models/login-user'; 
import { User } from '@app/Models/user.model'; 
import { environment } from '@app/Environments/environment'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;
  private apiUrl = `${environment.apiUrl}/auth`;
  public usuario: User | null = null;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {
    this.currentUserSubject = new BehaviorSubject<User | null>(this.getUserFromToken());
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  login(loginUser: LoginUser): Observable<JwtDto> {
    return this.http.post<JwtDto>(`${this.apiUrl}/login`, loginUser)
      .pipe(
        tap(response => this.handleAuthentication(response))
      );
  }
  
  register(user: User, rol: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register/${rol}`, user);
  }

   /*register(user: User, rol: string): Observable<any> {
    return throwError(() => ({
    status: 500,
    error: {
      codigo: "INTERNAL_ERROR",
      mensajes: ["Error interno del servidor. Intente más tarde"]
    }
  }));
 } */ 

   logout() {
    this.tokenService.removeTokens();
    this.currentUserSubject.next(null);
  }

  private handleAuthentication(response: JwtDto): void {
    this.tokenService.setToken(response.token);
    const user = this.getUserFromToken();
    this.currentUserSubject.next(user);
  }

  public getUserFromToken(): User | null {
    const token = this.tokenService.getToken();
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return {
        id: payload.id,
        username: payload.sub,
        nombre: payload.nombre || '',
        apellidos: payload.apellidos || '',
        email: payload.email || '',
        password: '',
        direccion: '',
        estado: '',
        fechaNacimiento: '',
        tipoIdentificacion: '',
        identificacion: '',
        genero: '',
        ocupacion: '',
        telefono: '',
        roles: payload.roles || []
      };
    }
    return null;
  }

  isLoggedIn(): boolean {
    return !!this.tokenService.getToken();
  }

  hasRole(role: string): boolean {
    const user = this.currentUserValue;
    return user && user.roles ? user.roles.includes(role) : false;
  }

  // Método que devuelve el rol actual del usuario
  getCurrentRole(): string | null {
    const user: User | null = this.currentUserValue;

    if (user && user.roles) {
      // Busca el primer rol que el usuario tenga
      const currentRole = user.roles.find(role => this.hasRole(role));
      return currentRole || null; // Retorna el rol encontrado o null si no hay ninguno
    }

    return null;
  }

 // Observable que notifica cambios en el rol
 /*roleChanges(): Observable<string | null> {
  return this.currentUserSubject.asObservable().pipe(
    map(user => {
      if (user && user.roles) {
        // Busca el primer rol que el usuario tenga
        const currentRole = user.roles.find(role => this.hasRole(role));
        return currentRole || null; // Retorna el rol encontrado o null si no hay ninguno
      }
      return null;
    })
  );*/

// Observable que notifica cambios en el rol
roleChanges(): Observable<string | null> {
  return this.currentUserSubject.asObservable().pipe(
    switchMap(user => {
      if (user && user.username) {
        return this.getUserbyEmail(user.username).pipe(
          map(us => {
            // Aquí puedes validar estado y roles
            if (us && us.estado === 'ACTIVO' && user.roles) {
              const currentRole = user.roles.find(role => this.hasRole(role));
              return currentRole || null;
            }
            return null;
          })
        );
      } else {
        return of(null);
      }
    })
  );
}

    getUserbyEmail(email: string): Observable<any>
    {

        return this.http.get(`${this.apiUrl}/email/${email}`)
    }

}
