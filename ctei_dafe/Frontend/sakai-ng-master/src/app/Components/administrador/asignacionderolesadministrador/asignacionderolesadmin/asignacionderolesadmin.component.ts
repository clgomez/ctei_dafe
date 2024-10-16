import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../../Services/auth.service';
import { User } from '../../../../Models/user.model';


@Component({
  selector: 'app-asignacionderolesadmin',
  templateUrl: './asignacionderolesadmin.component.html',
  styleUrls: ['./asignacionderolesadmin.component.css']
})
export class AsignacionDeRolesAdminComponent implements OnInit {
  currentUser: User | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.authService.currentUser.subscribe(user => this.currentUser = user);
  }

  isAdmin(): boolean {
    return this.authService.hasRole('ROL_ADMIN');
  }

  isUser(): boolean {
    return this.authService.hasRole('ROL_USER');
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
