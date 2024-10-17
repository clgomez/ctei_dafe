import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../Services/auth.service';
import { User } from '../../../Models/user.model';


@Component({
  selector: 'app-homeevaluador',
  templateUrl: './homeevaluador.component.html',
  styleUrls: ['./homeevaluador.component.css']
})
export class HomeEvaluadorComponent implements OnInit {
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
