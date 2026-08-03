import { Component, OnInit } from '@angular/core';
import { LayoutService } from '../service/app.layout.service';
import { AuthService } from '@app/Services/auth.service'; 
import { UsuarioService } from '@app/Services/usuario.service';
import { Usuario } from '@app/Models/usuario.model';
import { User } from '@app/Models/user.model';  

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
  styleUrls: ['./app.topbar.component.css']
})
export class AppTopBarComponent implements OnInit {

  currentUser: User | null = null;
  usuario: Usuario | null = null;

  constructor(
    public layoutService: LayoutService,
    public authService: AuthService,
    private usuarioService: UsuarioService
  ) {}

  ngOnInit(): void {

    // Escuchar usuario logueado
    this.authService.currentUser.subscribe(user => {
      this.currentUser = user;

      if (user?.username) {
        this.usuarioService.getUsuarioPorEmail(user.username).subscribe({
          next: (usuario) => {
            this.usuario = usuario;
          },
          error: () => {
            this.usuario = null;
          }
        });
      } else {
        this.usuario = null;
      }
    });
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
}